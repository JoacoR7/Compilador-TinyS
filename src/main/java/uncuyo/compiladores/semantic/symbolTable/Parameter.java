package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.semantic.JsonSerializable;

/**
 * Parámetro formal con nombre, tipo y posición desde cero.
 * La posición conserva el orden de la firma para comparar redefiniciones.
 */
public class Parameter implements JsonSerializable {

    private Type type;
    private String lexeme;
    private int position;

    public Parameter() {
    }

    /**
     * Crea la entrada de un parámetro formal.
     *
     * @param type tipo declarado
     * @param lexeme nombre del parámetro
     * @param position posición en la firma, comenzando en cero
     */
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

    /**
     * Exporta nombre, tipo y posición del parámetro formal.
     * Para los arreglos también incluye el tipo de elemento.
     *
     * @param indent indentación para los campos del objeto
     * @return representación JSON del parámetro
     */
    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getLexeme()).append("\",\n");

        if (getType() instanceof ArrayType) {
            sb.append(indent).append("\t\"type\": \"")
                    .append(getType().getLexeme()).append(" ").
                    append(((ArrayType) getType()).getSubType().getLexeme()).append("\",\n");
        } else {
            sb.append(indent).append("\t\"type\": \"")
                    .append(getType().getLexeme()).append("\",\n");
        }

        sb.append(indent).append("\t\"position\": ")
                .append(position).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
