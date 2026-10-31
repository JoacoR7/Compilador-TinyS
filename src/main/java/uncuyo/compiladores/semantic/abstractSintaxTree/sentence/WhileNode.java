package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;

/**
 * Ciclo {@code while} con condición, cuerpo y token de ubicación.
 */
public class WhileNode extends SentenceNode{
    private ExpressionNode expressionNode;
    private SentenceNode sentenceNode;
    private Token token;

    /**
     * Crea el nodo para un ciclo {@code while}.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param expressionNode la condición del ciclo {@code while}
     * @param sentenceNode el cuerpo del ciclo, o {@code null} si está vacío
     */
    public WhileNode(Token token , ExpressionNode expressionNode, SentenceNode sentenceNode){
        this.token = token;
        this.expressionNode = expressionNode;
        this.sentenceNode = sentenceNode;
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
     * Obtiene la condición del ciclo {@code while}.
     *
     * @return la condición del ciclo {@code while}
     */
    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    /**
     * Establece la condición del ciclo {@code while}.
     *
     * @param expressionNode la condición del ciclo {@code while}
     */
    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    /**
     * Obtiene el cuerpo del ciclo, o {@code null} si está vacío.
     *
     * @return el cuerpo del ciclo, o {@code null} si está vacío
     */
    public SentenceNode getSentenceNode() {
        return sentenceNode;
    }

    /**
     * Establece el cuerpo del ciclo, o {@code null} si está vacío.
     *
     * @param sentenceNode el cuerpo del ciclo, o {@code null} si está vacío
     */
    public void setSentenceNode(SentenceNode sentenceNode) {
        this.sentenceNode = sentenceNode;
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
