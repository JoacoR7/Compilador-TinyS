package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidMethodAccessException extends ASTException {

    public InvalidMethodAccessException(
            int rowIndex,
            int columnIndex,
            String methodName,
            String className
    ) {
        super(
                rowIndex,
                columnIndex,
                "No se puede invocar el metodo '" +
                        methodName +
                        "' de la clase '" +
                        className +
                        "'"
        );
    }
}