package uncuyo.compiladores.semantic;

import java.util.Hashtable;

public class Method {

    private Hashtable<String, Parameter> parameters;
    private Hashtable<String, Attribute> atributes;
    private Type returnType;
    private boolean isStatic;

    public Method() {
    }

    public Method(Hashtable<String, Parameter> parameters, Hashtable<String, Attribute> atributes, Type returnType, boolean isStatic) {
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

    public Hashtable<String, Attribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(Hashtable<String, Attribute> atributes) {
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
