package uncuyo.compiladores.lexer;

import uncuyo.compiladores.exceptions.lexical.LexerFileReadException;
import uncuyo.compiladores.exceptions.lexical.LexicalException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase responsable de analizar y extraer lexemas del archivo de entrada.
 *
 * @author Victor Ramirez
 * @author Joaquin Ruiz
 */
public class Lexer {

    /** Ubicación del archivo a analizar. */
    private String path;
    /** Índice de la fila actual en el archivo. */
    private int rowIndex;
    /** Índice de la columna actual en el archivo. */
    private int columnIndex;
    /** Clasificador de tokens. */
    private final TokenClassifier tokenClassifier;
    /** Lector de archivos. */
    private final BufferedReader reader;
    /** Línea actual del archivo. */
    private String currentData;
    /** Definición de tokens. */
    private final TokenDefinitions tokenDefinitions;

    /**
     * Constructor
     * @param path Archivo de entrada
     * @throws FileNotFoundException si el archivo no existe o no se puede encontrar
     * @author Victor Ramirez
     * @author Joaquin Ruiz
     */
    public Lexer(String path) throws FileNotFoundException {
        this.path = path;
        this.rowIndex = 0;
        this.columnIndex = 0;
        this.tokenClassifier = new TokenClassifier(this);
        this.reader = new BufferedReader(new java.io.FileReader(this.path));
        this.tokenDefinitions = new TokenDefinitions();
    }

    /**
     * Método que devuelve el siguiente token del archivo fuente
     * @return Token
     * @throws LexerFileReadException ocasionado al leer el archivo
     * @throws LexicalException ocasionado por un error léxico
     * @author Victor Ramirez
     * @author Joaquin Ruiz
     */
    public Token getNextToken() throws LexerFileReadException, LexicalException {

        Token token;

        while (true) {

            //Verifica si hay más datos en la línea actual del archivo.
            if (currentData == null || columnIndex >= currentData.length()) {
                if (!loadNextLine()) {
                    return new Token("EOF", TokenType.EOF, rowIndex, columnIndex + 1);
                }
                continue;
            }

            char currentChar = currentData.charAt(columnIndex);

            if (Character.isWhitespace(currentChar)) {
                columnIndex++;
                continue;
            }

            // Si viene el símbolo "/", debo checkear si es un símbolo de división o el inicio de comentario simple o multilínea
            if (currentChar == '/') {
                token = tokenClassifier.isCommentOrDiv();
                if (token != null) {
                    return token;
                }
                continue; // seguir buscando token
            }

            if (Character.isDigit(currentChar)) {
                return tokenClassifier.isNumber();
            }

            // Si es una letra, checkeo si es una clase o un keyword
            if (Character.isLetter(currentChar)) {
                if (Character.isUpperCase(currentChar)) {
                    return tokenClassifier.classifyClassIdentifier();
                }
                return tokenClassifier.classifyKeywordOrIdentifier();
            }

            if (tokenDefinitions.getOperators().containsKey(String.valueOf(currentChar))) {
                return tokenClassifier.classifyOperator();
            }

            if (tokenDefinitions.getSpecialSymbols().containsKey(currentChar)) {
                return tokenClassifier.classifySpecialSymbol();
            }

            if (tokenDefinitions.getUndefinedSymbols().containsKey(currentChar)) {
                throw new LexicalException(
                        getRowIndex(),
                        getColumnIndex(),
                        "Simbolo '" + currentChar + "' no reconocido"
                );
            }

            if(currentChar == '\"') {
                return tokenClassifier.classifyString();
            }

            columnIndex++;
        }
    }

    /**
     * Método que devuelve un listado de tokens, con el motivo de realizar pruebas sobre el analizador léxico
     * @return Lista de tokens del archivo fuente
     * @throws LexerFileReadException ocasionado al leer el archivo
     * @throws LexicalException ocasionado por un error léxico
     * @author Victor Ramirez
     * @author Joaquin Ruiz
     */
    public List<Token> getAllTokens() throws LexerFileReadException, LexicalException {
        List<Token> tokens = new ArrayList<>();
        Token token;
        do {
            token = getNextToken();
            tokens.add(token);

        } while (token.getTokenType() != TokenType.EOF);

        return tokens;
    }

    /**
     * Método que comprueba si la posición actual sobre la que se encuentra el analizador léxico
     * es un fin de línea, es decir, si ya leyó la línea actual
     * @return true si es fin de línea, falso en caso contrario
     * @author Victor Ramirez
     * @author Joaquin Ruiz
     */
    public boolean isEndOfLine() {
        return currentData == null || columnIndex >= currentData.length();
    }

    /**
     * Método que verifica si hay otra línea en el archivo fuente, en caso de que
     * exista, carga la línea y verifica si tiene contenido
     * @return true si la línea tiene contenido, caso contrario devuelve false
     * @throws LexerFileReadException ocasionado al leer el archivo
     * @author Victor Ramirez
     * @author Joaquin Ruiz
     */
    public boolean loadNextLine() throws LexerFileReadException {
        try {
            currentData = reader.readLine();
            // End of file
            if (currentData != null) {
                rowIndex++;
                columnIndex = 0;
            }
            return currentData != null;
        } catch (IOException e) {
            throw new LexerFileReadException(path, "No se pudo leer la siguiente línea", e);
        }
    }

    /**
     * Método que el caracter actual en la línea actual del archivo fuente
     * @return caracter actual
     * @author Victor Ramirez
     * @author Joaquin Ruiz
     */
    public char getCurrentChar() {
        return this.currentData.charAt(this.columnIndex);
    }

    /**
     * Getter de currentData
     * @return fila actual
     */
    public String getCurrentData() {
        return currentData;
    }

    /**
     * Método para incrementar la posición actual en la fila del archivo fuente
     */
    public void incrementPosition(){
        columnIndex++;
    }

    /**
     * Getter de rowIndex
     * @return número de fila actual
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Setter de robIndex
     * @param rowIndex fila actual
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * Getter de column index
     * @return número de columna actual
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Setter de columnIndex
     * @param columnIndex columna actual
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * Getter del path del archivo fuente
     * @return path del archivo fuente
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter del path del archivo fuente
     * @param path ubicación del archivo fuente
     */
    public void setPath(String path) {
        this.path = path;
    }
}
