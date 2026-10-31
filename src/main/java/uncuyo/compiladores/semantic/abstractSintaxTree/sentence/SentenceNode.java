package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Node;

/**
 * Base de las sentencias del AST con una marca mutable de retorno.
 * El visitante semántico actualiza y propaga la marca según el tipo de
 * sentencia; por sí sola no constituye un análisis de todos los caminos
 * de ejecución.
 */
public abstract class SentenceNode extends Node {
    /** Marca de retorno calculada y propagada por el visitante semántico. */
    protected boolean hasReturn = false;

    /**
     * Obtiene la marca de retorno asignada por el visitante semántico.
     *
     * @return la marca de retorno asignada por el visitante semántico
     */
    public boolean isHasReturn() {
        return hasReturn;
    }

    /**
     * Establece la marca de retorno asignada por el visitante semántico.
     *
     * @param hasReturn la marca de retorno asignada por el visitante semántico
     */
    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
}
