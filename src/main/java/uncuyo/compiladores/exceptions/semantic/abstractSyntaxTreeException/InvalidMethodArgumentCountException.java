package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidMethodArgumentCountException extends ASTException {

    public InvalidMethodArgumentCountException(
            int rowIndex,
            int columnIndex,
            String methodName,
            int expected,
            int received
    ) {
        super(
                rowIndex,
                columnIndex,
                "El metodo '" +
                        methodName +
                        "' espera " +
                        expected +
                        " argumento(s), pero se recibieron " +
                        received
        );
    }

}
