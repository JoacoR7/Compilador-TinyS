package uncuyo.compiladores.semantic;

import java.util.Hashtable;
import java.util.List;

public class Method {

    private Hashtable<String, Parameter> parameters;
    private Hashtable<String, Atribute> atributes;
    private Type returnType;
    private boolean isStatic;

    public Method() {
    }

    public Method(Hashtable<String, Parameter> parameters, Hashtable<String, Atribute> atributes, Type returnType, boolean isStatic) {
        this.parameters = parameters;
        this.atributes = atributes;
        this.returnType = returnType;
        this.isStatic = isStatic;
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

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }
}
