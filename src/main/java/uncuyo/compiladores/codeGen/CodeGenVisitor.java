package uncuyo.compiladores.codeGen;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.*;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.*;
import uncuyo.compiladores.semantic.symbolTable.Class;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Parameter;
import uncuyo.compiladores.semantic.symbolTable.Variable;

import java.util.List;

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

        codeGenerator.emitComment("Comienzo expresion binaria " + binaryExpressionNode.getBinaryOperatorToken().getLexeme() +
                " en " + binaryExpressionNode.getBinaryOperatorToken().getRowIndex() + "_" + binaryExpressionNode.getBinaryOperatorToken().getColumnIndex());

        // El valor del nodo izquierdo queda en $a0
        codeGenerator.emitComment("Obtengo el valor del nodo izquierdo");
        binaryExpressionNode.getLeftNode().accept(this);

        // Guardo en stack el $a0
        codeGenerator.push("$a0");

        // El valor del nodo derecho queda en $a0
        codeGenerator.emitComment("Obtengo el valor del nodo derecho");
        binaryExpressionNode.getRightNode().accept(this);
        codeGenerator.emitComment("Obtengo el valor del CIR");
        codeGenerator.emitText("lw $a0 4($a0)");

        // Cargo el valor del nodo izquierdo en $t0 y recupero la pila
        codeGenerator.emitComment("Recupero el valor del nodo izquierdo");
        codeGenerator.pop();
        codeGenerator.emitComment("Obtengo el valor del CIR");
        codeGenerator.emitText("lw $t0 4($t0)");

        // Guardo en $a0 el valor de la operación
        switch (binaryExpressionNode.getBinaryOperatorToken().getTokenType()) {

            case TokenType.OPERATOR_ADD:
                codeGenerator.emitComment("Sumo los operandos");

                codeGenerator.emitText("add $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_SUB:
                codeGenerator.emitComment("Resto el operando derecho al izquierdo");
                codeGenerator.emitText("sub $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_MUL:
                codeGenerator.emitComment("Multiplico los operandos");
                codeGenerator.emitText("mul $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_DIV:
                codeGenerator.emitComment("Checkeo division por 0");
                codeGenerator.emitText("beqz $a0 runtime_div_zero");

                codeGenerator.emitComment("Divido el operando izquierdo por el derecho");
                codeGenerator.emitText("div $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_EQ:
                codeGenerator.emitComment("Comparo si los operandos son iguales");
                codeGenerator.emitText("seq $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_NEQ:
                codeGenerator.emitComment("Comparo si los operandos son distintos");
                codeGenerator.emitText("sne $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_LT:
                codeGenerator.emitComment("Comparo si el operando izquierdo es menor que el derecho");
                codeGenerator.emitText("slt $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_LTE:
                codeGenerator.emitComment("Comparo si el operando izquierdo es menor o igual que el derecho");
                codeGenerator.emitText("sle $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_GT:
                codeGenerator.emitComment("Comparo si el operando izquierdo es mayor que el derecho");
                codeGenerator.emitText("sgt $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_GTE:
                codeGenerator.emitComment("Comparo si el operando izquierdo es mayor o igual que el derecho");
                codeGenerator.emitText("sge $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_OR:
                codeGenerator.emitComment("Realizo OR logico entre los operandos");
                codeGenerator.emitText("or $a0 $t0 $a0");
                break;

            case TokenType.OPERATOR_AND:
                codeGenerator.emitComment("Realizo AND logico entre los operandos");
                codeGenerator.emitText("and $a0 $t0 $a0");
                break;
        }

        codeGenerator.emitComment("Guardo en la pila el valor");
        codeGenerator.push("$a0");
        codeGenerator.emitComment("Genero nuevo CIR");
        List<TokenType> int_operations = List.of(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB, TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV);
        List<TokenType> bool_operations = List.of(TokenType.OPERATOR_OR, TokenType.OPERATOR_AND, TokenType.OPERATOR_GT, TokenType.OPERATOR_LT,
                TokenType.OPERATOR_GTE, TokenType.OPERATOR_LTE, TokenType.OPERATOR_EQ, TokenType.OPERATOR_NEQ);
        if (int_operations.contains(binaryExpressionNode.getBinaryOperatorToken().getTokenType())) {
            codeGenerator.generatePrimitiveCIR(TokenType.INT_CLASS);
        } else if (bool_operations.contains(binaryExpressionNode.getBinaryOperatorToken().getTokenType())) {
            codeGenerator.generatePrimitiveCIR(TokenType.BOOL_CLASS);
        }
        codeGenerator.pop();
        codeGenerator.emitComment("Recupero el valor de la expresion binaria");
        codeGenerator.emitComment("Guardo en el CIR el valor");
        codeGenerator.emitText("sw $t0 4($v0)");
        codeGenerator.emitText("move $a0 $v0");


        return null;
    }

    @Override
    public Void visit(ChainedCallNode chainedCallNode) {
        // ExpressionNodes son los argumentos
        Token currentToken = chainedCallNode.getToken();
        if (!chainedCallNode.isChainedNode()) {
            codeGenerator.emitSubsection("Comienzo llamada " + currentToken.getRowIndex() + "_"  + currentToken.getColumnIndex());
            if (chainedCallNode.getToken().getTokenType() == TokenType.IDENTIFIER_CLASS) {

                String className = currentToken.getLexeme();
                AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(className));
                codeGenerator.emitComment("Llamada estática sobre " + className);
                codeGenerator.emitComment("Guardo la vtable de " + className);
                codeGenerator.emitText("la $a0, vtable_" + className);


            } else if (chainedCallNode.getToken().getTokenType() == TokenType.IDENTIFIER_VAR_METHOD &&
                    AST.getSymbolTable().getCurrentMethod().getVariables().containsKey(currentToken.getLexeme())) {

            }
            ChainedNode nextNode = chainedCallNode.getChainedNode();
            if (nextNode != null) {
                nextNode.setIsChainedNode(true);
                nextNode.accept(this);
            }
        } else {
            // Obtengo el método de la clase actual (la clase actual se define cuando se hace llamada estática o directamente cuando obtenemos un objeto buscamos su clase)
            Method currentMethodCall = AST.getSymbolTable().getCurrentClass().getMethods().get(currentToken.getLexeme());
            // Genero el precall para reservar espacio para self, $fp llamador, return y parámetros
            codeGenerator.generateMethodPreCall(currentMethodCall);

            // En este punto el $sp apunta donde debería estar el $fp del RA, pero no hemos generado el prólogo
            // Empieza en offset 4 ya que ahí está el primer parámetro, si es que tiene
            int parameterOffset = 4;
            if(chainedCallNode.getExpressionNodes() != null && !chainedCallNode.getExpressionNodes().isEmpty()) {
                codeGenerator.emitSubsection("Evaluo parametros de " + currentMethodCall.getName());
            }
            for(ExpressionNode expressionNode : chainedCallNode.getExpressionNodes()) {
                codeGenerator.emitComment("Evaluo parametro n° " + (parameterOffset/4));
                // Retorna en $a0 el valor
                expressionNode.accept(this);

                codeGenerator.emitComment("Guardo parametro n° " + (parameterOffset/4));
                codeGenerator.emitText("sw $a0 " + parameterOffset + "($sp)");
                parameterOffset += 4;
            }

            if (currentMethodCall.isStatic()) {
                codeGenerator.emitText("");
                codeGenerator.emitComment("Como el metodo " + currentMethodCall.getName() + " es estatico, guardo la vtable en self");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(currentMethodCall) + "($sp)");
                codeGenerator.emitText("lw $a0 " + (currentMethodCall.getPosition()*4) + "($a0)");
                codeGenerator.emitText("jalr $a0");
            } else {
                codeGenerator.emitComment("Como el metodo " + currentMethodCall.getName() + " es un metodo de instancia, obtengo el self");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(currentMethodCall) + "($sp)");
                codeGenerator.emitComment("Busco la vtable de la instancia");
                codeGenerator.emitText("lw $a0 0($a0)");
                codeGenerator.emitComment("Busco el metodo " + currentMethodCall.getName() + " en la vtable");
                codeGenerator.emitText("lw $a0 " + (currentMethodCall.getPosition()*4) + "($a0)");
                codeGenerator.emitText("jalr $a0");
            }

        }


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
        Class currentClass = AST.getSymbolTable().getClasses().get(classConstructorNode.getToken().getLexeme());
        codeGenerator.emitSection("Generacion de CIR de " + currentClass.getName());
        int cirSpace = currentClass.getAttributes().size() + 1;
        codeGenerator.emitComment("Necesita " + cirSpace + " espacios para sus atributos y la vtable" );
        codeGenerator.emitText("li $a0 " + cirSpace * 4);
        codeGenerator.emitText("li $v0 9");
        codeGenerator.emitText("syscall");
        codeGenerator.emitComment("Guardo la direccion del heap en el stack para inicializar las variables");
        codeGenerator.push("$v0");
        codeGenerator.initializeAttributes(currentClass);
        codeGenerator.emitComment("Recupero la direccion de la instancia de clase de " + currentClass.getName());
        codeGenerator.pop();
        codeGenerator.emitText("move $a0 $t0");
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
        codeGenerator.emitComment("Genero el literal " + token.getLexeme());
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
        if (token.getTokenType() != TokenType.KW_NIL) {
            codeGenerator.emitComment("Guardo el literal en la pila");
            codeGenerator.push("$a0");
            codeGenerator.emitComment("Genero un CIR para el literal");
            codeGenerator.generatePrimitiveCIR(token.getTokenType());
            codeGenerator.emitComment("Recupero el literal de la pila");
            codeGenerator.pop();
            codeGenerator.emitComment("Guardo el literal en el CIR");
            codeGenerator.emitText("sw $t0 4($v0)");
            codeGenerator.emitText("move $a0 $v0");
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
        parenthesizedExpressionNode.getExpresion().accept(this);
        if (parenthesizedExpressionNode.getChainedNode() != null) {
            parenthesizedExpressionNode.getChainedNode().accept(this);
        }
        // TODO: Contemplar caso en que expresion parentizada tiene chained node
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
            codeGenerator.emitText("sw $a0, " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
        }
        // TODO: Generar el codigo MIPS para un acceso simple a una variable.
        return null;
    }

    @Override
    public Void visit(UnaryExpressionNode unaryExpressionNode) {

        codeGenerator.emitComment("Comienzo expresion unaria " + unaryExpressionNode.getUnaryOperatorToken().getLexeme() +
                " en " + unaryExpressionNode.getUnaryOperatorToken().getRowIndex() + "_" + unaryExpressionNode.getUnaryOperatorToken().getColumnIndex());
        // Proceso la expresión y la guardo en $a0
        // Como es una expresión unaria, no hace falta guardar en la pila $a0
        codeGenerator.emitComment("Obtengo el valor de la expresion");
        unaryExpressionNode.getExpressionNode().accept(this);


        switch (unaryExpressionNode.getUnaryOperatorToken().getTokenType()) {

            case TokenType.OPERATOR_ADD:
                codeGenerator.emitComment("Mantengo el valor sin cambios");
                break;

            case TokenType.OPERATOR_SUB:
                codeGenerator.emitComment("Cambio el signo del operando");
                codeGenerator.emitText("li $t0 -1");
                codeGenerator.emitText("mul $a0 $a0 $t0");
                break;

            case TokenType.OPERATOR_NOT:
                codeGenerator.emitComment("Niego el valor booleano");
                codeGenerator.emitText("xori $a0 $a0 1");
                break;

            case TokenType.OPERATOR_INC:
                codeGenerator.emitComment("Incremento el operando en 1");
                codeGenerator.emitText("li $t0 1");
                codeGenerator.emitText("add $a0 $a0 $t0");
                break;

            case TokenType.OPERATOR_DEC:
                codeGenerator.emitComment("Decremento el operando en 1");
                codeGenerator.emitText("li $t0 1");
                codeGenerator.emitText("sub $a0 $a0 $t0");
                break;
        }
        return null;
    }

    @Override
    public Void visit(VariableNode variableNode) {
        // TODO: Generar el codigo MIPS para el acceso a una variable.
        // Acceso a variable sin encadenamiento
        Variable currentVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(variableNode.getToken().getLexeme());
        if (!variableNode.isChainedNode() && variableNode.getChainedNode() == null) {
            if (currentVariable != null) {
                codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
            } else {
                Parameter currentParameter = AST.getSymbolTable().getCurrentMethod().getParameters().get(variableNode.getToken().getLexeme());
                codeGenerator.emitText("lw $a0 " + codeGenerator.getParameterOffset(currentParameter) + "($fp)");
            }

        }
        // Acceso a variable que tiene algo encadenado
        else if (!variableNode.isChainedNode()) {
            variableNode.getChainedNode().setIsChainedNode(true);
            if (variableNode.getChainedNode() instanceof ChainedCallNode) {
                codeGenerator.emitComment("Guardo en $a0 la direccion del CIR que invoca al metodo");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
                AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(variableNode.getClassName()));
            }

            variableNode.getChainedNode().accept(this);
        }
        return null;
    }


    // #################################################################
    // #               Visitor codeGen de sentencias                   #
    // #################################################################

    @Override
    public Void visit(AssignmentNode assignmentNode) {

        Token currentToken = assignmentNode.getSimpleAccessNode().getToken();
        codeGenerator.emitComment("Asignacion a \"" + currentToken.getLexeme() + "\" en " + currentToken.getRowIndex() + "_" + currentToken.getColumnIndex());

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
                if (methodName.equals(".")) {
                    AST.getSymbolTable().setCurrentMethod(AST.getSymbolTable().getClasses().get(className).getConstructor());
                    methodName = className + "_constructor";
                } else {
                    AST.getSymbolTable().setCurrentMethod(AST.getSymbolTable().getClasses().get(className).getMethods().get(methodName));
                    codeGenerator.emitData(".word " + methodName);
                }

                codeGenerator.emitLabel(methodName);
            }
            currentMethod = AST.getSymbolTable().getCurrentMethod();
            codeGenerator.generateMethodPrologue(AST.getSymbolTable().getCurrentMethod());
            codeGenerator.emitText("# Body de " + methodName);
        }
        for (SentenceNode sentence : blockNode.getSentences()) {

            if (sentence != null) {
                sentence.accept(this);
                //blockNode.setHasReturn(blockNode.isHasReturn() || sentence.isHasReturn());
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
        String rowColumnLabel = ifElseNode.getToken().getRowIndex() + "_" + ifElseNode.getToken().getColumnIndex();

        codeGenerator.emitSection("if_" + rowColumnLabel);

        codeGenerator.emitComment("Evaluo expresion de if_" + rowColumnLabel);
        // Guardo en $a0 el resultado de la expresión a evaluar
        ifElseNode.getExpression().accept(this);
        codeGenerator.emitText("beqz $a0 " + (ifElseNode.getSentenceNodeElse() != null ? "else_" : "endif_") + rowColumnLabel);

        codeGenerator.emitComment("Cuerpo de if_" + rowColumnLabel);
        codeGenerator.emitSubLabel("if_" + rowColumnLabel);
        ifElseNode.getSentenceNodeIf().accept(this);
        if (ifElseNode.getSentenceNodeElse() != null) {
            codeGenerator.emitText("j endif_" + rowColumnLabel);
        }
        if (ifElseNode.getSentenceNodeElse() != null) {
            codeGenerator.emitComment("Cuerpo de else_" + rowColumnLabel);
            codeGenerator.emitSubLabel("else_" + rowColumnLabel);
            ifElseNode.getSentenceNodeElse().accept(this);
        }
        codeGenerator.emitSubLabel("endif_" + rowColumnLabel);


        return null;
    }

    @Override
    public Void visit(ReturnNode returnNode) {
        returnNode.getExpression().accept(this);
        int returnOffset = codeGenerator.getReturnOffset(AST.getSymbolTable().getCurrentMethod());
        codeGenerator.emitComment("Guardo el return");
        codeGenerator.emitText("sw $a0 " + returnOffset + "($fp)");
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
        String rowColumnLabel = whileNode.getToken().getRowIndex() + "_" + whileNode.getToken().getColumnIndex();
        codeGenerator.emitSection("while_" + rowColumnLabel);

        codeGenerator.emitSubLabel("while_" + rowColumnLabel);

        codeGenerator.emitComment("Evaluo expresion de while_" + rowColumnLabel);
        // Guardo en $a0 el resultado de la expresión a evaluar
        whileNode.getExpressionNode().accept(this);

        codeGenerator.emitText("beqz $a0 endwhile_" + rowColumnLabel);


        codeGenerator.emitSubsection("Cuerpo de while_" + rowColumnLabel);
        whileNode.getSentenceNode().accept(this);

        codeGenerator.emitText("j while_" + rowColumnLabel);
        codeGenerator.emitSubLabel("endwhile_" + rowColumnLabel);

        return null;
    }
}
