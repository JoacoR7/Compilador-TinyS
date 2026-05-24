package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.symbolTable.Type;

public class ArrayNode extends ChainedNode{
    private ExpressionNode expression;
    private String className;
    private String methodName;

    public ArrayNode(Token token, ChainedNode chainedNode, ExpressionNode expression, String className, String methodName) {
        super(token, chainedNode);
        this.expression = expression;
        this.className = className;
        this.methodName = methodName;
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
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
    public Type check() {
        return null;
    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
