package uncuyo.compiladores.semantic.symbolTable;

import java.util.Hashtable;
import java.util.LinkedHashMap;

public class Constructor {

    private LinkedHashMap<String, Parameter> parameters;
    private LinkedHashMap<String, Attribute> atributes;

    public Constructor() {
    }

    public Constructor(LinkedHashMap<String, Parameter> parameters, LinkedHashMap<String, Attribute> atributes) {
        this.parameters = parameters;
        this.atributes = atributes;
    }

    public LinkedHashMap<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(LinkedHashMap<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public LinkedHashMap<String, Attribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(LinkedHashMap<String, Attribute> atributes) {
        this.atributes = atributes;
    }
}
