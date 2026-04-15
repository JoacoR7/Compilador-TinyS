package uncuyo.compiladores.semantic;

public class Atribute {

    private Type type;
    private String lexeme;
    private int position;
    private boolean isPrivate;

    public Atribute() {
    }

    public Atribute(String lexeme, Type type, boolean isPrivate, int position) {
        this.lexeme = lexeme;
        this.type = type;
        this.isPrivate = isPrivate;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
