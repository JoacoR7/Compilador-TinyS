package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.semantic.abstractSintaxTree.Node;
import uncuyo.compiladores.semantic.symbolTable.Type;

public abstract class ExpressionNode extends Node {
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public abstract Type check();
}
