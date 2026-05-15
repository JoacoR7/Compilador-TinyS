package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidLiteralNodeException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.symbolTable.Type;

import java.util.List;

public class LiteralNode extends OperandNode{

    private Token token;

    public LiteralNode(Token token) {
        this.token = token;
        this.setType(new Type(this.token.getTokenType(), this.token.getLexeme()));
    }


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public Type check() {
        List<TokenType> literalTokenTypes = List.of(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT,
                TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT);

        if(!literalTokenTypes.contains(this.getType().getTokenType())) {
            throw new InvalidLiteralNodeException(this.token.getRowIndex(), this.token.getColumnIndex(),
                    "Se esperaba 'nil', constante entera, booleana o string");
        }

        return this.getType();
    }

    @Override
    public String toJson(String indent) {
        String childIndent = indent + "\t";

        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(childIndent)
                .append("\"nodeType\": \"LiteralNode\",\n");

        sb.append(childIndent)
                .append("\"lexeme\": \"")
                .append(token.getLexeme())
                .append("\",\n");

        sb.append(childIndent)
                .append("\"tokenType\": \"")
                .append(token.getTokenType())
                .append("\"\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
