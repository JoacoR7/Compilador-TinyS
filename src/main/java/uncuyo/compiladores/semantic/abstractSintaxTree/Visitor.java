package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.*;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.*;

public interface Visitor<T> {
    // #################################################################
    // #               Visitor type de expresiones                     #
    // #################################################################
    T visit(ArrayConstructorNode arrayConstructorNode);
    T visit(ArrayNode arrayNode);
    T visit(BinaryExpressionNode binaryExpressionNode);
    T visit(ChainedCallNode chainedCallNode);
    T visit(ChainedNode chainedNode);
    T visit(ClassConstructorNode classConstructorNode);
    T visit(ConclassorCallNode conclassorCallNode);
    T visit(ExpressionNode expressionNode);
    T visit(LiteralNode literalNode);
    T visit(OperandNode operandNode);
    T visit(ParenthesizedExpressionNode parenthesizedExpressionNode);
    T visit(SelfNode selfNode);
    T visit(SimpleAccessNode simpleAccessNode);
    T visit(SimpleArrayNode simpleArrayNode);
    T visit(SimpleChainedNode simpleChainedNode);
    T visit(SimpleSelfNode simpleSelfNode);
    T visit(SimpleVariableNode simpleVariableNode);
    T visit(UnaryExpressionNode unaryExpressionNode);
    T visit(VariableNode variableNode);


    // #################################################################
    // #               Visitor type de sentencias                      #
    // #################################################################
    T visit(AssignmentNode assignmentNode);
    T visit(BlockNode blockNode);
    T visit(ForNode forNode);
    T visit(IfElseNode ifElseNode);
    T visit(ReturnNode returnNode);
    T visit(SentenceNode sentenceNode);
    T visit(SimpleSentenceNode simpleSentenceNode);
    T visit(WhileNode whileNode);

}
