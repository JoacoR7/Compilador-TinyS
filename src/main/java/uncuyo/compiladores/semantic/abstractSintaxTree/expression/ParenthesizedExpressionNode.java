package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Expresión agrupada entre paréntesis con un encadenado posterior opcional.
 */
public class ParenthesizedExpressionNode extends ExpressionNode {

    private ExpressionNode expresion;
    private ChainedNode chainedNode;

    /**
     * Crea el nodo para una expresión entre paréntesis.
     *
     * @param expresion la expresión agrupada entre paréntesis
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public ParenthesizedExpressionNode(ExpressionNode expresion, ChainedNode chainedNode) {
        this.expresion = expresion;
        this.chainedNode = chainedNode;
    }

    /**
     * Obtiene la expresión agrupada entre paréntesis.
     *
     * @return la expresión agrupada entre paréntesis
     */
    public ExpressionNode getExpresion() {
        return expresion;
    }

    /**
     * Establece la expresión agrupada entre paréntesis.
     *
     * @param expresion la expresión agrupada entre paréntesis
     */
    public void setExpresion(ExpressionNode expresion) {
        this.expresion = expresion;
    }

    /**
     * Obtiene el siguiente acceso o llamada, o {@code null} si termina el encadenado.
     *
     * @return el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public ChainedNode getChainedNode() {
        return chainedNode;
    }

    /**
     * Establece el siguiente acceso o llamada, o {@code null} si termina el encadenado.
     *
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
