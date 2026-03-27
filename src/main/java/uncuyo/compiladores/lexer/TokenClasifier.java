package uncuyo.compiladores.lexer;

import uncuyo.compiladores.exceptions.LexerFileReadException;
import uncuyo.compiladores.exceptions.LexicalException;

import java.util.Optional;

public class TokenClasifier {
    private Lexer lexer;
    private TokenDefinitions tokenDefinitions;

    public TokenClasifier(Lexer lexer) {
        this.lexer = lexer;
        this.tokenDefinitions = new TokenDefinitions();
    }

    public Token isCommentOrDiv() throws LexerFileReadException, LexicalException {

        if (lexer.getColumnIndex() + 1 >= lexer.getCurrentData().length()){
            return classifyOperator();
        }

        char nextCharacter = lexer.getCurrentData().charAt(lexer.getColumnIndex() + 1);
        switch (nextCharacter) {

            case '*':
                lexer.incrementPosition();
                lexer.incrementPosition();
                multiComment(lexer.getRowIndex(), lexer.getColumnIndex());
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

    public void multiComment(int initRowIndex, int initColumnIndex) throws LexerFileReadException, LexicalException {
        while (true) {

            if (lexer.getColumnIndex() + 1 >= lexer.getCurrentData().length()) {
                if (!lexer.loadNextLine()) {
                    throw new LexicalException(
                            initRowIndex,
                            initColumnIndex,
                            "COMENTARIO MULTILÍNEA NO CERRADO"
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
    public void simpleComment() throws LexerFileReadException {
        lexer.loadNextLine();
    }

    public Token classifyOperator() throws LexicalException {

        String operator = String.valueOf(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
        TokenType tokenType = TokenType.UNDEFINED;
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

        if (tokenType.equals(TokenType.UNDEFINED)){
            throw new LexicalException(
                    lexer.getRowIndex(),
                    initialPosition + 1,
                    "Simbolo '" + operator + "' no reconocido"
            );
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

        while ((!lexer.isEndOfLine()) && Character.isDigit(currentChar)) {
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

            buffer.append(lexer.getCurrentChar());
            lexer.incrementPosition();

            // Verifico si puedo seguir consumiendo caracteres de la línea actual
            if (!lexer.isEndOfLine()){
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

            if (!lexer.isEndOfLine()){
                vistazo = lexer.getCurrentData().charAt(lexer.getColumnIndex());
            }else {
                vistazo = ' ';
            }

        } while (Character.isLetterOrDigit(vistazo) || vistazo == '_');

        String palabra = buffer.toString();

        // Verifico si la palabra es un keyword, en caso que no sea encontrada en el hashmap, devuelve null
        Optional<TokenType> tokenType = Optional.ofNullable(tokenDefinitions.getKeywords().get(palabra));

        // Si el hashmap devolvió un keyword, el token se definirá como ese tipo de keyword, caso contrario será id object
        return generateToken(palabra, tokenType.orElse(TokenType.IDENTIFIER_OBJECT), lexer.getRowIndex(), initialPosition);
    }

    public Token classifySpecialSymbol() {

        String palabra = String.valueOf(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
        int initialPosition = lexer.getColumnIndex();

        TokenType tokenType = tokenDefinitions.getSpecialSymbols().get(lexer.getCurrentChar());

        lexer.incrementPosition();
        return generateToken(palabra, tokenType, lexer.getRowIndex(), initialPosition);
    }

    public Token classifyString() throws LexicalException {

        StringBuilder buffer = new StringBuilder();
        int initialPosition = lexer.getColumnIndex();
        boolean endString = false;

        lexer.incrementPosition(); // saltar comilla inicial
        buffer.append("\"");

        while (!endString) {

            if (lexer.isEndOfLine()) {
                throw new LexicalException(
                        lexer.getRowIndex(),
                        initialPosition + 1,
                        "STRING SIN CERRAR: " + buffer.toString()
                );
            }

            char c = lexer.getCurrentChar();

            if (c == '"') {
                lexer.incrementPosition();
                buffer.append("\"");
                endString = true;
            }
            else if (c == '\\') {

                lexer.incrementPosition();

                if (lexer.isEndOfLine()) {
                    throw new LexicalException(
                            lexer.getRowIndex(),
                            lexer.getColumnIndex(),
                            "Escape incompleto en string"
                    );
                }

                char next = lexer.getCurrentChar();

                switch (next) {
                    case 'n':
                    case 't':
                    case 'r':
                    case '"':
                    case '\'':
                    case '\\':
                        buffer.append('\\');
                        buffer.append(next);
                        break;

                    default:
                        throw new LexicalException(
                                lexer.getRowIndex(),
                                lexer.getColumnIndex(),
                                "Escape inválido: \\" + next
                        );
                }

                lexer.incrementPosition();
            }
            else {

                if (c == '\0') {
                    throw new LexicalException(
                            lexer.getRowIndex(),
                            lexer.getColumnIndex(),
                            "String contiene caracter nulo"
                    );
                }

                if (!Alphabet.isValidCharacter(c)) {
                    throw new LexicalException(
                            lexer.getRowIndex(),
                            lexer.getColumnIndex(),
                            "Caracter inválido en string: " + c
                    );
                }

                buffer.append(c);
                lexer.incrementPosition();
            }

            if (buffer.length() > 1024) {
                throw new LexicalException(
                        lexer.getRowIndex(),
                        initialPosition + 1,
                        "String excede 1024 caracteres"
                );
            }
        }

        return generateToken(buffer.toString(),
                TokenType.STRING_CONSTANT,
                lexer.getRowIndex(),
                initialPosition
        );
    }

    private Token generateToken(String lexema, TokenType tokenType, int indexRow, int indexColumn){
        return new Token(lexema , tokenType, indexRow, indexColumn + 1);
    }


}
