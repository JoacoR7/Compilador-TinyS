package uncuyo.compiladores.semantic;

import uncuyo.compiladores.exceptions.SemanticException;

import java.util.Hashtable;
import java.util.Map;

public class SymbolTable {

    private Hashtable<String, Class> classes;
    private Class currentClass;
    private Method currentMethod;
    private Method start;

    public SymbolTable() {
        this.classes = new Hashtable<>();
        this.currentClass = null;
        this.currentMethod = null;
        this.start = null;
    }

    public SymbolTable(Hashtable<String, Class> classes, Class currentClass, Method currentMethod, Method start) {
        this.classes = classes;
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        this.start = start;
    }

    public Hashtable<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(Hashtable<String, Class> classes) {
        this.classes = classes;
    }

    public Class getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(Class currentClass) {
        this.currentClass = currentClass;
    }

    public Method getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(Method currentMethod) {
        this.currentMethod = currentMethod;
    }

    public Method getStart() {
        return start;
    }

    public void setStart(Method start) {
        this.start = start;
    }

    public Class searchClass(String idclass){
        return classes.getOrDefault(idclass, null);
    }

    public Class saveClass(Class newClass) {
        Class auxClass = searchClass(newClass.getToken().getLexeme());

        if (auxClass == null) {
            insertNewClass(newClass);
            auxClass = newClass;
        } else {
            auxClass.merge(newClass);
        }
        return auxClass;
    }

    private void insertNewClass(Class newClass) {
        classes.put(newClass.getToken().getLexeme(), newClass);
    }

}
