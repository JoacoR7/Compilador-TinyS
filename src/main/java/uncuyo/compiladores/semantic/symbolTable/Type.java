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

    public static boolean areCompatible(Type t1, Type t2) {
        TokenType a = t1 != null ? t1.getTokenType() : null;
        TokenType b = t2 != null ? t2.getTokenType() : null;

        return a == b
                || (a == TokenType.STR_CLASS && b == TokenType.STRING_CONSTANT)
                || (a == TokenType.STRING_CONSTANT && b == TokenType.STR_CLASS)
                || (a == TokenType.INT_CLASS && b == TokenType.INT_CONSTANT)
                || (a == TokenType.INT_CONSTANT && b == TokenType.INT_CLASS)
                || (a == TokenType.BOOL_CLASS && b == TokenType.BOOLEAN_CONSTANT)
                || (a == TokenType.BOOLEAN_CONSTANT && b == TokenType.BOOL_CLASS)
                || (a == TokenType.KW_VOID && b == null)
                || (a == null && b == TokenType.KW_VOID);
    }

    public String getTypeName() {
        return switch (tokenType) {
            case INT_CONSTANT, INT_CLASS -> "Int";
            case STRING_CONSTANT, STR_CLASS -> "Str";
            case BOOLEAN_CONSTANT, BOOL_CLASS -> "Bool";
            case KW_VOID -> "void";
            default -> lexeme;
        };
    }
}
