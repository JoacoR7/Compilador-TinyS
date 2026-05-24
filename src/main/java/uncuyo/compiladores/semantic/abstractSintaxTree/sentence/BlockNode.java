package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

import java.util.LinkedList;

public class BlockNode extends SentenceNode{
    private LinkedList<SentenceNode> sentences;
    private String className;
    private String methodName;
    private boolean isMethod;

    public BlockNode(LinkedList<SentenceNode> sentences, String className, String methodName, boolean isMethod) {
        this.sentences = sentences;
        this.className = className;
        this.methodName = methodName;
        this.isMethod = isMethod;
    }

    public LinkedList<SentenceNode> getSentences() {
        return sentences;
    }

    public void setSentences(LinkedList<SentenceNode> sentences) {
        this.sentences = sentences;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isMethod() {
        return isMethod;
    }

    public void setMethod(boolean method) {
        isMethod = method;
    }

    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");

        if (isMethod) {

            sb.append(innerIndent)
                    .append("\"className\": \"")
                    .append(className)
                    .append("\",\n");

            sb.append(innerIndent)
                    .append("\"methodName\": \"")
                    .append(methodName)
                    .append("\",\n");

        }




        if (sentences != null && !sentences.isEmpty()) {

            sb.append(innerIndent)
                    .append("\"sentences\": [\n");

            int total = sentences.size();
            int i = 0;

            for (SentenceNode sentence : sentences) {

                sb.append(sentence.toJson(innerIndent + "\t"));

                if (++i < total) {
                    sb.append(",");
                }

                sb.append("\n");
            }
            sb.append(innerIndent).append("]\n");
        } else {
            sb.append(innerIndent)
                    .append("\"sentences\": []\n");
        }

        sb.append(indent).append("}");

        return sb.toString();
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
