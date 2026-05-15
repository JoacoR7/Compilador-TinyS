package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.OperandNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleChainedNode;

public class AssignmentNode extends SentenceNode {

    private OperandNode operandNode;
    private ExpressionNode expressionNode;

    public AssignmentNode(OperandNode operandNode, ExpressionNode expressionNode) {
        this.operandNode = operandNode;
        this.expressionNode = expressionNode;
    }

    public OperandNode getOperandNode() {
        return operandNode;
    }

    public void setOperandNode(OperandNode operandNode) {
        this.operandNode = operandNode;
    }

    public ExpressionNode getExpressionNode() {
        return expressionNode;
    }

    public void setExpressionNode(ExpressionNode expressionNode) {
        this.expressionNode = expressionNode;
    }

    @Override
    public void check() {

    }

    @Override
    public String toJson(String indent) {
        return "";
    }
}
