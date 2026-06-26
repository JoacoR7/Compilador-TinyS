package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidVoidAccessException extends ASTException {
    public InvalidVoidAccessException(
            int rowIndex,
            int columnIndex,
            String methodName
    ) {
        super(rowIndex,
                columnIndex,
                "El método '" + methodName + "' retorna void, no se puede acceder a sus atributos o métodos");
    }
}
