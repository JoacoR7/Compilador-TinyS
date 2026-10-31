package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Acceso indexado a un arreglo en el destino de una asignación.
 */
public class SimpleArrayNode extends SimpleAccessNode{

    private ExpressionNode expressionNode;

    /**
     * Crea el nodo para un acceso indexado de destino.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param expressionNode la expresión del índice del arreglo
     */
    public SimpleArrayNode(Token token, ExpressionNode expressionNode) {
        super(token);
        this.expressionNode = expressionNode;
    }

    /**
     * Obtiene la expresión del índice del arreglo.
     *
     * @return la expresión del índice del arreglo
     */
    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    /**
     * Establece la expresión del índice del arreglo.
     *
     * @param expressionNode la expresión del índice del arreglo
     */
    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
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
