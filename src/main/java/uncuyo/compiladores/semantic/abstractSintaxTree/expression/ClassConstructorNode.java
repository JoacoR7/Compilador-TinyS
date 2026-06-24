package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

import java.util.LinkedList;

public class ClassConstructorNode extends ConclassorCallNode {

    private LinkedList<ExpressionNode> expressionNodes;
    private ChainedNode chainedNode;

    public ClassConstructorNode(Token token, Type type, LinkedList<ExpressionNode> expressionNodes, ChainedNode chainedNode) {
        super(token, type, chainedNode);
        this.expressionNodes = expressionNodes;
    }

    public LinkedList<ExpressionNode> getExpressionNodes() {
        return expressionNodes;
    }

    public void setExpressionNodes(LinkedList<ExpressionNode> expressionNodes) {
        this.expressionNodes = expressionNodes;
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
