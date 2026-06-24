package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidConstructorArgumentTypeException extends ASTException {

    public InvalidConstructorArgumentTypeException(
            int rowIndex,
            int columnIndex,
            String className,
            int parameterPosition,
            String expectedType,
            String receivedType
    ) {
        super(
                rowIndex,
                columnIndex,
                "Argumento inválido en la posición " +
                        parameterPosition +
                        " al invocar el constructor de '" +
                        className +
                        "': se esperaba '" +
                        expectedType +
                        "' pero se recibió '" +
                        receivedType +
                        "'"
        );
    }
}