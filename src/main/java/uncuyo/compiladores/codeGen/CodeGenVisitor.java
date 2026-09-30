package uncuyo.compiladores.codeGen;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.*;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.*;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Variable;

public class CodeGenVisitor implements Visitor<Void> {

    private CodeGenerator codeGenerator;
    public CodeGenVisitor() {
        this.codeGenerator = new CodeGenerator();
    }

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

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
        // El valor del nodo izquierdo queda en $a0
        binaryExpressionNode.getLeftNode().accept(this);

        // Guardo en stack el $a0
        codeGenerator.push("$a0");

        // El valor del nodo derecho queda en $a0
        binaryExpressionNode.getRightNode().accept(this);

        // Cargo el valor del nodo izquierdo en $t0 y recupero la pila
        codeGenerator.pop();

        // Guardo en $a0 el valor de la operación
        switch (binaryExpressionNode.getBinaryOperatorToken().getTokenType()) {
            case TokenType.OPERATOR_ADD:
                codeGenerator.emitText("add $a0, $t0, $a0");
                break;
        }

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

        Token token = literalNode.getToken();
        switch (token.getTokenType()){
            case TokenType.STRING_CONSTANT:
                codeGenerator.emitText("la $a0 " + codeGenerator.emitString(token.getLexeme()));
                break;
            case TokenType.INT_CONSTANT:
                codeGenerator.emitText("li $a0 " + token.getLexeme());
                break;
            case TokenType.BOOLEAN_CONSTANT:
                codeGenerator.emitText("li $a0 " + (token.getLexeme().equals("true") ? 1 : 0));
                break;
            case KW_NIL:
                codeGenerator.emitText("li $a0 0");
            default:
                break;
        }
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
        Variable currentVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(simpleVariableNode.getToken().getLexeme());
        if (simpleVariableNode.getSimpleChainedNode() == null) {
            // Cargo la dirección del objeto en el stack frame
            codeGenerator.emitText("lw $t0, " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
            // Cargo el valor en la posicion del value del CIR
            codeGenerator.emitText("sw $a0, 4($t0)");
        }
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
        // Genero lado derecho de la igualdad
        // Al terminar, $a0 contiene el valor de la expresión
        assignmentNode.getExpressionNode().accept(this);

        // Guardo $a0 en la ubicación correspondiente
        // Si es local, guardo en offset($fp)
        assignmentNode.getSimpleAccessNode().accept(this);
        // TODO: Falta hacer el lado izquiero para guardar lo que hay en $a0
        return null;
    }

    @Override
    public Void visit(BlockNode blockNode) {
        Method currentMethod = null;
        if (blockNode.isMethod()) {
            codeGenerator.setInMain("start".equals(blockNode.getMethodName()));
            String className = blockNode.getClassName();
            String methodName = blockNode.getMethodName();
            if (methodName.equals("start")) {
                AST.getSymbolTable().setCurrentMethod(AST.getSymbolTable().getStart());

            } else {
                AST.getSymbolTable().setCurrentMethod(AST.getSymbolTable().getClasses().get(className).getMethods().get(methodName));
                codeGenerator.emitLabel(methodName);
            }
            currentMethod = AST.getSymbolTable().getCurrentMethod();
            codeGenerator.generateMethodPrologue(AST.getSymbolTable().getCurrentMethod());
            codeGenerator.emitText("# Body de " + methodName);
        }
        for (SentenceNode sentence : blockNode.getSentences()) {

            if (sentence != null) {
                sentence.accept(this);
                blockNode.setHasReturn(blockNode.isHasReturn() || sentence.isHasReturn());
            }
        }
        if (blockNode.isMethod()) {
            codeGenerator.generateMethodEpilogue(currentMethod);
        }
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
        simpleSentenceNode.getExpressionNode().accept(this);
        return null;
    }

    @Override
    public Void visit(WhileNode whileNode) {
        // TODO: Generar el codigo MIPS para una sentencia while.
        return null;
    }
}
