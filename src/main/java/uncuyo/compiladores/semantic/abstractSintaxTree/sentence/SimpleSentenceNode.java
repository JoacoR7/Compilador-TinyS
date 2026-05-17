package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class SimpleSentenceNode extends SentenceNode{
    private ExpressionNode expressionNode;
    public SimpleSentenceNode(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    @Override
    public void check() {

    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
