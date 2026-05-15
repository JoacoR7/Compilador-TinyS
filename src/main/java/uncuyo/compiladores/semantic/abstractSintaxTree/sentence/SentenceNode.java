package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Node;
import uncuyo.compiladores.semantic.symbolTable.Type;

public abstract class SentenceNode extends Node {
    public abstract void check();
}
