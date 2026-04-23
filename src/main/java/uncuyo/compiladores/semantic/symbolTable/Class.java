package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.MultipleInheritanceException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.PrimitiveInheritanceException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.*;

public class Class implements JsonSerializable {

    private Token token;
    private LinkedHashMap<String, Attribute> attributes = new LinkedHashMap<>();
    private LinkedHashMap<String, Method> methods = new LinkedHashMap<>();
    private Constructor constructor = new Constructor();
    private String parent;
    private boolean foundClass;
    private boolean foundImpl;

    public Class() {
    }

    public Class(Token token) {
        this.token = token;
    }

    public Class(Token token, LinkedHashMap<String, Attribute> attributes, LinkedHashMap<String, Method> methods, Constructor constructor, String parent) {
        this.token = token;
        this.attributes = attributes;
        this.methods = methods;
        this.constructor = constructor;
        this.parent = parent;
    }

    public Class(Class other) {
        this.token = other.token;
        this.parent = other.parent;
        this.foundClass = other.foundClass;
        this.foundImpl = other.foundImpl;
        this.attributes = new LinkedHashMap<>();
        this.methods = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(LinkedHashMap<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public LinkedHashMap<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(LinkedHashMap<String, Method> methods) {
        this.methods = methods;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isFoundClass() {
        return foundClass;
    }

    public void setFoundClass(boolean foundClass) {
        this.foundClass = foundClass;
    }

    public boolean isFoundImpl() {
        return foundImpl;
    }

    public void setFoundImpl(boolean foundImpl) {
        this.foundImpl = foundImpl;
    }

    public int getLine() {
        return this.getToken().getRowIndex();
    }

    public int getColumn() {
        return this.getToken().getColumnIndex();
    }

    public String getName() {
        return this.getToken().getLexeme();
    }

    public Attribute searchAttribute(String attributeName){
        return attributes.getOrDefault(attributeName, null);
    }

    public void mergeDeclarations(Class newClassDeclaration) {
        mergeAttributes(newClassDeclaration);
        validateAndSetParent(newClassDeclaration);
        this.setFoundClass(newClassDeclaration.isFoundClass());
    }

    public void mergeImplements(Class newClassDeclaration) {
        mergeMethods(newClassDeclaration);
        this.setFoundImpl(newClassDeclaration.isFoundImpl());
    }

    private void mergeAttributes(Class newClassDeclaration) {
        for (Map.Entry<String, Attribute> entry : newClassDeclaration.getAttributes().entrySet()) {

            Attribute attribute = entry.getValue();

            if (this.getAttributes().containsKey(attribute.getName())) {
                throw new SemanticException(
                        attribute.getToken().getRowIndex(),
                        attribute.getToken().getColumnIndex(),
                        "El atributo '" + attribute.getName() +
                                "' ya esta definido en la clase '" +
                                this.getToken().getLexeme() + "'"
                );
            }

            this.insertAttribute(attribute);
        }
    }

    private void mergeMethods(Class newClassDeclaration) {
        for (Map.Entry<String, Method> entry : newClassDeclaration.getMethods().entrySet()) {

            Method method = entry.getValue();

            if (this.getMethods().containsKey(method.getName())) {
                throw new SemanticException(
                        method.getToken().getRowIndex(),
                        method.getToken().getColumnIndex(),
                        "El metodo '" + method.getName() + "' ya esta definido para la clase '" +
                                newClassDeclaration.getToken().getLexeme() + "' en la linea " +
                                this.getMethods().get(method.getName()).getToken().getRowIndex()
                );
            }
            this.insertMethod(method);
        }
    }

    public void validateParent() {
        String newClassDeclarationParent = this.getParent();
        List<String> primitiveTypes = List.of("Int", "Bool", "Str");

        if (newClassDeclarationParent != null && primitiveTypes.contains(newClassDeclarationParent)) {
            throw new PrimitiveInheritanceException(this.getLine(),
                    this.getColumn(), newClassDeclarationParent);
        }
    }

    public void validateAndSetParent(Class newClassDeclaration) {
        String currentParent = this.getParent();
        String newClassDeclarationParent = newClassDeclaration.getParent();

        newClassDeclaration.validateParent();

        if (newClassDeclarationParent == null) {
            // Caso de implements
        } else if (currentParent == null) {
            this.setParent(newClassDeclarationParent);
            this.setToken(newClassDeclaration.getToken());
        } else {
            boolean differentParents = !currentParent.equals(newClassDeclarationParent);

            if (differentParents) {
                throw new MultipleInheritanceException(newClassDeclaration.getLine(), newClassDeclaration.getColumn(),
                        this.getName(), newClassDeclarationParent, currentParent, this.getLine());
            }
        }


    }

    public boolean attributeAlreadyExists(String attributeName){
        return this.searchAttribute(attributeName) != null;
    }

    public void insertMethod(Method newMethod){
        newMethod.setPosition(this.getMethods().size());
        this.getMethods().put(newMethod.getToken().getLexeme(), newMethod);
    }

    public void insertAttribute(Attribute newAttribute){
        newAttribute.setPosition(this.getAttributes().size());
        this.getAttributes().put(newAttribute.getToken().getLexeme(), newAttribute);
    }


    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getToken().getLexeme()).append("\",\n");

        sb.append(indent).append("\t\"parent\": \"")
                .append(getParent()).append("\",\n");

        sb.append(indent).append("\t\"foundClass\": ")
                .append(isFoundClass()).append(",\n");

        sb.append(indent).append("\t\"foundImpl\": ")
                .append(isFoundImpl()).append(",\n");

        // ATTRIBUTES
        sb.append(indent).append("\t\"attributes\": [\n");

        int i = 0;
        int total = getAttributes().size();

        for (Map.Entry<String, Attribute> entry : this.getAttributes().entrySet()) {
            Attribute a = entry.getValue();
            sb.append(a.toJson(indent + "\t\t"));

            if (++i < total) sb.append(",");
            sb.append("\n");
        }

        sb.append(indent).append("\t],\n");

        // METHODS
        sb.append(indent).append("\t\"methods\": [\n");

        i = 0;
        total = getMethods().size();

        for (Map.Entry<String, Method> entry : this.getMethods().entrySet()) {
            Method m = entry.getValue();

            sb.append(m.toJson(indent + "\t\t"));

            if (++i < total) sb.append(",");
            sb.append("\n");
        }

        sb.append(indent).append("\t]\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
