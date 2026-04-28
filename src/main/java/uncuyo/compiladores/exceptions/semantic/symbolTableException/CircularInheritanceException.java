package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.lexer.Token;

public class CircularInheritanceException extends SymbolTableException {
    public CircularInheritanceException(Token token, String className) {
        super(token.getRowIndex(), token.getColumnIndex(),
                "Herencia circular detectada en la clase '" + token.getLexeme() + "' con la superclase '" + className + "'");
    }
}
