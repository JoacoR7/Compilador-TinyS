package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.exceptions.semantic.SemanticException;

/**
 * Base de los errores asociados a las entradas y comprobaciones de la TS.
 * Sus subclases representan conflictos de declaración y errores de búsqueda
 * de entidades; algunas también se utilizan durante el chequeo de sentencias.
 */
public class SymbolTableException extends SemanticException {
    /**
     * Crea un error de TS con la ubicación conservada por la entidad.
     *
     * @param rowIndex línea del error
     * @param columnIndex columna del error
     * @param description detalle de la incompatibilidad
     */
    public SymbolTableException(int rowIndex, int columnIndex, String description) {
        super(rowIndex, columnIndex, description);
    }
    /**
     * Crea un error de TS sin información de ubicación.
     *
     * @param description detalle del error
     */
    public SymbolTableException(String description) {
        super(description);
    }
}
