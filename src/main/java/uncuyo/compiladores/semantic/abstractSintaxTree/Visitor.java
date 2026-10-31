package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ArrayConstructorNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ArrayNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.BinaryExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ChainedCallNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ChainedNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ClassConstructorNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ConclassorCallNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.LiteralNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.OperandNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ParenthesizedExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SelfNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleAccessNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleArrayNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleChainedNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleSelfNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleVariableNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.UnaryExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.VariableNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.AssignmentNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.BlockNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.ForNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.IfElseNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.ReturnNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.SentenceNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.SimpleSentenceNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.WhileNode;

/**
 * Contrato de una operación sobre las expresiones y sentencias del AST.
 * Cada nodo concreto selecciona su sobrecarga desde {@link Node#accept}.
 * Los recorridos de los hijos y el significado del resultado dependen de
 * cada implementación.
 *
 * @param <T> tipo de resultado de la operación aplicada a un nodo
 */
public interface Visitor<T> {

    // #################################################################
    // #                      Expresiones                              #
    // #################################################################

    /**
     * Procesa una creación de arreglo.
     *
     * @param arrayConstructorNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ArrayConstructorNode arrayConstructorNode);

    /**
     * Procesa un acceso indexado a un arreglo.
     *
     * @param arrayNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ArrayNode arrayNode);

    /**
     * Procesa una operación binaria.
     *
     * @param binaryExpressionNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(BinaryExpressionNode binaryExpressionNode);

    /**
     * Procesa una llamada encadenada.
     *
     * @param chainedCallNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ChainedCallNode chainedCallNode);

    /**
     * Procesa un acceso encadenado.
     *
     * @param chainedNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ChainedNode chainedNode);

    /**
     * Procesa una creación de instancia.
     *
     * @param classConstructorNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ClassConstructorNode classConstructorNode);

    /**
     * Procesa una expresión de construcción.
     *
     * @param conclassorCallNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ConclassorCallNode conclassorCallNode);

    /**
     * Procesa una expresión.
     *
     * @param expressionNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ExpressionNode expressionNode);

    /**
     * Procesa un literal.
     *
     * @param literalNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(LiteralNode literalNode);

    /**
     * Procesa un operando.
     *
     * @param operandNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(OperandNode operandNode);

    /**
     * Procesa una expresión entre paréntesis.
     *
     * @param parenthesizedExpressionNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ParenthesizedExpressionNode parenthesizedExpressionNode);

    /**
     * Procesa una referencia a {@code self}.
     *
     * @param selfNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SelfNode selfNode);

    /**
     * Procesa un acceso de destino.
     *
     * @param simpleAccessNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SimpleAccessNode simpleAccessNode);

    /**
     * Procesa un acceso indexado de destino.
     *
     * @param simpleArrayNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SimpleArrayNode simpleArrayNode);

    /**
     * Procesa un atributo encadenado de destino.
     *
     * @param simpleChainedNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SimpleChainedNode simpleChainedNode);

    /**
     * Procesa un acceso de destino mediante {@code self}.
     *
     * @param simpleSelfNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SimpleSelfNode simpleSelfNode);

    /**
     * Procesa un acceso de destino a una variable.
     *
     * @param simpleVariableNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SimpleVariableNode simpleVariableNode);

    /**
     * Procesa una operación unaria.
     *
     * @param unaryExpressionNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(UnaryExpressionNode unaryExpressionNode);

    /**
     * Procesa una referencia a una variable o atributo.
     *
     * @param variableNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(VariableNode variableNode);


    // #################################################################
    // #                       Sentencias                              #
    // #################################################################
    /**
     * Procesa una asignación.
     *
     * @param assignmentNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(AssignmentNode assignmentNode);

    /**
     * Procesa un bloque de sentencias.
     *
     * @param blockNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(BlockNode blockNode);

    /**
     * Procesa un recorrido {@code for}.
     *
     * @param forNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ForNode forNode);

    /**
     * Procesa una sentencia {@code if/else}.
     *
     * @param ifElseNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(IfElseNode ifElseNode);

    /**
     * Procesa una sentencia de retorno.
     *
     * @param returnNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(ReturnNode returnNode);

    /**
     * Procesa una sentencia.
     *
     * @param sentenceNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SentenceNode sentenceNode);

    /**
     * Procesa una expresión usada como sentencia.
     *
     * @param simpleSentenceNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(SimpleSentenceNode simpleSentenceNode);

    /**
     * Procesa un ciclo {@code while}.
     *
     * @param whileNode nodo sobre el que se aplica la operación
     * @return resultado definido por la implementación del visitante
     */
    T visit(WhileNode whileNode);

}
