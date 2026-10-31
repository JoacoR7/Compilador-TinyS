package uncuyo.compiladores.semantic.abstractSintaxTree.expression;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.symbolTable.Type;



/**
 * Literal cuyo token conserva el lexema y la ubicación en el archivo fuente.
 * Al construirlo, su tipo se inicializa con la categoría y el lexema del
 * token. El visitante semántico comprueba que la categoría sea admitida.
 */
public class LiteralNode extends OperandNode{

    private Token token;

    /**
     * Crea el literal e inicializa su tipo con la categoría y el lexema del token.
     *
     * @param token el token del literal con su categoría, lexema y ubicación
     */
    public LiteralNode(Token token) {
        this.token = token;
        this.setType(new Type(this.token.getTokenType(), this.token.getLexeme()));
    }


    /**
     * Obtiene el token del literal con su categoría, lexema y ubicación.
     *
     * @return el token del literal con su categoría, lexema y ubicación
     */
    public Token getToken() {
        return token;
    }

    /**
     * Establece el token del literal con su categoría, lexema y ubicación.
     * No recalcula el tipo almacenado al construir el literal.
     *
     * @param token el token del literal con su categoría, lexema y ubicación
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    /**
     * Serializa los datos locales del nodo y los hijos incluidos por esta implementación.
     *
     * @param indent sangría inicial para el objeto y sus hijos
     * @return representación JSON local del nodo
     */
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
