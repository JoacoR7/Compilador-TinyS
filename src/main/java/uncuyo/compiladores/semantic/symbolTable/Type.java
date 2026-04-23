package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.TokenType;

public class Type {

    private TokenType tokenType;
    private String lexeme;

    public Type() {
    }

    public Type(TokenType tokenType, String lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
}
