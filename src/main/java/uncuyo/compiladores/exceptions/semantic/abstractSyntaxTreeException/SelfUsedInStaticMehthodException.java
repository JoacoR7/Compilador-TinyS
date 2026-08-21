package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class SelfUsedInStaticMehthodException extends ASTException {
    public SelfUsedInStaticMehthodException(
            int rowIndex,
            int columnIndex,
            String methodName,
            String className) {

        super(
                rowIndex,
                columnIndex,
                "No se puede usar  'self' dentro del metodo estatico '" + methodName +
                        "' de la clase '" + className + "'. "
        );
    }
}
