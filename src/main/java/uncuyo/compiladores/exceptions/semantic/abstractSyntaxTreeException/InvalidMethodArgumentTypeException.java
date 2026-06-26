package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidMethodArgumentTypeException extends ASTException {
    public InvalidMethodArgumentTypeException(
            int rowIndex,
            int columnIndex,
            String methodName,
            int parameterPosition,
            String expectedType,
            String receivedType
    ) {
        super(
                rowIndex,
                columnIndex,
                "Argumento inválido en la posición " +
                        parameterPosition +
                        " del metodo '" +
                        methodName +
                        "': se esperaba '" +
                        expectedType +
                        "' pero se recibió '" +
                        receivedType +
                        "'"
        );
    }
}
