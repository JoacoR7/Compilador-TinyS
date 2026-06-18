package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

public class ReturnNode extends SentenceNode {
    private Token token;
    private ExpressionNode expression;

    public ReturnNode(Token token, ExpressionNode expression) {
        this.token = token;
        this.expression = expression;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
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

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
