package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class VariableRedeclarationInForLoopException extends ASTException {
    public VariableRedeclarationInForLoopException(int rowIndex, int columnIndex, String variableName, String methodName) {
        super(rowIndex, columnIndex,
                "La variable '" + variableName + "' ya se encuentra declarada en el metodo '"
                + methodName + "' y no puede ser redeclarada en el ambito del bucle for"
        );
    }
}
