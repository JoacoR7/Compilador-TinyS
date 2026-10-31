package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

/**
 * Base de las expresiones de construcción de objetos y arreglos.
 * Almacena el token de construcción, el tipo creado y un encadenado opcional.
 */
public abstract class ConclassorCallNode extends ExpressionNode{
    private Token token;
    private ChainedNode chainedNode;

    /**
     * Crea el nodo para una expresión de construcción.
     *
     * @param token el token que identifica el acceso o la sentencia y su ubicación
     * @param type el tipo de la instancia o arreglo que se crea
     * @param chainedNode el siguiente acceso o llamada, o {@code null} si termina el encadenado
     */
    public ConclassorCallNode(Token token, Type type, ChainedNode chainedNode) {
        this.setType(type);
        this.chainedNode = chainedNode;
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
