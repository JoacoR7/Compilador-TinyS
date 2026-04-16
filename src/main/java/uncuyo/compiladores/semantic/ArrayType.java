package uncuyo.compiladores.semantic;

import uncuyo.compiladores.lexer.TokenType;

public class ArrayType extends Type{
    private Type subType;

    public ArrayType(Type subType) {
        super(TokenType.ARRAY_CLASS, "Array");
        this.subType = subType;
    }

    public Type getSubType() {
        return subType;
    }

    public void setSubType(Type subType) {
        this.subType = subType;
    }
}
