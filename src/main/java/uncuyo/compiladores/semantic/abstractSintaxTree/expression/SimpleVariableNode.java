package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class SimpleVariableNode extends  SimpleAccessNode {

    private Token token;
    private SimpleChainedNode simpleChainedNode;

    public SimpleVariableNode(Token token, SimpleChainedNode simpleChainedNode) {
        this.token = token;
        this.simpleChainedNode = simpleChainedNode;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public SimpleChainedNode getSimpleChainedNode() {
        return simpleChainedNode;
    }

    public void setSimpleChainedNode(SimpleChainedNode simpleChainedNode) {
        this.simpleChainedNode = simpleChainedNode;
    }

    @Override
    public Type check() {
        return null;
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
                .append(token.getLexeme())
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
