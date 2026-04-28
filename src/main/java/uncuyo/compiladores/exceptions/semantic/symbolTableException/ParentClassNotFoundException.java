package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.lexer.Token;

public class ParentClassNotFoundException extends SymbolTableException {
    public ParentClassNotFoundException(Token token, String parentClassName) {
        super(token.getRowIndex(), token.getColumnIndex(), "La clase '" + token.getLexeme()
                + "' extiende una clase desconocida '" + parentClassName + "'");
    }
}
