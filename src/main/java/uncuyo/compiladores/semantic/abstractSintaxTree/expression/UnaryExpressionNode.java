package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class UnaryExpressionNode extends ExpressionNode {
    private Token unaryOperatorToken;
    private ExpressionNode expressionNode;

    public UnaryExpressionNode(ExpressionNode expressionNode, Token unaryOperatorToken) {
        this.expressionNode = expressionNode;
        this.unaryOperatorToken = unaryOperatorToken;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    public Token getUnaryOperatorToken() {
        return unaryOperatorToken;
    }

    public void setUnaryOperatorToken(Token unaryOperatorToken) {
        this.unaryOperatorToken = unaryOperatorToken;
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
