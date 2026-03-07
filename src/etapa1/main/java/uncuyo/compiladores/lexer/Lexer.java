package uncuyo.compiladores.lexer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    public Lexer(String path) throws FileNotFoundException {
        this.path = path;
        this.rowIndex = 0;
        this.columnIndex = 0;
        this.tokenClasifier = new TokenClasifier(this);
        this.reader = new BufferedReader(new java.io.FileReader(this.path));

    }

    public Token getNextToken() throws IOException {

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

            if (currentChar == '/') {
                Token token = tokenClasifier.isCommentOrDiv();
                if (token != null) {
                    return token;
                }
                continue; // seguir buscando token
            }

            // identificadores
            // números
            // strings
            // operadores

            columnIndex++;
        }
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
