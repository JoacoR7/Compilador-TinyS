package uncuyo.compiladores.semantic.abstractSintaxTree;

import org.w3c.dom.Attr;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.*;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.AttributeNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.ClassNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.MethodNotFoundException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.VariableNotFoundException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenDefinitions;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.*;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.*;
import uncuyo.compiladores.semantic.symbolTable.*;
import uncuyo.compiladores.semantic.symbolTable.Class;

import java.util.ArrayList;
import java.util.List;

public class TypeVisitor implements Visitor<Type> {

    private final SymbolTable symbolTable = AST.getSymbolTable();

    private final List<TokenType> literalTokenTypes = List.of(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT,
            TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT);

    private final List<String> predefinedClasses = List.of("Int", "Str", "Bool", "Array");


    // #################################################################
    // #               Visitor type de expresiones                     #
    // #################################################################

    public Type visit(ArrayConstructorNode arrayConstructorNode) {
        return arrayConstructorNode.getType();
    }

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

    public Type visit(ChainedCallNode chainedCallNode) {
        Type type;
        Token chainedCallNodeToken = chainedCallNode.getToken();
        Method method = symbolTable.getCurrentClass().getMethodInHierarchy(chainedCallNodeToken.getLexeme(), symbolTable.getClasses());
        if (method == null) {
            throw new InvalidMethodAccessException(chainedCallNodeToken.getRowIndex(), chainedCallNodeToken.getColumnIndex(),
                    chainedCallNodeToken.getLexeme(), symbolTable.getCurrentClass().getName());
        }
        for(ExpressionNode expressionNode : chainedCallNode.getExpressionNodes()) {
            expressionNode.accept(this);
        }
        type = method.getType();
        if (chainedCallNode.getChainedNode() != null) {
            Class currentClass = symbolTable.getClasses().get(type.getLexeme());
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
            type = chainedCallNode.getChainedNode().accept(this);
        }
        chainedCallNode.setType(type);
        return type;
    }

    public Type visit(ChainedNode chainedNode) {
        return chainedNode.accept(this);
    }

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

    public Type visit(ConclassorCallNode conclassorCallNode) {
        return conclassorCallNode.accept(this);
    }

    public Type visit(ExpressionNode expressionNode) {
        return expressionNode.accept(this);
    }

    public Type visit(LiteralNode literalNode) {
        if (!literalTokenTypes.contains(literalNode.getType().getTokenType())) {
            throw new InvalidLiteralNodeException(literalNode.getToken().getRowIndex(), literalNode.getToken().getColumnIndex(),
                    "Se esperaba 'nil', constante entera, booleana o string");
        }
        return literalNode.getType();
    }

    public Type visit(OperandNode operandNode) {
        return operandNode.accept(this);
    }

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

    public Type visit(SelfNode selfNode) {

        Class currentClass = symbolTable.getCurrentClass();

        Type type = new Type(
                currentClass.getToken().getTokenType(),
                currentClass.getName()
        );

        if (selfNode.getChainedNode() == null) {
            selfNode.setType(type);
        } else {
            Token chainedNodeToken = selfNode.getChainedNode().getToken();
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
            type = selfNode.getChainedNode().accept(this);
            selfNode.setType(type);
        }
        return type;
    }

