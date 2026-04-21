package uncuyo.compiladores.semantic;

import uncuyo.compiladores.exceptions.SemanticException;
import uncuyo.compiladores.lexer.Token;

import java.util.Hashtable;
import java.util.Map;

public class Class {

    private Token token;
    private Hashtable<String, Attribute> attributes = new Hashtable<>();
    private Hashtable<String, Method> methods = new Hashtable<>();
    private Constructor constructor = new Constructor();
    private String parent = "Object";

    public Class() {
    }

    public Class(Token token) {
        this.token = token;
    }

    public Class(Token token, Hashtable<String, Attribute> attributes, Hashtable<String, Method> methods, Constructor constructor, String parent) {
        this.token = token;
        this.attributes = attributes;
        this.methods = methods;
        this.constructor = constructor;
        this.parent = parent;
    }

    public Hashtable<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Hashtable<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public Hashtable<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(Hashtable<String, Method> methods) {
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

    public Attribute searchAttribute(String attributeName){
        return attributes.getOrDefault(attributeName, null);
    }

    public void saveAttribute(Token token, Type type , boolean isPrivate){
        Attribute attribute = new Attribute(token, type, isPrivate, this.attributes.size());
        this.attributes.put(token.getLexeme(), attribute);
    }

    public void merge(Class newClassDeclaration) {
        mergeAttributes(newClassDeclaration);
        mergeMethods(newClassDeclaration);
        validateAndSetParent(newClassDeclaration);
    }

    private void mergeAttributes(Class newClassDeclaration) {
        for (Map.Entry<String, Attribute> entry : newClassDeclaration.getAttributes().entrySet()) {

            if (this.getAttributes().containsKey(entry.getKey())) {
                throw new SemanticException(
                        newClassDeclaration.getToken().getRowIndex(),
                        newClassDeclaration.getToken().getColumnIndex(),
                        "El atributo '" + entry.getKey() + "' ya esta definido"
                );
            }

            this.getAttributes().put(entry.getKey(), entry.getValue());
        }
    }

    private void mergeMethods(Class newClassDeclaration) {
        for (Map.Entry<String, Method> entry : newClassDeclaration.getMethods().entrySet()) {

            if (this.getMethods().containsKey(entry.getKey())) { // <-- FIX
                throw new SemanticException(
                        newClassDeclaration.getToken().getRowIndex(),
                        newClassDeclaration.getToken().getColumnIndex(),
                        "El metodo '" + entry.getKey() + "' ya esta definido"
                );
            }

            newClassDeclaration.getMethods().put(entry.getKey(), entry.getValue());
        }
    }

    private void validateAndSetParent(Class newClassDeclaration) {
        String currentParent = this.getParent();
        String newClassDeclarationParent = newClassDeclaration.getParent();

        boolean differentParents = !currentParent.equals(newClassDeclarationParent);

        if (differentParents) {
            throw new SemanticException(
                    newClassDeclaration.getToken().getRowIndex(),
                    newClassDeclaration.getToken().getColumnIndex(),
                    "La clase '" + newClassDeclaration.getToken().getLexeme() +
                            "' no puede heredar de '" + newClassDeclarationParent +
                            "' por que ya hereda de '" + currentParent + "' en la línea " +
                            this.getToken().getRowIndex()
            );
        }
    }

    public boolean attributeAlreadyExists(String attributeName){
        return this.searchAttribute(attributeName) != null;
    }

    public void insertMethod(Method newMethod){
        newMethod.setPosition(this.getMethods().size());
        this.getMethods().put(newMethod.getToken().getLexeme(), newMethod);
    }
}
