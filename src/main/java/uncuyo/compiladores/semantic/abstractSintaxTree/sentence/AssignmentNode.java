package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.OperandNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleAccessNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleChainedNode;

public class AssignmentNode extends SentenceNode {

    private SimpleAccessNode simpleAccessNode;
    private ExpressionNode expressionNode;

    public AssignmentNode(SimpleAccessNode simpleAccessNode, ExpressionNode expressionNode) {
        this.simpleAccessNode = simpleAccessNode;
        this.expressionNode = expressionNode;
    }

    public SimpleAccessNode getSimpleAccessNode() {
        return simpleAccessNode;
    }

    public void setSimpleAccessNode(SimpleAccessNode simpleAccessNode) {
        this.simpleAccessNode = simpleAccessNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    @Override
    public void check() {

    }

    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");

        sb.append(innerIndent)
                .append("\"nodeType\": \"AssignmentNode\",\n");

        sb.append(innerIndent)
                .append("\"leftSide\": \n");

        if (simpleAccessNode != null) {
            sb.append(simpleAccessNode.toJson(innerIndent + "\t"));
        } else {
            sb.append(innerIndent).append("\tnull");
        }

        sb.append(",\n");

        sb.append(innerIndent)
                .append("\"rightSide\": \n");

        if (expressionNode != null) {
            sb.append(expressionNode.toJson(innerIndent + "\t"));
        } else {
            sb.append(innerIndent).append("\tnull");
        }

        sb.append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
