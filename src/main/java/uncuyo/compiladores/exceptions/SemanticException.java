package uncuyo.compiladores.exceptions;

public class SemanticException extends RuntimeException {
    public SemanticException(int rowIndex, int columnIndex, String description) {
        super("ERROR SINTACTICO" + "\n" +
                "| NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) | DESCRIPCIÓN: |" +  "\n" +
                "| LÍNEA " + rowIndex + " (COLUMNA " + columnIndex + ") | " + description + " |"
        );
    }
}
