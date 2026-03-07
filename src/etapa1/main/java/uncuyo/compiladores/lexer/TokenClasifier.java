package uncuyo.compiladores.lexer;

import java.io.IOException;

public class TokenClasifier {
    private Lexer lexer;
    private TokenDefinitions tokenDefinitions;

    public TokenClasifier(Lexer lexer) {
        this.lexer = lexer;
        this.tokenDefinitions = new TokenDefinitions();
    }

    public Token isCommentOrDiv() throws IOException {

        if (lexer.getColumnIndex() + 1 >= lexer.getCurrentData().length()){
            return classifyOperator();
        }

        char nextCharacter = lexer.getCurrentData().charAt(lexer.getColumnIndex() + 1);
        switch (nextCharacter) {

            case '*':
                lexer.incrementPosition();
                lexer.incrementPosition();
                multiComment();
                return null;

            case '/':
                lexer.incrementPosition();
                lexer.incrementPosition();
                simpleComment();
                return null;

            default:
                return classifyOperator();
        }
    }

    public void multiComment() throws IOException {
        while (true) {

            if (lexer.getColumnIndex() + 1 >= lexer.getCurrentData().length()) {
                if (!lexer.loadNextLine()) {
                    throw new RuntimeException(
                            "Comentario multilinea no cerrado"
                    );
                }
                continue;
            }

            if (lexer.getCurrentData().charAt(lexer.getColumnIndex()) == '*' &&
                    lexer.getCurrentData().charAt(lexer.getColumnIndex() + 1) == '/') {

                lexer.incrementPosition();
                lexer.incrementPosition();
                return;
            }

            lexer.incrementPosition();
        }
    }
    public void simpleComment() throws IOException {
        lexer.loadNextLine();
        return;
    }

    public Token classifyOperator(){

        String operator = String.valueOf(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
        TokenType tokenType = null;
        int initialPosition = lexer.getColumnIndex(); // Columna donde empieza el token

        if (tokenDefinitions.getOperators().containsKey(operator)) {

            if (lexer.getColumnIndex() + 1 < lexer.getCurrentData().length()) {
                char vistazo = lexer.getCurrentData().charAt(lexer.getColumnIndex() + 1);
                if (tokenDefinitions.getOperators().containsKey(operator + vistazo)) {
                    tokenType = tokenDefinitions.getOperators().get(operator + vistazo);
                    operator = operator + vistazo;
                    lexer.incrementPosition();
                }else {
                    tokenType = tokenDefinitions.getOperators().get(operator);
                }
            } else {
                tokenType = tokenDefinitions.getOperators().get(operator);
            }

        }
        lexer.incrementPosition();
        return generateToken(operator, tokenType, lexer.getRowIndex(), initialPosition);
    }

    public Token isNumber() {
        int initialPosition = lexer.getColumnIndex();
        StringBuilder digits = new StringBuilder();
        char currentChar = lexer.getCurrentChar();
        digits.append(currentChar);
        lexer.incrementPosition();

        while ((lexer.getColumnIndex() < lexer.getCurrentData().length()) && Character.isDigit(currentChar)) {
            currentChar = lexer.getCurrentChar();

            if (Character.isDigit(currentChar)) {
                digits.append(currentChar);
                lexer.incrementPosition();
            }
        }

        return generateToken(digits.toString(), TokenType.INT_CONSTANT, lexer.getRowIndex(), initialPosition);
    }

    private Token generateToken(String lexema, TokenType tokenType, int indexRow, int indexColumn){
        return new Token(lexema , tokenType, indexRow, indexColumn + 1);
    }


}
