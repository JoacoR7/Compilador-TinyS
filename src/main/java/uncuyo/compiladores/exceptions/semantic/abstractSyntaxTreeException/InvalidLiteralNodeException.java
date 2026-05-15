package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class InvalidLiteralNodeException extends ASTException {
    public InvalidLiteralNodeException(int rowIndex, int columnIndex, String description) {
        super(rowIndex, columnIndex, description);
    }
}
