package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;

public abstract class ChainedNode extends OperandNode {
    private Token token;
    private ChainedNode chainedNode;

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


    @Override
    public String toJson(String indent) {
        return "";
    }
}
