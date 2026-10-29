package uncuyo.compiladores.exceptions.semantic;

/**
 * Error semántico que interrumpe el análisis al detectar la primera incompatibilidad.
 * Construye el diagnóstico con o sin ubicación. Los ejecutadores pueden
 * sustituir su encabezado general por el de declaraciones o sentencias.
 */
public class SemanticException extends RuntimeException {
    /**
     * Crea un diagnóstico asociado a una ubicación del archivo fuente.
     *
     * @param rowIndex línea del token que origina el error
     * @param columnIndex columna del token que origina el error
     * @param description detalle del control que falló
     */
    public SemanticException(int rowIndex, int columnIndex, String description) {
        super("ERROR SEMANTICO" + "\n" +
                "| NUMERO DE LINEA (NUMERO DE COLUMNA) | DESCRIPCION: |" +  "\n" +
                "| LINEA " + rowIndex + " (COLUMNA " + columnIndex + ") | " + description + " |"
        );
    }

    /**
     * Crea un diagnóstico sin línea ni columna.
     *
     * @param description detalle del error cuando no se dispone de un token
     */
    public SemanticException(String description) {
        super("ERROR SEMANTICO" + "\n" +
                "| DESCRIPCION: |" +  "\n" +
                "| " + description + " |"
        );
    }
}
