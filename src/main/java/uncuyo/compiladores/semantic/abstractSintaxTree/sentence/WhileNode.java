package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class WhileNode extends SentenceNode{
    private ExpressionNode expressionNode;
    private SentenceNode sentenceNode;

    public WhileNode(ExpressionNode expressionNode, SentenceNode sentenceNode){
        this.expressionNode = expressionNode;
        this.sentenceNode = sentenceNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public SentenceNode getSentenceNode() {
        return sentenceNode;
    }

    public void setSentenceNode(SentenceNode sentenceNode) {
        this.sentenceNode = sentenceNode;
    }

    @Override
    public void check() {

    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
