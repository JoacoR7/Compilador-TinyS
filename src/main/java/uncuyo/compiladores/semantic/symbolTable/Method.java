package uncuyo.compiladores.semantic.symbolTable;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import uncuyo.compiladores.exceptions.semantic.symbolTableException.VariableAlreadyDefined;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

/**
 *
 * <p>Guarda tipo, token, posición y clase asociada, además de
 * maps ordenados de parámetros y variables locales.
 */
public class Method implements JsonSerializable {

    private Token token;
    private LinkedHashMap<String, Parameter> parameters = new LinkedHashMap<>();
    private LinkedHashMap<String, Variable> variables = new LinkedHashMap<>();
    private LinkedList<Block> blocks = new LinkedList<>();
    private Type type;
    private boolean isStatic;
    private String className;
    private int position;
    private boolean isOverride = false;

    /**
     * Crea un método con maps de parámetros y variables vacíos.
     *
     * @param token token del nombre; para un constructor sería "."
     * @param type tipo de retorno, o tipo de la clase para un constructor
     * @param isStatic indica si el método es estático
     */
    public Method(Token token, Type type, boolean isStatic) {
        this.token = token;
        this.type = type;
        this.isStatic = isStatic;
    }

    /**
     * Copia un método para incorporarlo como método heredado.
     * Las colecciones son nuevas, pero sus parámetros, variables y bloques,
     * así como el token y el tipo, se comparten con el método original.
     *
     * @param other método que se copiará
     */
    public Method(Method other) {
        this.token = other.token;
        this.parameters = new LinkedHashMap<>(other.parameters);
        this.variables = new LinkedHashMap<>(other.variables);
        this.blocks = new LinkedList<>(other.blocks);
        this.type = other.type;
        this.isStatic = other.isStatic;
        this.className = other.className;
        this.position = other.position;
        this.isOverride = other.isOverride;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public LinkedHashMap<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public LinkedHashMap<String, Variable> getVariables() {
        return variables;
    }

    public void setVariables(LinkedHashMap<String, Variable> variables) {
        this.variables = variables;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return token.getLexeme();
    }

    public boolean isOverride() {
        return isOverride;
    }

    public void setOverride(boolean override) {
        isOverride = override;
    }

    public LinkedList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(LinkedList<Block> blocks) {
        this.blocks = blocks;
    }

    /**
     * Registra un parámetro en la siguiente posición, comenzando en cero.
     *
     * @param type tipo declarado del parámetro
     * @param name nombre del parámetro
     */
    public void insertParameter(Type type, String name) {
        int position = this.getParameters().size();
        this.getParameters().put(name, new Parameter(type, name, position));
    }

    /**
     * Registra una variable local y le asigna la siguiente posición desde cero.
     * Los nombres de variables locales no pueden repetirse ni coincidir con los
     * parámetros del mismo método.
     *
     * @param newVariable variable que se incorporará
     * @throws VariableAlreadyDefined si el nombre ya existe como parámetro o variable local
     */
    public void insertVariable(Variable newVariable) {
        if (this.getParameters().containsKey(newVariable.getName())){
            throw new VariableAlreadyDefined(newVariable.getToken().getRowIndex(),
                    newVariable.getToken().getColumnIndex(),
                    newVariable.getName(),
                    this.getName());
        }
        if (this.getVariables().containsKey(newVariable.getName())) {
            throw new VariableAlreadyDefined(newVariable.getToken().getRowIndex(),
                    newVariable.getToken().getColumnIndex(),
                    newVariable.getName(),
                    this.getName());
        }
        newVariable.setPosition(this.getVariables().size());
        this.getVariables().put(newVariable.getName(), newVariable);
    }

    /**
     * Exporta la firma, los parámetros, las variables locales y los bloques del método.
     * Los bloques se conservan en el orden en que se reconocen sus ámbitos.
     * El tipo de retorno de un arreglo incluye su tipo de elemento; los cuerpos
     * de sentencias se exportan desde el AST.
     *
     * @param indent indentación inicial
     * @return representación JSON de la entrada
     */
    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getToken().getLexeme()).append("\",\n");

        if (getType() instanceof ArrayType) {
            sb.append(indent).append("\t\"type\": \"")
                    .append(getType().getLexeme()).append(" ").
                    append(((ArrayType) getType()).getSubType().getLexeme()).append("\",\n");
        } else {
            sb.append(indent).append("\t\"type\": \"")
                    .append(getType().getLexeme()).append("\",\n");
        }

        sb.append(indent).append("\t\"static\": ")
                .append(isStatic()).append(",\n");

        sb.append(indent).append("\t\"className\": ")
                .append(getClassName() == null ? "null" : "\"" + getClassName() + "\"")
                .append(",\n");

        sb.append(indent).append("\t\"position\": ")
                .append(getPosition()).append(",\n");

        int parameterSize = this.getParameters().size() - 1;
        String methodObjectsIndent = indent + "\t\t";

        if (parameterSize >= 0){
            sb.append(indent).append("\t\"parameters\": [\n");

            for (Parameter parameter : getParameters().values()) {
                if (parameterSize != parameter.getPosition()) {
                    sb.append(methodObjectsIndent)
                            .append(parameter.toJson(methodObjectsIndent))
                            .append(",\n");
                } else {
                    sb.append(methodObjectsIndent)
                            .append(parameter.toJson(methodObjectsIndent))
                            .append("\n");
                }
            }

            sb.append(indent).append("\t],\n");
        }else {
            sb.append(indent).append("\t\"parameters\": [],\n");
        }

        int variableSize = this.getVariables().size() - 1;

        if (variableSize >= 0){
            sb.append(indent).append("\t\"variables\": [\n");
            for (Variable variable : getVariables().values()) {
                if (variableSize != variable.getPosition()) {
                    sb.append(methodObjectsIndent)
                            .append(variable.toJson(methodObjectsIndent))
                            .append(",\n");
                } else {
                    sb.append(methodObjectsIndent)
                            .append(variable.toJson(methodObjectsIndent))
                            .append("\n");
                }
            }

            sb.append(indent).append("\t],\n");
        }else {
            sb.append(indent).append("\t\"variables\": [],\n");
        }

        if (blocks.isEmpty()) {
            sb.append(indent).append("\t\"blocks\": []\n");
        } else {
            sb.append(indent).append("\t\"blocks\": [\n");
            int remaining = blocks.size();
            for (Block block : blocks) {
                sb.append(block.toJson(methodObjectsIndent));
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
