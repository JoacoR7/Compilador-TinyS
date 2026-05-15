package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.semantic.symbolTable.Type;

public class ParenthesizedExpressionNode extends ExpressionNode {

    private ExpressionNode expresion;
    private ChainedNode chainedNode;

    public ParenthesizedExpressionNode(ExpressionNode expresion, ChainedNode chainedNode) {
        this.expresion = expresion;
        this.chainedNode = chainedNode;
    }

    public ExpressionNode getExpresion() {
        return expresion;
    }

    public void setExpresion(ExpressionNode expresion) {
        this.expresion = expresion;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
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
