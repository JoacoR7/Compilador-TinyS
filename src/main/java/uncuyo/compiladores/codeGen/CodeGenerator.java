package uncuyo.compiladores.codeGen;

import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.symbolTable.*;
import uncuyo.compiladores.semantic.symbolTable.Class;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public int getForDepth() {
        return forDepth;
    }

    public void setForDepth(int forDepth) {
        this.forDepth = forDepth;
    }

    /**
     * Carga un archivo desde resources.
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
     * Agrega una indentación adicional a todo un bloque.
     *
     * Por ejemplo:
     *
     * vtable_str:
     *     .word length_str
     *
     * pasa a:
     *
     *     vtable_str:
     *         .word length_str
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
     * Emite una directiva/dato dentro de .data.
     *
     * Tiene 8 espacios porque:
     *
     * .data
     *     label:
     *         contenido
     */
    public void emitAsciiz(String line) {
        strings.append("        ")
                .append(".asciiz ")
                .append(line)
                .append("\n");
    }

    public void emitVtableFunction(String line) {
        vtables.append("        ")
                .append(".word ")
                .append(line)
                .append("\n");
    }

    /**
     * Emite una etiqueta para string dentro de .data.
     */
    public void emitSringDataLabel(String label) {
        strings.append("    ")
                .append(label)
                .append(":\n");
    }

    /**
     * Emite una etiqueta para vtable dentro de .data.
     */
    public void emitVtableDataLabel(String label) {
        vtables.append("    ")
                .append(label)
                .append(":\n");
    }

    /**
     * Emite una instrucción dentro del bloque de código actual.
     *
     * current puede ser:
     * - main
     * - text
     */
    public void emitText(String line) {
        current.append("        ")
                .append(line)
                .append("\n");
    }

    /**
     * Emite una etiqueta de código.
     *
     * Ejemplo:
     *
     *     Persona_getEdad:
     */
    public void emitLabel(String label) {
        current.append("\n")
                .append("    ")
                .append(label)
                .append(":\n");
    }

    public void emitSubLabel(String label) {
        current.append("        ")
                .append(label)
                .append(":\n");
    }

    /**
     * Decide si las próximas instrucciones se generan
     * dentro de main o dentro del text general.
     */
    public void setInMain(boolean inMain) {
        this.current = inMain ? main : text;
    }

    /**
     * Genera/agrega un literal String en .data.
     *
     * Si el String ya fue generado anteriormente,
     * reutiliza su label.
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
            emitAsciiz(lexeme);
        }

        return label;
    }

    /**
     * Construye el archivo ASM final.
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
     * Escribe el ASM generado a un archivo.
     */
    public void writeOutput(String path) throws IOException {
        Files.writeString(
                Path.of(path),
                generate()
        );
    }

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

    /*
        Según el tipo:
            Int  -> referencia a CIR Int(0)
            Bool -> referencia a CIR Bool(false)
            Str  -> referencia a CIR Str("")
            Clase -> nil
            Array -> nil
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

    /*
    Inicialización de atributos de una instancia.

    Según el tipo:
        Int   -> referencia a CIR Int(0)
        Bool  -> referencia a CIR Bool(false)
        Str   -> referencia a CIR Str("")
        Clase -> nil
        Array -> nil

    $s0 -> dirección base del CIR de self
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
        emitComment("Guardo la direccion de la vtable de " + classEntry.getName());
        emitText("la $a0 vtable_" + classEntry.getName());
        emitText("sw $a0 0($t0)");
    }

    public int getAttributeOffset(Attribute attribute) {
        return (attribute.getPosition() + 1) * 4;
    }

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

    public int getVariableOffset(Variable variable) {
        return -((variable.getPosition() + 1) * 4);
    }

    public int getParameterOffset(Parameter parameter) {
        return (parameter.getPosition() + 1) * 4;
    }

    public int getReturnOffset(Method method) {
        return (method.getParameters().size() + 1) * 4;
    }

    public int getCallerFpOffset(Method method) {
        return (method.getParameters().size() + 2) * 4;
    }

    public int getSelfOffset(Method method) {
        return (method.getParameters().size() + 3) * 4;
    }

    public void push(String register) {
        emitText("sw " + register + " 0($sp)");
        emitText("addiu $sp $sp -4");
    }

    public void pop() {
        emitText("lw $t0 4($sp)");
        emitText("addiu $sp $sp 4");
    }

    public void emitComment(String comment) {
        current.append("        # ")
                .append(comment)
                .append("\n");
    }

    public void emitBlankLine() {
        current.append("\n");
    }

    public void emitSection(String title) {
        current.append("\n");
        current.append("        # ========================================\n");
        current.append("        # ").append(title).append("\n");
        current.append("        # ========================================\n");
    }

    public void emitSubsection(String title) {
        current.append("\n");
        current.append("        # --- ").append(title).append(" ---\n");
    }

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

    public void checkNullAccess(String register, int rowIndex, int columnIndex) {
        emitComment("Checkeo acceso nulo");
        emitText("li $a2 " + rowIndex);
        emitText("li $a3 " + columnIndex);
        emitText("beqz " + register + " runtime_null");
    }

    public void checkNegativeArrayIndex(String index) {
        emitComment("Checkeo indice negativo");
        emitText("bltz " + index + " runtime_negative_index");
    }

    public void checkIndexOutOfBounds(String index, String length) {
        emitComment("Checkeo indice fuera de rango");
        emitText("slt $t2 " + index + " " + length);
        emitText("beqz $t2 runtime_out_of_range");
    }

    public void checkIndex(String index, String length) {
        checkNegativeArrayIndex(index);
        checkIndexOutOfBounds(index, length);
    }


}
