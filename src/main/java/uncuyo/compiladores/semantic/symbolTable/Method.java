package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.exceptions.semantic.symbolTableException.VariableAlreadyDefined;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.LinkedHashMap;
import java.util.LinkedList;

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

    public Method(Token token, Type type, boolean isStatic) {
        this.token = token;
        this.type = type;
        this.isStatic = isStatic;
    }

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

    public void insertParameter(Type type, String name) {
        int position = this.getParameters().size();
        this.getParameters().put(name, new Parameter(type, name, position));
    }

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
                .append(getClassName()).append(",\n");

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

            sb.append(indent).append("\t]\n");
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

            sb.append(indent).append("\t]\n");
        }else {
            sb.append(indent).append("\t\"variables\": []\n");
        }

        sb.append(indent).append("}");

        return sb.toString();
    }
}
