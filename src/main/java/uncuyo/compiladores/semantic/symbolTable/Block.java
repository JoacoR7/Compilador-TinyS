package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.LinkedHashMap;

/**
 * Ámbito de variables locales de un bloque dentro de un método.
 * Block representa el bloque de método, if, else, for y while.
 */
public class Block implements JsonSerializable {

    private LinkedHashMap<String, Variable> variables = new LinkedHashMap<>();
    private Block parent;
    private Method method;

    /**
     * Constructor de bloque
     *
     * @param method método al que pertenece el bloque
     */
    public Block(Method method) {
        this.method = method;
    }

    /**
     * Obtiene el map de variables declaradas en este bloque.
     *
     * @return variables indexadas por nombre y ordenadas por inserción
     */
    public LinkedHashMap<String, Variable> getVariables() {
        return variables;
    }

    /**
     * Reemplaza el map de variables del bloque sin copiarlo.
     *
     * @param variables nuevo map de variables
     */
    public void setVariables(LinkedHashMap<String, Variable> variables) {
        this.variables = variables;
    }

    /**
     * Establece el ámbito exterior utilizado para buscar variables.
     *
     * @param parent bloque padre, o {@code null} si no existe
     */
    public void setParent(Block parent) {
        this.parent = parent;
    }

    /**
     * Obtiene el método asociado al bloque.
     *
     * @return método propietario
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Reemplaza la referencia al método asociado al bloque.
     *
     * @param method nuevo método propietario
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * Busca una variable por nombre en este bloque y luego en sus padres.
     * La primera coincidencia encontrada determina la variable visible.
     *
     * @param variableName nombre de la variable que se busca
     * @return variable encontrada, o {@code null} si ningún bloque la contiene
     */
    public  Variable findVariable(String variableName) {
        Variable variable = variables.get(variableName);

        if (variable != null) {
            return variable;
        }

        if (parent != null) {
            return parent.findVariable(variableName);
        }

        return null;
    }

    /**
     * Exporta las variables de este bloque en orden de inserción.
     * Un bloque sin variables se representa con {@code "variables": []}.
     *
     * @param indent indentación inicial del objeto
     * @return representación JSON del bloque
     */
    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();
        String variableIndent = indent + "\t\t";

        sb.append(indent).append("{\n");

        if (variables.isEmpty()) {
            sb.append(indent).append("\t\"variables\": []\n");
        } else {
            sb.append(indent).append("\t\"variables\": [\n");
            int remaining = variables.size();
            for (Variable variable : variables.values()) {
                sb.append(variableIndent).append(variable.toJson(variableIndent));
                if (--remaining > 0) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append(indent).append("\t]\n");
        }

        sb.append(indent).append("}");
        return sb.toString();
    }
}
