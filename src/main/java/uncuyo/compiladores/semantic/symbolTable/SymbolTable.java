package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.exceptions.semantic.symbolTableException.*;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.ClassNotFoundException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tabla de símbolos y contexto de reconocimiento de un programa TinyS.
 *
 * <p>Las clases se indexan por nombre y conservan el orden de inserción.
 * Cada entrada reúne las partes {@code class} e {@code impl}; el método
 * {@code start} se almacena por separado. Las referencias de contexto
 * indican dónde deben registrarse las declaraciones que reconoce el analizador.
 *
 */
public class SymbolTable implements JsonSerializable {

    private LinkedHashMap<String, Class> classes;
    private Class currentClass;
    private Method currentMethod;
    private Block currentBlock;
    private Method start;

    /**
     * Crea una tabla vacía, sin clases predefinidas ni contexto activo.
     */
    public SymbolTable() {
        this.classes = new LinkedHashMap<>();
        this.currentClass = null;
        this.currentMethod = null;
        this.currentBlock = null;
        this.start = null;
    }


    public LinkedHashMap<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(LinkedHashMap<String, Class> classes) {
        this.classes = classes;
    }

    public Class getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class currentClass) {
        this.currentClass = currentClass;
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(Method currentMethod) {
        this.currentMethod = currentMethod;
    }

    public Method getStart() {
        return start;
    }

    public void setStart(Method start) {
        this.start = start;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
    }

    /**
     * Busca la entidad original de una clase, sin copiarla.
     *
     * @param idclass nombre de la clase
     * @return entidad mutable almacenada, o {@code null} si no existe
     */
    public Class searchOriginalClass(String idclass) {
        return classes.getOrDefault(idclass, null);
    }

    /**
     * Prepara una copia para reconocer otra parte de una clase.
     *
     * <p>La copia conserva el token, el padre y las marcas de presencia de
     * {@code class} e {@code impl}, pero comienza sin atributos, métodos ni
     * constructor. Sus miembros se integran luego en la entrada original.</p>
     *
     * @param idclass nombre de la clase
     * @return copia temporal, o {@code null} si todavía no está registrada
     */
    public Class searchAndCopyClass(String idclass) {
        Class original = classes.getOrDefault(idclass, null);
        if (original != null) {
            original = new Class(original);
        }
        return original;
    }

    /**
     * Valida el tipo de padre y guarda una clase por su nombre.
     * Si la clave ya existe, esta operación reemplaza su entrada; la integración
     * de partes existentes debe realizarse mediante los métodos de combinación.
     *
     * @param newClass clase que se incorporará a la tabla
     * @throws PrimitiveInheritanceException si intenta heredar de un tipo prohibido
     */
    public void saveClass(Class newClass) {
        newClass.validateParent();
        classes.put(newClass.getToken().getLexeme(), newClass);
    }

    /**
     * Integra los atributos y el padre de la clase actual en su declaración original.
     *
     * @param existingClass entidad original correspondiente a la clase actual
     * @throws uncuyo.compiladores.exceptions.semantic.SemanticException si la integración detecta un conflicto
     */
    public void mergeClassDeclarations(Class existingClass) {
        existingClass.mergeDeclarations(this.currentClass);
    }

    /**
     * Integra los métodos y el constructor de la implementación actual.
     *
     * @param existingClass entidad original correspondiente a la clase actual
     * @throws uncuyo.compiladores.exceptions.semantic.SemanticException si se repiten miembros
     */
    public void mergeClassImplements(Class existingClass) {
        existingClass.mergeImplements(this.currentClass);
    }

    /**
     * Comprueba que el nombre del padre esté registrado en la tabla.
     *
     * @param currentClass clase cuya relación de herencia se comprobará
     * @throws ParentClassNotFoundException si el padre no existe
     */
    public void checkParentExistence(Class currentClass) {
        String parentName = currentClass.getParent();
        if(!classes.containsKey(parentName)) {
            throw new ParentClassNotFoundException(currentClass.getToken(), parentName);
        }
    }

    /**
     * Comprueba y consolida los miembros de las clases del usuario.
     *
     * <p>En orden de inserción, exige implementación, constructor y declaración;
     * luego verifica el padre y la circularidad e incorpora los miembros heredados.
     * Las clases predefinidas quedan excluidas.</p>
     *
     * @throws ImplNotFoundException si falta una parte {@code impl}
     * @throws ConstructorNotFoundException si falta un constructor
     * @throws ClassNotFoundException si una implementación no tiene parte {@code class}
     * @throws ParentClassNotFoundException si el padre directo no existe
     * @throws CircularInheritanceException si se detecta un ciclo de herencia
     * @throws InheritedAttributeRedeclaration si se redeclara un atributo del padre
     * @throws InvalidOverrideException si la firma de una redefinición es incompatible
     */
    public void checkClasses() {
        List<String> predefinedClasses = List.of("Object", "IO", "Int", "Str", "Bool", "Array");
        for (Class currentClass : classes.values()) {
            if(!predefinedClasses.contains(currentClass.getToken().getLexeme())) {
                if(!currentClass.isFoundImpl()) {
                    throw new ImplNotFoundException(currentClass.getName());
                }
                if(currentClass.getConstructor() == null) {
                    throw new ConstructorNotFoundException(currentClass.getName());
                }
                if (!currentClass.isFoundClass()) {
                    throw new ClassNotFoundException(currentClass.getName());
                }
                String currentClassParent = currentClass.getParent();

                if(!currentClassParent.equals("Object")) {
                    checkParentExistence(currentClass);
                    checkCircularInheritance(currentClass);
                    if(!currentClass.isAtributesConsolidationChecked()) {
                        currentClass.checkAndSetInheritedAttributes(classes.get(currentClassParent));
                    }
                    if(!currentClass.isMethodsConsolidationChecked()) {
                        currentClass.checkAndSetInheritedMethods(classes.get(currentClassParent));
                    }
                } else {
                    currentClass.setAtributesConsolidationChecked(true);
                    currentClass.setMethodsConsolidationChecked(true);
                }
            }
        }
    }

    /**
     * Verifica los nombres de tipo de las variables locales de {@code start}.
     *
     * @throws ObjectDeclarationException si una variable tiene tipo {@code Object}
     * @throws ClassNotFoundException si el nombre del tipo no está registrado
     */
    public void checkStart(){
        LinkedHashMap<String, Class> classList = this.getClasses()
                .entrySet()
                .stream()
                .filter(entry -> !entry.getKey().equals("Object"))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        for (Variable variable : this.getStart().getVariables().values()) {
            if (!classList.containsKey(variable.getType().getLexeme())) {
                if (variable.getType().getLexeme().equals("Object")) {
                    throw new ObjectDeclarationException(variable.getToken());
                } else {
                    throw new ClassNotFoundException(variable.getType().getLexeme());
                }
            }
        }
    }

    /**
     * Inicia el recorrido de la cadena de herencia con un conjunto de visitados vacío.
     * Los padres consultados deben estar registrados en la tabla.
     *
     * @param currentClass clase desde la cual se recorrerá la cadena
     * @throws CircularInheritanceException si se vuelve a visitar un padre
     */
    public void checkCircularInheritance(Class currentClass) {
        validateInheritanceChain(currentClass, new HashSet<>());
    }

    /**
     * Recorre los padres hasta {@code Object} o una clase previamente comprobada.
     * Al regresar marca la clase como verificada para evitar recorridos repetidos.
     *
     * @param currentClass clase del paso actual
     * @param visited nombres de padres visitados en este recorrido
     * @throws CircularInheritanceException si el padre ya pertenece al conjunto
     */
    private void validateInheritanceChain(Class currentClass, Set<String> visited) {
        String parentName = currentClass.getParent();

        if(parentName.equals("Object")) {
            currentClass.setCircularInheritanceChecked(true);
        } else if (!currentClass.isCircularInheritanceChecked()) {

            if (!visited.add(parentName)) {
                throw new CircularInheritanceException(currentClass.getToken(), parentName, visited);
            }

            Class parentClass = classes.get(parentName);

            if (!parentClass.isCircularInheritanceChecked()) {
                validateInheritanceChain(parentClass, visited);
            }
            currentClass.setCircularInheritanceChecked(true);
        }
    }

    /**
     * Completa la TS después de reconocer todas las declaraciones y el fin de archivo.
     *
     * <p>Incorpora las clases predefinidas, comprueba las clases del usuario y
     * verifica las variables de {@code start}, en ese orden. Modifica la tabla
     * en memoria y propaga el primer error; no revierte las modificaciones
     * realizadas antes de la excepción ni comprueba las sentencias del AST.</p>
     *
     * @throws uncuyo.compiladores.exceptions.semantic.SemanticException si falla un control de declaraciones
     */
    public void consolidate() {
        init();
        checkClasses();
        checkStart();
    }

    /**
     * Serializa las clases y el método de entrada en orden de inserción.
     *
     * @param indent indentación inicial
     * @return objeto JSON con las entradas {@code classes} y {@code startMethod}
     */
    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");
        int total = this.getClasses().size();
        if (total > 0 ){
            sb.append(innerIndent).append("\"classes\": [\n");

            int i = 0;

            for (Class c : this.getClasses().values()) {
                sb.append(c.toJson(innerIndent + "\t"));

                if (++i < total) sb.append(",");
                sb.append("\n");
            }
            sb.append(innerIndent).append("],\n");

        }else {
            sb.append(innerIndent).append("\"classes\": [],\n");
        }

        sb.append(innerIndent).append("\"startMethod\": \n");
        sb.append(this.getStart().toJson(innerIndent)).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }

    /**
     * Registra {@code Object}, {@code IO}, {@code Int}, {@code Str}, {@code Bool}
     * y {@code Array}, junto con sus métodos predefinidos.
     *
     * <p>Se ejecuta al consolidar la tabla. Los tokens sintéticos tienen línea y
     * columna cero. Una entrada previa con uno de esos nombres se reemplaza.</p>
     */
    public void init() {
        Class Object = createPredefinedClass("Object", TokenType.OBJECT_CLASS, null);
        this.getClasses().put(Object.getToken().getLexeme(), Object);

        Class IO = createPredefinedClass("IO", TokenType.IO_CLASS, "Object");
        setPredefinedClassMethods(IO, "out_str", true, new Type(TokenType.KW_VOID, "void"),
                new Parameter(new Type(TokenType.STR_CLASS, "Str"), "s", 0));
        setPredefinedClassMethods(IO, "out_int", true, new Type(TokenType.KW_VOID, "void"),
                new Parameter(new Type(TokenType.INT_CLASS, "Int"), "i", 0));
        setPredefinedClassMethods(IO, "out_bool", true, new Type(TokenType.KW_VOID, "void"),
                new Parameter(new Type(TokenType.BOOL_CLASS, "Bool"), "b", 0));
        setPredefinedClassMethods(IO, "out_array_int", true, new Type(TokenType.KW_VOID, "void"),
                new Parameter(new ArrayType(new Type(TokenType.INT_CLASS, "Int")), "a", 0));
        setPredefinedClassMethods(IO, "out_array_str", true, new Type(TokenType.KW_VOID, "void"),
                new Parameter(new ArrayType(new Type(TokenType.STR_CLASS, "Str")), "a", 0));
        setPredefinedClassMethods(IO, "out_array_bool", true, new Type(TokenType.KW_VOID, "void"),
                new Parameter(new ArrayType(new Type(TokenType.BOOL_CLASS, "Bool")), "a", 0));
        setPredefinedClassMethods(IO, "in_str", true, new Type(TokenType.STR_CLASS, "Str"),
                 null);
        setPredefinedClassMethods(IO, "in_int", true, new Type(TokenType.INT_CLASS, "Int"),
                null);
        setPredefinedClassMethods(IO, "in_bool", true, new Type(TokenType.BOOL_CLASS, "Bool"),
                null);
        this.getClasses().put(IO.getToken().getLexeme(), IO);

        Class Int = createPredefinedClass("Int", TokenType.INT_CLASS, "Object");
        this.getClasses().put(Int.getToken().getLexeme(), Int);

        Class Str = createPredefinedClass("Str", TokenType.STR_CLASS, "Object");
        setPredefinedClassMethods(Str, "length", false, new Type(TokenType.INT_CLASS, "Int"),
                null);
        setPredefinedClassMethods(Str, "concat", false, new Type(TokenType.STR_CLASS, "Str"),
                new Parameter(new Type(TokenType.STR_CLASS, "Str"), "s", 0));
        this.getClasses().put(Str.getToken().getLexeme(), Str);

        Class Bool = createPredefinedClass("Bool", TokenType.BOOL_CLASS, "Object");
        this.getClasses().put(Bool.getToken().getLexeme(), Bool);

        Class Array = createPredefinedClass("Array", TokenType.ARRAY_CLASS, "Object");

        setPredefinedClassMethods(Array, "length", false, new Type(TokenType.INT_CLASS, "Int"),
                null);
        setPredefinedClassMethods(Array, "hasNext", false, new Type(TokenType.BOOL_CLASS, "Bool"),
                null);
        setPredefinedClassMethods(Array, "next_int", false, new Type(TokenType.INT_CLASS, "Int"),
                null);
        setPredefinedClassMethods(Array, "next_bool", false, new Type(TokenType.BOOL_CLASS, "Bool"),
                null);
        setPredefinedClassMethods(Array, "next_str", false, new Type(TokenType.STR_CLASS, "Str"),
                null);

        this.getClasses().put(Array.getToken().getLexeme(), Array);

    }

    /**
     * Crea una entrada predefinida con sus marcas de comprobación activadas.
     *
     * @param name nombre de la clase
     * @param type tipo de token del nombre predefinido
     * @param parent nombre del padre, o {@code null} para la raíz
     * @return nueva entrada, todavía no insertada en el mapa
     */
    private Class createPredefinedClass(String name, TokenType type, String parent) {
        Class c = new Class();
        setPredefinedClassAtributes(c, name, type);
        if (parent != null) {
            c.setParent(parent);
        }
        return c;
    }

    /**
     * Asigna un token sintético y marca una clase predefinida como declarada y comprobada.
     *
     * @param predefinedClass entrada que se inicializará
     * @param lexeme nombre predefinido
     * @param tokenType tipo de token asociado al nombre
     */
    public void setPredefinedClassAtributes(Class predefinedClass, String lexeme, TokenType tokenType) {
        predefinedClass.setFoundClass(true);
        predefinedClass.setFoundImpl(true);
        predefinedClass.setCircularInheritanceChecked(true);
        predefinedClass.setAtributesConsolidationChecked(true);
        predefinedClass.setMethodsConsolidationChecked(true);
        predefinedClass.setToken(new Token(lexeme, tokenType, 0, 0));
    }

    /**
     * Crea y registra un método predefinido con un token de posición cero.
     *
     * @param predefinedClass clase predefinida que recibirá el método
     * @param lexeme nombre del método
     * @param isStatic indica si el método es estático
     * @param type tipo de retorno
     * @param parameters parámetros con sus posiciones; admite {@code null} o un arreglo vacío
     */
    public void setPredefinedClassMethods(Class predefinedClass, String lexeme, boolean isStatic, Type type, Parameter... parameters) {
        Method method = new Method(new Token(lexeme, TokenType.IDENTIFIER_VAR_METHOD, 0, 0), type, isStatic);

        if (parameters != null && parameters.length > 0) {
            LinkedHashMap<String, Parameter> params = new LinkedHashMap<>();

            for (Parameter parameter : parameters) {
                params.put(parameter.getLexeme(), parameter);
            }

            method.setParameters(params);
        }

        predefinedClass.insertMethod(method);
    }

}
