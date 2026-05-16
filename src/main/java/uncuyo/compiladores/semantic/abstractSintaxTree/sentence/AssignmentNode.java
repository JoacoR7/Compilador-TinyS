package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.OperandNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleAccessNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleChainedNode;

public class AssignmentNode extends SentenceNode {

    private SimpleAccessNode simpleAccessNode;
    private ExpressionNode expressionNode;

    public AssignmentNode(SimpleAccessNode simpleAccessNode, ExpressionNode expressionNode) {
        this.simpleAccessNode = simpleAccessNode;
        this.expressionNode = expressionNode;
    }

    public SimpleAccessNode getSimpleAccessNode() {
        return simpleAccessNode;
    }

    public void setSimpleAccessNode(SimpleAccessNode simpleAccessNode) {
        this.simpleAccessNode = simpleAccessNode;
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
