package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

/**
 * Sentencia {@code ret} con token de ubicación y expresión opcional.
 */
public class ReturnNode extends SentenceNode {
    private Token token;
    private ExpressionNode expression;

    /**
     * Crea el nodo para una sentencia de retorno.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param expression la expresión devuelta, o {@code null} para un retorno sin valor
     */
    public ReturnNode(Token token, ExpressionNode expression) {
        this.token = token;
        this.expression = expression;
    }

    /**
     * Obtiene la expresión devuelta, o {@code null} para un retorno sin valor.
     *
     * @return la expresión devuelta, o {@code null} para un retorno sin valor
     */
    public ExpressionNode getExpression() {
        return expression;
    }

    /**
     * Establece la expresión devuelta, o {@code null} para un retorno sin valor.
     *
     * @param expression la expresión devuelta, o {@code null} para un retorno sin valor
     */
    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
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
                .append("\"type\": \"ReturnNode\",\n");

        if (expression != null) {

            sb.append(innerIndent)
                    .append("\"expression\": \n");

            sb.append(expression.toJson(innerIndent + "\t"))
                    .append("\n");

        } else {

            sb.append(innerIndent)
                    .append("\"expression\": null\n");
        }

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
