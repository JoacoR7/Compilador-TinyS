package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class SimpleSelfNode extends SimpleAccessNode {

    private SimpleChainedNode simpleChainedNode;

    public SimpleSelfNode(SimpleChainedNode simpleChainedNode) {
        this.simpleChainedNode = simpleChainedNode;
    }

    public SimpleChainedNode getSimpleChainedNode() {
        return simpleChainedNode;
    }

    public void setSimpleChainedNode(SimpleChainedNode simpleChainedNode) {
        this.simpleChainedNode = simpleChainedNode;
    }


    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");

        sb.append(innerIndent)
                .append("\"nodeType\": \"SimpleSelfNode\",\n");

        sb.append(innerIndent)
                .append("\"value\": \"self\"");

        if (simpleChainedNode != null) {

            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"chained\": \n");

            sb.append(simpleChainedNode.toJson(innerIndent + "\t"));
        }

        sb.append("\n");
        sb.append(indent).append("}");

        return sb.toString();
    }
}
