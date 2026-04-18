package uncuyo.compiladores.lexer;

import java.util.Objects;

public class Token {
    private int rowIndex;
    private int columnIndex;
    private String lexeme;
    private TokenType tokenType;

    public Token() {
    }
    public Token(String lexeme, TokenType tokenType, int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.lexeme = lexeme;
        this.tokenType = tokenType;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "Token{" +
                "rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                ", lexeme='" + lexeme + '\'' +
                ", tokenType=" + tokenType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return rowIndex == token.rowIndex && columnIndex == token.columnIndex && Objects.equals(lexeme, token.lexeme) && tokenType == token.tokenType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, columnIndex, lexeme, tokenType);
    }

    public static Token copy(Token token){
        Token newToken = new Token();
        newToken.rowIndex = token.getRowIndex();
        newToken.columnIndex = token.getColumnIndex();
        newToken.lexeme = token.getLexeme();
        newToken.tokenType = token.getTokenType();
        return newToken;
    }
}
