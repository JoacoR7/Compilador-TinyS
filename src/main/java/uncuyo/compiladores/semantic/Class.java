package uncuyo.compiladores.semantic;

import uncuyo.compiladores.lexer.Token;

import java.util.Hashtable;

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

    public void saveAttribute(String attributeName, Type type , boolean isPrivate){
        Attribute attribute = new Attribute(attributeName, type, isPrivate, this.attributes.size());
        this.attributes.put(attributeName, attribute);
    }
}
