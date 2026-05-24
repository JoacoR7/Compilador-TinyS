package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ArrayNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.LiteralNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.VariableNode;

public interface Visitor {
    void visit(LiteralNode literalNode);
}
