package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.semantic.JsonSerializable;

/**
 * Base de los nodos del árbol sintáctico abstracto.
 * Permite aplicar operaciones mediante {@link Visitor} sin incorporarlas
 * a cada nodo. La exportación completa del programa utiliza
 * {@link AstJsonVisitor}, independientemente de los serializadores locales.
 */
public abstract class Node implements JsonSerializable {

    /**
     * Aplica el visitante mediante la sobrecarga correspondiente al nodo concreto.
     *
     * @param <T> tipo del resultado producido por el visitante
     * @param visitor operación que se aplica al nodo
     * @return resultado de la visita; su significado depende del visitante
     */
    public abstract <T> T accept(Visitor<T> visitor);

    /**
     * Obtiene la representación JSON local definida por el nodo concreto.
     * Algunas subclases todavía no implementan este serializador y devuelven
     * una cadena vacía. Para exportar el programa completo se utiliza
     * {@link AST#toJson(String)}.
     *
     * @param indent sangría inicial que se utiliza al construir el texto
     * @return texto producido por el serializador local del nodo
     */
    public abstract String toJson(String indent);
}
