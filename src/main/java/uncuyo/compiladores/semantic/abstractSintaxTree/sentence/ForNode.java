package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.VariableNode;
import uncuyo.compiladores.semantic.symbolTable.Type;

/**
 * Recorrido {@code for} con variable de iteración, arreglo y cuerpo.
 * Almacena el tipo declarado de la variable y el nodo del arreglo recorrido.
 */
public class ForNode extends SentenceNode {
    private Type type;
    private VariableNode varNode1;
    private VariableNode varNode2;
    private SentenceNode sentenceNode;

    /**
     * Crea el nodo para un recorrido {@code for}.
     *
     * @param type el tipo declarado de la variable de iteración
     * @param varNode1 la variable de iteración del {@code for}
     * @param varNode2 la referencia al arreglo recorrido por el {@code for}
     * @param sentenceNode el cuerpo del ciclo, o {@code null} si está vacío
     */
    public ForNode(Type type, VariableNode varNode1, VariableNode varNode2, SentenceNode sentenceNode) {
        this.type = type;
        this.varNode1 = varNode1;
        this.varNode2 = varNode2;
        this.sentenceNode = sentenceNode;
    }

    /**
     * Obtiene el tipo declarado de la variable de iteración.
     *
     * @return el tipo declarado de la variable de iteración
     */
    public Type getType() {
        return type;
    }

    /**
     * Establece el tipo declarado de la variable de iteración.
     *
     * @param type el tipo declarado de la variable de iteración
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Obtiene la variable de iteración del {@code for}.
     *
     * @return la variable de iteración del {@code for}
     */
    public VariableNode getVarNode1() {
        return varNode1;
    }

    /**
     * Establece la variable de iteración del {@code for}.
     *
     * @param varNode1 la variable de iteración del {@code for}
     */
    public void setVarNode1(VariableNode varNode1) {
        this.varNode1 = varNode1;
    }

    /**
     * Obtiene la referencia al arreglo recorrido por el {@code for}.
     *
     * @return la referencia al arreglo recorrido por el {@code for}
     */
    public VariableNode getVarNode2() {
        return varNode2;
    }

    /**
     * Establece la referencia al arreglo recorrido por el {@code for}.
     *
     * @param varNode2 la referencia al arreglo recorrido por el {@code for}
     */
    public void setVarNode2(VariableNode varNode2) {
        this.varNode2 = varNode2;
    }

    /**
     * Obtiene el cuerpo del ciclo, o {@code null} si está vacío.
     *
     * @return el cuerpo del ciclo, o {@code null} si está vacío
     */
    public SentenceNode getSentenceNode() {
        return sentenceNode;
    }

    /**
     * Establece el cuerpo del ciclo, o {@code null} si está vacío.
     *
     * @param sentenceNode el cuerpo del ciclo, o {@code null} si está vacío
     */
    public void setSentenceNode(SentenceNode sentenceNode) {
        this.sentenceNode = sentenceNode;
    }


    /**
     * Serializador local pendiente de implementación.

     * @param indent sangría prevista para la representación local
     * @return una cadena vacía
     */
    @Override
    public String toJson(String indent) {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
