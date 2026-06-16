package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.lexer.Token;

public class ObjectDeclarationException extends SymbolTableException {
    public ObjectDeclarationException(Token token) {
        super(token.getRowIndex(), token.getColumnIndex(), "No se puede definir una variable de tipo Object");
    }
}
