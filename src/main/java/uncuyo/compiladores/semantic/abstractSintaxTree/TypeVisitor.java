package uncuyo.compiladores.semantic.abstractSintaxTree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.ASTException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidAttributeAccessException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidConstructorArgumentTypeException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidConstructorArgumentsCountException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidLiteralNodeException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidLiteralOperationException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidMethodAccessException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidMethodArgumentCountException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidMethodArgumentTypeException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidPrimitiveAccessException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidReturnException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidVoidAccessException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.MissingReturnException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.NonStaticMethodCalledFromClassException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.SelfUsedInStaticMehthodException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.StaticMethodCalledFromInstanceException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.TypeMismatchException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.VariableRedeclarationInForLoopException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.AttributeNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.ClassNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.LocalVariableNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.MethodNotFoundException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenDefinitions;
import uncuyo.compiladores.lexer.TokenType;
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
import uncuyo.compiladores.semantic.symbolTable.Block;
import uncuyo.compiladores.semantic.symbolTable.Class;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Parameter;
import uncuyo.compiladores.semantic.symbolTable.SymbolTable;
import uncuyo.compiladores.semantic.symbolTable.Type;
import uncuyo.compiladores.semantic.symbolTable.Variable;

/**
 * Visitante que comprueba tipos, accesos, llamadas y retornos del AST.
 * Utiliza la tabla compartida de {@link AST} y actualiza el contexto de
 * clase, método y bloque durante el recorrido. Los nodos conservan los
 * tipos calculados y las marcas de retorno que asigna cada sobrecarga.
 * La primera incompatibilidad detectada interrumpe el recorrido con una
 * excepción semántica. Sus instancias mantienen estado de recorrido y no
 * deben compartirse entre chequeos concurrentes.
 */
public class TypeVisitor implements Visitor<Type> {

    private final SymbolTable symbolTable = AST.getSymbolTable();

    private final List<TokenType> literalTokenTypes = List.of(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT,
            TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT);

    private final List<String> predefinedClasses = List.of("Int", "Str", "Bool", "Array");
    private int blockIndex = 0;
    private Block currentBlock;


    // #################################################################
    // #               Visitor type de expresiones                     #
    // #################################################################

    /**
     * Devuelve el tipo declarado del arreglo.
     *
     * @param arrayConstructorNode nodo que se comprueba
     * @return tipo almacenado en el nodo
     */
    public Type visit(ArrayConstructorNode arrayConstructorNode) {
        return arrayConstructorNode.getType();
    }

    /**
     * Comprueba que el índice sea entero y resuelve el tipo del elemento.
     *
     * @param arrayNode nodo que se comprueba
     * @return subtipo del arreglo, que también se almacena en el nodo
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     */
    public Type visit(ArrayNode arrayNode) {
        Type indexType = arrayNode.getExpression().accept(this);
        if (!indexType.getTokenType().isInt()) {
            throw new TypeMismatchException(
                    arrayNode.getToken().getRowIndex(),
                    arrayNode.getToken().getColumnIndex(),
                    "Int", indexType.getTypeName());
        }

        String varName = arrayNode.getToken().getLexeme();
        Type type = null;

        if (arrayNode.getClassName() != null) {
            Class currentClass = symbolTable.getClasses().get(arrayNode.getClassName());
            if (currentClass != null && currentClass.getAttributes().containsKey(varName)) {
                type = currentClass.getAttributes().get(varName).getType();
            }
        }
        if (type == null) {
            Method currentMethod = symbolTable.getCurrentMethod();
            if (currentMethod != null) {
                if (currentMethod.getVariables().containsKey(varName)) {
                    type = currentMethod.getVariables().get(varName).getType();
                } else if (currentMethod.getParameters().containsKey(varName)) {
                    type = currentMethod.getParameters().get(varName).getType();
                }
            }
        }

        if (!(type instanceof ArrayType)) {
            throw new TypeMismatchException(
                    arrayNode.getToken().getRowIndex(),
                    arrayNode.getToken().getColumnIndex(),
                    "Array",
                    type != null ? type.getTypeName() : varName);
        }

        Type subType = ((ArrayType) type).getSubType();
        arrayNode.setType(subType);

        return subType;
    }

