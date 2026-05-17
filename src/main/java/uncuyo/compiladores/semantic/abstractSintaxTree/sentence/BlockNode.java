package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import java.util.LinkedList;

public class BlockNode extends SentenceNode{
    private LinkedList<SentenceNode> sentences;

    public BlockNode(LinkedList<SentenceNode> sentences) {
        this.sentences = sentences;
    }

    public LinkedList<SentenceNode> getSentences() {
        return sentences;
    }

    public void setSentences(LinkedList<SentenceNode> sentences) {
        this.sentences = sentences;
    }

    @Override
    public void check() {

    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
