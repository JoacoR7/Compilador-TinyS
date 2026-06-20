package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class WhileNode extends SentenceNode{
    private ExpressionNode expressionNode;
    private SentenceNode sentenceNode;
    private Token token;

    public WhileNode(Token token , ExpressionNode expressionNode, SentenceNode sentenceNode){
        this.token = token;
        this.expressionNode = expressionNode;
        this.sentenceNode = sentenceNode;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
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
    public String toJson(String indent) {
        return "";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