    /**
     * Comprueba los operandos y determina el tipo de la operación binaria.
     *
     * @param binaryExpressionNode nodo que se comprueba
     * @return tipo de resultado, almacenado también en el nodo
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     */
    public Type visit(BinaryExpressionNode binaryExpressionNode) {
        Type type = null;
        Type leftNodeType = binaryExpressionNode.getLeftNode().accept(this);
        Type rightNodeType = binaryExpressionNode.getRightNode().accept(this);
        Token binaryOperationToken = binaryExpressionNode.getBinaryOperatorToken();
        TokenType binaryOperationTokenType = binaryOperationToken.getTokenType();
        if (binaryOperationTokenType == TokenType.OPERATOR_AND ||
                binaryOperationTokenType == TokenType.OPERATOR_OR) {

            if (!leftNodeType.getTokenType().isBool()){
                throw new TypeMismatchException(binaryOperationToken.getRowIndex(),
                        binaryOperationToken.getColumnIndex(),
                        "Bool", leftNodeType.getTypeName());
            }
            if (!rightNodeType.getTokenType().isBool()){
                throw new TypeMismatchException(binaryOperationToken.getRowIndex(), binaryOperationToken.getColumnIndex(),
                        "Bool", rightNodeType.getTypeName());
            }

            type = Type.promote(leftNodeType, rightNodeType);

        } else if (binaryOperationTokenType == TokenType.OPERATOR_ADD ||
                binaryOperationTokenType == TokenType.OPERATOR_SUB ||
                binaryOperationTokenType == TokenType.OPERATOR_MUL ||
                binaryOperationTokenType == TokenType.OPERATOR_DIV) {

            if (!leftNodeType.getTokenType().isInt()){
                throw new TypeMismatchException(binaryOperationToken.getRowIndex(),
                        binaryOperationToken.getColumnIndex(),
                        "Int", leftNodeType.getTypeName());
            }
            if (!rightNodeType.getTokenType().isInt()){
                throw new TypeMismatchException(binaryOperationToken.getRowIndex(), binaryOperationToken.getColumnIndex(),
                        "Int", rightNodeType.getTypeName());
            }

            type = Type.promote(leftNodeType, rightNodeType);

        } else if (binaryOperationTokenType == TokenType.OPERATOR_EQ ||
                binaryOperationTokenType == TokenType.OPERATOR_NEQ) {

            if (!Type.areCompatible(leftNodeType, rightNodeType)){
                throw new TypeMismatchException(binaryOperationToken.getRowIndex(),
                        binaryOperationToken.getColumnIndex(),
                        leftNodeType.getTypeName(), rightNodeType.getTypeName());
            }

            type = new Type(TokenType.BOOL_CLASS, "Bool");
        } else if (binaryOperationTokenType == TokenType.OPERATOR_GT ||
                binaryOperationTokenType == TokenType.OPERATOR_LT ||
                binaryOperationTokenType == TokenType.OPERATOR_GTE ||
                binaryOperationTokenType == TokenType.OPERATOR_LTE) {

            if (!leftNodeType.getTokenType().isInt()){
                throw new TypeMismatchException(binaryOperationToken.getRowIndex(),
                        binaryOperationToken.getColumnIndex(),
                        "Int", leftNodeType.getTypeName());
            }
            if (!rightNodeType.getTokenType().isInt()){
                throw new TypeMismatchException(binaryOperationToken.getRowIndex(), binaryOperationToken.getColumnIndex(),
                        "Int", rightNodeType.getTypeName());
            }

            type = new Type(TokenType.BOOL_CLASS, "Bool");
        }
        binaryExpressionNode.setType(type);

        return type;
    }

    /**
     * Resuelve la llamada, comprueba argumentos y continúa el encadenado.
     *
     * @param chainedCallNode nodo que se comprueba
     * @return tipo de la llamada o del último acceso del encadenado
     * @throws ClassNotFoundException si la clase requerida no está registrada
     * @throws InvalidMethodAccessException si no se encuentra el método solicitado en el receptor
     * @throws NonStaticMethodCalledFromClassException si se accede a un método de instancia desde una clase
     * @throws InvalidMethodArgumentCountException si la cantidad de argumentos no coincide con la firma
     * @throws InvalidMethodArgumentTypeException si un argumento no es compatible con su parámetro
     * @throws InvalidVoidAccessException si se intenta continuar un acceso desde un retorno {@code void}
     * @throws InvalidPrimitiveAccessException si el encadenado solicita un acceso no permitido sobre un primitivo
     * @throws InvalidAttributeAccessException si el acceso al atributo privado no está permitido
     */
    public Type visit(ChainedCallNode chainedCallNode) {
        Type type;
        Token chainedCallNodeToken = chainedCallNode.getToken();
        if (chainedCallNodeToken.getTokenType() == TokenType.IDENTIFIER_CLASS) {
            symbolTable.setCurrentClass(symbolTable.getClasses().get(chainedCallNodeToken.getLexeme()));
            if (symbolTable.getCurrentClass() == null) {
                throw new ClassNotFoundException(chainedCallNodeToken.getLexeme());
            }
            if (chainedCallNode.getChainedNode() instanceof ChainedCallNode) {
                Method nextMethod = symbolTable.getCurrentClass().getMethods().get(chainedCallNode.getChainedNode().getToken().getLexeme());
                Token chainedNodeToken = chainedCallNode.getChainedNode().getToken();
                if (nextMethod == null) {
                    throw new InvalidMethodAccessException(chainedNodeToken.getRowIndex(), chainedNodeToken.getColumnIndex(), chainedNodeToken.getLexeme(), symbolTable.getCurrentClass().getName());
                }
                if (!nextMethod.isStatic()) {
                    throw new NonStaticMethodCalledFromClassException(chainedNodeToken.getRowIndex(), chainedNodeToken.getColumnIndex(), nextMethod.getToken().getLexeme(), symbolTable.getCurrentClass().getName());
                }
            }
            type = chainedCallNode.getChainedNode().accept(this);
            chainedCallNode.setType(type);
        } else {
            Method method = symbolTable.getCurrentClass().getMethodInHierarchy(chainedCallNodeToken.getLexeme(), symbolTable.getClasses());
            if (method == null) {
                throw new InvalidMethodAccessException(chainedCallNodeToken.getRowIndex(), chainedCallNodeToken.getColumnIndex(),
                        chainedCallNodeToken.getLexeme(), symbolTable.getCurrentClass().getName());
            }
            type = method.getType();

            LinkedHashMap<String,Parameter> parameters = method.getParameters();
            int expectedParametersSize = parameters.size();
            int recivedParametersSize = chainedCallNode.getExpressionNodes().size();

            if (expectedParametersSize != recivedParametersSize) {
                throw new InvalidMethodArgumentCountException(chainedCallNodeToken.getRowIndex(),
                        chainedCallNodeToken.getColumnIndex(),
                        method.getName(),
                        expectedParametersSize,
                        recivedParametersSize
                );
            }

            List<Parameter> parameterList = new ArrayList<>(parameters.values());
            for (int i = 0; i < parameterList.size(); i++) {
                ExpressionNode expressionNode = chainedCallNode.getExpressionNodes().get(i);
                Type argType = expressionNode.accept(this);
                Parameter parameter = parameterList.get(i);

                if (!Type.areCompatible(argType , parameter.getType())){
                    throw new InvalidMethodArgumentTypeException(chainedCallNodeToken.getRowIndex(),
                            chainedCallNodeToken.getColumnIndex(),
                            method.getName(),
                            i, parameter.getType().getTypeName() , argType.getTypeName());
                }

            }

            if (chainedCallNode.getChainedNode() != null) {
                if (type.getTypeName().equals("void")){
                    throw new InvalidVoidAccessException(
                            chainedCallNodeToken.getRowIndex(),
                            chainedCallNodeToken.getColumnIndex(),
                            method.getName()
                    );
                } else if (Type.isPrimitiveType(type.getTypeName())
                        && !(chainedCallNode.getChainedNode() instanceof ChainedCallNode)) {
                    throw new InvalidPrimitiveAccessException(
                            chainedCallNodeToken.getRowIndex(),
                            chainedCallNodeToken.getColumnIndex(),
                            method.getName(),
                            type.getTypeName()
                    );
                }

                Class currentClass = symbolTable.getClasses().get(type.getLexeme());
                if (currentClass != null) {
                    symbolTable.setCurrentClass(currentClass);
                }
                if (currentClass != null && !(chainedCallNode.getChainedNode() instanceof ChainedCallNode)) {
                    Token chainedNodeToken = chainedCallNode.getChainedNode().getToken();
                    Attribute attribute = currentClass.getAttributes().get(chainedNodeToken.getLexeme());
                    if (attribute != null && attribute.isPrivate()) {
                        if (!attribute.getClassName().equals(currentClass.getName())) {
                            throw new InvalidAttributeAccessException(
                                    chainedNodeToken.getRowIndex(),
                                    chainedNodeToken.getColumnIndex(),
                                    chainedNodeToken.getLexeme(),
                                    currentClass.getName()
                            );
                        }
                    }
                    if (chainedCallNode.getChainedNode() instanceof VariableNode) {
                        VariableNode variableNode = (VariableNode) chainedCallNode.getChainedNode();
                        variableNode.setType(attribute != null ? attribute.getType() : null);
                        variableNode.setClassName(currentClass.getName());
                        variableNode.setIsChainedNode(true);
                    }
                }
                chainedCallNode.setType(type);
                type = chainedCallNode.getChainedNode().accept(this);
            }
        }

        return type;
    }

