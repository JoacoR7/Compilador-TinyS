package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.ASTException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidLiteralNodeException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidReturnException;
import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.TypeMismatchException;
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
        return null;
    }

    public Type visit(ArrayNode arrayNode) {

        return null;
    }

    public Type visit(BinaryExpressionNode binaryExpressionNode) {
        Type type;
        Type leftNodeType = binaryExpressionNode.getLeftNode().accept(this);
        Type rightNodeType = binaryExpressionNode.getRightNode().accept(this);
        Token binaryOperationToken = binaryExpressionNode.getBinaryOperatorToken();
        List<TokenType> intTokenTypes = List.of(TokenType.INT_CLASS, TokenType.INT_CONSTANT);
        if (!intTokenTypes.contains(leftNodeType.getTokenType())){
            throw new TypeMismatchException(binaryOperationToken.getRowIndex(),
                    binaryOperationToken.getColumnIndex(),
                    "Int", binaryExpressionNode.getLeftNode().getType().getTokenType().toString());
        }
        if (!intTokenTypes.contains(rightNodeType.getTokenType())){
            throw new TypeMismatchException(binaryOperationToken.getRowIndex(), binaryOperationToken.getColumnIndex(),
                    "Int", binaryExpressionNode.getRightNode().getType().getTokenType().toString());
        }

        if (leftNodeType == rightNodeType) {
            binaryExpressionNode.setType(leftNodeType);
            type = leftNodeType;
        } else {
            if(leftNodeType.getTokenType() == TokenType.INT_CLASS) {
                binaryExpressionNode.setType(leftNodeType);
                type = leftNodeType;
            } else {
                binaryExpressionNode.setType(rightNodeType);
                type = rightNodeType;
            }
        }

        return type;
    }

    public Type visit(ChainedCallNode chainedCallNode) {
        return null;
    }

    public Type visit(ChainedNode chainedNode) {
        return null;
    }

    public Type visit(ClassConstructorNode classConstructorNode) {
        return classConstructorNode.getType();
    }

    public Type visit(ConclassorCallNode conclassorCallNode) {
        return null;
    }

    public Type visit(ExpressionNode expressionNode) {
        return null;
    }

    public Type visit(LiteralNode literalNode) {
        if (!literalTokenTypes.contains(literalNode.getType().getTokenType())) {
            throw new InvalidLiteralNodeException(literalNode.getToken().getRowIndex(), literalNode.getToken().getColumnIndex(),
                    "Se esperaba 'nil', constante entera, booleana o string");
        }
        return literalNode.getType();
    }

    public Type visit(OperandNode operandNode) {
        return null;
    }

    public Type visit(ParenthesizedExpressionNode parenthesizedExpressionNode) {
        return null;
    }

    public Type visit(SelfNode selfNode) {
        selfNode.setType(selfNode.getChainedNode().accept(this));
        return selfNode.getType();
    }

    public Type visit(SimpleAccessNode simpleAccessNode) {
        return simpleAccessNode.accept(this);
    }

    public Type visit(SimpleArrayNode simpleArrayNode) {
        return null;
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
        // hijo.getPadre().getMascota().edad
        // persona
        if (simpleVariableNode.getSimpleChainedNode() == null) {
            return simpleVariableNode.getType();
        } else {
            Class currentClass = symbolTable.getClasses().get(simpleVariableNode.getType().getLexeme());

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
        }
        if (variableNode.getChainedNode() == null) {
            if (!AST.getSymbolTable().getClasses().containsKey(variableNode.getClassName())) {
                throw new ClassNotFoundException(variableNode.getClassName());
            }
            if(!variableNode.getMethodName().equals("start")) {
                if (!AST.getSymbolTable().getClasses().get(variableNode.getClassName()).getMethods().containsKey(variableNode.getMethodName())) {
                    throw new MethodNotFoundException(variableNode.getMethodName());
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

                    if (currentMethod != null){
                        if (currentMethod.getParameters().containsKey(variableNode.getToken().getLexeme())){
                            type = currentMethod.getParameters().get(variableNode.getToken().getLexeme()).getType();
                        } else if (currentMethod.getVariables().containsKey(variableNode.getToken().getLexeme())){
                            type = currentMethod.getVariables().get(variableNode.getToken().getLexeme()).getType();
                        } else {
                            type = currentClass.getAttributes().get(variableNode.getToken().getLexeme()).getType();
                        }
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
                    leftNodeType.getLexeme(), rightNodeType.getTokenType().toString());
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
        return null;
    }

    public Type visit(IfElseNode ifElseNode) {
        Type expressionType = ifElseNode.getExpression().accept(this);
        if(!expressionType.getTypeName().equals("Bool")) {
            throw new TypeMismatchException(ifElseNode.getToken().getRowIndex(), ifElseNode.getToken().getColumnIndex(), "Bool", expressionType.getTypeName());
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
        return null;
    }

    public Type visit(SimpleSentenceNode simpleSentenceNode) {
        return null;
    }

    public Type visit(WhileNode whileNode) {
        return null;
    }


    // #################################################################
    // #                     Funciones auxiliares                      #
    // #################################################################


}
