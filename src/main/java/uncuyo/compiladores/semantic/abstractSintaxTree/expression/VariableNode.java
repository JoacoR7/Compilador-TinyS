package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.exceptions.semantic.symbolTableException.AttributeNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.ClassNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.MethodNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.VariableNotFoundException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.symbolTable.SymbolTable;
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

    // persona.mascota.edad

    @Override
    public Type check() {
        Type type = null;
        if(this.getClassName() == null) {
            this.setClassName(AST.getSymbolTable().getStart().getVariables().get(this.getToken().getLexeme()).getType().getLexeme());
        }
        if (this.getChainedNode() == null) {
            if (!AST.getSymbolTable().getClasses().containsKey(className)) {
                throw new ClassNotFoundException(className);
            }
            if(!this.getMethodName().equals("start")) {
                if (!AST.getSymbolTable().getClasses().get(className).getMethods().containsKey(methodName)) {
                    throw new MethodNotFoundException(methodName);
                }
            } else {
                if (!this.isChainedNode() && !AST.getSymbolTable().getStart().getVariables().containsKey(this.getToken().getLexeme())) {
                    throw new VariableNotFoundException(this.getToken().getLexeme());
                }
            }
            type = AST.getSymbolTable().getClasses().get(className).getAttributes().get(this.getToken().getLexeme()).getType();
        }else {
            ChainedNode chainedNode = this.getChainedNode();
            chainedNode.setIsChainedNode(true);
            Token token = chainedNode.getToken();
            if (chainedNode instanceof VariableNode) {
                if (!AST.getSymbolTable().getClasses().get(className).getAttributes().containsKey(token.getLexeme())) {
                    throw new AttributeNotFoundException(token.getLexeme(), className);
                }
                if (((VariableNode) chainedNode).getClassName() == null) {
                    ((VariableNode) chainedNode).setClassName(className);
                }
            } else if (chainedNode instanceof ArrayNode) {
                if (!AST.getSymbolTable().getClasses().get(className).getAttributes().containsKey(token.getLexeme())) {
                    throw new AttributeNotFoundException(token.getLexeme(), className);
                }
                if(((ArrayNode) chainedNode).getClassName() == null) {
                    ((ArrayNode) chainedNode).setClassName(className);
                }
            } else {
                if (!AST.getSymbolTable().getClasses().get(className).getMethods().containsKey(token.getLexeme())) {
                    throw new MethodNotFoundException(token.getLexeme());
                }
            }
            type = chainedNode.check();
        }

        return type;
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
