package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

/**
 * Expresión utilizada como sentencia, por ejemplo una llamada a un método.
 */
public class SimpleSentenceNode extends SentenceNode{
    private ExpressionNode expressionNode;
    /**
     * Crea el nodo para una expresión usada como sentencia.
     *
     * @param expressionNode la expresión utilizada como sentencia
     */
    public SimpleSentenceNode(ExpressionNode expressionNode){
        this.expressionNode = expressionNode;
    }

    /**
     * Obtiene la expresión utilizada como sentencia.
     *
     * @return la expresión utilizada como sentencia
     */
    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    /**
     * Establece la expresión utilizada como sentencia.
     *
     * @param expressionNode la expresión utilizada como sentencia
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
                .append("\"nodeType\": \"SimpleSentenceNode\",\n");

        sb.append(innerIndent)
                .append("\"expression\": \n");

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
