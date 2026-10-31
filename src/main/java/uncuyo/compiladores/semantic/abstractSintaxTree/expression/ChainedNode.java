package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;

/**
 * Base de los accesos y llamadas que pueden formar un encadenado.
 * Además del siguiente nodo, conserva una marca que indica si este acceso
 * se resuelve como continuación de un receptor previo (isChainedNode).
 */
public abstract class ChainedNode extends OperandNode {
    private Token token;
    private ChainedNode chainedNode;
    private boolean isChainedNode;

    /**
     * Crea el nodo para un acceso encadenado.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public ChainedNode(Token token, ChainedNode chainedNode) {
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
     * Obtiene el siguiente acceso o llamada, o {@code null} si termina el encadenado.
     *
     * @return el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public ChainedNode getChainedNode() {
        return chainedNode;
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
     * Obtiene la marca que indica si el acceso se resuelve desde un receptor previo.
     *
     * @return la marca que indica si el acceso se resuelve desde un receptor previo
     */
    public boolean isChainedNode() {
        return isChainedNode;
    }

    /**
     * Establece la marca que indica si el acceso se resuelve desde un receptor previo.
     *
     * @param chainedNode la marca que indica si el acceso se resuelve desde un receptor previo
     */
    public void setIsChainedNode(boolean chainedNode) {
        isChainedNode = chainedNode;
    }

}
