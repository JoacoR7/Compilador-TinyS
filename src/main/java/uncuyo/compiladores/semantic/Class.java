package uncuyo.compiladores.semantic;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;

import java.util.Hashtable;
import java.util.List;

public class Class {

    private Token token;
    private Hashtable<String, Atribute> atributes = new Hashtable<>();
    private Hashtable<String, Method> methods = new Hashtable<>();
    private Constructor constructor = new Constructor();
    private String parent = "Object";

    public Class() {
    }

    public Class(Token token) {
        this.token = token;
    }

    public Class(Token token, Hashtable<String, Atribute> atributes, Hashtable<String, Method> methods, Constructor constructor, String parent) {
        this.token = token;
        this.atributes = atributes;
        this.methods = methods;
        this.constructor = constructor;
        this.parent = parent;
    }

    public Hashtable<String, Atribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(Hashtable<String, Atribute> atributes) {
        this.atributes = atributes;
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
}
