package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

/**
 * Creación de un arreglo con una expresión de longitud y un tipo declarado.
 * La expresión de longitud se conserva en el AST para los visitantes.
 */
public class ArrayConstructorNode extends ConclassorCallNode {

    private ExpressionNode expression;

    /**
     * Crea el nodo para una creación de arreglo.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param type el tipo de la instancia o arreglo que se crea
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     * @param expression la expresión que determina la longitud del arreglo
     */
    public ArrayConstructorNode(Token token, Type type, ChainedNode chainedNode, ExpressionNode expression) {
        super(token, type, chainedNode);
        this.expression = expression;
    }

    /**
     * Obtiene la expresión que determina la longitud del arreglo.
     *
     * @return la expresión de la longitud del arreglo
     */
    public ExpressionNode getExpression() {
        return expression;
    }

    /**
     * Establece la expresión que determina la longitud del arreglo.
     *
     * @param expression la expresión de la longitud del arreglo
     */
    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
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
