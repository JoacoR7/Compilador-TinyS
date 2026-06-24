package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidConstructorArgumentsCountException extends ASTException {

    public InvalidConstructorArgumentsCountException(
            int rowIndex,
            int columnIndex,
            String className,
            int expected,
            int received
    ) {
        super(
                rowIndex,
                columnIndex,
                "El constructor de la clase '" +
                        className +
                        "' espera " +
                        expected +
                        " argumento(s), pero se recibieron " +
                        received
        );
    }
}