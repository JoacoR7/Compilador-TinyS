package uncuyo.compiladores.codeGen;

import org.w3c.dom.Attr;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.*;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.*;
import uncuyo.compiladores.semantic.symbolTable.*;
import uncuyo.compiladores.semantic.symbolTable.Class;

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
        codeGenerator.emitSubsection("Constructor array linea " + arrayConstructorNode.getToken().getRowIndex() +
                ", columna " + arrayConstructorNode.getToken().getColumnIndex());
        codeGenerator.emitComment("Evaluo expresion de constructor array");
        // Me devuelve en $a0 el CIR de la expresión
        arrayConstructorNode.getExpression().accept(this);
        codeGenerator.emitComment("Recupero el valor del CIR");
        codeGenerator.emitText("lw $a0 4($a0)");
        codeGenerator.emitComment("Guardo la longitud del array en la pila");
        codeGenerator.push("$a0");
        codeGenerator.emitComment("Necesito la cantidad de elementos del array mas 8 bytes: 4 para la direccion de la vtable y 4 para la longitud");
        codeGenerator.emitText("li $t0 4");
        codeGenerator.emitText("mul $a0 $a0 $t0");
        codeGenerator.emitText("addiu $a0 $a0 8");
        codeGenerator.emitText("li $v0 9");
        codeGenerator.emitText("syscall");

        String arrayType = ((ArrayType) arrayConstructorNode.getType()).getSubType().getLexeme();
        codeGenerator.emitComment("El array es de tipo " + arrayType + ", por lo tanto guardo su vtable correspondiente");
        codeGenerator.emitText("la $a0 vtable_array_" + arrayType.toLowerCase());
        codeGenerator.emitText("sw $a0 0($v0)");

        codeGenerator.emitComment("Recupero la longitud del array de la pila en $t0");
        codeGenerator.pop();

        codeGenerator.emitComment("Guardo la longitud del array");
        codeGenerator.emitText("sw $t0 4($v0)");

        codeGenerator.emitComment("Inicializacion de elementos del array");
        codeGenerator.emitComment("$t1 va a ser el contador");
        codeGenerator.emitText("li $t1 0");
        codeGenerator.emitComment("$t2 contendra la direccion a la vtable correspondiente");
        if (arrayType.equals("Str")) {
            codeGenerator.emitText("la $t2 vtable_" + arrayType.toLowerCase());
        } else {
            codeGenerator.emitText("li $t2 0");
        }
        codeGenerator.emitComment("$t3 contendrá la direccion del primer elemento del array");
        codeGenerator.emitText("addiu $t3 $v0 8");
        codeGenerator.emitComment("Guardo la direccion del array en la pila");
        codeGenerator.push("$v0");
        if (arrayType.equals("Str")) {
            codeGenerator.emitComment("Como el array es de tipo Str, guardo la direccion al str vacio");
            codeGenerator.emitText("la $t4 empty_str");
        } else {
            codeGenerator.emitComment("Como el array es de tipo " + arrayType + ", guardo un 0 en $t4");
            codeGenerator.emitText("li $t4 0");
        }
        codeGenerator.emitComment("$a0 valdra 8, que son los bytes necesarios para cada elemento");
        codeGenerator.emitText("li $a0 8");
        codeGenerator.emitSubLabel("inicializacion_array_" + arrayConstructorNode.getToken().getRowIndex() + "_" + arrayConstructorNode.getToken().getColumnIndex());
        codeGenerator.emitComment("Hago el syscall para almacenar el espacio");
        codeGenerator.emitText("li $v0 9");
        codeGenerator.emitText("syscall");

        codeGenerator.emitComment("Guardo la vtable correspondiente");
        codeGenerator.emitText("sw $t2 0($v0)");

        codeGenerator.emitComment("Como el array es de tipo " + arrayType + ", guardo " + (arrayType.equals("Str") ? " la direccion al str vacio" : "un 0"));
        codeGenerator.emitText("sw $t4 4($v0)");

        codeGenerator.emitComment("Guardo el CIR en la posicion correspondiente");
        codeGenerator.emitText("sw $v0 0($t3)");

        codeGenerator.emitComment("Avanzo de posicion");
        codeGenerator.emitText("addiu $t3 $t3 4");
        codeGenerator.emitComment("Incremento el contador");
        codeGenerator.emitText("addiu $t1 $t1 1");

        codeGenerator.emitComment("Comparo el contador con la longitud para saber si salgo del loop");
        codeGenerator.emitText("bne $t1 $t0 inicializacion_array_" + arrayConstructorNode.getToken().getRowIndex() + "_" + arrayConstructorNode.getToken().getColumnIndex());

        codeGenerator.emitComment("Recupero la direccion del array");
        codeGenerator.pop();

        codeGenerator.emitText("move $a0 $t0");

        // TODO: Generar el codigo MIPS para la construccion de un arreglo.
        return null;
    }

    @Override
    public Void visit(ArrayNode arrayNode) {
        // TODO: Generar el codigo MIPS para el acceso a un arreglo.
        Token currentToken = arrayNode.getToken();
        codeGenerator.emitComment("Evaluo la expresion de acceso a arreglo");
        arrayNode.getExpression().accept(this);
        codeGenerator.emitComment("Guardo el resultado en la pila");
        codeGenerator.push("$a0");

        codeGenerator.emitComment("Busco el array");
        Variable currentVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(currentToken.getLexeme());
        if (currentVariable != null) {
            codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
        }

        codeGenerator.checkNullAccess("$a0" , currentToken.getRowIndex(), currentToken.getColumnIndex());

        codeGenerator.emitComment("Recupero la expresion de la pila");
        codeGenerator.pop();
        codeGenerator.emitText("lw $t0 4($t0)");

        codeGenerator.emitComment("Recupero la longitud del array para hacer checkeo de indice");
        codeGenerator.emitText("lw $t1 4($a0)");

        codeGenerator.checkIndex("$t0", "$t1");

        codeGenerator.emitComment("Multiplico por 4 y sumo 8 para obtener la direccion al elemento correspondiente");
        codeGenerator.emitText("sll $t0 $t0 2");
        codeGenerator.emitText("addiu $t0 $t0 8");

        codeGenerator.emitText("add $a0 $a0 $t0");
        codeGenerator.emitText("lw $a0 0($a0)");

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


            } else if (chainedCallNode.getToken().getTokenType() == TokenType.IDENTIFIER_VAR_METHOD) {
                codeGenerator.emitComment("Obtengo el self para hacer la llamada de metodo de instancia");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(AST.getSymbolTable().getCurrentMethod()) + "($fp)");
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

                // Llamadas encadenadas
                if (chainedCallNode.getChainedNode() != null) {
                    AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(chainedCallNode.getType().getLexeme()));
                    chainedCallNode.getChainedNode().setIsChainedNode(true);
                    chainedCallNode.getChainedNode().accept(this);
                }

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

            // Llamadas encadenadas
            if (chainedCallNode.getChainedNode() != null) {
                AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(chainedCallNode.getType().getLexeme()));
                chainedCallNode.getChainedNode().setIsChainedNode(true);
                chainedCallNode.getChainedNode().accept(this);
            }
        }

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
        codeGenerator.generateMethodPreCall(currentClass.getConstructor());
        int i = 4;
        for (ExpressionNode expressionNode : classConstructorNode.getExpressionNodes()) {
            expressionNode.accept(this);
            codeGenerator.emitText("sw $a0 " + i + "($sp)");
            i += 4;
        }
        codeGenerator.emitText("la $t0 " + currentClass.getName() + "_constructor");
        codeGenerator.emitText("jalr $t0");
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
        if (selfNode.getChainedNode() instanceof VariableNode) {
            AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(((VariableNode) selfNode.getChainedNode()).getClassName()));
        }
        codeGenerator.emitComment("Accedo al self");
        codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(AST.getSymbolTable().getCurrentMethod()) + "($fp)");
        selfNode.getChainedNode().setIsChainedNode(true);
        selfNode.getChainedNode().accept(this);

        return null;
    }

    @Override
    public Void visit(SimpleAccessNode simpleAccessNode) {
        // TODO: Generar el codigo MIPS para un acceso simple generico.
        return null;
    }

    @Override
    public Void visit(SimpleArrayNode simpleArrayNode) {
        codeGenerator.emitComment("Evaluo la expresion de acceso a array");
        simpleArrayNode.getExpressionNode().accept(this);
        codeGenerator.push("$a0");
        Token currentToken = simpleArrayNode.getToken();
        Variable currentVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(currentToken.getLexeme());
        if (currentVariable != null) {
            codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
        }

        codeGenerator.checkNullAccess("$a0" , currentToken.getRowIndex(), currentToken.getColumnIndex());

        codeGenerator.emitComment("Recupero la expresion");
        codeGenerator.pop();
        codeGenerator.emitText("lw $t0 4($t0)");

        codeGenerator.emitComment("Recupero la longitud del array para hacer checkeo de indice");
        codeGenerator.emitText("lw $t1 4($a0)");

        codeGenerator.checkIndex("$t0", "$t1");

        codeGenerator.emitComment("Multiplico por 4 y sumo 8 para acceder al elemento correcto");
        codeGenerator.emitText("sll $t0 $t0 2");
        codeGenerator.emitText("addiu $t0 $t0 8");
        codeGenerator.emitText("add $a0 $a0 $t0");
        return null;
    }

    @Override
    public Void visit(SimpleChainedNode simpleChainedNode) {
        // TODO: Generar el codigo MIPS para un acceso simple encadenado.
        if (simpleChainedNode.getSimpleChainedNode() == null) {
            Attribute currentAttribute = AST.getSymbolTable().getCurrentClass().getAttributes().get(simpleChainedNode.getToken().getLexeme());
            codeGenerator.emitComment("Obtengo el atributo del self");
            codeGenerator.emitText("addiu $a0 $a0 " + codeGenerator.getAttributeOffset(currentAttribute));
        }
        return null;
    }

    @Override
    public Void visit(SimpleSelfNode simpleSelfNode) {
        // TODO: Generar el codigo MIPS para un acceso simple mediante self.
        codeGenerator.emitComment("Obtengo el CIR del self " + AST.getSymbolTable().getCurrentClass().getName());
        codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(AST.getSymbolTable().getCurrentMethod()) + "($fp)");
        simpleSelfNode.getSimpleChainedNode().accept(this);
        return null;
    }

    @Override
    public Void visit(SimpleVariableNode simpleVariableNode) {
        Token currentToken = simpleVariableNode.getToken();
        Variable currentVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(currentToken.getLexeme());
        Attribute currentAttribute = null;
        if (AST.getSymbolTable().getCurrentClass() != null) {
            currentAttribute = AST.getSymbolTable().getCurrentClass().getAttributes().get(currentToken.getLexeme());
        }
        Parameter currentParameter = AST.getSymbolTable().getCurrentMethod().getParameters().get(currentToken.getLexeme());
        String className;
        if (currentVariable != null) {
            // Obtengo la direccion del slot de la variable
            codeGenerator.emitComment("Cargo la direccion de " + currentVariable.getName());

            codeGenerator.emitText("addiu $a0 $fp " + codeGenerator.getVariableOffset(currentVariable));
            className = currentVariable.getType().getLexeme();
        } else if (currentAttribute != null) {

            // Obtengo self
            codeGenerator.emitComment("Obtengo el self");

            codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(AST.getSymbolTable().getCurrentMethod()) + "($fp)");

            // Obtengo la direccion del slot del atributo
            codeGenerator.emitText("addiu $a0 $a0 " + codeGenerator.getAttributeOffset(currentAttribute));

            className = currentAttribute.getType().getLexeme();
        } else {
            codeGenerator.emitComment("Obtengo el parametro");
            codeGenerator.emitText("addiu $a0 $fp " + codeGenerator.getParameterOffset(currentParameter));
            className = currentParameter.getType().getLexeme();
        }

        if (simpleVariableNode.getSimpleChainedNode() != null) {

            SimpleChainedNode simpleChainedNode = simpleVariableNode.getSimpleChainedNode();

            Attribute attribute = AST.getSymbolTable().getClasses().get(className).getAttributes().get(simpleChainedNode.getToken().getLexeme());

            if (attribute != null) {

                // $a0 contiene la direccion del slot actual.
                // Cargo la referencia al objeto almacenada ahi.
                codeGenerator.emitComment("Cargo la referencia al objeto");
                codeGenerator.emitText("lw $a0 0($a0)");

                codeGenerator.checkNullAccess("$a0", simpleChainedNode.getToken().getRowIndex(),simpleChainedNode.getToken().getColumnIndex());

                // Calculo la direccion del slot del atributo
                int attributeOffset = codeGenerator.getAttributeOffset(attribute);

                codeGenerator.emitComment("Obtengo la direccion del atributo " + attribute.getName());

                codeGenerator.emitText("addiu $a0 $a0 " + attributeOffset);
                if (simpleChainedNode.getSimpleChainedNode() != null) {
                    AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(attribute.getType().getTypeName()));
                    codeGenerator.emitText("lw $a0 0($a0)");
                    simpleChainedNode.getSimpleChainedNode().accept(this);
                }
            }
        }

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
                codeGenerator.emitText("lw $t1 4($a0)");
                codeGenerator.emitText("mul $t0 $t1 $t0");
                codeGenerator.emitText("sw $t0 4($a0)");
                break;

            case TokenType.OPERATOR_NOT:
                codeGenerator.emitComment("Niego el valor booleano");
                codeGenerator.emitText("lw $t0 4($a0)");
                codeGenerator.emitText("xori $t0 $t0 1");
                codeGenerator.emitText("sw $t0 4($a0)");
                break;

            case TokenType.OPERATOR_INC:
                codeGenerator.emitComment("Incremento el operando en 1");
                codeGenerator.emitText("li $t0 1");
                codeGenerator.emitText("lw $t1 4($a0)");
                codeGenerator.emitText("add $t0 $t1 $t0");
                codeGenerator.emitText("sw $t0 4($a0)");
                break;

            case TokenType.OPERATOR_DEC:
                codeGenerator.emitComment("Decremento el operando en 1");
                codeGenerator.emitText("li $t0 1");
                codeGenerator.emitText("lw $t1 4($a0)");
                codeGenerator.emitText("sub $t0 $t1 $t0");
                codeGenerator.emitText("sw $t0 4($a0)");
                break;
        }
        return null;
    }

    @Override
    public Void visit(VariableNode variableNode) {
        // TODO: Generar el codigo MIPS para el acceso a una variable.
        // Acceso a variable sin encadenamiento
        Variable currentVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(variableNode.getToken().getLexeme());
        Parameter currentParameter = AST.getSymbolTable().getCurrentMethod().getParameters().get(variableNode.getToken().getLexeme());
        Attribute currentAttribute = AST.getSymbolTable().getCurrentClass().getAttributes().get(variableNode.getToken().getLexeme());
        if (!variableNode.isChainedNode() && variableNode.getChainedNode() == null) {
            if (currentVariable != null) {
                codeGenerator.emitComment("Busco la variable en el RA");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
                codeGenerator.checkNullAccess("$a0", currentVariable.getToken().getRowIndex(), currentVariable.getToken().getColumnIndex());
            } else if (currentParameter != null){
                codeGenerator.emitComment("Busco el parámetro en el RA");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getParameterOffset(currentParameter) + "($fp)");
                codeGenerator.checkNullAccess("$a0", variableNode.getToken().getRowIndex(), variableNode.getToken().getColumnIndex());
            } else {
                codeGenerator.emitComment("Cargo el self ya que busco un atributo");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(AST.getSymbolTable().getCurrentMethod()) + "($fp)");
                codeGenerator.checkNullAccess("$a0", variableNode.getToken().getRowIndex(), variableNode.getToken().getColumnIndex());
                codeGenerator.emitComment("Cargo el atributo " + currentAttribute.getName());
                codeGenerator.emitText("lw $a0 " + codeGenerator.getAttributeOffset(currentAttribute) + "($a0)");
            }

        }
        // Acceso a variable que tiene algo encadenado
        else if (!variableNode.isChainedNode()) {
            variableNode.getChainedNode().setIsChainedNode(true);
            if (variableNode.getChainedNode() instanceof ChainedCallNode) {
                codeGenerator.emitComment("Guardo en $a0 la direccion del CIR que invoca al metodo");
                codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
                codeGenerator.checkNullAccess("$a0", variableNode.getToken().getRowIndex(), variableNode.getToken().getColumnIndex());
                AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(variableNode.getClassName()));
            }


            // Checkeo si se accederá a un atributo público
            AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(variableNode.getClassName()));
            if (variableNode.getChainedNode() != null) {
                if (variableNode.getMethodName().equals("start")) {
                    Class variableClass = AST.getSymbolTable().getClasses().get(variableNode.getClassName());
                    if (variableClass != null) {
                        Attribute publicAttribute = variableClass.getAttributes().get(variableNode.getChainedNode().getToken().getLexeme());
                        if (publicAttribute != null) {
                            codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");
                        }
                    }
                }
            }
            variableNode.getChainedNode().accept(this);
        } else {
            Class currentClass = AST.getSymbolTable().getCurrentClass();
            currentAttribute = currentClass.getAttributes().get(variableNode.getToken().getLexeme());
            codeGenerator.emitComment("Accedo al atributo " + currentAttribute.getToken().getLexeme() + " del self");
            codeGenerator.emitText("lw $a0 " + codeGenerator.getAttributeOffset(AST.getSymbolTable().getCurrentClass().getAttributes().get(currentAttribute.getToken().getLexeme())) + "($a0)");
            if (variableNode.getChainedNode() != null) {
                codeGenerator.checkNullAccess("$a0", variableNode.getToken().getRowIndex(), variableNode.getToken().getColumnIndex());
                AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(currentAttribute.getType().getTypeName()));
                variableNode.getChainedNode().setIsChainedNode(true);
                variableNode.getChainedNode().accept(this);
            }
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
        // Al terminar, $a0 contiene el CIR de la expresión
        codeGenerator.emitComment("Evalúo el lado derecho de la asignacion");
        assignmentNode.getExpressionNode().accept(this);

        codeGenerator.emitComment("Guardo en la pila el resultado de la expresion");
        codeGenerator.push("$a0");

        // Al terminar, $a0 contiene el CIR del lado izquierdo
        // Si es local, guardo en offset($fp)
        assignmentNode.getSimpleAccessNode().accept(this);
        codeGenerator.checkNullAccess("$a0", assignmentNode.getSimpleAccessNode().getToken().getRowIndex(), assignmentNode.getSimpleAccessNode().getToken().getColumnIndex());

        codeGenerator.emitComment("Recupero de la pila el resultado de la expresion");
        codeGenerator.pop();

        codeGenerator.emitComment("Guardo la expresión en la direccion del lado izquierdo");
        codeGenerator.emitText("sw $t0 0($a0)");

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
                    AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(className));
                    methodName = "constructor";
                } else {
                    AST.getSymbolTable().setCurrentMethod(AST.getSymbolTable().getClasses().get(className).getMethods().get(methodName));
                    AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(className));
                }

                codeGenerator.emitLabel(className + "_" + methodName);
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
        // Obtengo la clase array
        Class arrayClass = AST.getSymbolTable().getClasses().get("Array");
        // Obtengo el método actual
        Method currentMethod = AST.getSymbolTable().getCurrentMethod();
        // Obtengo el método hasNext de array
        Method hasNext = arrayClass.getMethods().get("hasNext");
        // Obtengo el metodo next correspondiente
        Method next = null;
        switch (((ArrayType) forNode.getVarNode2().getType()).getSubType().getTypeName()) {
            case "Int":
                next = arrayClass.getMethods().get("next_int");
                break;
            case "Str":
                next = arrayClass.getMethods().get("next_str");
                break;
            case "Bool":
                next = arrayClass.getMethods().get("next_bool");
                break;
        }
        codeGenerator.emitSection("Comienzo for " + forNode.getVarNode1().getToken().getRowIndex() + "_" + forNode.getVarNode1().getToken().getColumnIndex());

        /*
        int offsetBase = -4 * (currentMethod.getVariables().size() + 1);

        int offsetVariable = offsetBase - (codeGenerator.getForDepth() * 8);
         */


        Variable currentVariable = new Variable(forNode.getVarNode1().getToken(), forNode.getVarNode1().getType());
        currentVariable.setPosition(currentMethod.getVariables().size() + codeGenerator.getForDepth());
        codeGenerator.setForDepth(codeGenerator.getForDepth() + 1);
        currentMethod.getVariables().put(currentVariable.getName(), currentVariable);
        codeGenerator.generatePrimitiveCIR(currentVariable.getToken().getTokenType());
        codeGenerator.emitComment("Guardo la nueva variable en el RA");
        codeGenerator.push("$v0");

        codeGenerator.emitComment("Cargo el array");
        Variable currentArrayVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(forNode.getVarNode2().getToken().getLexeme());
        if (currentArrayVariable != null) {
            codeGenerator.emitText("lw $a0 " + codeGenerator.getVariableOffset(currentArrayVariable) + "($fp)");
        } else {
            Parameter currentArrayParameter = AST.getSymbolTable().getCurrentMethod().getParameters().get(forNode.getVarNode2().getToken().getLexeme());
            codeGenerator.emitText("lw $a0 " + codeGenerator.getParameterOffset(currentArrayParameter) + "($fp)");
        }

        codeGenerator.emitComment("Lo guardo en la pila");
        codeGenerator.push("$a0");

        codeGenerator.emitComment("Genero un objeto iterator");
        codeGenerator.emitText("li $a0 8");
        codeGenerator.emitText("li $v0 9");
        codeGenerator.emitText("syscall");

        codeGenerator.emitComment("Recupero el array");
        codeGenerator.pop();
        codeGenerator.emitComment("Guardo el array en el primer espacio del CIR de iterator");
        codeGenerator.emitText("sw $t0 0($v0)");

        codeGenerator.emitComment("Cargo indice -1");
        codeGenerator.emitText("li $t0 -1");
        codeGenerator.emitComment("Guardo el indice en la segunda posicion del CIR del iterator");
        codeGenerator.emitText("sw $t0 4($v0)");

        codeGenerator.emitComment("Guardo el iterator en la pila");
        codeGenerator.push("$v0");

        codeGenerator.emitComment("Comienzo loop for");
        codeGenerator.emitSubLabel("for_" + forNode.getVarNode1().getToken().getRowIndex() + "_" + forNode.getVarNode1().getToken().getColumnIndex());
        codeGenerator.emitComment("Cargo el objeto iterator");
        codeGenerator.emitText("lw $a0 4($sp)");
        codeGenerator.generateMethodPreCall(hasNext);

        codeGenerator.emitComment("Salto a hasNext");
        codeGenerator.emitText("jal has_next_array");

        codeGenerator.emitComment("Tengo en $a0 el CIR booleano");
        codeGenerator.emitComment("Recupero el valor del booleano");
        codeGenerator.emitText("lw $a0 4($a0)");

        codeGenerator.emitText("beqz $a0 end_for_" + forNode.getVarNode1().getToken().getRowIndex() + "_" + forNode.getVarNode1().getToken().getColumnIndex());

        codeGenerator.emitComment("Cargo el objeto iterator");
        codeGenerator.emitText("lw $a0 4($sp)");

        codeGenerator.generateMethodPreCall(next);
        codeGenerator.emitComment("Salto a next");
        codeGenerator.emitText("jal " + next.getName() + "_array");

        codeGenerator.emitComment("Tengo en $a0 el del elemento actual");
        codeGenerator.emitComment("Guardo el valor en la posicion correspondiente de la variable");
        codeGenerator.emitText("sw $a0 " + codeGenerator.getVariableOffset(currentVariable) + "($fp)");

        forNode.getSentenceNode().accept(this);


        codeGenerator.emitText("j for_" + forNode.getVarNode1().getToken().getRowIndex() + "_" + forNode.getVarNode1().getToken().getColumnIndex());
        codeGenerator.emitSubLabel("end_for_" + forNode.getVarNode1().getToken().getRowIndex() + "_" + forNode.getVarNode1().getToken().getColumnIndex());
        codeGenerator.emitComment("Quito el cir iterator de la pila");
        codeGenerator.pop();

        currentMethod.getVariables().remove(currentVariable.getName());
        codeGenerator.emitComment("Quito la variable del contexto for del RA");
        codeGenerator.pop();
        codeGenerator.setForDepth(codeGenerator.getForDepth() - 1);
        return null;
    }

    @Override
    public Void visit(IfElseNode ifElseNode) {
        String rowColumnLabel = ifElseNode.getToken().getRowIndex() + "_" + ifElseNode.getToken().getColumnIndex();
        codeGenerator.emitSection("if_" + rowColumnLabel);

        codeGenerator.emitComment("Evaluo expresion de if_" + rowColumnLabel);
        // Guardo en $a0 el resultado de la expresión a evaluar
        ifElseNode.getExpression().accept(this);
        codeGenerator.emitText("lw $a0 4($a0)");
        codeGenerator.emitText("beqz $a0 " + (ifElseNode.getSentenceNodeElse() != null ? "else_" : "endif_") + rowColumnLabel);
        codeGenerator.emitComment("Cuerpo de if_" + rowColumnLabel);
        codeGenerator.emitSubLabel("if_" + rowColumnLabel);
        if (ifElseNode.getSentenceNodeIf() != null) {
            ifElseNode.getSentenceNodeIf().accept(this);
        }
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
        Method currentMethod = AST.getSymbolTable().getCurrentMethod();
        codeGenerator.emitText("la $t0 epilogue_" + currentMethod.getClassName() + "_" + (currentMethod.getName().equals(".") ? "constructor" : currentMethod.getName()));
        codeGenerator.emitText("jr $t0");
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
        codeGenerator.emitText("lw $a0 4($a0)");
        codeGenerator.emitText("beqz $a0 endwhile_" + rowColumnLabel);


        codeGenerator.emitSubsection("Cuerpo de while_" + rowColumnLabel);
        whileNode.getSentenceNode().accept(this);

        codeGenerator.emitText("j while_" + rowColumnLabel);
        codeGenerator.emitSubLabel("endwhile_" + rowColumnLabel);

        return null;
    }
}
