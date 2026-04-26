package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.lexer.Token;

public class ConstructorRedefinitionException extends SymbolTableException {
    public ConstructorRedefinitionException(int rowIndex, int columnIndex, String className,
                                            Token constructorToken) {
        super(rowIndex, columnIndex,
                "El constructor para la clase '" + className + "' ya ha sido definido en la línea " +
                constructorToken.getRowIndex() + " (columna " + constructorToken.getColumnIndex() + ").");
    }
}