    /**
     * @param chainedNode nodo que se comprueba
     * @return tipo devuelto por la visita del nodo concreto
     */
    public Type visit(ChainedNode chainedNode) {
        return chainedNode.accept(this);
    }

    /**
     * Comprueba la cantidad y los tipos de los argumentos del constructor.
     *
     * @param classConstructorNode nodo que se comprueba
     * @return tipo de la instancia creada
     * @throws InvalidConstructorArgumentsCountException si la cantidad de argumentos no coincide con el constructor
     * @throws InvalidConstructorArgumentTypeException si un argumento no es compatible con el constructor
     */
    public Type visit(ClassConstructorNode classConstructorNode) {
        Method constructor = symbolTable.getClasses().get(classConstructorNode.getType().getLexeme()).getConstructor();
        if(classConstructorNode.getExpressionNodes().size() != constructor.getParameters().size()) {
            throw new InvalidConstructorArgumentsCountException(classConstructorNode.getToken().getRowIndex(), classConstructorNode.getToken().getColumnIndex(),
                    classConstructorNode.getToken().getLexeme(), constructor.getParameters().size(), classConstructorNode.getExpressionNodes().size());
        }
        int paramIndex = 0;
        List<Parameter> parameters =
                new ArrayList<>(constructor.getParameters().values());
        for (ExpressionNode expressionNode : classConstructorNode.getExpressionNodes()) {
            Type type = expressionNode.accept(this);
            if (!Type.areCompatible(parameters.get(paramIndex).getType(), type)) {
                throw new InvalidConstructorArgumentTypeException(classConstructorNode.getToken().getRowIndex(), classConstructorNode.getToken().getColumnIndex(),
                        classConstructorNode.getToken().getLexeme(), paramIndex, parameters.get(paramIndex).getType().getTypeName(), type.getTypeName());
            }
            paramIndex++;
        }
        return classConstructorNode.getType();
    }

    /**
     * @param conclassorCallNode nodo que se comprueba
     * @return tipo devuelto por la visita del nodo concreto
     */
    public Type visit(ConclassorCallNode conclassorCallNode) {
        return conclassorCallNode.accept(this);
    }

    /**
     * @param expressionNode nodo que se comprueba
     * @return tipo devuelto por la visita del nodo concreto
     */
    public Type visit(ExpressionNode expressionNode) {
        return expressionNode.accept(this);
    }

    /**
     * Comprueba que la categoría del literal sea admitida.
     *
     * @param literalNode nodo que se comprueba
     * @return tipo inicializado por el literal
     * @throws InvalidLiteralNodeException si el token no corresponde a un literal admitido
     */
    public Type visit(LiteralNode literalNode) {
        if (!literalTokenTypes.contains(literalNode.getType().getTokenType())) {
            throw new InvalidLiteralNodeException(literalNode.getToken().getRowIndex(), literalNode.getToken().getColumnIndex(),
                    "Se esperaba 'nil', constante entera, booleana o string");
        }
        return literalNode.getType();
    }

    /**
     * @param operandNode nodo que se comprueba
     * @return tipo devuelto por la visita del nodo concreto
     */
    public Type visit(OperandNode operandNode) {
        return operandNode.accept(this);
    }

    /**
     * Visita la expresión agrupada y su encadenado, si existe.
     *
     * @param parenthesizedExpressionNode nodo que se comprueba
     * @return tipo de la expresión o del último acceso encadenado
     * @throws InvalidAttributeAccessException si el acceso al atributo privado no está permitido
     */
    public Type visit(ParenthesizedExpressionNode parenthesizedExpressionNode) {
        Type type = parenthesizedExpressionNode.getExpresion().accept(this);
        if (parenthesizedExpressionNode.getChainedNode() != null) {
            Token chainedNodeToken = parenthesizedExpressionNode.getChainedNode().getToken();
            Class currentClass = symbolTable.getClasses().get(type.getLexeme());
            Attribute attribute = currentClass.getAttributes().get(chainedNodeToken.getLexeme());
            if (attribute != null && attribute.isPrivate()) {
                if (!attribute.getClassName().equals(currentClass.getName())) {
                    throw new InvalidAttributeAccessException(
                            chainedNodeToken.getRowIndex(),
                            chainedNodeToken.getColumnIndex(),
                            chainedNodeToken.getLexeme(),
                            currentClass.getName()
                    );
                }
            }
            type = parenthesizedExpressionNode.getChainedNode().accept(this);
            parenthesizedExpressionNode.setType(type);
        }
        return type;
    }

