package uncuyo.compiladores.semantic;

import uncuyo.compiladores.lexer.Token;

import java.util.Hashtable;

public class Method {

    private Token token;
    private Hashtable<String, Parameter> parameters;
    private Hashtable<String, Attribute> atributes;
    private Type type;
    private boolean isStatic;
    private int position;

    public Method(Token token, Type type, boolean isStatic) {
        this.token = token;
        this.type = type;
        this.isStatic = isStatic;
    }

    public Method(Token token, Hashtable<String, Parameter> parameters, Hashtable<String, Attribute> atributes, Type type, boolean isStatic) {
        this.token = token;
        this.parameters = parameters;
        this.atributes = atributes;
        this.type = type;
        this.isStatic = isStatic;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Hashtable<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Hashtable<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Hashtable<String, Attribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(Hashtable<String, Attribute> atributes) {
        this.atributes = atributes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void insertParameter(Type type, String name) {
        int position = this.getParameters().size();
        this.getParameters().put(name, new Parameter(type, name, position));
    }
}
