package uncuyo.compiladores.semantic;

import java.util.Hashtable;
import java.util.List;

public class Constructor {

    private Hashtable<String, Parameter> parameters;
    private Hashtable<String, Atribute> atributes;

    public Constructor() {
    }

    public Constructor(Hashtable<String, Parameter> parameters, Hashtable<String, Atribute> atributes) {
        this.parameters = parameters;
        this.atributes = atributes;
    }

    public Hashtable<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Hashtable<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public Hashtable<String, Atribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(Hashtable<String, Atribute> atributes) {
        this.atributes = atributes;
    }
}