    /**
     * Resuelve el tipo de {@code self} o de su encadenado.
     *
     * @param selfNode nodo que se comprueba
     * @return tipo de la referencia o del acceso final
     * @throws SelfUsedInStaticMehthodException si el acceso encadenado mediante {@code self} ocurre en un método estático
     * @throws InvalidAttributeAccessException si el acceso al atributo privado no está permitido
     */
    public Type visit(SelfNode selfNode) {

        Class currentClass = symbolTable.getClasses().get(symbolTable.getCurrentMethod().getClassName());

        Type type = new Type(
                currentClass.getToken().getTokenType(),
                currentClass.getName()
        );

        if (selfNode.getChainedNode() == null) {
            selfNode.setType(type);
        } else {
            Method method = symbolTable.getCurrentMethod();
            if (method != null && method.isStatic()){
                Token token = selfNode.getToken();
                throw new SelfUsedInStaticMehthodException(token.getRowIndex(),
                        token.getColumnIndex(),
                        method.getName(),
                        method.getClassName());
            }

            Token chainedNodeToken = selfNode.getChainedNode().getToken();
            Attribute attribute = currentClass.getAttributes().get(chainedNodeToken.getLexeme());
            if (attribute != null && attribute.isPrivate()) {
                if (!attribute.getClassName().equals(currentClass.getName())) {
                    throw new InvalidAttributeAccessException(
                            chainedNodeToken.getRowIndex(),
                            chainedNodeToken.getColumnIndex(),
                            chainedNodeToken.getLexeme(),
                            attribute.getClassName()
                    );
                }
            }
            type = selfNode.getChainedNode().accept(this);
            selfNode.setType(type);
        }
        return type;
    }

    /**
     * @param simpleAccessNode nodo que se comprueba
     * @return tipo devuelto por la visita del nodo concreto
     */
    public Type visit(SimpleAccessNode simpleAccessNode) {
        return simpleAccessNode.accept(this);
    }

    /**
     * Comprueba el índice y resuelve el tipo del elemento de destino.
     *
     * @param simpleArrayNode nodo que se comprueba
     * @return subtipo del arreglo, que también se almacena en el nodo
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     */
    public Type visit(SimpleArrayNode simpleArrayNode) {
        Type indexType = simpleArrayNode.getExpressionNode().accept(this);
        if (!indexType.getTokenType().isInt()) {
            throw new TypeMismatchException(
                    simpleArrayNode.getToken().getRowIndex(),
                    simpleArrayNode.getToken().getColumnIndex(),
                    "Int", indexType.getTypeName());
        }

        String varName = simpleArrayNode.getToken().getLexeme();
        Type type = null;

        Method currentMethod = symbolTable.getCurrentMethod();
        if (currentMethod != null) {
            if (currentMethod.getVariables().containsKey(varName)) {
                type = currentMethod.getVariables().get(varName).getType();
            } else if (currentMethod.getParameters().containsKey(varName)) {
                type = currentMethod.getParameters().get(varName).getType();
            }
        }

        if (!(type instanceof ArrayType)) {
            throw new TypeMismatchException(
                    simpleArrayNode.getToken().getRowIndex(),
                    simpleArrayNode.getToken().getColumnIndex(),
                    "Array",
                    type != null ? type.getTypeName() : varName);
        }

        Type subType = ((ArrayType) type).getSubType();
        simpleArrayNode.setType(subType);
        return subType;
    }

    /**
     * Resuelve el atributo de destino, su visibilidad y la continuación.
     *
     * @param simpleChainedNode nodo que se comprueba
     * @return tipo del acceso final, o {@code null} si no se resuelve la clase de contexto
     * @throws AttributeNotFoundException si no se encuentra el atributo en la clase de contexto
     * @throws InvalidAttributeAccessException si el acceso al atributo privado no está permitido
     */
    public Type visit(SimpleChainedNode simpleChainedNode) {

        if (simpleChainedNode.getClassName() == null){
            return null;
        }

        Class currentClass = symbolTable.getClasses().get(simpleChainedNode.getClassName());
        if (currentClass == null){
            return null;
        }

        String attributeName = simpleChainedNode.getToken().getLexeme();

        if (!currentClass.getAttributes().containsKey(attributeName)) {
            throw new AttributeNotFoundException(attributeName, currentClass.getName());
        } else {
            Attribute attribute = currentClass.getAttributes().get(attributeName);
            if ( !attribute.getClassName().equals(currentClass.getName()) &&
                attribute.isPrivate()){
                throw new InvalidAttributeAccessException(attribute.getToken().getRowIndex(),
                        attribute.getToken().getColumnIndex(),
                        attributeName,
                        attribute.getClassName());
            }
        }

        Type attributeType = currentClass.getAttributes().get(attributeName).getType();
        simpleChainedNode.setType(attributeType);

        if (simpleChainedNode.getSimpleChainedNode() != null) {
            simpleChainedNode.getSimpleChainedNode().setClassName(attributeType.getLexeme());
            return simpleChainedNode.getSimpleChainedNode().accept(this);
        }

        return attributeType;

    }

