package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

/**
 * Acceso de destino que comienza en una variable o un atributo.
 * Conserva su tipo inicial y un encadenado opcional de atributos.
 */
public class SimpleVariableNode extends SimpleAccessNode {

    private SimpleChainedNode simpleChainedNode;

    /**
     * Crea el nodo para un acceso de destino a una variable.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param type el tipo semántico almacenado, o {@code null} si todavía no se resolvió
     * @param simpleChainedNode el siguiente atributo de destino, o {@code null} si termina el encadenado
     */
    public SimpleVariableNode(Token token, Type type, SimpleChainedNode simpleChainedNode) {
        super(token);
        this.setType(type);
        this.simpleChainedNode = simpleChainedNode;
    }

    /**
     * Obtiene el siguiente atributo, o {@code null} si termina el encadenado.
     *
     * @return el siguiente atributo, o {@code null} si termina el encadenado
     */
    public SimpleChainedNode getSimpleChainedNode() {
        return simpleChainedNode;
    }

    /**
     * Establece el siguiente atributo, o {@code null} si termina el encadenado.
     *
     * @param simpleChainedNode el siguiente atributo, o {@code null} si termina el encadenado
     */
    public void setSimpleChainedNode(SimpleChainedNode simpleChainedNode) {
        this.simpleChainedNode = simpleChainedNode;
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
                .append("\"nodeType\": \"SimpleVariableNode\",\n");

        sb.append(innerIndent)
                .append("\"name\": \"")
                .append(this.getToken().getLexeme())
                .append("\"");

        if (simpleChainedNode != null) {
            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"chained\": \n");

            sb.append(simpleChainedNode.toJson(innerIndent + "\t"));
        }

        sb.append("\n");
        sb.append(indent).append("}");

        return sb.toString();
    }
}
