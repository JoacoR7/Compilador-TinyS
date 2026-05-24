package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class IfElseNode extends SentenceNode {
    private ExpressionNode expression;
    private SentenceNode sentenceNodeIf;
    private SentenceNode sentenceNodeElse;

    public IfElseNode(ExpressionNode expression, SentenceNode sentenceNodeIf, SentenceNode sentenceNodeElse) {
        this.expression = expression;
        this.sentenceNodeIf = sentenceNodeIf;
        this.sentenceNodeElse = sentenceNodeElse;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public SentenceNode getSentenceNodeIf() {
        return sentenceNodeIf;
    }

    public void setSentenceNodeIf(SentenceNode sentenceNodeIf) {
        this.sentenceNodeIf = sentenceNodeIf;
    }

    public SentenceNode getSentenceNodeElse() {
        return sentenceNodeElse;
    }

    public void setSentenceNodeElse(SentenceNode sentenceNodeElse) {
        this.sentenceNodeElse = sentenceNodeElse;
    }

    @Override
    public void check() {
        
    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