    /**
     * Comprueba el acceso de destino desde {@code self} y visita sus atributos.
     *
     * @param simpleSelfNode nodo que se comprueba
     * @return tipo del encadenado, o {@code null} si no existe
     * @throws SelfUsedInStaticMehthodException si el acceso encadenado mediante {@code self} ocurre en un método estático
     */
    public Type visit(SimpleSelfNode simpleSelfNode) {
        if (simpleSelfNode.getSimpleChainedNode() == null) {
            return null;
        } else {
            Method method = symbolTable.getCurrentMethod();
            if (method != null && method.isStatic()){
                Token token = simpleSelfNode.getToken();
                throw new SelfUsedInStaticMehthodException(token.getRowIndex(),
                        token.getColumnIndex(),
                        method.getName(),
                        method.getClassName());
            }
            return simpleSelfNode.getSimpleChainedNode().accept(this);
        }
    }

    /**
     * Resuelve el tipo del destino y comprueba los atributos encadenados.
     *
     * @param simpleVariableNode nodo que se comprueba
     * @return tipo de la variable o del destino final; puede ser {@code null} si no se resuelve
     * @throws InvalidAttributeAccessException si el acceso al atributo privado no está permitido
     */
    public Type visit(SimpleVariableNode simpleVariableNode) {
        Class currentClass = AST.getSymbolTable().getCurrentClass();
        Method currentMethod = symbolTable.getCurrentMethod();
        if (simpleVariableNode.getSimpleChainedNode() == null) {
            if (simpleVariableNode.getType() == null){
                if (currentClass != null) {
                    Attribute currentAttribute = currentClass.getAttributes().get(simpleVariableNode.getToken().getLexeme());
                    if (currentAttribute != null){
                        simpleVariableNode.setType(currentAttribute.getType());
                    }
                }
                if (simpleVariableNode.getType() == null) {
                    if (currentMethod != null) {
                        Variable currentVariable = currentMethod.getVariables().get(simpleVariableNode.getToken().getLexeme());
                        Parameter currentParameter = currentMethod.getParameters().get(simpleVariableNode.getToken().getLexeme());
                        if (currentVariable != null){
                            simpleVariableNode.setType(currentVariable.getType());
                        } else if (currentParameter != null){
                            simpleVariableNode.setType(currentParameter.getType());
                        }
                    }
                }
            }
            return simpleVariableNode.getType();
        } else {
            currentClass = symbolTable.getClasses().get(simpleVariableNode.getType().getLexeme());
            Token auxToken = simpleVariableNode.getSimpleChainedNode().getToken();
            Attribute attribute = currentClass.getAttributes().get(auxToken.getLexeme());
            if (attribute != null) {
                if (attribute.isPrivate()) {
                    throw new InvalidAttributeAccessException(auxToken.getRowIndex(), auxToken.getColumnIndex(), auxToken.getLexeme(), currentClass.getName());
                }
            }

            simpleVariableNode.getSimpleChainedNode().setClassName(currentClass.getName());
            return simpleVariableNode.getSimpleChainedNode().accept(this);
        }
    }

    /**
     * Comprueba el tipo requerido por el operador unario.
     *
     * @param unaryExpressionNode nodo que se comprueba
     * @return tipo del operando; los incrementos y decrementos rechazan literales directos
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     * @throws InvalidLiteralOperationException si se incrementa o decrementa directamente un literal
     */
    public Type visit(UnaryExpressionNode unaryExpressionNode) {
        List<TokenType> intOperators = List.of(TokenType.OPERATOR_SUB, TokenType.OPERATOR_ADD, TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC);
        if (unaryExpressionNode.getExpressionNode().getType() == null) {
            Type type = unaryExpressionNode.getExpressionNode().accept(this);
            unaryExpressionNode.getExpressionNode().setType(type);
        }
        if(intOperators.contains(unaryExpressionNode.getUnaryOperatorToken().getTokenType())){
            if (unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.INT_CLASS &&
                    unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.INT_CONSTANT){
                throw new TypeMismatchException(unaryExpressionNode.getUnaryOperatorToken().getRowIndex(), unaryExpressionNode.getUnaryOperatorToken().getColumnIndex(), "Int", TokenDefinitions.getLexeme(unaryExpressionNode.getExpressionNode().getType().getTokenType()));
            }
            if ((unaryExpressionNode.getUnaryOperatorToken().getTokenType() == TokenType.OPERATOR_DEC || unaryExpressionNode.getUnaryOperatorToken().getTokenType() == TokenType.OPERATOR_INC)
            && unaryExpressionNode.getExpressionNode() instanceof LiteralNode) {
                throw new InvalidLiteralOperationException(((LiteralNode) unaryExpressionNode.getExpressionNode()).getToken().getRowIndex(), ((LiteralNode) unaryExpressionNode.getExpressionNode()).getToken().getColumnIndex(),
                        unaryExpressionNode.getUnaryOperatorToken().getLexeme());
            }
        } else if (unaryExpressionNode.getUnaryOperatorToken().getTokenType() == TokenType.OPERATOR_NOT) {
            if (unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.BOOLEAN_CONSTANT &&
                    unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.BOOL_CLASS) {
                throw new TypeMismatchException(unaryExpressionNode.getUnaryOperatorToken().getRowIndex(), unaryExpressionNode.getUnaryOperatorToken().getColumnIndex(),
                        "Bool", unaryExpressionNode.getExpressionNode().getType().getTypeName());
            }
        }
        return unaryExpressionNode.getExpressionNode().getType();
    }

