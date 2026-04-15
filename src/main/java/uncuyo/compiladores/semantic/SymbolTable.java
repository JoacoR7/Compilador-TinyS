package uncuyo.compiladores.semantic;

import java.util.Hashtable;

public class SymbolTable {

    private Hashtable<String, Class> classes;
    private Class currentClass;
    private Method currentMethod;
    private Method start;
}
