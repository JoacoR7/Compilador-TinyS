package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class VariableNode extends ChainedNode{

    private String className;
    private String methodName;

    public VariableNode(Token token, ChainedNode chainedNode, String className, String methodName) {
        super(token, chainedNode);
        this.className = className;
        this.methodName = methodName;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
                .append("\"nodeType\": \"VariableNode\",\n");

        sb.append(innerIndent)
                .append("\"name\": \"")
                .append(getToken().getLexeme())
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

        if (getChainedNode() != null) {
            sb.append(",\n");
            sb.append(innerIndent)
                    .append("\"chained\": \n");

            sb.append(getChainedNode().toJson(innerIndent + "\t"));
        }

        sb.append("\n");
        sb.append(indent).append("}");

        return sb.toString();
    }
}
