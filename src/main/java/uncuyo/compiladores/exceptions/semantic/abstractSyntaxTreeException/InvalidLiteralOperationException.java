
package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidLiteralOperationException extends ASTException {

    public InvalidLiteralOperationException(int rowIndex, int columnIndex, String operator) {
        super(rowIndex, columnIndex,
                "No se puede aplicar el operador '" + operator + "' sobre un literal");
    }
}
