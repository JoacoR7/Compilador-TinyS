package uncuyo.compiladores.lexer;

import java.io.IOException;
import java.util.Optional;

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

    public Token classifyClassIdentifier(){

        StringBuilder buffer = new StringBuilder();
        TokenType tokenType = TokenType.IDENTIFIER_CLASS;
        int initialPosition = lexer.getColumnIndex();
        char vistazo;
        do {

            buffer.append(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
            lexer.incrementPosition();

            if (lexer.getColumnIndex() < lexer.getCurrentData().length()){
                vistazo = lexer.getCurrentData().charAt(lexer.getColumnIndex());
            }else {
                vistazo = ' ';
            }

        } while (Character.isLetterOrDigit(vistazo) || vistazo == '_');

        return generateToken(buffer.toString(), tokenType, lexer.getRowIndex(), initialPosition);
    }

    public Token classifyKeywordOrIdentifier(){

        StringBuilder buffer = new StringBuilder();
        int initialPosition = lexer.getColumnIndex();

        char vistazo;
        do {

            buffer.append(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
            lexer.incrementPosition();

            if (lexer.getColumnIndex() < lexer.getCurrentData().length()){
                vistazo = lexer.getCurrentData().charAt(lexer.getColumnIndex());
            }else {
                vistazo = ' ';
            }

        } while (Character.isLetterOrDigit(vistazo) || vistazo == '_');

        String palabra = buffer.toString();

        Optional<TokenType> tokenType = Optional.ofNullable(tokenDefinitions.getKeywords().get(palabra));

        return generateToken(palabra, tokenType.orElse(TokenType.IDENTIFIER_OBJECT), lexer.getRowIndex(), initialPosition);
    }

    public Token classifySpecialSymbol() {

        String palabra = String.valueOf(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
        int initialPosition = lexer.getColumnIndex();

        Optional<TokenType> tokenType = Optional
                .ofNullable(tokenDefinitions.getSpecialSymbols()
                        .get(lexer.getCurrentData().charAt(lexer.getColumnIndex()))); // Me fijo si es un simbolo especial

        lexer.incrementPosition();
        return generateToken(palabra, tokenType.orElse(TokenType.IDENTIFIER_OBJECT), lexer.getRowIndex(), initialPosition);
    }

    private Token generateToken(String lexema, TokenType tokenType, int indexRow, int indexColumn){
        return new Token(lexema , tokenType, indexRow, indexColumn + 1);
    }


}
