package uncuyo.compiladores.exceptions.syntactic;

import uncuyo.compiladores.lexer.Token;

public class MethodOutsideImplException extends SyntacticException {
    public MethodOutsideImplException(Token token)  {
        super(token.getRowIndex(), token.getColumnIndex(),
                "El metodo '" + token.getLexeme() + "' debe estar contenido dentro de un impl");
    }
}