    /**
     * Resuelve la variable o atributo y comprueba su encadenado en el contexto actual.
     *
     * @param variableNode nodo que se comprueba
     * @return tipo resuelto del acceso final, almacenado también en el nodo
     * @throws VariableRedeclarationInForLoopException si la resolución detecta una variable repetida en el ámbito del recorrido
     * @throws LocalVariableNotFoundException si la variable no se encuentra en el contexto local
     * @throws ClassNotFoundException si la clase requerida no está registrada
     * @throws MethodNotFoundException si el método requerido no pertenece a la clase de contexto
     * @throws AttributeNotFoundException si no se encuentra el atributo en la clase de contexto
     * @throws InvalidAttributeAccessException si el acceso al atributo privado no está permitido
     * @throws ASTException si no se puede resolver el acceso encadenado
     * @throws StaticMethodCalledFromInstanceException si se invoca un método estático mediante una instancia
     */
    public Type visit(VariableNode variableNode){
        if (variableNode.isChainedNode()
                && variableNode.getChainedNode() == null
                && variableNode.getType() != null) {

            return variableNode.getType();
        }

        Type type = null;
        if(variableNode.getClassName() == null) {
            Variable variable = AST.getSymbolTable().getStart().getVariables().get(variableNode.getToken().getLexeme());
            if (currentBlock != null) {
                Variable blockVariable = currentBlock.findVariable(variableNode.getToken().getLexeme());
                if (variable != null && blockVariable != null) {
                    throw new VariableRedeclarationInForLoopException(
                            variableNode.getToken().getRowIndex(),
                            variableNode.getToken().getColumnIndex(),
                            variableNode.getToken().getLexeme(),
                            variableNode.getMethodName()
                    );
                } else if (variable == null && blockVariable != null) {
                    variable = blockVariable;
                }
            }
            if (variable == null) {
                throw new LocalVariableNotFoundException(variableNode.getToken().getRowIndex(), variableNode.getToken().getColumnIndex(),
                        variableNode.getToken().getLexeme(), AST.getSymbolTable().getCurrentMethod().getName());
            }
            type = variable.getType();

            variableNode.setClassName(type.getLexeme());

            symbolTable.setCurrentClass(
                    symbolTable.getClasses().get(variableNode.getClassName())
            );
        }
        if (variableNode.getChainedNode() == null) {
            if (!AST.getSymbolTable().getClasses().containsKey(variableNode.getClassName())) {
                throw new ClassNotFoundException(variableNode.getClassName());
            }
            if(!variableNode.getMethodName().equals("start")) {
                if (!AST.getSymbolTable().getClasses().get(variableNode.getClassName()).getMethods().containsKey(variableNode.getMethodName())) {
                    if (!variableNode.getMethodName().equals(".")) {
                        throw new MethodNotFoundException(variableNode.getToken().getRowIndex(), variableNode.getToken().getColumnIndex(), symbolTable.getCurrentClass().getName(), variableNode.getMethodName());
                    }
                }
            } else {
                if (!variableNode.isChainedNode() && !AST.getSymbolTable().getStart().getVariables().containsKey(variableNode.getToken().getLexeme())) {
                    if (currentBlock != null && currentBlock.findVariable(variableNode.getToken().getLexeme()) == null) {
                        throw new LocalVariableNotFoundException(variableNode.getToken().getRowIndex(), variableNode.getToken().getColumnIndex(),
                                variableNode.getToken().getLexeme(), AST.getSymbolTable().getCurrentMethod().getName());
                    }
                }
            }
            if (predefinedClasses.contains(variableNode.getClassName())) {
                TokenDefinitions tokenDefinitions = new TokenDefinitions();
                if (!variableNode.getClassName().equals("Array")) {
                    type = new Type(tokenDefinitions.getPrimitiveTypes().get(variableNode.getClassName()), variableNode.getClassName());
                } else {
                    if (symbolTable.getCurrentMethod().getVariables().get(variableNode.getToken().getLexeme()) != null) {
                        type = symbolTable.getCurrentMethod().getVariables().get(variableNode.getToken().getLexeme()).getType();
                    } else if (symbolTable.getCurrentMethod().getParameters().get(variableNode.getToken().getLexeme()) != null) {
                        type = symbolTable.getCurrentMethod().getParameters().get(variableNode.getToken().getLexeme()).getType();
                    } else {
                        type = symbolTable.getCurrentClass().getAttributes().get(variableNode.getToken().getLexeme()).getType();
                    }
                }

            } else {

                Class currentClass = AST.getSymbolTable().getClasses().get(variableNode.getClassName());

                if (currentClass != null){
                    Method currentMethod = variableNode.getMethodName().equals(".")
                            ? currentClass.getConstructor()
                            : currentClass.getMethods().get(variableNode.getMethodName());
                    Attribute attribute = currentClass.getAttributes().get(variableNode.getToken().getLexeme());

                    if (currentMethod != null){
                        if (currentMethod.getParameters().containsKey(variableNode.getToken().getLexeme())){
                            if (currentBlock != null && currentBlock.findVariable(variableNode.getToken().getLexeme()) != null) {
                                throw new VariableRedeclarationInForLoopException(
                                        variableNode.getToken().getRowIndex(),
                                        variableNode.getToken().getColumnIndex(),
                                        variableNode.getToken().getLexeme(),
                                        variableNode.getMethodName()
                                );
                            }
                            type = currentMethod.getParameters().get(variableNode.getToken().getLexeme()).getType();
                        } else if (currentMethod.getVariables().containsKey(variableNode.getToken().getLexeme())){
                            if (currentBlock != null && currentBlock.findVariable(variableNode.getToken().getLexeme()) != null) {
                                throw new VariableRedeclarationInForLoopException(
                                        variableNode.getToken().getRowIndex(),
                                        variableNode.getToken().getColumnIndex(),
                                        variableNode.getToken().getLexeme(),
                                        variableNode.getMethodName()
                                );
                            }
                            type = currentMethod.getVariables().get(variableNode.getToken().getLexeme()).getType();
                        } else {
                            if (currentBlock != null) {
                                Variable blockVariable = currentBlock.findVariable(variableNode.getToken().getLexeme());
                                if (blockVariable != null) {
                                    type = blockVariable.getType();
                                }
                            }
                            if (type == null) {
                                Attribute attr = currentClass.getAttributes().get(variableNode.getToken().getLexeme());
                                if (attr != null) {
                                    type = attr.getType();
                                }
                            }
                        }
                    } else if (attribute != null) {
                        type = attribute.getType();
                    }
                }

            }

        }else {
            ChainedNode chainedNode = variableNode.getChainedNode();
            chainedNode.setIsChainedNode(true);
            Token token = chainedNode.getToken();
            if (chainedNode instanceof VariableNode) {
                if (!AST.getSymbolTable().getClasses().get(variableNode.getClassName()).getAttributes().containsKey(token.getLexeme())) {
                    throw new AttributeNotFoundException(token.getLexeme(), variableNode.getClassName());
                }
                if (((VariableNode) chainedNode).getClassName() == null) {
                    Attribute attribute = symbolTable.getCurrentClass().searchAttribute(chainedNode.getToken().getLexeme());
                    Method method = symbolTable.getCurrentClass().getMethods().get(chainedNode.getToken().getLexeme());
                    if (attribute.isPrivate()) {
                        if (method == null || !method.getClassName().equals(attribute.getClassName())) {
                            throw new InvalidAttributeAccessException(token.getRowIndex(), token.getColumnIndex(), token.getLexeme(), attribute.getClassName());
                        }
                    }
                    String className;
                    if (attribute != null) {

                        Type attributeType = attribute.getType();

                        // Conservo el tipo completo del atributo
                        ((VariableNode) chainedNode).setType(attributeType);

                        // Obtengo el nombre base del tipo
                        className = attributeType.getLexeme();

                    } else if (method != null) {
                        className = method.getType().getTypeName();
                    } else {
                        throw new ASTException(1,1,"todo");
                    }
                    ((VariableNode) chainedNode).setClassName(className);
                    symbolTable.setCurrentClass(symbolTable.getClasses().get(className));
                    //((VariableNode) chainedNode).setClassName(variableNode.getClassName());
                }
            } else if (chainedNode instanceof ArrayNode) {
                if (!AST.getSymbolTable().getClasses().get(variableNode.getClassName()).getAttributes().containsKey(token.getLexeme())) {
                    throw new AttributeNotFoundException(token.getLexeme(), variableNode.getClassName());
                }
                if(((ArrayNode) chainedNode).getClassName() == null) {
                    ((ArrayNode) chainedNode).setClassName(variableNode.getClassName());
                }
            } else {
                Class currentClass = AST.getSymbolTable().getClasses().get(variableNode.getClassName());
                Method chainedMethod = currentClass.getMethods().get(token.getLexeme());
                if (chainedMethod == null) {
                    throw new MethodNotFoundException(token.getRowIndex(), token.getColumnIndex(), currentClass.getName(), token.getLexeme());
                }
                if (variableNode.getToken().getTokenType() == TokenType.IDENTIFIER_VAR_METHOD && chainedMethod.isStatic()) {
                    throw new StaticMethodCalledFromInstanceException(token.getRowIndex(), token.getColumnIndex(), chainedMethod.getName(), currentClass.getName());
                }
                /*
                String variableType = symbolTable.getCurrentClass().getAttributes().get(variableNode.getToken().getLexeme()).getType().getLexeme();
                symbolTable.setCurrentClass(
                        symbolTable.getClasses().get(variableType));*/
            }
            type = chainedNode.accept(this);
        }
        variableNode.setType(type);
        return type;
    }

