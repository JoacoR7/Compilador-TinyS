package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class SimpleSelfNode extends SimpleAccessNode {

    private SimpleChainedNode simpleChainedNode;

    public SimpleSelfNode(SimpleChainedNode simpleChainedNode) {
        this.simpleChainedNode = simpleChainedNode;
    }

    public SimpleChainedNode getSimpleChainedNode() {
        return simpleChainedNode;
    }

    public void setSimpleChainedNode(SimpleChainedNode simpleChainedNode) {
        this.simpleChainedNode = simpleChainedNode;
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
