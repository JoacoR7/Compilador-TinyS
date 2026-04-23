package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class PrimitiveInheritanceException extends SymbolTableException {
    public PrimitiveInheritanceException(int rowIndex, int columnIndex, String parentType) {
        super(rowIndex, columnIndex,
                "No se puede heredar del tipo primitivo '" + parentType + "'");
    }
}
