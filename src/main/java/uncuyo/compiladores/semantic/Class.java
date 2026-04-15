package uncuyo.compiladores.semantic;

import java.util.Hashtable;
import java.util.List;

public class Class {

    private Hashtable<String, Atribute> atributes;
    private Hashtable<String, Method> methods;
    private Constructor constructor;
    private Class parent;

    public Class() {
    }

    public Class(Hashtable<String, Atribute> atributes, Hashtable<String, Method> methods, Constructor constructor, Class parent) {
        this.atributes = atributes;
        this.methods = methods;
        this.constructor = constructor;
        this.parent = parent;
    }

    public Hashtable<String, Atribute> getAtributes() {
        return atributes;
    }

    public void setAtributes(Hashtable<String, Atribute> atributes) {
        this.atributes = atributes;
    }

    public Hashtable<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(Hashtable<String, Method> methods) {
        this.methods = methods;
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    public Class getParent() {
        return parent;
    }

    public void setParent(Class parent) {
        this.parent = parent;
    }
}
