package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

public abstract class ConclassorCallNode extends ExpressionNode{
    private ChainedNode chainedNode;

    public ConclassorCallNode(Type type, ChainedNode chainedNode) {
        this.setType(type);
        this.chainedNode = chainedNode;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

}
