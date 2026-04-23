package uncuyo.compiladores.exceptions.lexical;

public class LexicalException extends Exception{

    public LexicalException(int rowIndex, int columnIndex, String description) {
        super("ERROR LÉXICO" + "\n" +
                "| NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) | DESCRIPCIÓN: |" +  "\n" +
                "| LÍNEA " + rowIndex + " (COLUMNA " + columnIndex + ") | " + description + " |"
        );
    }
}
