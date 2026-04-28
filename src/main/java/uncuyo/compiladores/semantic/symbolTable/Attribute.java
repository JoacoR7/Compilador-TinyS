package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

public class Attribute implements JsonSerializable {

    private Type type;
    private Token token;
    private int position;
    private boolean isPrivate;
    private boolean isInherited;
    private String className;

    public Attribute() {
    }

    public Attribute(Token token, Type type, boolean isPrivate) {
        this.token = token;
        this.type = type;
        this.isPrivate = isPrivate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getName() {
        return token.getLexeme();
    }

    public boolean isInherited() {
        return isInherited;
    }

    public void setInherited(boolean inherited) {
        isInherited = inherited;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getName()).append("\",\n");

        sb.append(indent).append("\t\"type\": \"")
                .append(type.getLexeme()).append("\",\n");

        sb.append(indent).append("\t\"className\": \"")
                .append(getClassName()).append("\",\n");

        sb.append(indent).append("\t\"private\": ")
                .append(isPrivate).append(",\n");

        sb.append(indent).append("\t\"position\": ")
                .append(position).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
