package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

import uncuyo.compiladores.exceptions.semantic.SemanticException;

public class ASTException extends SemanticException {
    public ASTException(int rowIndex, int columnIndex, String description) {
        super(rowIndex, columnIndex, description);
    }

    public ASTException(String description) {
        super(description);
    }
}
