package uncuyo.compiladores.codeGen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.symbolTable.Attribute;
import uncuyo.compiladores.semantic.symbolTable.Class;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Parameter;
import uncuyo.compiladores.semantic.symbolTable.Variable;

/**
 * Construye el ensamblador MIPS de un programa TinyS para su ejecución en MARS.
 * Acumula por separado los literales, las virtual tables, el cuerpo de
 * {@code main} y el código de los métodos, e incorpora las rutinas predefinidas
 * de {@code codeGen/data.asm} y {@code codeGen/text.asm}.
 *
 * <p>El código emitido utiliza palabras de cuatro bytes. Los registros de
 * activación se referencian mediante {@code $fp}; {@code $sp} apunta a la
 * siguiente palabra libre de la pila. Los valores primitivos se representan
 * mediante un CIR (registro de instancia de clase) de ocho bytes, con la
 * virtual table en el primer campo y el valor en el segundo.</p>
 */
public class CodeGenerator {

    private StringBuilder strings;
    private StringBuilder vtables;
    private StringBuilder text;
    private StringBuilder main;
    private StringBuilder current;
    private int forDepth = 0;

    private String baseData;
    private String baseText;

    private Map<String, String> stringLabels = new LinkedHashMap<>();
    private int strCount = 0;

    /**
     * Inicializa los bloques de salida y carga las rutinas predefinidas.
     * Las instrucciones se emiten inicialmente en el bloque de métodos.
     *
     * @throws RuntimeException si un recurso de ensamblador no existe o no se puede leer
     */
    public CodeGenerator() {
        this.strings = new StringBuilder();
        this.vtables = new StringBuilder();
        this.text = new StringBuilder();
        this.main = new StringBuilder();

        // Por defecto, el código se genera fuera de main
        this.current = this.text;

        this.baseData = loadResource("codeGen/data.asm");
        this.baseText = loadResource("codeGen/text.asm");
    }

    /**
     * Obtiene la cantidad de bucles {@code for} activos durante el recorrido.
     *
     * @return profundidad de anidamiento utilizada para ubicar las variables de iteración
     */
    public int getForDepth() {
        return forDepth;
    }

    /**
     * Actualiza la profundidad usada al generar variables de iteración.
     *
     * @param forDepth cantidad de bucles {@code for} activos
     */
    public void setForDepth(int forDepth) {
        this.forDepth = forDepth;
    }

    /**
     * Lee un recurso del path.
     *
     * @param path ruta del recurso relativa a la raíz del classpath
     * @return contenido del recurso
     * @throws RuntimeException si el recurso no existe o falla su lectura
     */
    private String loadResource(String path) {
        try {
            InputStream resourceStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(path);

            if (resourceStream == null) {
                throw new RuntimeException(
                        "No se encontró el recurso: " + path
                );
            }

            try (InputStream stream = resourceStream) {
                return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al cargar " + path,
                    e
            );
        }
    }

