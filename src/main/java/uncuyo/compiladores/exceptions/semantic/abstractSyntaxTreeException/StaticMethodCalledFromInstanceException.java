package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class StaticMethodCalledFromInstanceException extends ASTException {

    public StaticMethodCalledFromInstanceException(
            int rowIndex,
            int columnIndex,
            String methodName,
            String className) {

        super(
                rowIndex,
                columnIndex,
                "El método estático '" + methodName +
                        "' de la clase '" + className +
                        "' no debe invocarse mediante una instancia."
        );
    }
}