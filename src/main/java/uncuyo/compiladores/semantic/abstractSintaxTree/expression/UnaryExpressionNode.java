package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Expresión formada por un operador unario y su operando.
 */
public class UnaryExpressionNode extends ExpressionNode {
    private Token unaryOperatorToken;
    private ExpressionNode expressionNode;

    /**
     * Crea el nodo para una operación unaria.
     *
     * @param expressionNode el operando de la operación unaria
     * @param unaryOperatorToken el token del operador unario y su ubicación
     */
    public UnaryExpressionNode(ExpressionNode expressionNode, Token unaryOperatorToken) {
        this.expressionNode = expressionNode;
        this.unaryOperatorToken = unaryOperatorToken;
    }

    /**
     * Obtiene el operando de la operación unaria.
     *
     * @return el operando de la operación unaria
     */
    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    /**
     * Establece el operando de la operación unaria.
     *
     * @param expressionNode el operando de la operación unaria
     */
    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    /**
     * Obtiene el token del operador unario y su ubicación.
     *
     * @return el token del operador unario y su ubicación
     */
    public Token getUnaryOperatorToken() {
        return unaryOperatorToken;
    }

    /**
     * Establece el token del operador unario y su ubicación.
     *
     * @param unaryOperatorToken el token del operador unario y su ubicación
     */
    public void setUnaryOperatorToken(Token unaryOperatorToken) {
        this.unaryOperatorToken = unaryOperatorToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Serializador local pendiente de implementación.
     * @param indent sangría prevista para la representación local
     * @return una cadena vacía
     */
    @Override
    public String toJson(String indent) {
        return "";
    }

}