    // #################################################################
    // #               Visitor type de sentencias                      #
    // #################################################################

    /**
     * Comprueba la compatibilidad entre el destino y el valor de la asignación.
     *
     * @param assignmentNode nodo que se comprueba
     * @return tipo del destino de la asignación
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     */
    @Override
    public Type visit(AssignmentNode assignmentNode) {
        Type leftNodeType = assignmentNode.getSimpleAccessNode().accept(this);
        assignmentNode.getSimpleAccessNode().setType(leftNodeType);
        Type rightNodeType = assignmentNode.getExpressionNode().accept(this);
        assignmentNode.getExpressionNode().setType(rightNodeType);

        if(!Type.areCompatible(leftNodeType, rightNodeType)) {
            Token token = assignmentNode.getSimpleAccessNode().getToken();
            throw new TypeMismatchException(token.getRowIndex(),token.getColumnIndex(),
                    leftNodeType.getTypeName(), rightNodeType.getTypeName());
        }
        return leftNodeType;
    }

    /**
     * Establece el contexto del bloque, visita sus sentencias y propaga sus retornos.
     *
     * @param blockNode nodo que se comprueba
     * @return {@code null}; los resultados del chequeo quedan en los nodos
     * @throws MissingReturnException si un bloque raíz de método no {@code void} no tiene marca de retorno
     */
    public Type visit(BlockNode blockNode) {
        if (blockNode.isMethod()) {
            blockIndex = 0;
            currentBlock = null;
            symbolTable.setCurrentBlock(null);
        }
        Class currentClass = blockNode.getClassName() != null ? symbolTable.getClasses().get(blockNode.getClassName()) : null;
        symbolTable.setCurrentClass(currentClass);
        if (currentClass != null) {
            if (blockNode.getMethodName().equals(".")) {
                symbolTable.setCurrentMethod(currentClass.getConstructor());
            } else {
                symbolTable.setCurrentMethod(currentClass.getMethods().get(blockNode.getMethodName()));
            }

        } else {
            if (blockNode.getMethodName().equals("start")) {
                symbolTable.setCurrentMethod(symbolTable.getStart());
            }
        }
        for (SentenceNode sentence : blockNode.getSentences()) {
            if (sentence != null) {
                sentence.accept(this);
                blockNode.setHasReturn(blockNode.isHasReturn() || sentence.isHasReturn());
            }
        }
        if (symbolTable.getCurrentMethod() != null) {
            if (!symbolTable.getCurrentMethod().getType().getLexeme().equals("void") && !symbolTable.getCurrentMethod().getName().equals(".")) {
                if (!blockNode.isHasReturn() && blockNode.isMethod()) {
                    throw new MissingReturnException(symbolTable.getCurrentMethod());
                }
            }
        }
        return null;
    }

