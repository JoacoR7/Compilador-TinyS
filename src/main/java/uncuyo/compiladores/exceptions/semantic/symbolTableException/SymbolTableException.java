package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.exceptions.semantic.SemanticException;

public class SymbolTableException extends SemanticException {
    public SymbolTableException(int rowIndex, int columnIndex, String description) {
        super(rowIndex, columnIndex, description);
    }
    public SymbolTableException(String description) {
        super(description);
    }
}
