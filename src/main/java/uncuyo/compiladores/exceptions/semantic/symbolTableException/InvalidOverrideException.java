package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.lexer.Token;

public class InvalidOverrideException extends SymbolTableException {
    public InvalidOverrideException(Token token, String description) {
        super(token.getRowIndex(), token.getColumnIndex(), description);
    }
}
