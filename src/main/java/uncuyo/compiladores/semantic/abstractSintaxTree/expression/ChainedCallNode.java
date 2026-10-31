package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import java.util.LinkedList;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Llamada a un método con argumentos en orden y continuación opcional (nodo encadenado).
 * También representa el receptor de clase en un acceso estático.
 */
public class ChainedCallNode extends ChainedNode{

    private LinkedList<ExpressionNode> expressionNodes;

    /**
     * Crea el nodo para una llamada encadenada.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     * @param expressionNodes la lista de argumentos en el orden de la llamada
     */
    public ChainedCallNode(Token token, ChainedNode chainedNode, LinkedList<ExpressionNode> expressionNodes) {
        super(token, chainedNode);
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

    /**
     * Serializa los datos locales del nodo y los hijos incluidos por esta implementación.
     * @param indent sangría inicial para el objeto y sus hijos
     * @return representación JSON local del nodo
     */
    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");

        sb.append(innerIndent)
                .append("\"nodeType\": \"ChainedCallNode\",\n");

        sb.append(innerIndent)
                .append("\"method\": \"")
                .append(getToken().getLexeme())
                .append("\",\n");



        if (expressionNodes != null && !expressionNodes.isEmpty()) {
            sb.append(innerIndent)
                    .append("\"arguments\": [\n");

            int total = expressionNodes.size();
            int i = 0;

            for (ExpressionNode expressionNode : expressionNodes) {

                sb.append(expressionNode.toJson(innerIndent + "\t"));

                if (++i < total) {
                    sb.append(",");
                }

                sb.append("\n");
            }

            sb.append(innerIndent).append("]");
        } else {
            sb.append(innerIndent)
                    .append("\"arguments\": []");
        }

        if (getChainedNode() != null) {

            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"chained\": \n");

            sb.append(getChainedNode().toJson(innerIndent + "\t"));
        }

        sb.append("\n");
        sb.append(indent).append("}");

        return sb.toString();
    }
}
