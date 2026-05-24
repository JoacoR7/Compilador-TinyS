package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class ReturnNode extends SentenceNode {
    private ExpressionNode expression;

    public ReturnNode(ExpressionNode expression) {
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
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
                .append("\"type\": \"ReturnNode\",\n");

        if (expression != null) {

            sb.append(innerIndent)
                    .append("\"expression\": \n");

            sb.append(expression.toJson(innerIndent + "\t"))
                    .append("\n");

        } else {

            sb.append(innerIndent)
                    .append("\"expression\": null\n");
        }

        sb.append(indent).append("}");

        return sb.toString();
    }
}
