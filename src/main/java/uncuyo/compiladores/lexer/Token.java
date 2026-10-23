
package uncuyo.compiladores.lexer;

import java.util.Objects;

/**
 * Representa un token identificado durante el análisis léxico de un código fuente.
 * <p>
 * Un token contiene el lexema reconocido, su tipo y su posición dentro del
 * código fuente, especificada mediante el índice de fila y columna.
 */
public class Token {

    /** Índice de la fila en la que se encuentra el token. */
    private int rowIndex;

    /** Índice de la columna en la que comienza el token. */
    private int columnIndex;

    /** Cadena de caracteres que representa el lexema del token. */
    private String lexeme;

    /** Tipo de token identificado por el analizador léxico. */
    private TokenType tokenType;

    /**
     * Construye un token sin inicializar sus atributos explícitamente.
     * Los atributos tendrán sus valores predeterminados.
     */
    public Token() {
    }

    /**
     * Construye un token con su lexema, tipo y posición en el código fuente.
     *
     * @param lexeme     cadena de caracteres que representa el lexema
     * @param tokenType  tipo de token identificado
     * @param rowIndex   índice de la fila donde se encuentra el token
     * @param columnIndex índice de la columna donde comienza el token
     */
    public Token(String lexeme, TokenType tokenType, int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.lexeme = lexeme;
        this.tokenType = tokenType;
    }

    /**
     * Obtiene el índice de la fila donde se encuentra el token.
     *
     * @return índice de la fila del token
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Establece el índice de la fila donde se encuentra el token.
     *
     * @param rowIndex nuevo índice de la fila
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * Obtiene el índice de la columna donde comienza el token.
     *
     * @return índice de la columna del token
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Establece el índice de la columna donde comienza el token.
     *
     * @param columnIndex nuevo índice de la columna
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * Obtiene el lexema asociado al token.
     *
     * @return cadena de caracteres que representa el lexema
     */
    public String getLexeme() {
        return lexeme;
    }

    /**
     * Establece el lexema asociado al token.
     *
     * @param lexeme nuevo lexema del token
     */
    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    /**
     * Obtiene el tipo del token.
     *
     * @return tipo de token
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Devuelve una representación textual del token con sus atributos.
     *
     * @return cadena que contiene la posición, el lexema y el tipo del token
     */
    @Override
    public String toString() {
        return "Token{" +
                "rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                ", lexeme='" + lexeme + '\'' +
                ", tokenType=" + tokenType +
                '}';
    }

    /**
     * Compara este token con otro objeto para determinar si son iguales.
     * Dos tokens son iguales si tienen los mismos índices de fila y columna,
     * el mismo lexema y el mismo tipo.
     *
     * @param o objeto con el que se compara el token
     * @return {@code true} si ambos objetos son iguales; {@code false} en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return rowIndex == token.rowIndex
                && columnIndex == token.columnIndex
                && Objects.equals(lexeme, token.lexeme)
                && tokenType == token.tokenType;
    }

    /**
     * Calcula el código hash del token a partir de sus atributos.
     *
     * @return código hash del token
     */
    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, columnIndex, lexeme, tokenType);
    }

}
