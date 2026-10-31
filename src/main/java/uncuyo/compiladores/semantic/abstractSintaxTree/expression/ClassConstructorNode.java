package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import java.util.LinkedList;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

/**
 * Creación de una instancia de clase con argumentos y encadenado opcional.
 */
public class ClassConstructorNode extends ConclassorCallNode {

    private LinkedList<ExpressionNode> expressionNodes;
    private ChainedNode chainedNode;

    /**
     * Crea el nodo para una creación de instancia.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param type el tipo de la instancia o arreglo que se crea
     * @param expressionNodes la lista de argumentos en el orden de la llamada
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public ClassConstructorNode(Token token, Type type, LinkedList<ExpressionNode> expressionNodes, ChainedNode chainedNode) {
        super(token, type, chainedNode);
        this.expressionNodes = expressionNodes;
    }

    /**
     * Obtiene la lista de argumentos en el orden de la llamada.
     *
     * @return la lista de argumentos en el orden de la llamada
     */
    public LinkedList<ExpressionNode> getExpressionNodes() {
        return expressionNodes;
    }

    /**
     * Establece la lista de argumentos en el orden de la llamada.
     *
     * @param expressionNodes la lista de argumentos en el orden de la llamada
     */
    public void setExpressionNodes(LinkedList<ExpressionNode> expressionNodes) {
        this.expressionNodes = expressionNodes;
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
