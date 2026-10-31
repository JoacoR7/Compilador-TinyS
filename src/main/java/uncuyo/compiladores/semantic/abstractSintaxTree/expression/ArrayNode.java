package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Acceso a un elemento de arreglo dentro de una expresión o un encadenado.
 */
public class ArrayNode extends ChainedNode{
    private ExpressionNode expression;
    private String className;
    private String methodName;

    /**
     * Crea el nodo para un acceso indexado a un arreglo.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     * @param expression la expresión del índice del arreglo
     * @param className el nombre de clase en la que se encuentra
     * @param methodName el nombre del método de contexto
     */
    public ArrayNode(Token token, ChainedNode chainedNode, ExpressionNode expression, String className, String methodName) {
        super(token, chainedNode);
        this.expression = expression;
        this.className = className;
        this.methodName = methodName;
    }

    /**
     * Obtiene la expresión del índice del arreglo.
     *
     * @return  expresión
     */
    public ExpressionNode getExpression() {
        return expression;
    }

    /**
     * Establece la expresión del índice del arreglo.
     *
     * @param expression expresión
     */
    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    /**
     * Obtiene el nombre de clase utilizado 
     *
     * @return el nombre de clase
     */
    public String getClassName() {
        return className;
    }

    /**
     * Establece el nombre de clase utilizado
     *
     * @param className el nombre de clase
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Obtiene el nombre del método de contexto; {@code .} identifica al constructor.
     *
     * @return el nombre del método
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Establece el nombre del método de contexto; {@code .} identifica al constructor.
     *
     * @param methodName el nombre del método
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

    @Override
    public String toJson(String indent) {
        return "";
    }
}
