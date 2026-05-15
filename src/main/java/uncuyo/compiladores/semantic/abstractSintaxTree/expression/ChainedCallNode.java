package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

import java.util.LinkedList;

public class ChainedCallNode extends ChainedNode{

    private LinkedList<ExpressionNode> expressionNodes;

    public ChainedCallNode(Token token, ChainedNode chainedNode, LinkedList<ExpressionNode> expressionNodes) {
        super(token, chainedNode);
        this.expressionNodes = expressionNodes;
    }

    public LinkedList<ExpressionNode> getExpressionNodes() {
        return expressionNodes;
    }

    public void setExpressionNodes(LinkedList<ExpressionNode> expressionNodes) {
        this.expressionNodes = expressionNodes;
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
