package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
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
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");

        sb.append(innerIndent)
                .append("\"nodeType\": \"ChainedCallNode\",\n");

        sb.append(innerIndent)
                .append("\"method\": \"")
                .append(getToken().getLexeme())
                .append("\",\n");



        if (expressionNodes != null && !expressionNodes.isEmpty()) {
            sb.append(innerIndent)
                    .append("\"arguments\": [\n");

            int total = expressionNodes.size();
            int i = 0;

            for (ExpressionNode expressionNode : expressionNodes) {

                sb.append(expressionNode.toJson(innerIndent + "\t"));

                if (++i < total) {
                    sb.append(",");
                }

                sb.append("\n");
            }

            sb.append(innerIndent).append("]");
        } else {
            sb.append(innerIndent)
                    .append("\"arguments\": []");
        }

        if (getChainedNode() != null) {

            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"chained\": \n");

            sb.append(getChainedNode().toJson(innerIndent + "\t"));
        }

        sb.append("\n");
        sb.append(indent).append("}");

        return sb.toString();
    }
}
