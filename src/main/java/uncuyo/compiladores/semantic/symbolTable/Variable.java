package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

/**
 * Variable local con tipo, token de declaración y posición.
 * El token conserva el nombre y la ubicación,
 * el método propietario asigna la posición al registrar la variable.
 */
public class Variable implements JsonSerializable {
    private Type type;
    private Token token;
    private int position;

    /**
     * Crea una variable antes de insertarla en su ámbito.
     *
     * @param token identificador de la declaración y su ubicación
     * @param type tipo declarado
     */
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

    /**
     * Exporta nombre, lexema del tipo y posición de la variable.
     * Para un arreglo, el lexema exportado es {@code Array}, sin su subtipo.
     *
     * @param indent sangría para los campos del objeto
     * @return representación JSON de la variable
     */
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
