package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

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
        boolean isLeftSideArray = t1.getTokenType() == TokenType.ARRAY_CLASS;
        TokenType a = t1 != null ?
                (t1.getTokenType() != TokenType.ARRAY_CLASS ? t1.getTokenType() : ((ArrayType) t1).getSubType().getTokenType())
                : null;
        TokenType b = t2 != null ?
                (t2.getTokenType() != TokenType.ARRAY_CLASS ? t2.getTokenType() : ((ArrayType) t2).getSubType().getTokenType())
                : null;

        return a == b
                || (a == TokenType.STR_CLASS && b == TokenType.STRING_CONSTANT)
                || (a == TokenType.STRING_CONSTANT && b == TokenType.STR_CLASS)
                || (a == TokenType.INT_CLASS && b == TokenType.INT_CONSTANT)
                || (a == TokenType.INT_CONSTANT && b == TokenType.INT_CLASS)
                || (a == TokenType.BOOL_CLASS && b == TokenType.BOOLEAN_CONSTANT)
                || (a == TokenType.BOOLEAN_CONSTANT && b == TokenType.BOOL_CLASS)
                || (a == TokenType.KW_VOID && b == null)
                || (a == null && b == TokenType.KW_VOID)
                || (a == TokenType.IDENTIFIER_CLASS && b == TokenType.KW_NIL)
                || (isLeftSideArray && b == TokenType.KW_NIL);
    }

    public String getTypeName() {
        return switch (tokenType) {
            case INT_CONSTANT, INT_CLASS -> "Int";
            case STRING_CONSTANT, STR_CLASS -> "Str";
            case BOOLEAN_CONSTANT, BOOL_CLASS -> "Bool";
            case KW_VOID -> "void";
            case ARRAY_CLASS -> "Array " + ((ArrayType) this).getSubType().getTypeName();
            default -> lexeme;
        };
    }

    public static Type promote(Type t1, Type t2) {
        if (t1.getTokenType().name().endsWith("_CLASS")) {
            return t1;
        } else if (!t2.getTokenType().name().endsWith("_class")) {
            return t1;
        }

        return t2;
    }

    public static boolean isPrimitiveType(String typeName) {
        List<String> primitiveTypes = List.of("Int", "Str", "Bool", "Array");
        return primitiveTypes.contains(typeName);
    }
}
