package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;


/**
 * Referencia a una variable o atributo dentro de una expresión.
 * Conserva los nombres de clase y método utilizados en la resolución y
 * puede continuar con accesos o llamadas encadenados.
 */
public class VariableNode extends ChainedNode{

    private String className;
    private String methodName;

    /**
     * Crea el nodo para una referencia a una variable o atributo.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     * @param className el nombre de clase utilizado como contexto de resolución
     * @param methodName el nombre del método de contexto; {@code .} identifica al constructor
     */
    public VariableNode(Token token, ChainedNode chainedNode, String className, String methodName) {
        super(token, chainedNode);
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Crea el nodo para una referencia a una variable o atributo.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param type el tipo semántico almacenado, o {@code null} si todavía no se resolvió
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     * @param className el nombre de clase utilizado como contexto de resolución
     * @param methodName el nombre del método de contexto; {@code .} identifica al constructor
     */
    public VariableNode(Token token, Type type, ChainedNode chainedNode, String className, String methodName) {
        super(token, chainedNode);
        this.setType(type);
        this.className = className;
        this.methodName = methodName;
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

    // persona.mascota.edad


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
                .append("\"nodeType\": \"VariableNode\",\n");

        sb.append(innerIndent)
                .append("\"name\": \"")
                .append(getToken().getLexeme())
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
