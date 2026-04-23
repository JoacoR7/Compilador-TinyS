package uncuyo.compiladores.semantic.symbolTable;

import java.util.Hashtable;

public class Constructor {

    private Hashtable<String, Parameter> parameters;
    private Hashtable<String, Attribute> atributes;

    public Constructor() {
    }

    public Constructor(Hashtable<String, Parameter> parameters, Hashtable<String, Attribute> atributes) {
        this.parameters = parameters;
        this.atributes = atributes;
    }

    public Hashtable<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Hashtable<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Hashtable<String, Attribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(Hashtable<String, Attribute> atributes) {
        this.atributes = atributes;
    }
}
