package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class SimpleChainedNode extends OperandNode{
    private Token token;
    private SimpleChainedNode simpleChainedNode;
    private String className;
    private String methodName;


    public SimpleChainedNode(Token token, SimpleChainedNode simpleChainedNode, String className, String methodName) {
        this.token = token;
        this.simpleChainedNode = simpleChainedNode;
        this.className = className;
        this.methodName = methodName;
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
                .append("\"nodeType\": \"SimpleChainedNode\",\n");

        sb.append(innerIndent)
                .append("\"name\": \"")
                .append(token.getLexeme())
                .append("\"");

        if (className != null) {
            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"className\": \"")
                    .append(className)
                    .append("\"");
        }

        if (methodName != null) {
            sb.append(",\n");

            sb.append(innerIndent)
                    .append("\"methodName\": \"")
                    .append(methodName)
                    .append("\"");
        }

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
