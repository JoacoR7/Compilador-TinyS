package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class MethodNotFoundException extends SymbolTableException{
    public MethodNotFoundException(int rowIndex, int columnIndex, String className, String methodName) {
        super(rowIndex, columnIndex, "El metodo '" + methodName + "' no se encuentra definido en la clase '" + className + "'");
    }
}
