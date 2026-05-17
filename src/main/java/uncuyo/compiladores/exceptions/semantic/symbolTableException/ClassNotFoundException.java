package uncuyo.compiladores.exceptions.semantic.symbolTableException;


public class ClassNotFoundException extends SymbolTableException {
    public ClassNotFoundException(String className) {
        super("La clase '" + className + "' no se encuentra declarada");
    }
}
