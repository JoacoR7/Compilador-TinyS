package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.semantic.abstractSintaxTree.Node;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

/**
 * Base de las expresiones del AST con un tipo semántico mutable.
 * El tipo puede estar disponible desde la construcción o ser asignado
 * durante el chequeo; mientras no esté resuelto puede ser {@code null}.
 */
public abstract class ExpressionNode extends Node {
    private Type type;

    /**
     * Obtiene el tipo semántico almacenado, o {@code null} si todavía no se resolvió.
     *
     * @return el tipo semántico almacenado, o {@code null} si todavía no se resolvió
     */
    public Type getType() {
        return type;
    }

    /**
     * Establece el tipo semántico almacenado, o {@code null} si todavía no se resolvió.
     *
     * @param type el tipo semántico almacenado, o {@code null} si todavía no se resolvió
     */
    public void setType(Type type) {
        this.type = type;
    }


}
