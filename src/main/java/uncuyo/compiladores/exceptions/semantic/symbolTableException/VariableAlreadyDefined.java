package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class VariableAlreadyDefined extends SymbolTableException {
    public VariableAlreadyDefined(int rowIndex , int columnIndex, String variableName, String methodName) {
        super(rowIndex, columnIndex, "La variable local '"
                + variableName + "' ya existe como parametro en el metodo " + methodName);
    }
}
