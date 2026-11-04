package uncuyo.compiladores.codeGen;

import java.util.List;

import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.VariableRedeclarationInForLoopException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
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
import uncuyo.compiladores.semantic.symbolTable.ArrayType;
import uncuyo.compiladores.semantic.symbolTable.Attribute;
import uncuyo.compiladores.semantic.symbolTable.Class;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Parameter;
import uncuyo.compiladores.semantic.symbolTable.Variable;

/**
 * Recorre el AST de TinyS y emite código MIPS mediante {@link CodeGenerator}.
 * Requiere un árbol chequeado semánticamente y la tabla de símbolos asociada
 * a {@link AST}, con los tipos y las posiciones de sus entradas resueltos.
 *
 * <p>Las expresiones dejan en {@code $a0} la referencia a su CIR (registro de
 * instancia de clase), o cero para {@code nil}. Los accesos utilizados como
 * destino de una asignación dejan en ese registro la dirección donde se debe
 * guardar la referencia. Los métodos de visita devuelven {@code null}; el
 * resultado del recorrido se acumula en el generador.</p>
 *
 * <p>El recorrido actualiza la clase y el método actuales de la tabla de
 * símbolos, y marca los nodos encadenados que procesa.</p>
 */
public class CodeGenVisitor implements Visitor<Void> {

    private CodeGenerator codeGenerator;
    /**
     * Crea un visitante con un generador de código vacío y sus recursos predefinidos.
     *
     * @throws RuntimeException si no se pueden cargar los recursos de ensamblador
     */
    public CodeGenVisitor() {
        this.codeGenerator = new CodeGenerator();
    }

