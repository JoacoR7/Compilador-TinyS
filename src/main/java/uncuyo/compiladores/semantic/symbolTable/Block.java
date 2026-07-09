package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.LinkedHashMap;

public class Block implements JsonSerializable {


    private LinkedHashMap<String, Variable> variables = new LinkedHashMap<>();
    private Block parent;
    private Method method;

    public Block(Method method) {
        this.method = method;
    }

    public LinkedHashMap<String, Variable> getVariables() {
        return variables;
    }

    public void setVariables(LinkedHashMap<String, Variable> variables) {
        this.variables = variables;
    }

    public Block getParent() {
        return parent;
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

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

    @Override
    public String toJson(String indent) {
        return "";
    }
}
