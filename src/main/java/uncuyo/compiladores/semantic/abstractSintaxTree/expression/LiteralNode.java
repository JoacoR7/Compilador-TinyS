package uncuyo.compiladores.semantic.abstractSintaxTree.expression;

import uncuyo.compiladores.exceptions.semantic.abstractSyntaxTreeException.InvalidLiteralNodeException;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
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
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
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
