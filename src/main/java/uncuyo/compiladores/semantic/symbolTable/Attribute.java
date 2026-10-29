package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

/**
 * Entrada de un atributo de clase en la tabla de símbolos.
 * Conserva token, tipo, posición, privacidad, si es heredado y la clase de origen.
 */
public class Attribute implements JsonSerializable {

    private Type type;
    private Token token;
    private int position;
    private boolean isPrivate;
    private boolean isInherited;
    private String className;

    public Attribute() {
    }

    /**
     * Crea un atributo antes de asignarle su posición y clase de origen.
     *
     * @param token identificador declarado y su ubicación
     * @param type tipo declarado
     * @param isPrivate {@code true} si la declaración no incluye {@code pub}
     */
    public Attribute(Token token, Type type, boolean isPrivate) {
        this.token = token;
        this.type = type;
        this.isPrivate = isPrivate;
    }

    /**
     * Copia los datos de un atributo para incorporarlo a una clase hija.
     * El token y el tipo se comparten.
     *
     * @param other atributo de la clase padre
     */
    public Attribute(Attribute other) {
        this.type = other.type;
        this.position = other.position;
        this.token = other.token;
        this.isPrivate = other.isPrivate;
        this.isInherited = other.isInherited;
        this.className = other.className;
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

    /**
     * Exporta nombre, lexema del tipo, clase de origen, privacidad y posición.
     *
     * @param indent indentación inicial
     * @return representación JSON del atributo
     */
    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getName()).append("\",\n");

        sb.append(indent).append("\t\"type\": \"")
                .append(type.getLexeme()).append("\",\n");

        if (type instanceof ArrayType) {
            sb.append(indent).append("\t\"subtype\": \"")
                    .append(((ArrayType) type).getSubType().getLexeme()).append("\",\n");
        }

        sb.append(indent).append("\t\"className\": \"")
                .append(getClassName()).append("\",\n");

        sb.append(indent).append("\t\"isInherited\": \"")
                .append(isInherited).append("\",\n");

        sb.append(indent).append("\t\"private\": ")
                .append(isPrivate).append(",\n");

        sb.append(indent).append("\t\"position\": ")
                .append(position).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
