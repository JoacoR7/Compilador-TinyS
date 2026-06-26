package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class NonStaticMethodCalledFromClassException extends ASTException {

    public NonStaticMethodCalledFromClassException(
            int rowIndex,
            int columnIndex,
            String methodName,
            String className) {

        super(
                rowIndex,
                columnIndex,
                "El metodo no estatico '" + methodName +
                        "' de la clase '" + className +
                        "' debe invocarse mediante una instancia de clase."
        );
    }
}