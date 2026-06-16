package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class LocalVariableNotFoundException extends SymbolTableException {

    public LocalVariableNotFoundException(int rowIndex, int columnIndex,
                                          String variableName,
                                          String methodName) {
        super(rowIndex, columnIndex,
                "La variable local '" + variableName +
                        "' no fue encontrada en el método '" + methodName + "'."
        );
    }
}