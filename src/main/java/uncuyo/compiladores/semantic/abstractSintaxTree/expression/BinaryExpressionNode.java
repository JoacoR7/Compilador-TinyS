package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode leftNode;
    private ExpressionNode rightNode;
    private Token binaryOperatorToken;

    public BinaryExpressionNode(Token binaryOperatorToken) {
        this.binaryOperatorToken = binaryOperatorToken;
    }

    public BinaryExpressionNode(Token binaryOperatorToken, ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.binaryOperatorToken = binaryOperatorToken;
    }

    public ExpressionNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(ExpressionNode leftNode) {
        this.leftNode = leftNode;
    }

    public ExpressionNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(ExpressionNode rightNode) {
        this.rightNode = rightNode;
    }

    public Token getBinaryOperatorToken() {
        return binaryOperatorToken;
    }

    public void setBinaryOperatorToken(Token binaryOperatorToken) {
        this.binaryOperatorToken = binaryOperatorToken;
    }

    @Override
    public Type check() {
        return null;
    }

    @Override
    public String toJson(String indent) {
        String childIndent = indent + "\t";

        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(childIndent)
                .append("\"nodeType\": \"")
                .append(BinaryExpressionNode.class.getSimpleName())
                .append("\",\n");

        sb.append(childIndent)
                .append("\"operator\": \"")
                .append(this.binaryOperatorToken.getLexeme())
                .append("\",\n");

        sb.append(childIndent)
                .append("\"leftNode\": \n")
                .append(leftNode.toJson(childIndent))
                .append(",\n");

        sb.append(childIndent)
                .append("\"rightNode\": \n")
                .append(rightNode.toJson(childIndent))
                .append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
