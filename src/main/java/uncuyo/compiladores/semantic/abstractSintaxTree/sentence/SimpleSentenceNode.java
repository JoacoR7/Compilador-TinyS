package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class SimpleSentenceNode extends SentenceNode{
    private ExpressionNode expressionNode;
    public SimpleSentenceNode(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }


    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");

        sb.append(innerIndent)
                .append("\"nodeType\": \"SimpleSentenceNode\",\n");

        sb.append(innerIndent)
                .append("\"expression\": \n");

        if (expressionNode != null) {
            sb.append(expressionNode.toJson(innerIndent + "\t"));
        } else {
            sb.append(innerIndent).append("\tnull");
        }

        sb.append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
