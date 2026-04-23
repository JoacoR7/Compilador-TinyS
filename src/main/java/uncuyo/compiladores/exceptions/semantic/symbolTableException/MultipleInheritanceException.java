package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class MultipleInheritanceException extends SymbolTableException {
    public MultipleInheritanceException(int rowIndex, int columnIndex,
                                        String className,
                                        String newParent,
                                        String currentParent,
                                        int originalLine) {
        super(rowIndex, columnIndex,
                "La clase '" + className + "' no puede heredar de '" + newParent +
                        "' porque ya hereda de '" + currentParent +
                        "' (declarado en la linea " + originalLine + ")");
    }
}