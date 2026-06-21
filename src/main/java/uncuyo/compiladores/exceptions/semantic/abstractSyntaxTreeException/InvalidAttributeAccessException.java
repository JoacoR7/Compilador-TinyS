package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidAttributeAccessException extends ASTException {

    public InvalidAttributeAccessException(
            int rowIndex,
            int columnIndex,
            String attributeName,
            String className
    ) {
        super(
                rowIndex,
                columnIndex,
                "No se puede acceder al atributo privado '" +
                        attributeName +
                        "' de la clase '" +
                        className +
                        "'"
        );
    }
}