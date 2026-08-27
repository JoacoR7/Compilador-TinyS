package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.*;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.*;

public class CodeGenVisitor implements Visitor<Void> {
    // #################################################################
    // #               Visitor codeGen de expresiones                  #
    // #################################################################

    @Override
    public Void visit(ArrayConstructorNode arrayConstructorNode) {
        // TODO: Generar el codigo MIPS para la construccion de un arreglo.
        return null;
    }

    @Override
    public Void visit(ArrayNode arrayNode) {
        // TODO: Generar el codigo MIPS para el acceso a un arreglo.
        return null;
    }

    @Override
    public Void visit(BinaryExpressionNode binaryExpressionNode) {
        // TODO: Generar el codigo MIPS para una expresion binaria.
        return null;
    }

    @Override
    public Void visit(ChainedCallNode chainedCallNode) {
        // TODO: Generar el codigo MIPS para una llamada encadenada.
        return null;
    }

    @Override
    public Void visit(ChainedNode chainedNode) {
        // TODO: Generar el codigo MIPS para un acceso encadenado generico.
        return null;
    }

    @Override
    public Void visit(ClassConstructorNode classConstructorNode) {
        // TODO: Generar el codigo MIPS para la construccion de un objeto.
        return null;
    }

    @Override
    public Void visit(ConclassorCallNode conclassorCallNode) {
        // TODO: Generar el codigo MIPS para una llamada a constructor o clase.
        return null;
    }

    @Override
    public Void visit(ExpressionNode expressionNode) {
        // TODO: Generar el codigo MIPS para una expresion generica.
        return null;
    }

    @Override
    public Void visit(LiteralNode literalNode) {
        // TODO: Generar el codigo MIPS para cargar un literal.
        return null;
    }

    @Override
    public Void visit(OperandNode operandNode) {
        // TODO: Generar el codigo MIPS para un operando generico.
        return null;
    }

    @Override
    public Void visit(ParenthesizedExpressionNode parenthesizedExpressionNode) {
        // TODO: Generar el codigo MIPS para una expresion entre parentesis.
        return null;
    }

    @Override
    public Void visit(SelfNode selfNode) {
        // TODO: Generar el codigo MIPS para el acceso a self.
        return null;
    }

    @Override
    public Void visit(SimpleAccessNode simpleAccessNode) {
        // TODO: Generar el codigo MIPS para un acceso simple generico.
        return null;
    }

    @Override
    public Void visit(SimpleArrayNode simpleArrayNode) {
        // TODO: Generar el codigo MIPS para un acceso simple a un arreglo.
        return null;
    }

    @Override
    public Void visit(SimpleChainedNode simpleChainedNode) {
        // TODO: Generar el codigo MIPS para un acceso simple encadenado.
        return null;
    }

    @Override
    public Void visit(SimpleSelfNode simpleSelfNode) {
        // TODO: Generar el codigo MIPS para un acceso simple mediante self.
        return null;
    }

    @Override
    public Void visit(SimpleVariableNode simpleVariableNode) {
        // TODO: Generar el codigo MIPS para un acceso simple a una variable.
        return null;
    }

    @Override
    public Void visit(UnaryExpressionNode unaryExpressionNode) {
        // TODO: Generar el codigo MIPS para una expresion unaria.
        return null;
    }

    @Override
    public Void visit(VariableNode variableNode) {
        // TODO: Generar el codigo MIPS para el acceso a una variable.
        return null;
    }


    // #################################################################
    // #               Visitor codeGen de sentencias                   #
    // #################################################################

    @Override
    public Void visit(AssignmentNode assignmentNode) {
        // TODO: Generar el codigo MIPS para una asignacion.
        return null;
    }

    @Override
    public Void visit(BlockNode blockNode) {
        // TODO: Generar el codigo MIPS para las sentencias de un bloque.
        return null;
    }

    @Override
    public Void visit(ForNode forNode) {
        // TODO: Generar el codigo MIPS para una sentencia for.
        return null;
    }

    @Override
    public Void visit(IfElseNode ifElseNode) {
        // TODO: Generar el codigo MIPS para una sentencia if/else.
        return null;
    }

    @Override
    public Void visit(ReturnNode returnNode) {
        // TODO: Generar el codigo MIPS para una sentencia return.
        return null;
    }

    @Override
    public Void visit(SentenceNode sentenceNode) {
        // TODO: Generar el codigo MIPS para una sentencia generica.
        return null;
    }

    @Override
    public Void visit(SimpleSentenceNode simpleSentenceNode) {
        // TODO: Generar el codigo MIPS para una sentencia simple.
        return null;
    }

    @Override
    public Void visit(WhileNode whileNode) {
        // TODO: Generar el codigo MIPS para una sentencia while.
        return null;
    }
}
