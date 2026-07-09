package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class ForVariableOutOfScopeException extends SymbolTableException {

    public ForVariableOutOfScopeException(int rowIndex, int columnIndex,
                                          String variableName,
                                          String methodName) {
        super(rowIndex, columnIndex,
                "La variable local '" + variableName +
                        "' no es accesible desde el ambito actual en el metodo '" + methodName + "'."
        );
    }
}
