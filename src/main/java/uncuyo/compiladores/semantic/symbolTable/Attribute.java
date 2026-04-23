package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.Token;

public class Attribute {

    private Type type;
    private Token token;
    private int position;
    private boolean isPrivate;
    private boolean isInherited;

    public Attribute() {
    }

    public Attribute(Token token, Type type, boolean isPrivate) {
        this.token = token;
        this.type = type;
        this.isPrivate = isPrivate;
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
}
