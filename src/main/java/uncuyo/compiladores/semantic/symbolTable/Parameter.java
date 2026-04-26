package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.semantic.JsonSerializable;

public class Parameter implements JsonSerializable {

    private Type type;
    private String lexeme;
    private int position;

    public Parameter() {
    }

    public Parameter(Type type, String lexeme, int position) {
        this.type = type;
        this.lexeme = lexeme;
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getLexeme()).append("\",\n");

        sb.append(indent).append("\t\"type\": \"")
                .append(type.getLexeme()).append("\",\n");

        sb.append(indent).append("\t\"position\": ")
                .append(position).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
