package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

public abstract class ConclassorCallNode extends ExpressionNode{
    private Token token;
    private ChainedNode chainedNode;

    public ConclassorCallNode(Token token, Type type, ChainedNode chainedNode) {
        this.setType(type);
        this.chainedNode = chainedNode;
        this.token = token;
    }

    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