    /**
     * Obtiene el generador que acumula las instrucciones emitidas durante el recorrido.
     *
     * @return generador asociado a este visitante
     */
    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }

    // #################################################################
    // #               Visitor codeGen de expresiones                  #
    // #################################################################

    /**
     * Emite la reserva de un arreglo y la inicialización de sus elementos.
     * Guarda la virtual table y la longitud en sus dos primeras palabras,
     * seguidas por las referencias a los CIR de los elementos. Deja la
     * referencia del arreglo en {@code $a0}.
     *
     * @param arrayConstructorNode construcción con el tipo de elemento y la expresión de longitud
     * @return {@code null}
     */
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
        codeGenerator.emitComment("$t2 contendra la direccion a la virtual table correspondiente");
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

        return null;
    }

    /**
     * Emite la lectura de un elemento de un arreglo local.
     * Evalúa el índice, comprueba la referencia y sus límites, y carga en
     * {@code $a0} la referencia almacenada en la posición seleccionada.
     *
     * @param arrayNode acceso con el identificador del arreglo y la expresión del índice
     * @return {@code null}
     */
    @Override
    public Void visit(ArrayNode arrayNode) {
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

    /**
     * Emite la evaluación de ambos operandos, de izquierda a derecha, y la
     * operación binaria. Procesa los valores primitivos y construye un
     * CIR para el resultado, cuya referencia queda en {@code $a0}.
     * La división incluye un salto de error si el divisor es cero.
     *
     * @param binaryExpressionNode expresión con sus operandos, operador y tipo resueltos
     * @return {@code null}
     */
    @Override
    public Void visit(BinaryExpressionNode binaryExpressionNode) {

        List<TokenType> primitiveClasses = List.of(TokenType.INT_CLASS, TokenType.INT_CONSTANT, TokenType.BOOL_CLASS, 
        TokenType.BOOLEAN_CONSTANT,TokenType.STR_CLASS, TokenType.STRING_CONSTANT);

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

        // Solo obtengo el valor del CIR si es de tipo primitivo
        if(primitiveClasses.contains(binaryExpressionNode.getRightNode().getType().getTokenType())) {
            codeGenerator.emitComment("Obtengo el valor del CIR");
            codeGenerator.emitText("lw $a0 4($a0)");
        }
        
        // Cargo el valor del nodo izquierdo en $t0 y recupero la pila
        codeGenerator.emitComment("Recupero el valor del nodo izquierdo");
        codeGenerator.pop();
        // Solo obtengo el valor del CIR si es de tipo primitivo
        if(primitiveClasses.contains(binaryExpressionNode.getLeftNode().getType().getTokenType())) {
            codeGenerator.emitComment("Obtengo el valor del CIR");
            codeGenerator.emitText("lw $t0 4($t0)");
        }
        
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

    /**
     * Emite una llamada estática o de instancia y continúa su encadenamiento.
     * Prepara el registro de activación, evalúa los argumentos y obtiene la
     * dirección del método desde la tabla virtual correspondiente. Actualiza
     * la clase actual para resolver los accesos siguientes.
     *
     * @param chainedCallNode llamada con el receptor, los argumentos y el encadenamiento
     * @return {@code null}
     */
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

    /**
     * @param chainedNode nodo genérico de acceso encadenado
     * @return {@code null}
     */
    @Override
    public Void visit(ChainedNode chainedNode) {
        return null;
    }

    /**
     * Emite la reserva del CIR de un objeto, inicializa sus atributos y su
     * virtual table, y llama a su constructor con los argumentos evaluados.
     * La llamada deja en {@code $a0} la referencia a la instancia creada.
     *
     * @param classConstructorNode construcción con la clase y los argumentos del constructor
     * @return {@code null}
     */
    @Override
    public Void visit(ClassConstructorNode classConstructorNode) {
        Class currentClass = AST.getSymbolTable().getClasses().get(classConstructorNode.getToken().getLexeme());
        codeGenerator.emitSection("Generacion de CIR de " + currentClass.getName());
        int cirSpace = currentClass.getAttributes().size() + 1;
        codeGenerator.emitComment("Necesita " + cirSpace + " espacios para sus atributos y la virtual table" );
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

    /**
     * @param conclassorCallNode nodo genérico de llamada a constructor o clase
     * @return {@code null}
     */
    @Override
    public Void visit(ConclassorCallNode conclassorCallNode) {
        return null;
    }

    /**
     * @param expressionNode nodo genérico de expresión
     * @return {@code null}
     */
    @Override
    public Void visit(ExpressionNode expressionNode) {
        return null;
    }

    /**
     * Emite un literal entero, booleano, de cadena o {@code nil}.
     * Los valores primitivos se guardan en un nuevo CIR; las cadenas
     * reutilizan sus bytes en {@code .data}. Deja la referencia o el valor
     * nulo en {@code $a0}.
     *
     * @param literalNode literal con su token y tipo resueltos
     * @return {@code null}
     */
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

    /**
     * @param operandNode nodo genérico de operando
     * @return {@code null}
     */
    @Override
    public Void visit(OperandNode operandNode) {
        return null;
    }

    /**
     * Recorre la expresión entre paréntesis y, si existe, su encadenamiento.
     *
     * @param parenthesizedExpressionNode expresión agrupada que se desea evaluar
     * @return {@code null}
     */
    @Override
    public Void visit(ParenthesizedExpressionNode parenthesizedExpressionNode) {
        parenthesizedExpressionNode.getExpresion().accept(this);
        if (parenthesizedExpressionNode.getChainedNode() != null) {
            parenthesizedExpressionNode.getChainedNode().accept(this);
        }
        return null;
    }

    /**
     * Emite la carga de {@code self} desde el registro de activación y recorre
     * el acceso encadenado que lo acompaña. Ajusta la clase actual cuando el
     * siguiente nodo es un acceso a variable.
     *
     * @param selfNode acceso a {@code self} con un nodo encadenado presente
     * @return {@code null}
     */
    @Override
    public Void visit(SelfNode selfNode) {
        if (selfNode.getChainedNode() instanceof VariableNode) {
            AST.getSymbolTable().setCurrentClass(AST.getSymbolTable().getClasses().get(((VariableNode) selfNode.getChainedNode()).getClassName()));
        }
        codeGenerator.emitComment("Accedo al self");
        codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(AST.getSymbolTable().getCurrentMethod()) + "($fp)");
        selfNode.getChainedNode().setIsChainedNode(true);
        selfNode.getChainedNode().accept(this);

        return null;
    }

    /**
     * @param simpleAccessNode nodo genérico de acceso asignable
     * @return {@code null}
     */
    @Override
    public Void visit(SimpleAccessNode simpleAccessNode) {
        return null;
    }

    /**
     * Emite el cálculo de la dirección de una posición de un arreglo local.
     * Comprueba la referencia y los límites del índice y deja en {@code $a0}
     * la dirección donde se debe guardar la referencia al elemento.
     *
     * @param simpleArrayNode acceso indexado utilizado como destino de asignación
     * @return {@code null}
     */
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

    /**
     * Emite el desplazamiento hasta un atributo asignable cuando es el último
     * nodo del encadenamiento. Requiere la instancia en {@code $a0} y su clase
     * en el contexto actual; deja la dirección del atributo en {@code $a0}.
     *
     * @param simpleChainedNode acceso encadenado a un atributo asignable
     * @return {@code null}
     */
    @Override
    public Void visit(SimpleChainedNode simpleChainedNode) {
        if (simpleChainedNode.getSimpleChainedNode() == null) {
            Attribute currentAttribute = AST.getSymbolTable().getCurrentClass().getAttributes().get(simpleChainedNode.getToken().getLexeme());
            codeGenerator.emitComment("Obtengo el atributo del self");
            codeGenerator.emitText("addiu $a0 $a0 " + codeGenerator.getAttributeOffset(currentAttribute));
        }
        return null;
    }

    /**
     * Emite la carga de {@code self} y recorre el acceso asignable que lo sigue.
     *
     * @param simpleSelfNode destino mediante {@code self} con un encadenamiento presente
     * @return {@code null}
     */
    @Override
    public Void visit(SimpleSelfNode simpleSelfNode) {
        codeGenerator.emitComment("Obtengo el CIR del self " + AST.getSymbolTable().getCurrentClass().getName());
        codeGenerator.emitText("lw $a0 " + codeGenerator.getSelfOffset(AST.getSymbolTable().getCurrentMethod()) + "($fp)");
        simpleSelfNode.getSimpleChainedNode().accept(this);
        return null;
    }

    /**
     * Emite el cálculo de la dirección de una variable local, atributo o
     * parámetro utilizado como destino de asignación. Para atributos
     * encadenados carga la referencia del receptor y comprueba que no sea nula.
     * Deja la dirección de destino en {@code $a0}.
     *
     * @param simpleVariableNode acceso asignable con sus identificadores resueltos
     * @return {@code null}
     */
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

    /**
     * Emite la evaluación del operando y la aplicación del operador unario.
     * El signo positivo conserva el valor; los demás operadores modifican el
     * valor almacenado en el CIR del operando, cuya referencia queda en {@code $a0}.
     *
     * @param unaryExpressionNode expresión con el operando y el operador unario
     * @return {@code null}
     */
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

    /**
     * Emite la lectura de una variable local, parámetro o atributo y continúa
     * los accesos o llamadas encadenados. Usa la tabla de símbolos para
     * resolver las posiciones y actualiza la clase actual al cambiar de receptor.
     * La referencia leída queda en {@code $a0}.
     *
     * @param variableNode acceso con sus datos de clase, método y tipo resueltos
     * @return {@code null}
     */
    @Override
    public Void visit(VariableNode variableNode) {
        // Acceso a variable sin encadenamiento
        Variable currentVariable = AST.getSymbolTable().getCurrentMethod().getVariables().get(variableNode.getToken().getLexeme());
        Parameter currentParameter = AST.getSymbolTable().getCurrentMethod().getParameters().get(variableNode.getToken().getLexeme());
        Attribute currentAttribute = null;
        if (AST.getSymbolTable().getCurrentClass() != null) {
            currentAttribute = AST.getSymbolTable().getCurrentClass().getAttributes().get(variableNode.getToken().getLexeme());
        }
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

    /**
     * Emite la evaluación del lado derecho y guarda su referencia en la
     * dirección calculada por el acceso del lado izquierdo. Conserva el
     * resultado temporalmente en la pila mientras calcula el destino.
     *
     * @param assignmentNode asignación con su expresión y acceso de destino
     * @return {@code null}
     */
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

    /**
     * Recorre las sentencias no nulas de un bloque en su orden de aparición.
     * Si representa un método, selecciona el bloque de salida, actualiza el
     * contexto de la tabla de símbolos y emite su prólogo y epílogo. El
     * método {@code start} se genera dentro de {@code main}.
     *
     * @param blockNode bloque de sentencias o cuerpo de método o constructor
     * @return {@code null}
     */
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

    /**
     * Emite un recorrido de arreglo mediante un iterador y las rutinas
     * {@code hasNext} y {@code next} del tipo de elemento correspondiente.
     * Agrega temporalmente la variable de iteración al método actual y
     * administra su espacio de pila y la profundidad de bucles activos.
     *
     * @param forNode bucle con la variable de iteración, el arreglo y el cuerpo
     * @return {@code null}
     * @throws VariableRedeclarationInForLoopException si la variable de iteración ya existe
     *         entre las variables del método actual
     */
    @Override
    public Void visit(ForNode forNode) {
        // Obtengo la clase array
        Class arrayClass = AST.getSymbolTable().getClasses().get("Array");
        // Obtengo el método actual
        Method currentMethod = AST.getSymbolTable().getCurrentMethod();
        if (currentMethod.getVariables().containsKey(forNode.getVarNode1().getToken().getLexeme())) {
            throw new VariableRedeclarationInForLoopException(
                    forNode.getVarNode1().getToken().getRowIndex(),
                    forNode.getVarNode1().getToken().getColumnIndex(),
                    forNode.getVarNode1().getToken().getLexeme(),
                    currentMethod.getName()
            );
        }
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

    /**
     * Emite la evaluación de una condición y los saltos entre las ramas
     * {@code if} y {@code else}, cuando esta última está presente.
     * Identifica las etiquetas mediante la fila y columna del nodo.
     *
     * @param ifElseNode sentencia condicional con su expresión y sus ramas
     * @return {@code null}
     */
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

    /**
     * Emite la evaluación de la expresión de retorno, guarda su referencia en
     * el registro de activación y salta al epílogo del método actual.
     *
     * @param returnNode retorno con una expresión presente
     * @return {@code null}
     */
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

    /**
     * @param sentenceNode nodo genérico de sentencia
     * @return {@code null}
     */
    @Override
    public Void visit(SentenceNode sentenceNode) {
        return null;
    }

    /**
     * Recorre la expresión utilizada como sentencia
     *
     * @param simpleSentenceNode sentencia formada por una expresión
     * @return {@code null}
     */
    @Override
    public Void visit(SimpleSentenceNode simpleSentenceNode) {
        simpleSentenceNode.getExpressionNode().accept(this);
        return null;
    }

    /**
     * Emite un bucle que evalúa su condición antes de cada ejecución del cuerpo.
     * Extrae el valor del CIR booleano y salta al final cuando es falso.
     * Identifica las etiquetas mediante la fila y columna del nodo.
     *
     * @param whileNode bucle con su condición y cuerpo
     * @return {@code null}
     */
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
