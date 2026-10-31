package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

/**
 * Sentencia condicional con condición, rama {@code if} y rama {@code else} opcional.
 * Una rama puede ser {@code null} cuando representa una sentencia vacía;
 * la ausencia de {@code else} también se representa con {@code null}.
 */
public class IfElseNode extends SentenceNode {
    private Token token;
    private ExpressionNode expression;
    private SentenceNode sentenceNodeIf;
    private SentenceNode sentenceNodeElse;

    /**
     * Crea el nodo para una sentencia {@code if/else}.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param expression la condición del {@code if}
     * @param sentenceNodeIf la rama {@code if}, o {@code null} si está vacía
     * @param sentenceNodeElse la rama {@code else}, o {@code null} si está ausente o vacía
     */
    public IfElseNode(Token token, ExpressionNode expression, SentenceNode sentenceNodeIf, SentenceNode sentenceNodeElse) {
        this.token = token;
        this.expression = expression;
        this.sentenceNodeIf = sentenceNodeIf;
        this.sentenceNodeElse = sentenceNodeElse;
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
     * Obtiene la condición del {@code if}.
     *
     * @return la condición del {@code if}
     */
    public ExpressionNode getExpression() {
        return expression;
    }

    /**
     * Establece la condición del {@code if}.
     *
     * @param expression la condición del {@code if}
     */
    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    /**
     * Obtiene la rama {@code if}, o {@code null} si está vacía.
     *
     * @return la rama {@code if}, o {@code null} si está vacía
     */
    public SentenceNode getSentenceNodeIf() {
        return sentenceNodeIf;
    }

    /**
     * Establece la rama {@code if}, o {@code null} si está vacía.
     *
     * @param sentenceNodeIf la rama {@code if}, o {@code null} si está vacía
     */
    public void setSentenceNodeIf(SentenceNode sentenceNodeIf) {
        this.sentenceNodeIf = sentenceNodeIf;
    }

    /**
     * Obtiene la rama {@code else}, o {@code null} si está ausente o vacía.
     *
     * @return la rama {@code else}, o {@code null} si está ausente o vacía
     */
    public SentenceNode getSentenceNodeElse() {
        return sentenceNodeElse;
    }

    /**
     * Establece la rama {@code else}, o {@code null} si está ausente o vacía.
     *
     * @param sentenceNodeElse la rama {@code else}, o {@code null} si está ausente o vacía
     */
    public void setSentenceNodeElse(SentenceNode sentenceNodeElse) {
        this.sentenceNodeElse = sentenceNodeElse;
    }


    /**
     * Serializador local pendiente de implementación.
     * @param indent sangría prevista para la representación local
     * @return una cadena vacía
     */
    @Override
    public String toJson(String indent) {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
