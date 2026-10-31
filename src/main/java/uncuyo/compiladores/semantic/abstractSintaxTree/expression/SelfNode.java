package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Referencia a la instancia actual mediante {@code self}, con acceso posterior opcional.
 */
public class SelfNode extends OperandNode{

    private ChainedNode chainedNode;
    private Token token;


    /**
     * Crea el nodo para una referencia a {@code self}.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public SelfNode(Token token, ChainedNode chainedNode) {
        this.token = token;
        this.chainedNode = chainedNode;
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
     * Establece el siguiente acceso o llamada, o {@code null} si termina el encadenado.
     *
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public void setChainedNode(ChainedNode chainedNode) {
        this.chainedNode = chainedNode;
    }

    /**
     * Obtiene el siguiente acceso o llamada, o {@code null} si termina el encadenado.
     *
     * @return el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public ChainedNode getChainedNode() {
        return chainedNode;
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
