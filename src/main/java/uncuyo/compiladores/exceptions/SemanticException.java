package uncuyo.compiladores.exceptions;

public class SemanticException extends RuntimeException {
    public SemanticException(int rowIndex, int columnIndex, String description) {
        super("ERROR SEMANTICO" + "\n" +
                "| NUMERO DE LINEA (NUMERO DE COLUMNA) | DESCRIPCION: |" +  "\n" +
                "| LINEA " + rowIndex + " (COLUMNA " + columnIndex + ") | " + description + " |"
        );
    }
}
