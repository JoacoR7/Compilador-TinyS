package uncuyo.compiladores.exceptions.semantic.symbolTableException;


public class ImplNotFoundException extends SymbolTableException {
    public ImplNotFoundException(String className) {
        super("No se encontró una implementación para la clase '" + className + "'");
    }
}
