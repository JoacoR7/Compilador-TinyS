package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;

/**
 * Base de los accesos que pueden ocupar el destino de una asignación.
 */
public abstract  class SimpleAccessNode extends OperandNode{
    private Token token;

    /**
     * Crea el nodo para un acceso de destino.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     */
    public SimpleAccessNode(Token token) {
        this.token = token;
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
}