    /**
     * Comprueba el arreglo y el tipo de iteración, y visita el cuerpo en su ámbito.
     *
     * @param forNode nodo que se comprueba
     * @return {@code null}; propaga al ciclo la marca de retorno del cuerpo
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     */
    public Type visit(ForNode forNode) {
        Type var1Type = forNode.getType();
        Type var2Type = forNode.getVarNode2().accept(this);

        if (!var2Type.getTokenType().equals(TokenType.ARRAY_CLASS)){
            throw new TypeMismatchException(
                    forNode.getVarNode2().getToken().getRowIndex(),
                    forNode.getVarNode2().getToken().getColumnIndex(),
                    "Array",
                    var2Type.getTypeName()
            );
        }

        if (!Type.areCompatible(var1Type, ((ArrayType) var2Type).getSubType())){
            throw new TypeMismatchException(
                    forNode.getVarNode1().getToken().getRowIndex(),
                    forNode.getVarNode1().getToken().getColumnIndex(),
                    ((ArrayType) var2Type).getSubType().getTypeName(),
                    var1Type.getTypeName()
            );
        }

        visitScopedSentence(forNode.getSentenceNode());
        forNode.setHasReturn(forNode.getSentenceNode() != null && forNode.getSentenceNode().isHasReturn());
        return null;
    }

    /**
     * Visita una sentencia en el siguiente ámbito registrado por el analizador.
     * Consume un índice de bloque y restaura el ámbito anterior incluso si
     * la visita lanza una excepción. Una sentencia vacía también consume su ámbito.
     *
     * @param sentence cuerpo o rama que se visita; puede ser {@code null}
     */
    private void visitScopedSentence(SentenceNode sentence) {
        Block savedBlock = currentBlock;
        currentBlock = symbolTable.getCurrentMethod().getBlocks().get(blockIndex++);
        symbolTable.setCurrentBlock(currentBlock);
        try {
            if (sentence != null) {
                sentence.accept(this);
            }
        } finally {
            currentBlock = savedBlock;
            symbolTable.setCurrentBlock(savedBlock);
        }
    }

    /**
     * Exige una condición booleana y comprueba cada rama en su ámbito.
     *
     * @param ifElseNode nodo que se comprueba
     * @return {@code null}; marca retorno sólo si ambas ramas lo tienen
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     */
    public Type visit(IfElseNode ifElseNode) {
        Type expressionType = ifElseNode.getExpression().accept(this);
        if(!expressionType.getTypeName().equals("Bool")) {
            throw new TypeMismatchException(ifElseNode.getToken().getRowIndex(), ifElseNode.getToken().getColumnIndex(), "Bool", expressionType.getTypeName());
        }
        visitScopedSentence(ifElseNode.getSentenceNodeIf());

        if (ifElseNode.getSentenceNodeElse() != null) {
            visitScopedSentence(ifElseNode.getSentenceNodeElse());
        }

        // Casos para verificar si el nodo tiene retorno
        // Caso A: Sólo hay nodo if
        if (ifElseNode.getSentenceNodeElse() == null) {
            //ifElseNode.setHasReturn(ifElseNode.getSentenceNodeIf().isHasReturn());
            // Debería ser siempre falso ya que si el método es solo un if con un return, solo retornará en caso de que se cumpla la condición
            ifElseNode.setHasReturn(false);
        } else {
            // Caso B: Tiene nodo if y else
            ifElseNode.setHasReturn((ifElseNode.getSentenceNodeIf() != null ?
                    ifElseNode.getSentenceNodeIf().isHasReturn() :
                    false)
                    && ifElseNode.getSentenceNodeElse().isHasReturn());
        }
        return null;
    }

    /**
     * Comprueba el retorno frente al método actual y marca la sentencia.
     *
     * @param returnNode nodo que se comprueba
     * @return tipo de la expresión devuelta, o {@code null} si no hay expresión
     * @throws InvalidReturnException si el retorno no es compatible con el tipo del método actual
     */
    public Type visit(ReturnNode returnNode) {
        Type returnType = returnNode.getExpression() != null ?
                returnNode.getExpression().accept(this) : null;
        if (!Type.areCompatible(returnType, symbolTable.getCurrentMethod().getType())) {
            throw new InvalidReturnException(returnNode.getToken(), symbolTable.getCurrentMethod(), returnType);
        }
        returnNode.setHasReturn(true);
        return returnType;
    }

    /**
     * @param sentenceNode nodo que se comprueba
     * @return tipo devuelto por la visita del nodo concreto
     */
    public Type visit(SentenceNode sentenceNode) {
        return sentenceNode.accept(this);
    }

    /**
     * Comprueba la expresión utilizada como sentencia.
     *
     * @param simpleSentenceNode nodo que se comprueba
     * @return tipo obtenido al visitar la expresión
     */
    public Type visit(SimpleSentenceNode simpleSentenceNode) {
        return simpleSentenceNode.getExpressionNode().accept(this);
    }

    /**
     * Exige una condición booleana y comprueba el cuerpo en su ámbito.
     *
     * @param whileNode nodo que se comprueba
     * @return {@code null}; propaga al ciclo la marca de retorno del cuerpo
     * @throws TypeMismatchException si los tipos no cumplen los requisitos de la operación
     */
    public Type visit(WhileNode whileNode) {
        Type expressionType = whileNode.getExpressionNode().accept(this);
        if(!expressionType.getTypeName().equals("Bool")) {
            throw new TypeMismatchException(whileNode.getToken().getRowIndex(), whileNode.getToken().getColumnIndex(), "Bool", expressionType.getTypeName());
        }
        visitScopedSentence(whileNode.getSentenceNode());
        whileNode.setHasReturn(whileNode.getSentenceNode() != null && whileNode.getSentenceNode().isHasReturn());
        return null;
    }

}
