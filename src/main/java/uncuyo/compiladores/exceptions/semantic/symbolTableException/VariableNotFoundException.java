package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class VariableNotFoundException extends SymbolTableException{
    public VariableNotFoundException(String variableName) {
        super("La variable '" + variableName + "' no se encuentra definida");
    }
}