    /**
     * Agrega espacios al inicio de cada línea no vacía de un bloque.
     *
     * @param block texto que se desea indentar
     * @param spaces cantidad de espacios de sangría adicional
     * @return bloque indentado, con un salto de línea por cada línea de entrada
     */
    private String indentBlock(String block, int spaces) {
        String indent = " ".repeat(spaces);
        StringBuilder result = new StringBuilder();

        for (String line : block.split("\\R", -1)) {

            if (line.isBlank()) {
                result.append("\n");
            } else {
                result.append(indent)
                        .append(line)
                        .append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Emite un {@code .asciiz} en el bloque de literales.
     *
     * @param line valor con las comillas y escapes de ensamblador necesarios
     */
    public void emitAsciiz(String line) {
        strings.append("        ")
                .append(".asciiz ")
                .append(line)
                .append("\n");
    }

    /**
     * Agrega una dirección de método a la virtual table en construcción.
     *
     * @param line etiqueta del método que se guarda mediante {@code .word}
     */
    public void emitVtableFunction(String line) {
        vtables.append("        ")
                .append(".word ")
                .append(line)
                .append("\n");
    }

    /**
     * Emite una etiqueta en el bloque de literales de {@code .data}.
     *
     * @param label nombre de la etiqueta sin los dos puntos finales
     */
    public void emitSringDataLabel(String label) {
        strings.append("    ")
                .append(label)
                .append(":\n");
    }

    /**
     * Emite una etiqueta en el bloque de tablas virtuales de {@code .data}.
     *
     * @param label nombre de la tabla virtual sin los dos puntos finales
     */
    public void emitVtableDataLabel(String label) {
        vtables.append("    ")
                .append(label)
                .append(":\n");
    }

    /**
     * Emite una línea indentada en el bloque de código seleccionado.
     *
     * @param line instrucción o texto de ensamblador que se agrega al bloque
     */
    public void emitText(String line) {
        current.append("        ")
                .append(line)
                .append("\n");
    }

    /**
     * Emite una etiqueta de código precedida por una línea en blanco.
     *
     * @param label nombre de la etiqueta sin los dos puntos finales
     */
    public void emitLabel(String label) {
        current.append("\n")
                .append("    ")
                .append(label)
                .append(":\n");
    }

    /**
     * Emite una etiqueta interna con la misma sangría que las instrucciones.
     *
     * @param label nombre de la etiqueta sin los dos puntos finales
     */
    public void emitSubLabel(String label) {
        current.append("        ")
                .append(label)
                .append(":\n");
    }

    /**
     * Selecciona el bloque que recibirá las próximas instrucciones y etiquetas.
     *
     * @param inMain {@code true} para emitir en {@code main}; {@code false} para emitir en métodos
     */
    public void setInMain(boolean inMain) {
        this.current = inMain ? main : text;
    }

    /**
     * Emite como {@code .ascii} el fragmento acumulado y vacía su contenido.
     * Si el fragmento está vacío, no emite bada.
     *
     * @param fragment caracteres ASCII ya preparados para el literal de ensamblador
     */
    private void emitAsciiFragment(StringBuilder fragment) {
        if (fragment.length() == 0) {
            return;
        }

        strings.append("        .ascii \"")
                .append(fragment)
                .append("\"\n");

        fragment.setLength(0);
    }

    /**
     * Emite un valor mediante {@code .byte} en el bloque de literales.
     *
     * @param value valor numérico del byte que se desea almacenar
     */
    private void emitByte(int value) {
        strings.append("        .byte ")
                .append(value)
                .append("\n");
    }

    /**
     * Emite el contenido de un literal mediante directivas {@code .ascii} y
     * {@code .byte}, seguidas de un único fin de cadena. Interpreta los
     * escapes de TinyS y codifica en UTF-8 los caracteres que requieren bytes.
     *
     * @param lexeme lexema del literal, incluidas sus comillas exteriores
     * @throws IllegalArgumentException si un escape está incompleto o no es reconocido
     */
    private void emitStringContent(String lexeme) {

        StringBuilder fragment = new StringBuilder();

        // Ignoro las comillas exteriores del lexema.
        for (int i = 1; i < lexeme.length() - 1; i++) {

            char c = lexeme.charAt(i);

            if (c == '\\') {

                if (i + 1 >= lexeme.length() - 1) {
                    throw new IllegalArgumentException(
                            "Escape incompleto: " + lexeme
                    );
                }

                emitAsciiFragment(fragment);

                char escape = lexeme.charAt(++i);

                int ascii = switch (escape) {
                    case 'n' -> 10;
                    case 't' -> 9;
                    case 'v' -> 11;
                    case 'r' -> 13;
                    case '"' -> 34;
                    case '\'' -> 39;
                    case '\\' -> 92;

                    // Solo si extendés TinyS para aceptar \v:
                    // case 'v' -> 11;

                    default -> throw new IllegalArgumentException(
                            "Escape no reconocido: \\" + escape
                    );
                };

                emitByte(ascii);

            } else if (c >= 32 && c <= 126
                    && c != '"' && c != '\\') {

                // Caracter ASCII normal: acumulo para .ascii
                fragment.append(c);

            } else {

                // Otros caracteres: codifico sus bytes.
                emitAsciiFragment(fragment);

                byte[] bytes = String.valueOf(c)
                        .getBytes(StandardCharsets.UTF_8);

                for (byte b : bytes) {
                    emitByte(Byte.toUnsignedInt(b));
                }
            }
        }

        // Último fragmento de texto.
        emitAsciiFragment(fragment);

        // Finalizador de string.
        emitByte(0);
    }

    /**
     * Agrega un literal de cadena a {@code .data}, reutilizando su etiqueta
     * cuando ya se emitió el mismo lexema. La cadena vacía utiliza
     * {@code empty_str}, definida en los recursos predefinidos.
     *
     * @param lexeme lexema del literal, incluidas sus comillas exteriores
     * @return etiqueta de los bytes de la cadena terminada en cero
     * @throws IllegalArgumentException si el literal contiene un escape inválido
     */
    public String emitString(String lexeme) {

        // El string vacío ya existe en data.asm
        if (lexeme.equals("\"\"")) {
            return "empty_str";
        }

        String label = stringLabels.get(lexeme);

        if (label == null) {

            label = "str_" + strCount;
            strCount++;

            stringLabels.put(lexeme, label);

            emitSringDataLabel(label);
            emitStringContent(lexeme);
        }

        return label;
    }

    /**
     * Ensambla las secciones de datos y código junto con las rutinas predefinidas.
     *
     * @return programa MIPS completo con el punto de entrada {@code main}
     */
    public String generate() {

        return ".data\n"
                + indentBlock(baseData, 4)
                + strings
                + "\n"
                + vtables

                + ".text\n"

                + "    .globl main\n"
                + "\n"

                + "    main:\n"
                + main
                + "\n"

                + indentBlock(baseText, 4)
                + text;


    }

    /**
     * Escribe el programa generado, creando o reemplazando el archivo.
     *
     * @param path ruta del archivo de salida
     * @throws IOException si no se puede crear o escribir el archivo
     */
    public void writeOutput(String path) throws IOException {
        Files.writeString(
                Path.of(path),
                generate()
        );
    }

    /**
     * Emite la preparación del registro de activación de una llamada.
     * Reserva espacio para parámetros, retorno, {@code $fp} del llamador y
     * contexto; guarda como contexto el valor de {@code $a0} e inicializa el
     * retorno en cero. Los argumentos se deben emitir después de esta preparación.
     *
     * @param method método o constructor que se va a invocar
     */
    public void generateMethodPreCall(Method method) {

        int parameterCount = method.getParameters().size();
        int selfOffset = getSelfOffset(method);
        int callerFpOffset = getCallerFpOffset(method);
        int returnOffset = getReturnOffset(method);

        emitSection("PRECALL " + (method.getName().equals(".") ? ("Constructor " + method.getClassName()) : method.getName()));

        emitComment("Layout del registro de activacion:");
        emitComment("Self        -> " + selfOffset + "($sp)");
        emitComment("$fp llamador -> " + callerFpOffset + "($sp)");
        emitComment("return      -> " + returnOffset + "($sp)");

        for (int i = parameterCount; i >= 1; i--) {
            emitComment("Param " + i + "     -> " + (i * 4) + "($sp)");
        }

        emitComment("$ra          -> 0($sp) [lo guarda el callee]");

        int callSpace = selfOffset;

        emitBlankLine();

        emitComment("Reservo el registro de activacion del caller");
        emitText("addiu $sp, $sp, -" + callSpace);

        emitComment("Guardo Self/contexto");
        emitText("sw $a0, " + selfOffset + "($sp)");

        emitComment("Guardo $fp del llamador");
        emitText("sw $fp, " + callerFpOffset + "($sp)");

        emitComment("Inicializo return");
        emitText("sw $zero, " + returnOffset + "($sp)");

        emitBlankLine();
    }

    /**
     * Emite la tabla virtual de una clase respetando el orden de sus métodos.
     * Cada entrada referencia la etiqueta de la clase que define el método.
     *
     * @param currentClass clase con sus métodos consolidados y ordenados
     */
    public void generateVTable(Class currentClass) {

        emitVtableDataLabel("vtable_" + currentClass.getName());

        for (Method method : currentClass.getMethods().values()) {
            emitVtableFunction(method.getClassName() + "_" + method.getName());

        }

    }

    /**
     * Emite el prólogo que fija {@code $fp}, guarda {@code $ra}, reserva el
     * espacio de variables locales y las inicializa con sus valores por defecto.
     *
     * @param method método cuyo registro de activación se está construyendo
     */
    public void generateMethodPrologue(Method method) {

        int variableCount = method.getVariables().size();
        int variableSpace = variableCount * 4;
        int frameSpace = 4 + variableSpace;
        int parameterCount = method.getParameters().size();
        int parameterSpace = parameterCount * 4;


        emitSection("PROLOGUE " + (method.getName().equals(".") ? ("Constructor " + method.getClassName()) : method.getName()));

        emitComment("Registro de activacion del metodo:");
        emitComment("self         -> " + (parameterSpace + 12) + "($fp)");
        emitComment("$fp llamador -> " + (parameterSpace + 8) + "($fp)");
        emitComment("return       -> " + (parameterSpace + 4) + "($fp)");
        for (int i = parameterCount; i > 0; i--) {
            emitComment("Par " + i + "        -> " + (i * 4) + "($fp)");
        }
        emitComment("$ra          -> 0($fp)");

        for (int i = 1; i <= variableCount; i++) {
            emitComment("Var " + i + "        -> -" + (i * 4) + "($fp)");
        }

        emitBlankLine();

        emitComment("Fijo $fp en la posicion donde se guardara el $ra");
        emitText("move $fp, $sp");

        emitComment("Guardo la direccion de retorno de la llamada");
        emitText("sw $ra, 0($fp)");

        emitBlankLine();

        emitComment("Reservo " + frameSpace +
                        " bytes para variables locales y el siguiente word libre");
        emitText("addiu $sp, $sp, -" + frameSpace);

        emitBlankLine();

        if (variableCount > 0) {
            emitSubsection("INICIALIZACION DE VARIABLES LOCALES");
            initializeLocalVariables(method);
            emitBlankLine();
        }

        emitComment(
                "$sp queda apuntando al primer word libre del frame"
        );

        emitBlankLine();
    }

    /**
     * Emite la inicialización de las variables locales del método.
     * Los tipos {@code Int}, {@code Bool} y {@code Str} reciben un CIR con
     * cero, falso y cadena vacía, respectivamente; las demás referencias
     * reciben {@code nil}. Los resultados se guardan en posiciones relativas
     * a {@code $fp}.
     *
     * @param method método cuyas variables locales se inicializan
     */
    public void initializeLocalVariables(Method method) {
        emitText("# Inicializacion variables locales de " + method.getName());
        for (Variable variable : method.getVariables().values()) {
            emitText("# Inicializacion de " + variable.getName());
            switch (variable.getType().getTypeName()) {
                case "Int":
                case "Bool":
                    emitText("li $a0 8");
                    emitText("li $v0 9");
                    emitText("syscall");
                    emitText("sw $zero 0($v0)");
                    emitText("sw $zero 4($v0)");
                    break;
                case "Str":
                    emitText("li $a0 8");
                    emitText("li $v0 9");
                    emitText("syscall");
                    emitText("la $t0 empty_str");
                    emitText("sw $t0 4($v0)");
                    emitText("la $t0 vtable_str");
                    emitText("sw $t0 0($v0)");
                    break;
                default:
                    emitText("move $v0 $zero");
            }
            emitText("sw $v0 " + getVariableOffset(variable) + "($fp)");

        }
    }

    /**
     * Emite la inicialización de los atributos y de la virtual table de una instancia.
     * Los atributos primitivos reciben un CIR con su valor por defecto y las
     * demás referencias reciben {@code nil}. Requiere la dirección de la
     * instancia en {@code 4($sp)}; si no hay atributos, la toma de {@code $v0}.
     *
     * @param classEntry clase de la instancia que se está inicializando
     */
    public void initializeAttributes(Class classEntry) {
        emitSubsection("Inicializacion atributos de " + classEntry.getName());

        for (Attribute attribute : classEntry.getAttributes().values()) {
            emitComment("Inicializacion de atributo " + attribute.getName() + " de tipo " + attribute.getType().getTypeName());

            List<TokenType> primitiveClasses = List.of(TokenType.INT_CLASS, TokenType.INT_CONSTANT, TokenType.BOOL_CLASS, TokenType.BOOLEAN_CONSTANT, TokenType.STR_CLASS, TokenType.STRING_CONSTANT);
            if (primitiveClasses.contains(attribute.getType().getTokenType())) {
                generatePrimitiveCIR(attribute.getType().getTokenType());
            } else {
                emitText("move $v0 $zero");
            }


            // Guardar referencia en el atributo del CIR de self
            emitComment("Guardo la direccion del atributo " + attribute.getName());
            emitText("lw $t0 4($sp)");
            emitText("sw $v0 " + getAttributeOffset(attribute) + "($t0)");
        }
        if(classEntry.getAttributes().isEmpty()) {
            emitComment("No tiene atributos, recupero el valor del CIR de " + classEntry.getName());
            emitText("move $t0 $v0");
        }
        emitComment("Guardo la direccion de la vtable de " + classEntry.getName());
        emitText("la $a0 vtable_" + classEntry.getName());
        emitText("sw $a0 0($t0)");
    }

    /**
     * Calcula el desplazamiento de un atributo, después de la virtual table del CIR.
     *
     * @param attribute atributo con posición asignada en la clase consolidada
     * @return desplazamiento en bytes respecto del inicio de la instancia
     */
    public int getAttributeOffset(Attribute attribute) {
        return (attribute.getPosition() + 1) * 4;
    }

    /**
     * Emite la salida del método y libera su registro de activación.
     * Recupera {@code $ra} y el {@code $fp} del llamador; para métodos con
     * resultado deja el retorno en {@code $a0}. Un constructor devuelve
     * {@code self} y {@code start} finaliza la ejecución mediante un syscall.
     *
     * @param method método cuyo cuerpo acaba de generarse
     */
    public void generateMethodEpilogue(Method method) {
        if (method.getName().equals("start")) {
            emitText("li $v0 10");
            emitText("syscall");
        } else {
            int parameterSpace = method.getParameters().size() * 4;
            int variableSpace = method.getVariables().size() * 4;

            int frameSpace = 16 + parameterSpace + variableSpace;

            emitSubsection("Epilogue de " + method.getName());
            emitLabel("epilogue_" + method.getClassName() + "_" + (method.getName().equals(".") ? "constructor" : method.getName()));
            emitComment("Recupero el $ra anterior");
            emitText("lw $ra 0($fp)");
            if (!method.getType().getTypeName().equals("void")) {
                if (method.getName().equals(".")) {
                    emitComment("Como es un constructor, guardo en return el self (CIR)");
                    emitText("lw $t0 " + getSelfOffset(method) + "($fp)");
                    emitText("sw $t0 " + getReturnOffset(method) + "($fp)");
                }
                emitComment("Recupero el valor del return");
                emitText("lw $a0 " + (parameterSpace + 4) + "($fp)");
            }
            emitComment("Recupero el $fp del llamador");
            emitText("lw $fp " + (parameterSpace + 8) + "($fp)");
            emitComment("Recupero el espacio usado");
            emitText("addiu $sp $sp " + frameSpace);

            emitText("jr $ra");
        }

    }

    /**
     * Calcula la posición de una variable local debajo del {@code $fp}.
     *
     * @param variable variable con posición asignada en el método
     * @return desplazamiento negativo en bytes respecto de {@code $fp}
     */
    public int getVariableOffset(Variable variable) {
        return -((variable.getPosition() + 1) * 4);
    }

    /**
     * Calcula la posición de un parámetro después del {@code $ra} guardado.
     *
     * @param parameter parámetro con posición asignada en el método
     * @return desplazamiento en bytes respecto de la base del registro de activación
     */
    public int getParameterOffset(Parameter parameter) {
        return (parameter.getPosition() + 1) * 4;
    }

    /**
     * Calcula la posición del resultado, a continuación de los parámetros.
     *
     * @param method método cuyo registro de activación se consulta
     * @return desplazamiento en bytes respecto de la base del registro de activación
     */
    public int getReturnOffset(Method method) {
        return (method.getParameters().size() + 1) * 4;
    }

    /**
     * Calcula la posición donde se conserva el {@code $fp} del llamador.
     *
     * @param method método cuyo registro de activación se consulta
     * @return desplazamiento en bytes respecto de la base del registro de activación
     */
    public int getCallerFpOffset(Method method) {
        return (method.getParameters().size() + 2) * 4;
    }

    /**
     * Calcula la posición del contexto de la llamada: instancia o virtual table.
     *
     * @param method método cuyo registro de activación se consulta
     * @return desplazamiento en bytes respecto de la base del registro de activación
     */
    public int getSelfOffset(Method method) {
        return (method.getParameters().size() + 3) * 4;
    }

    /**
     * Emite el guardado de un registro en {@code 0($sp)} y decrementa
     * {@code $sp} en cuatro bytes para dejarlo en la siguiente posición libre.
     *
     * @param register nombre del registro MIPS que se desea apilar, incluido {@code $}
     */
    public void push(String register) {
        emitText("sw " + register + " 0($sp)");
        emitText("addiu $sp $sp -4");
    }

    /**
     * Emite la recuperación del último valor apilado en {@code $t0} e
     * incrementa {@code $sp} en cuatro bytes.
     */
    public void pop() {
        emitText("lw $t0 4($sp)");
        emitText("addiu $sp $sp 4");
    }

    /**
     * Agrega un comentario de ensamblador al bloque de código seleccionado.
     *
     * @param comment texto del comentario sin el prefijo {@code #}
     */
    public void emitComment(String comment) {
        current.append("        # ")
                .append(comment)
                .append("\n");
    }

    /**
     * Agrega una línea en blanco al bloque de código seleccionado.
     */
    public void emitBlankLine() {
        current.append("\n");
    }

    /**
     * Agrega un título de sección entre separadores de comentarios.
     *
     * @param title texto del título
     */
    public void emitSection(String title) {
        current.append("\n");
        current.append("        # ========================================\n");
        current.append("        # ").append(title).append("\n");
        current.append("        # ========================================\n");
    }

    /**
     * Agrega un título de subsección como comentario de ensamblador.
     *
     * @param title texto del título
     */
    public void emitSubsection(String title) {
        current.append("\n");
        current.append("        # --- ").append(title).append(" ---\n");
    }

    /**
     * Emite la reserva de un CIR primitivo de ocho bytes y deja su dirección
     * en {@code $v0}. Para {@code Int} y {@code Bool} inicializa ambos campos
     * en cero; para {@code Str} guarda {@code vtable_str} y {@code empty_str}.
     * Otros tipos de token no generan instrucciones.
     *
     * @param tokenType tipo de clase primitiva o de su literal correspondiente
     */
    public void generatePrimitiveCIR(TokenType tokenType) {
        switch (tokenType) {
            case INT_CLASS:
            case INT_CONSTANT:
            case BOOLEAN_CONSTANT:
            case BOOL_CLASS:
                emitText("li $a0 8");
                emitText("li $v0 9");
                emitText("syscall");
                emitText("sw $zero 0($v0)");
                emitText("sw $zero 4($v0)");
                break;

            case STR_CLASS:
            case STRING_CONSTANT:
                // Reservar CIR del String
                emitText("li $a0 8");
                emitText("li $v0 9");
                emitText("syscall");

                // VTable
                emitText("la $t0 vtable_str");
                emitText("sw $t0 0($v0)");

                // String vacío
                emitText("la $t0 empty_str");
                emitText("sw $t0 4($v0)");
                break;


        }
    }

    /**
     * Emite un salto a {@code runtime_null} si la referencia indicada es cero.
     * Carga la ubicación del acceso en {@code $a2} y {@code $a3} para el diagnóstico.
     *
     * @param register registro MIPS que contiene la referencia
     * @param rowIndex fila del acceso en el código fuente
     * @param columnIndex columna del acceso en el código fuente
     */
    public void checkNullAccess(String register, int rowIndex, int columnIndex) {
        emitComment("Checkeo acceso nulo");
        emitText("li $a2 " + rowIndex);
        emitText("li $a3 " + columnIndex);
        emitText("beqz " + register + " runtime_null");
    }

    /**
     * Emite un salto a {@code runtime_negative_index} si el índice es negativo.
     *
     * @param index registro MIPS que contiene el valor del índice
     */
    public void checkNegativeArrayIndex(String index) {
        emitComment("Checkeo indice negativo");
        emitText("bltz " + index + " runtime_negative_index");
    }

    /**
     * Emite un salto a {@code runtime_out_of_range} si el índice es mayor o
     * igual que la longitud. Usa {@code $t2} para almacenar la comparación.
     *
     * @param index registro MIPS que contiene el valor del índice
     * @param length registro MIPS que contiene la longitud del arreglo
     */
    public void checkIndexOutOfBounds(String index, String length) {
        emitComment("Checkeo indice fuera de rango");
        emitText("slt $t2 " + index + " " + length);
        emitText("beqz $t2 runtime_out_of_range");
    }

    /**
     * Emite las comprobaciones de índice negativo y de límite superior del arreglo.
     *
     * @param index registro MIPS que contiene el valor del índice
     * @param length registro MIPS que contiene la longitud del arreglo
     */
    public void checkIndex(String index, String length) {
        checkNegativeArrayIndex(index);
        checkIndexOutOfBounds(index, length);
    }


}
