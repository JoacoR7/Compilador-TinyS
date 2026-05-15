package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.semantic.symbolTable.Type;

public class SelfNode extends OperandNode{

    private ChainedNode chainedNode;


    public SelfNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
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
