package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class MethodNotFoundException extends SymbolTableException{
    public MethodNotFoundException(String methodName) {
        super("El metodo '" + methodName + "' no se encuentra definido");
    }
}
