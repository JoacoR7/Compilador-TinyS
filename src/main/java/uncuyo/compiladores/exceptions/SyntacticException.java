package uncuyo.compiladores.exceptions;

public class SyntacticException extends Exception {
    public SyntacticException(int rowIndex, int columnIndex, String description) {
        super("ERROR SINTACTICO" + "\n" +
                "| NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) | DESCRIPCIÓN: |" +  "\n" +
                "| LÍNEA " + rowIndex + " (COLUMNA " + columnIndex + ") | " + description + " |"
        );
    }
}
