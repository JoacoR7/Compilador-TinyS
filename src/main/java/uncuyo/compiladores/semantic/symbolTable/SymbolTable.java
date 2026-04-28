package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.exceptions.semantic.symbolTableException.CircularInheritanceException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.ParentClassNotFoundException;
import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.*;

public class SymbolTable implements JsonSerializable {

    private LinkedHashMap<String, Class> classes;
    private Class currentClass;
    private Method currentMethod;
    private Method start;

    public SymbolTable() {
        this.classes = new LinkedHashMap<>();
        this.currentClass = null;
        this.currentMethod = null;
        this.start = null;
    }

    public SymbolTable(LinkedHashMap<String, Class> classes, Class currentClass, Method currentMethod, Method start) {
        this.classes = classes;
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        this.start = start;
    }

    public LinkedHashMap<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(LinkedHashMap<String, Class> classes) {
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

    public Class searchOriginalClass(String idclass) {
        return classes.getOrDefault(idclass, null);
    }

    public Class searchAndCopyClass(String idclass) {
        Class original = classes.getOrDefault(idclass, null);
        if (original != null) {
            original = new Class(original);
        }
        return original;
    }

    public void saveClass(Class newClass) {
        newClass.validateParent();
        classes.put(newClass.getToken().getLexeme(), newClass);
    }

    public void mergeClassDeclarations(Class existingClass) {
        existingClass.mergeDeclarations(this.currentClass);
    }

    public void mergeClassImplements(Class existingClass) {
        existingClass.mergeImplements(this.currentClass);
    }

    public void checkParentExistance(Class currentClass) {
        String parentName = currentClass.getParent();
        if(!parentName.equals("Object") && !classes.containsKey(parentName)) {
            throw new ParentClassNotFoundException(currentClass.getToken(), parentName);
        }
    }

    public void checkClasses() {
        for (Class currentClass : classes.values()) {
            checkParentExistance(currentClass);
            checkCircularInheritance(currentClass);
        }
    }

    public void checkCircularInheritance(Class currentClass) {
        validateInheritanceChain(currentClass, new HashSet<>());
    }

    private void validateInheritanceChain(Class currentClass, Set<String> visited) {
        String parentName = currentClass.getParent();

        if(parentName.equals("Object")) {
            currentClass.setCircularInheritanceChecked(true);
        } else if (!currentClass.isCircularInheritanceChecked()) {

            if (!visited.add(parentName)) {
                throw new CircularInheritanceException(currentClass.getToken(), parentName);
            }

            Class parentClass = classes.get(parentName);

            if (!parentClass.isCircularInheritanceChecked()) {
                validateInheritanceChain(parentClass, visited);
            }
            currentClass.setCircularInheritanceChecked(true);
        }
    }

    public void consolidate() {
        checkClasses();
    }

    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");
        int total = this.getClasses().size();
        if (total > 0 ){
            sb.append(innerIndent).append("\"classes\": [\n");

            int i = 0;

            for (Class c : this.getClasses().values()) {
                sb.append(c.toJson(innerIndent + "\t"));

                if (++i < total) sb.append(",");
                sb.append("\n");
            }
            sb.append(innerIndent).append("],\n");

        }else {
            sb.append(innerIndent).append("\"classes\": [],\n");
        }

        sb.append(innerIndent).append("\"startMethod\": \n");
        sb.append(this.getStart().toJson(innerIndent)).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }


}
