package uncuyo.compiladores.semantic.abstractSintaxTree.sentence;

import java.util.LinkedList;

import uncuyo.compiladores.semantic.abstractSintaxTree.Visitor;

/**
 * Bloque con una lista ordenada de sentencias y su contexto de declaración.
 * La lista puede contener entradas {@code null} para sentencias vacías.
 * La marca {@code isMethod} distingue los bloques raíz de los anidados.
 */
public class BlockNode extends SentenceNode{
    private LinkedList<SentenceNode> sentences;
    private String className;
    private String methodName;
    private boolean isMethod;

    /**
     * Crea el nodo para un bloque de sentencias.
     *
     * @param sentences la lista de sentencias en orden, con {@code null} para las vacías
     * @param className el nombre de la clase propietaria, o {@code null} para {@code start}
     * @param methodName el nombre del método de contexto; {@code .} identifica al constructor
     * @param isMethod la marca de bloque raíz de método, constructor o {@code start}
     */
    public BlockNode(LinkedList<SentenceNode> sentences, String className, String methodName, boolean isMethod) {
        this.sentences = sentences;
        this.className = className;
        this.methodName = methodName;
        this.isMethod = isMethod;
    }

    /**
     * Obtiene la lista de sentencias en orden, con {@code null} para las vacías.
     *
     * @return la lista de sentencias en orden, con {@code null} para las vacías
     */
    public LinkedList<SentenceNode> getSentences() {
        return sentences;
    }

    /**
     * Establece la lista de sentencias en orden, con {@code null} para las vacías.
     *
     * @param sentences la lista de sentencias en orden, con {@code null} para las vacías
     */
    public void setSentences(LinkedList<SentenceNode> sentences) {
        this.sentences = sentences;
    }

    /**
     * Obtiene el nombre de la clase propietaria, o {@code null} para {@code start}.
     *
     * @return el nombre de la clase propietaria, o {@code null} para {@code start}
     */
    public String getClassName() {
        return className;
    }

    /**
     * Establece el nombre de la clase propietaria, o {@code null} para {@code start}.
     *
     * @param className el nombre de la clase propietaria, o {@code null} para {@code start}
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Obtiene el nombre del método de contexto; {@code .} identifica al constructor.
     *
     * @return el nombre del método de contexto; {@code .} identifica al constructor
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Establece el nombre del método de contexto; {@code .} identifica al constructor.
     *
     * @param methodName el nombre del método de contexto; {@code .} identifica al constructor
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Obtiene la marca de bloque raíz de método, constructor o {@code start}.
     *
     * @return la marca de bloque raíz de método, constructor o {@code start}
     */
    public boolean isMethod() {
        return isMethod;
    }

    /**
     * Establece la marca de bloque raíz de método, constructor o {@code start}.
     *
     * @param method la marca de bloque raíz de método, constructor o {@code start}
     */
    public void setMethod(boolean method) {
        isMethod = method;
    }

    /**
     * Serializa los datos locales del nodo y los hijos incluidos por esta implementación.

     * @param indent sangría inicial para el objeto y sus hijos
     * @return representación JSON local del nodo
     */
    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");

        if (isMethod) {

            sb.append(innerIndent)
                    .append("\"className\": \"")
                    .append(className)
                    .append("\",\n");

            sb.append(innerIndent)
                    .append("\"methodName\": \"")
                    .append(methodName)
                    .append("\",\n");

        }




        if (sentences != null && !sentences.isEmpty()) {

            sb.append(innerIndent)
                    .append("\"sentences\": [\n");

            int total = sentences.size();
            int i = 0;

            for (SentenceNode sentence : sentences) {

                sb.append(sentence.toJson(innerIndent + "\t"));

                if (++i < total) {
                    sb.append(",");
                }

                sb.append("\n");
            }
            sb.append(innerIndent).append("]\n");
        } else {
            sb.append(innerIndent)
                    .append("\"sentences\": []\n");
        }

        sb.append(indent).append("}");

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }
}
