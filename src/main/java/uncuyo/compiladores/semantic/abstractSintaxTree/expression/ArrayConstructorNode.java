package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class ArrayConstructorNode extends ConclassorCallNode {

    private ExpressionNode expression;

    public ArrayConstructorNode(Type type, ChainedNode chainedNode, ExpressionNode expression) {
        super(type, chainedNode);
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public Type check() {
        return null;
    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
