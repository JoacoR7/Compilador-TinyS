package uncuyo.compiladores.semantic;

public class Parameter {

    private Type type;
    private String lexeme;
    private int position;

    public Parameter() {
    }

    public Parameter(Type type, int position, String lexeme) {
        this.type = type;
        this.position = position;
        this.lexeme = lexeme;
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
}
