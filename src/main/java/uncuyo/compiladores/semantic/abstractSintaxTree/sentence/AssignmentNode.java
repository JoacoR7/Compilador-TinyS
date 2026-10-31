package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleAccessNode;

/**
 * Asignación que conserva el acceso de destino y la expresión de origen.
 */
public class AssignmentNode extends SentenceNode {

    private SimpleAccessNode simpleAccessNode;
    private ExpressionNode expressionNode;

    /**
     * Crea el nodo para una asignación.
     *
     * @param simpleAccessNode el acceso al destino de la asignación
     * @param expressionNode la expresión del valor que se asigna
     */
    public AssignmentNode(SimpleAccessNode simpleAccessNode, ExpressionNode expressionNode) {
        this.simpleAccessNode = simpleAccessNode;
        this.expressionNode = expressionNode;
    }

    /**
     * Obtiene el acceso al destino de la asignación.
     *
     * @return el acceso al destino de la asignación
     */
    public SimpleAccessNode getSimpleAccessNode() {
        return simpleAccessNode;
    }

    /**
     * Establece el acceso al destino de la asignación.
     *
     * @param simpleAccessNode el acceso al destino de la asignación
     */
    public void setSimpleAccessNode(SimpleAccessNode simpleAccessNode) {
        this.simpleAccessNode = simpleAccessNode;
    }

    /**
     * Obtiene la expresión del valor que se asigna.
     *
     * @return la expresión del valor que se asigna
     */
    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    /**
     * Establece la expresión del valor que se asigna.
     *
     * @param expressionNode la expresión del valor que se asigna
     */
    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
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
                .append("\"nodeType\": \"AssignmentNode\",\n");

        sb.append(innerIndent)
                .append("\"leftSide\": \n");

        if (simpleAccessNode != null) {
            sb.append(simpleAccessNode.toJson(innerIndent + "\t"));
        } else {
            sb.append(innerIndent).append("\tnull");
        }

        sb.append(",\n");

        sb.append(innerIndent)
                .append("\"rightSide\": \n");

        if (expressionNode != null) {
            sb.append(expressionNode.toJson(innerIndent + "\t"));
        } else {
            sb.append(innerIndent).append("\tnull");
        }

        sb.append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
