package uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class MissingReturnException  extends ASTException {
    public MissingReturnException(Method method) {
        super(method.getToken().getRowIndex(), method.getToken().getColumnIndex(),
                "Se esperaba un retorno de tipo '" + method.getType().getTypeName() + "' para el método '" +
                        method.getName() + "' pero no se encontro");
    }
}
