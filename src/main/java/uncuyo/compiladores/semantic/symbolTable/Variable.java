package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

public class Variable implements JsonSerializable {
    private Type type;
    private Token token;
    private int position;

    public Variable(Token token, Type type) {
        this.type = type;
        this.token = token;
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

    public String getName() {
        return getToken().getLexeme();
    }

    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getName()).append("\",\n");

        sb.append(indent).append("\t\"type\": \"")
                .append(type.getLexeme()).append("\",\n");


        sb.append(indent).append("\t\"position\": ")
                .append(position).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
