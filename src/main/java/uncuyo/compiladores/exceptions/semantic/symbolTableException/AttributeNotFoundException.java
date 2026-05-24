package uncuyo.compiladores.exceptions.semantic.symbolTableException;

public class AttributeNotFoundException extends SymbolTableException{
    public AttributeNotFoundException(String attribute, String className) {
        super("El atributo '" + attribute + "' no se encuentra definido en la clase " + className);
    }
}
