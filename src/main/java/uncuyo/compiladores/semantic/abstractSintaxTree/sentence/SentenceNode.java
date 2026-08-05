package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Node;
import uncuyo.compiladores.semantic.symbolTable.Type;

public abstract class SentenceNode extends Node {
    protected boolean hasReturn = false;

    public boolean isHasReturn() {
        return hasReturn;
    }

    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }
}
