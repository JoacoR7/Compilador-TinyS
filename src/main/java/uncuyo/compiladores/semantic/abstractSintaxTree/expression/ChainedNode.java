package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

public abstract class ChainedNode extends OperandNode {
    private Token token;
    private ChainedNode chainedNode;
    private boolean isChainedNode;

    public ChainedNode(Token token, ChainedNode chainedNode) {
        this.token = token;
        this.chainedNode = chainedNode;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public boolean isChainedNode() {
        return isChainedNode;
    }

    public void setIsChainedNode(boolean chainedNode) {
        isChainedNode = chainedNode;
    }

}
