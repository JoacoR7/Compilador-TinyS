package uncuyo.compiladores.lexer;

import uncuyo.compiladores.exceptions.LexicalException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase {@code Lexer} es responsable de analizar y extraer lexemas del archivo de entrada.
 *
 * @author Victor Ramirez
 * @author Joaquin Ruiz
 */
public class Lexer {

    private String path;
    private int rowIndex;
    private int columnIndex;
    private TokenClasifier tokenClasifier;
    private BufferedReader reader;
    private String currentData;
    private TokenDefinitions tokenDefinitions;

    public Lexer(String path) throws FileNotFoundException {
        this.path = path;
        this.rowIndex = 0;
        this.columnIndex = 0;
        this.tokenClasifier = new TokenClasifier(this);
        this.reader = new BufferedReader(new java.io.FileReader(this.path));
        this.tokenDefinitions = new TokenDefinitions();

    }

    public Token getNextToken() throws IOException, LexicalException {

        Token token;

        while (true) {

            //Verifica si hay más datos en la línea actual del archivo.
            if (currentData == null || columnIndex >= currentData.length()) {
                if (!loadNextLine()) {
                    return null;
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
                token = tokenClasifier.isCommentOrDiv();
                if (token != null) {
                    return token;
                }
                continue; // seguir buscando token
            }

            if (Character.isDigit(currentChar)) {
                return tokenClasifier.isNumber();
            }

            // Si es una letra, checkeo si es una clase o un keyword
            if (Character.isLetter(currentChar)) {
                if (Character.isUpperCase(currentChar)) {
                    return tokenClasifier.classifyClassIdentifier();
                }
                return tokenClasifier.classifyKeywordOrIdentifier();
            }

            if (tokenDefinitions.getOperators().containsKey(String.valueOf(currentChar))) {
                return tokenClasifier.classifyOperator();
            }

            if (tokenDefinitions.getSpecialSymbols().containsKey(currentChar)) {
                return tokenClasifier.classifySpecialSymbol();
            }

            if (tokenDefinitions.getUndefinedSymbols().containsKey(currentChar)) {
                throw new LexicalException(
                        getRowIndex(),
                        getColumnIndex(),
                        "Simbolo '" + currentChar + "' no reconocido"
                );
            }

            if(currentChar == '\"') {
                return tokenClasifier.classifyString();
            }

            // REVISAR, CREO QUE NO DEBERÍA AUMENTAR EL COLUMN INDEX, SINO DIRECTAMENTE RETORNAR NULO O ERROR
            // YA QUE SI LLEGÓ A ESTE PUNTO SIGNIFICA QUE NO RECONOCIÓ EL CARACTER ACTUAL
            // ESTO PASA POR EJEMPLO CON EL SÍMBOLO "@"
            columnIndex++;
        }
    }

    public List<Token> getAllTokens() throws IOException, LexicalException {
        List<Token> tokens = new ArrayList<>();
        Token token;

        while ((token = getNextToken()) != null) {
            tokens.add(token);
        }

        return tokens;
    }

    public boolean isEndOfLine() {
        return currentData == null || columnIndex >= currentData.length();
    }

    public boolean loadNextLine() throws IOException {
        currentData = reader.readLine();
        // End of file
        if (currentData != null) {
            rowIndex++;
            columnIndex = 0;
        }
        return currentData != null;
    }

    public char getCurrentChar() {
        return this.currentData.charAt(this.columnIndex);
    }


    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String getCurrentData() {
        return currentData;
    }

    public void setCurrentData(String currentData) {
        this.currentData = currentData;
    }

    public void incrementPosition(){
        columnIndex++;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public TokenClasifier getTokenClasiffier() {
        return tokenClasifier;
    }

    public void setTokenClasiffier(TokenClasifier tokenClasifier) {
        this.tokenClasifier = tokenClasifier;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
