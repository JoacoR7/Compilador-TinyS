package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
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
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
