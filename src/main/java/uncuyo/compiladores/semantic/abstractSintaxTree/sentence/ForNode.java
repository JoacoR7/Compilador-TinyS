package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.VariableNode;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class ForNode extends SentenceNode {
    private Type type;
    private VariableNode varNode1;
    private VariableNode varNode2;
    private SentenceNode sentenceNode;

    public ForNode(Type type, VariableNode varNode1, VariableNode varNode2, SentenceNode sentenceNode) {
        this.type = type;
        this.varNode1 = varNode1;
        this.varNode2 = varNode2;
        this.sentenceNode = sentenceNode;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public VariableNode getVarNode1() {
        return varNode1;
    }

    public void setVarNode1(VariableNode varNode1) {
        this.varNode1 = varNode1;
    }

    public VariableNode getVarNode2() {
        return varNode2;
    }

    public void setVarNode2(VariableNode varNode2) {
        this.varNode2 = varNode2;
    }

    public SentenceNode getSentenceNode() {
        return sentenceNode;
    }

    public void setSentenceNode(SentenceNode sentenceNode) {
        this.sentenceNode = sentenceNode;
    }

    @Override
    public void check() {

    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
