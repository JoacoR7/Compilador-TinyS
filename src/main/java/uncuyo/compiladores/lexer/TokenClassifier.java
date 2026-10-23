package uncuyo.compiladores.lexer;

import uncuyo.compiladores.exceptions.lexical.LexerFileReadException;
import uncuyo.compiladores.exceptions.lexical.LexicalException;

import java.util.Optional;

/**
 * Es el encargado de verificar y clasificar los tokens
 * <p>
 * Es un autómata finito determinista que devuelve el token
 * con su lexema, tipo, fila y columna correspondientes
 */
public class TokenClassifier {
    /** Analizador léxico. */
    private Lexer lexer;
    /** Definiciones de tokens. */
    private TokenDefinitions tokenDefinitions;


    /**
     * Construye un clasificador de tokens a partir del analizador léxico
     * y las definiciones predefinidas de tokens
     */
    public TokenClassifier(Lexer lexer) {
        this.lexer = lexer;
        this.tokenDefinitions = new TokenDefinitions();
    }

    /**
     * Método que analiza si la posición actual es una división o el comienzo
     * de un comentario simple o un comentario multilinea
     * @return Token
     * @throws LexerFileReadException ocasionado al leer el archivo
     * @throws LexicalException ocasionado por un error léxico
     */
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

    /**
     * Método que procesa el comentario multilinea
     * @param initRowIndex índice de fila donde comenzó el comentario multilinea
     * @param initColumnIndex índice de columna donde comenzó el comentario multilinea
     * @throws LexerFileReadException ocasionado al leer el archivo
     * @throws LexicalException si el comentario no fue cerrado
     */
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

    /**
     * Método que procesa el comentario simple
     * @throws LexerFileReadException ocasionado al leer el archivo
     */
    public void simpleComment() throws LexerFileReadException {
        lexer.loadNextLine();
    }

    /**
     * Método que clasifica el símbolo actual y determina qué operador es
     * @throws LexicalException si el símbolo no se reconoce
     */
    public Token classifyOperator() throws LexicalException {

        String operator = String.valueOf(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
        TokenType tokenType = TokenType.UNDEFINED;
        int initialPosition = lexer.getColumnIndex(); // Columna donde empieza el token

        if (tokenDefinitions.getOperators().containsKey(operator)) {

            if (lexer.getColumnIndex() + 1 < lexer.getCurrentData().length()) {
                char vistazo = lexer.getCurrentData().charAt(lexer.getColumnIndex() + 1);
                // Checkeo si el operador continua, por ejemplo tenia < y el vistazo encuentra =
                // Entonces el operador será <=
                if (tokenDefinitions.getOperators().containsKey(operator + vistazo)) {
                    tokenType = tokenDefinitions.getOperators().get(operator + vistazo);
                    operator = operator + vistazo;
                    lexer.incrementPosition();
                } else {
                    /*
                    if (tokenDefinitions.getOperators().containsKey(String.valueOf(vistazo))) {
                        throw new LexicalException(lexer.getRowIndex(), initialPosition, "Operador " + (operator + vistazo) + " no reconocido");
                    }
                    */
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


    /**
     * Método que determina si la información leída es un literal entero
     * @return token
     */
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

    /**
     * Método que determina si la información actual es un identificador de clase
     * @return token
     */
    public Token classifyClassIdentifier(){

        StringBuilder buffer = new StringBuilder();
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

        String lexeme = buffer.toString();
        TokenType tokenType = tokenDefinitions.getPrimitiveTypes().get(lexeme);
        if (tokenType == null){
            tokenType = TokenType.IDENTIFIER_CLASS;
        }

        return generateToken(lexeme, tokenType, lexer.getRowIndex(), initialPosition);
    }

    /**
     * Método que determina si la información actual es un identificador de variable o método o una palabra reservada
     * @return token
     */
    public Token classifyKeywordOrIdentifier() throws LexicalException {

        StringBuilder buffer = new StringBuilder();
        int initialPosition = lexer.getColumnIndex();
        char vistazo;

        do {

            if (Alphabet.hasAccent(lexer.getCurrentChar())) {
                throw new LexicalException(
                        lexer.getRowIndex(),
                        lexer.getColumnIndex(),
                        "Caracter invalido en identificador: " + lexer.getCurrentChar()
                );
            }
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
        return generateToken(palabra, tokenType.orElse(TokenType.IDENTIFIER_VAR_METHOD), lexer.getRowIndex(), initialPosition);
    }

    /**
     * Método que determina si la información actual es un símbolo especial
     * @return token
     */
    public Token classifySpecialSymbol() {

        String palabra = String.valueOf(lexer.getCurrentData().charAt(lexer.getColumnIndex()));
        int initialPosition = lexer.getColumnIndex();

        TokenType tokenType = tokenDefinitions.getSpecialSymbols().get(lexer.getCurrentChar());

        lexer.incrementPosition();
        return generateToken(palabra, tokenType, lexer.getRowIndex(), initialPosition);
    }

    /**
     * Método que procesa un literal str
     * @return token
     */
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
            } else if (c == '\\') {

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

                if (!Alphabet.isValidStringCharacter(c)) {
                    throw new LexicalException(
                            lexer.getRowIndex(),
                            lexer.getColumnIndex(),
                            "Caracter inválido en string: " + c
                    );
                }

                buffer.append(c);
                lexer.incrementPosition();
            }

            if (buffer.length() > 1026) {
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

    /**
     * Método que genera un nuevo token a partir de la información dada por los parámetros
     * @param lexema palabra que forma el token
     * @param tokenType tipo de token
     * @param indexRow indice de fila
     * @param indexColumn indice de columna
     * @return token
     */
    private Token generateToken(String lexema, TokenType tokenType, int indexRow, int indexColumn){
        return new Token(lexema , tokenType, indexRow, indexColumn + 1);
    }


}
