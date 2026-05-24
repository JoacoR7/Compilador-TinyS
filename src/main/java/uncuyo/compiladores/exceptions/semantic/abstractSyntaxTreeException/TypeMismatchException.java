package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

public class TypeMismatchException extends ASTException {
    public TypeMismatchException(int rowIndex, int columnIndex, String expectedType, String actualType) {
        super(rowIndex, columnIndex, "Se esperaba tipo '" + expectedType + "' pero se encontro un tipo '" + actualType + "'");
    }
}
