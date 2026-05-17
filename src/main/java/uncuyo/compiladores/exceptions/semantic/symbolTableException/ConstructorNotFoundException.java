package uncuyo.compiladores.exceptions.semantic.symbolTableException;


public class ConstructorNotFoundException extends SymbolTableException {
    public ConstructorNotFoundException(String className) {
        super("No se encontró un constructor para la clase '" + className + "'");
    }
}
