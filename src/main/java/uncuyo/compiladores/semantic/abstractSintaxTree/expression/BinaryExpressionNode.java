package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Expresión binaria con token de operador y operandos izquierdo y derecho.
 */
public class BinaryExpressionNode extends ExpressionNode {
    private ExpressionNode leftNode;
    private ExpressionNode rightNode;
    private Token binaryOperatorToken;

    /**
     * Crea una operación binaria
     * @param binaryOperatorToken el token del operador binario y su ubicación
     */
    public BinaryExpressionNode(Token binaryOperatorToken) {
        this.binaryOperatorToken = binaryOperatorToken;
    }

    /**
     * Crea el nodo para una operación binaria.
     *
     * @param binaryOperatorToken el token del operador binario y su ubicación
     * @param leftNode el operando izquierdo
     * @param rightNode el operando derecho
     */
    public BinaryExpressionNode(Token binaryOperatorToken, ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.binaryOperatorToken = binaryOperatorToken;
    }

    /**
     * Obtiene el operando izquierdo.
     *
     * @return el operando izquierdo
     */
    public ExpressionNode getLeftNode() {
        return leftNode;
    }

    /**
     * Establece el operando izquierdo.
     *
     * @param leftNode el operando izquierdo
     */
    public void setLeftNode(ExpressionNode leftNode) {
        this.leftNode = leftNode;
    }

    /**
     * Obtiene el operando derecho.
     *
     * @return el operando derecho
     */
    public ExpressionNode getRightNode() {
        return rightNode;
    }

    /**
     * Establece el operando derecho.
     *
     * @param rightNode el operando derecho
     */
    public void setRightNode(ExpressionNode rightNode) {
        this.rightNode = rightNode;
    }

    /**
     * Obtiene el token del operador binario y su ubicación.
     *
     * @return el token del operador binario y su ubicación
     */
    public Token getBinaryOperatorToken() {
        return binaryOperatorToken;
    }

    /**
     * Establece el token del operador binario y su ubicación.
     *
     * @param binaryOperatorToken el token del operador binario y su ubicación
     */
    public void setBinaryOperatorToken(Token binaryOperatorToken) {
        this.binaryOperatorToken = binaryOperatorToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Serializa los datos locales del nodo y los hijos.
     *
     * @param indent sangría inicial para el objeto y sus hijos
     * @return representación JSON local del nodo
     */
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