    public Type visit(SimpleAccessNode simpleAccessNode) {
        return simpleAccessNode.accept(this);
    }

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
        }

        Type attributeType = currentClass.getAttributes().get(attributeName).getType();
        simpleChainedNode.setType(attributeType);

        if (simpleChainedNode.getSimpleChainedNode() != null) {
            simpleChainedNode.getSimpleChainedNode().setClassName(attributeType.getLexeme());
            return simpleChainedNode.getSimpleChainedNode().accept(this);
        }

        return attributeType;

    }

    public Type visit(SimpleSelfNode simpleSelfNode) {
        if (simpleSelfNode.getSimpleChainedNode() == null) {
            return null;
        } else {
            return simpleSelfNode.getSimpleChainedNode().accept(this);
        }
    }

    public Type visit(SimpleVariableNode simpleVariableNode) {
        if (simpleVariableNode.getSimpleChainedNode() == null) {
            return simpleVariableNode.getType();
        } else {
            Class currentClass = symbolTable.getClasses().get(simpleVariableNode.getType().getLexeme());
            Token auxToken = simpleVariableNode.getSimpleChainedNode().getToken();
            Attribute attribute = currentClass.getAttributes().get(auxToken.getLexeme());
            if (attribute != null) {
                if (attribute.isPrivate()) {
                    throw new InvalidAttributeAccessException(auxToken.getRowIndex(), auxToken.getColumnIndex(), auxToken.getLexeme(), currentClass.getName());
                }
            } else {

            }
            simpleVariableNode.getSimpleChainedNode().setClassName(currentClass.getName());
            return simpleVariableNode.getSimpleChainedNode().accept(this);
        }
    }

    public Type visit(UnaryExpressionNode unaryExpressionNode) {
        List<TokenType> intOperators = List.of(TokenType.OPERATOR_SUB, TokenType.OPERATOR_ADD, TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC);
        if(intOperators.contains(unaryExpressionNode.getUnaryOperatorToken().getTokenType())){
            if (unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.INT_CLASS &&
                    unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.INT_CONSTANT){
                throw new TypeMismatchException(unaryExpressionNode.getUnaryOperatorToken().getRowIndex(), unaryExpressionNode.getUnaryOperatorToken().getColumnIndex(), "Int", TokenDefinitions.getLexeme(unaryExpressionNode.getExpressionNode().getType().getTokenType()));
            }
        } else if (unaryExpressionNode.getUnaryOperatorToken().getTokenType() == TokenType.OPERATOR_NOT) {
            if (unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.BOOLEAN_CONSTANT &&
                    unaryExpressionNode.getExpressionNode().getType().getTokenType() != TokenType.BOOL_CLASS) {
                throw new TypeMismatchException(unaryExpressionNode.getUnaryOperatorToken().getRowIndex(), unaryExpressionNode.getUnaryOperatorToken().getColumnIndex(),
                        "Bool", TokenDefinitions.getLexeme(unaryExpressionNode.getExpressionNode().getType().getTokenType()));
            }
        }
        return unaryExpressionNode.getExpressionNode().getType();
    }

    public Type visit(VariableNode variableNode){
        Type type = null;
        if(variableNode.getClassName() == null) {
            variableNode.setClassName(AST.getSymbolTable().getStart().getVariables().get(variableNode.getToken().getLexeme()).getType().getLexeme());
            symbolTable.setCurrentClass(symbolTable.getClasses().get(variableNode.getClassName()));
        }
        if (variableNode.getChainedNode() == null) {
            if (!AST.getSymbolTable().getClasses().containsKey(variableNode.getClassName())) {
                throw new ClassNotFoundException(variableNode.getClassName());
            }
            if(!variableNode.getMethodName().equals("start")) {
                if (!AST.getSymbolTable().getClasses().get(variableNode.getClassName()).getMethods().containsKey(variableNode.getMethodName())) {
                    if (!variableNode.getMethodName().equals(".")) {
                        throw new MethodNotFoundException(variableNode.getMethodName());
                    }
                }
            } else {
                if (!variableNode.isChainedNode() && !AST.getSymbolTable().getStart().getVariables().containsKey(variableNode.getToken().getLexeme())) {
                    throw new VariableNotFoundException(variableNode.getToken().getLexeme());
                }
            }
            if (predefinedClasses.contains(variableNode.getClassName())) {
                TokenDefinitions tokenDefinitions = new TokenDefinitions();
                type = new Type(tokenDefinitions.getPrimitiveTypes().get(variableNode.getClassName()), variableNode.getClassName());
            } else {

                Class currentClass = AST.getSymbolTable().getClasses().get(variableNode.getClassName());

                if (currentClass != null){
                    Method currentMethod = currentClass.getMethods().get(variableNode.getMethodName());
                    Attribute attribute = currentClass.getAttributes().get(variableNode.getToken().getLexeme());

                    if (currentMethod != null){
                        if (currentMethod.getParameters().containsKey(variableNode.getToken().getLexeme())){
                            type = currentMethod.getParameters().get(variableNode.getToken().getLexeme()).getType();
                        } else if (currentMethod.getVariables().containsKey(variableNode.getToken().getLexeme())){
                            type = currentMethod.getVariables().get(variableNode.getToken().getLexeme()).getType();
                        } else {
                            type = currentClass.getAttributes().get(variableNode.getToken().getLexeme()).getType();
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
                    ((VariableNode) chainedNode).setClassName(variableNode.getClassName());
                }
            } else if (chainedNode instanceof ArrayNode) {
                if (!AST.getSymbolTable().getClasses().get(variableNode.getClassName()).getAttributes().containsKey(token.getLexeme())) {
                    throw new AttributeNotFoundException(token.getLexeme(), variableNode.getClassName());
                }
                if(((ArrayNode) chainedNode).getClassName() == null) {
                    ((ArrayNode) chainedNode).setClassName(variableNode.getClassName());
                }
            } else {
                if (!AST.getSymbolTable().getClasses().get(variableNode.getClassName()).getMethods().containsKey(token.getLexeme())) {
                    throw new MethodNotFoundException(token.getLexeme());
                }
                String variableType = symbolTable.getCurrentClass().getAttributes().get(variableNode.getToken().getLexeme()).getType().getLexeme();
                symbolTable.setCurrentClass(
                        symbolTable.getClasses().get(variableType));
            }
            type = chainedNode.accept(this);
        }
        variableNode.setType(type);
        return type;
    }

    // #################################################################
    // #               Visitor type de sentencias                      #
    // #################################################################

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

    public Type visit(BlockNode blockNode) {
        Class currentClass = blockNode.getClassName() != null ? symbolTable.getClasses().get(blockNode.getClassName()) : null;
        symbolTable.setCurrentClass(currentClass);
        if (currentClass != null) {
            symbolTable.setCurrentMethod(currentClass.getMethods().get(blockNode.getMethodName()));
        } else {
            if (blockNode.getMethodName().equals("start")) {
                symbolTable.setCurrentMethod(symbolTable.getStart());
            }
        }
        for (SentenceNode sentence : blockNode.getSentences()) {
            sentence.accept(this);
        }
        return null;
    }

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

        forNode.getSentenceNode().accept(this);
        return null;
    }

    public Type visit(IfElseNode ifElseNode) {
        Type expressionType = ifElseNode.getExpression().accept(this);
        if(!expressionType.getTypeName().equals("Bool")) {
            throw new TypeMismatchException(ifElseNode.getToken().getRowIndex(), ifElseNode.getToken().getColumnIndex(), "Bool", expressionType.getTypeName());
        }
        ifElseNode.getSentenceNodeIf().accept(this);
        if (ifElseNode.getSentenceNodeElse() != null) {
            ifElseNode.getSentenceNodeElse().accept(this);
        }
        return null;
    }

    public Type visit(ReturnNode returnNode) {
        Type returnType = returnNode.getExpression() != null ?
                returnNode.getExpression().accept(this) : null;
        if (!Type.areCompatible(returnType, symbolTable.getCurrentMethod().getType())) {
            throw new InvalidReturnException(returnNode.getToken(), symbolTable.getCurrentMethod(), returnType);
        }
        return returnType;
    }

    public Type visit(SentenceNode sentenceNode) {
        return sentenceNode.accept(this);
    }

    public Type visit(SimpleSentenceNode simpleSentenceNode) {
        return simpleSentenceNode.getExpressionNode().accept(this);
    }

    public Type visit(WhileNode whileNode) {
        Type expressionType = whileNode.getExpressionNode().accept(this);
        if(!expressionType.getTypeName().equals("Bool")) {
            throw new TypeMismatchException(whileNode.getToken().getRowIndex(), whileNode.getToken().getColumnIndex(), "Bool", expressionType.getTypeName());
        }
        whileNode.getSentenceNode().accept(this);
        return null;
    }

}
