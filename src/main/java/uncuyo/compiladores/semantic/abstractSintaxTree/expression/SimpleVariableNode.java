package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class SimpleVariableNode extends SimpleAccessNode {

    private SimpleChainedNode simpleChainedNode;

    public SimpleVariableNode(Token token, Type type, SimpleChainedNode simpleChainedNode) {
        super(token);
        this.setType(type);
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
                .append("\"nodeType\": \"SimpleVariableNode\",\n");

        sb.append(innerIndent)
                .append("\"name\": \"")
                .append(this.getToken().getLexeme())
                .append("\"");

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
