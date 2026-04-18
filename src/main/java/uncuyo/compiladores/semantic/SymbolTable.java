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
            mergeClass(auxClass, newClass);
        }
        return auxClass;
    }

    private void insertNewClass(Class newClass) {
        classes.put(newClass.getToken().getLexeme(), newClass);
    }

    private void mergeClass(Class auxClass, Class newClass) {
        mergeAttributes(auxClass, newClass);
        mergeMethods(auxClass, newClass);
        validateAndSetParent(auxClass, newClass);
    }

    private void mergeAttributes(Class auxClass, Class newClass) {
        for (Map.Entry<String, Attribute> entry : newClass.getAttributes().entrySet()) {

            if (auxClass.getAttributes().containsKey(entry.getKey())) {
                throw new SemanticException(
                        newClass.getToken().getRowIndex(),
                        newClass.getToken().getColumnIndex(),
                        "El atributo '" + entry.getKey() + "' ya esta definido"
                );
            }

            auxClass.getAttributes().put(entry.getKey(), entry.getValue());
        }
    }

    private void mergeMethods(Class auxClass, Class newClass) {
        for (Map.Entry<String, Method> entry : newClass.getMethods().entrySet()) {

            if (auxClass.getMethods().containsKey(entry.getKey())) { // <-- FIX
                throw new SemanticException(
                        newClass.getToken().getRowIndex(),
                        newClass.getToken().getColumnIndex(),
                        "El metodo '" + entry.getKey() + "' ya esta definido"
                );
            }

            auxClass.getMethods().put(entry.getKey(), entry.getValue());
        }
    }

    private void validateAndSetParent(Class auxClass, Class newClass) {
        String auxParent = auxClass.getParent();
        String newParent = newClass.getParent();

        boolean differentParents = !auxParent.equals(newParent);
        boolean noneIsObject = !auxParent.equals("Object") && !newParent.equals("Object");

        if (differentParents && noneIsObject) {
            throw new SemanticException(
                    newClass.getToken().getRowIndex(),
                    newClass.getToken().getColumnIndex(),
                    "La clase '" + newClass.getToken().getLexeme() +
                            "' no puede heredar de '" + newParent +
                            "' por que ya hereda de '" + auxParent + "'"
            );
        }

        if (!newParent.equals("Object")) {
            auxClass.setParent(newClass.getParent());
        }
    }

    public boolean attributeAlreadyExistsInCurrentClass(String attributeName){
        return this.currentClass.searchAttribute(attributeName) != null;
    }

    public void saveClassAttribute(String attributeName, Type type, boolean isPrivate){
        this.currentClass.saveAttribute(attributeName,type,isPrivate);
    }

}
