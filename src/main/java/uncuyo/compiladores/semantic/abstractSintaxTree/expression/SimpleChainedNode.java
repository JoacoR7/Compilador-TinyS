package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Acceso a un atributo dentro del encadenado de un destino de asignación.
 * Conserva el contexto para resolver el atributo y la continuación opcional.
 */
public class SimpleChainedNode extends OperandNode{
    private Token token;
    private SimpleChainedNode simpleChainedNode;
    private String className;
    private String methodName;


    /**
     * Crea el nodo para un atributo encadenado de destino.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param simpleChainedNode el siguiente atributo de destino, o {@code null} si termina el encadenado
     * @param className el nombre de clase utilizado como contexto de resolución
     * @param methodName el nombre del método de contexto; {@code .} identifica al constructor
     */
    public SimpleChainedNode(Token token, SimpleChainedNode simpleChainedNode, String className, String methodName) {
        this.token = token;
        this.simpleChainedNode = simpleChainedNode;
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Obtiene el token que identifica el acceso o la sentencia y su ubicación.
     *
     * @return el token que identifica el acceso o la sentencia y su ubicación
     */
    public Token getToken() {
        return token;
    }

    /**
     * Establece el token que identifica el acceso o la sentencia y su ubicación.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * Obtiene el siguiente atributo de destino, o {@code null} si termina el encadenado.
     *
     * @return el siguiente atributo de destino, o {@code null} si termina el encadenado
     */
    public SimpleChainedNode getSimpleChainedNode() {
        return simpleChainedNode;
    }

    /**
     * Establece el siguiente atributo de destino, o {@code null} si termina el encadenado.
     *
     * @param simpleChainedNode el siguiente atributo de destino, o {@code null} si termina el encadenado
     */
    public void setSimpleChainedNode(SimpleChainedNode simpleChainedNode) {
        this.simpleChainedNode = simpleChainedNode;
    }

    /**
     * Obtiene el nombre de clase utilizado como contexto de resolución.
     *
     * @return el nombre de clase utilizado como contexto de resolución
     */
    public String getClassName() {
        return className;
    }

    /**
     * Establece el nombre de clase utilizado como contexto de resolución.
     *
     * @param className el nombre de clase utilizado como contexto de resolución
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Obtiene el nombre del método de contexto; {@code .} identifica al constructor.
     *
     * @return el nombre del método de contexto; {@code .} identifica al constructor
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Establece el nombre del método de contexto; {@code .} identifica al constructor.
     *
     * @param methodName el nombre del método de contexto; {@code .} identifica al constructor
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
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
                .append("\"nodeType\": \"SimpleChainedNode\",\n");

        sb.append(innerIndent)
                .append("\"name\": \"")
                .append(token.getLexeme())
                .append("\"");

        if (className != null) {
            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"className\": \"")
                    .append(className)
                    .append("\"");
        }

        if (methodName != null) {
            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"methodName\": \"")
                    .append(methodName)
                    .append("\"");
        }

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
