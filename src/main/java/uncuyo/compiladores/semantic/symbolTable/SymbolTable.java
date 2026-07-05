package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.exceptions.semantic.symbolTableException.*;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.ClassNotFoundException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.*;
import java.util.stream.Collectors;

public class SymbolTable implements JsonSerializable {

    private LinkedHashMap<String, Class> classes;
    private Class currentClass;
    private Method currentMethod;
    private Block currentBlock;
    private Method start;

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

    public Class searchOriginalClass(String idclass) {
        return classes.getOrDefault(idclass, null);
    }

    public Class searchAndCopyClass(String idclass) {
        Class original = classes.getOrDefault(idclass, null);
        if (original != null) {
            original = new Class(original);
        }
        return original;
    }

    public void saveClass(Class newClass) {
        newClass.validateParent();
        classes.put(newClass.getToken().getLexeme(), newClass);
    }

    public void mergeClassDeclarations(Class existingClass) {
        existingClass.mergeDeclarations(this.currentClass);
    }

    public void mergeClassImplements(Class existingClass) {
        existingClass.mergeImplements(this.currentClass);
    }

    public void checkParentExistence(Class currentClass) {
        String parentName = currentClass.getParent();
        if(!classes.containsKey(parentName)) {
            throw new ParentClassNotFoundException(currentClass.getToken(), parentName);
        }
    }

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

    public void checkCircularInheritance(Class currentClass) {
        validateInheritanceChain(currentClass, new HashSet<>());
    }

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

    public void consolidate() {
        init();
        checkClasses();
        checkStart();
    }

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
        setPredefinedClassMethods(Str, "concat", true, new Type(TokenType.STR_CLASS, "Str"),
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

    private Class createPredefinedClass(String name, TokenType type, String parent) {
        Class c = new Class();
        setPredefinedClassAtributes(c, name, type);
        if (parent != null) {
            c.setParent(parent);
        }
        return c;
    }

    public void setPredefinedClassAtributes(Class predefinedClass, String lexeme, TokenType tokenType) {
        predefinedClass.setFoundClass(true);
        predefinedClass.setFoundImpl(true);
        predefinedClass.setCircularInheritanceChecked(true);
        predefinedClass.setAtributesConsolidationChecked(true);
        predefinedClass.setMethodsConsolidationChecked(true);
        predefinedClass.setToken(new Token(lexeme, tokenType, 0, 0));
    }

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
