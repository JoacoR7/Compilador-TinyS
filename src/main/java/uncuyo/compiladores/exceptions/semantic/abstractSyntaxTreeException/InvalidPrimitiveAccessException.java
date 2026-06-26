package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidPrimitiveAccessException extends ASTException {
    public InvalidPrimitiveAccessException(
            int rowIndex,
            int columnIndex,
            String methodName,
            String typeName
    ) {
        super(rowIndex,
                columnIndex,
                "El metodo '" + methodName + "' retorna un tipo '" + typeName + "', no se puede acceder a sus atributos");
    }
}
