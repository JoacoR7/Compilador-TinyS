package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class InvalidReturnException extends ASTException {
    public InvalidReturnException(Token token, Method method, Type returnType) {
        super(token.getRowIndex(), token.getColumnIndex(),
                "Se esperaba un retorno de tipo '" + method.getType().getLexeme() + "' para el método '" +
                method.getName() + "' pero se encontro de tipo '" + (returnType != null ? returnType.getTypeName() : "void") + "'");
    }
}
