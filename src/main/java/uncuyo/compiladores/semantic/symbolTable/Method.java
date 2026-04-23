package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.Hashtable;
import java.util.LinkedHashMap;

public class Method implements JsonSerializable {

    private Token token;
    private LinkedHashMap<String, Parameter> parameters;
    private LinkedHashMap<String, Attribute> atributes;
    private Type type;
    private boolean isStatic;
    private boolean isInherited;
    private boolean isRedefined;
    private int position;

    public Method(Token token, Type type, boolean isStatic) {
        this.token = token;
        this.type = type;
        this.isStatic = isStatic;
    }

    public Method(Token token, LinkedHashMap<String, Parameter> parameters, LinkedHashMap<String, Attribute> atributes, Type type, boolean isStatic) {
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

    public LinkedHashMap<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public LinkedHashMap<String, Attribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(LinkedHashMap<String, Attribute> atributes) {
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

    public String getName() {
        return token.getLexeme();
    }

    public void insertParameter(Type type, String name) {
        int position = this.getParameters().size();
        this.getParameters().put(name, new Parameter(type, name, position));
    }

    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getToken().getLexeme()).append("\",\n");

        sb.append(indent).append("\t\"type\": \"")
                .append(getType().getLexeme()).append("\",\n");

        sb.append(indent).append("\t\"static\": ")
                .append(isStatic()).append(",\n");

        sb.append(indent).append("\t\"position\": ")
                .append(getPosition()).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
