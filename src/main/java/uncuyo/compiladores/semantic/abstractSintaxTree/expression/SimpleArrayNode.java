package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

import java.beans.Expression;

public class SimpleArrayNode extends SimpleAccessNode{

    private Token token;
    private ExpressionNode expressionNode;

    public SimpleArrayNode(Token token, ExpressionNode expressionNode) {
        this.token = token;
        this.expressionNode = expressionNode;
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

    @Override
    public Type check() {
        return null;
    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
