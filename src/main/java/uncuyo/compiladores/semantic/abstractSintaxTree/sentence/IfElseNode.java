package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class IfElseNode extends SentenceNode {
    private Token token;
    private ExpressionNode expression;
    private SentenceNode sentenceNodeIf;
    private SentenceNode sentenceNodeElse;

    public IfElseNode(Token token, ExpressionNode expression, SentenceNode sentenceNodeIf, SentenceNode sentenceNodeElse) {
        this.token = token;
        this.expression = expression;
        this.sentenceNodeIf = sentenceNodeIf;
        this.sentenceNodeElse = sentenceNodeElse;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
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
    public String toJson(String indent) {
        return "";
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
