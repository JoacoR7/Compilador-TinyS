package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.MultipleInheritanceException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.PrimitiveInheritanceException;
import uncuyo.compiladores.lexer.Token;

import java.util.*;

public class Class {

    private Token token;
    private Hashtable<String, Attribute> attributes = new Hashtable<>();
    private Hashtable<String, Method> methods = new Hashtable<>();
    private Constructor constructor = new Constructor();
    private String parent;
    private boolean foundClass;
    private boolean foundImpl;

    public Class() {
    }

    public Class(Token token) {
        this.token = token;
    }

    public Class(Token token, Hashtable<String, Attribute> attributes, Hashtable<String, Method> methods, Constructor constructor, String parent) {
        this.token = token;
        this.attributes = attributes;
        this.methods = methods;
        this.constructor = constructor;
        this.parent = parent;
    }

    public Hashtable<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(Hashtable<String, Attribute> attributes) {
        this.attributes = attributes;
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public boolean isFoundClass() {
        return foundClass;
    }

    public void setFoundClass(boolean foundClass) {
        this.foundClass = foundClass;
    }

    public boolean isFoundImpl() {
        return foundImpl;
    }

    public void setFoundImpl(boolean foundImpl) {
        this.foundImpl = foundImpl;
    }

    public int getLine() {
        return this.getToken().getRowIndex();
    }

    public int getColumn() {
        return this.getToken().getColumnIndex();
    }

    public String getName() {
        return this.getToken().getLexeme();
    }

    public Attribute searchAttribute(String attributeName){
        return attributes.getOrDefault(attributeName, null);
    }

    public void merge(Class newClassDeclaration) {
        mergeAttributes(newClassDeclaration);
        mergeMethods(newClassDeclaration);
        validateAndSetParent(newClassDeclaration);
    }

    private void mergeAttributes(Class newClassDeclaration) {
        for (Attribute attribute : newClassDeclaration.getAttributesInOrder()) {

            if (this.getAttributes().containsKey(attribute.getName())) {
                throw new SemanticException(
                        newClassDeclaration.getToken().getRowIndex(),
                        newClassDeclaration.getToken().getColumnIndex(),
                        "El atributo '" + attribute.getName() + "' ya esta definido para la clase '" + newClassDeclaration.getToken().getLexeme()
                                + "' en la linea " + this.getAttributes().get(attribute.getName()).getToken().getColumnIndex()
                );
            }
            this.insertAttribute(attribute);
        }
    }

    private void mergeMethods(Class newClassDeclaration) {
        for (Method method : newClassDeclaration.getMethodsInOrder()) {
            if (this.getMethods().containsKey(method.getName())) {
                throw new SemanticException(
                        method.getToken().getRowIndex(),
                        method.getToken().getColumnIndex(),
                        "El metodo '" + method.getName() + "' ya esta definido para la clase '" +
                                newClassDeclaration.getToken().getLexeme() + "' en la linea " +
                                this.getMethods().get(method.getName()).getToken().getRowIndex()
                );
            }
            this.insertMethod(method);
        }
        this.setFoundImpl(newClassDeclaration.isFoundImpl());
    }

    public void validateParent() {
        String newClassDeclarationParent = this.getParent();
        List<String> primitiveTypes = List.of("Int", "Bool", "Str");

        if (newClassDeclarationParent != null && primitiveTypes.contains(newClassDeclarationParent)) {
            throw new PrimitiveInheritanceException(this.getLine(),
                    this.getColumn(), newClassDeclarationParent);
        }
    }

    private void validateAndSetParent(Class newClassDeclaration) {
        String currentParent = this.getParent();
        String newClassDeclarationParent = newClassDeclaration.getParent();

        newClassDeclaration.validateParent();

        if (newClassDeclarationParent == null) {
            // Caso de implements
        } else if (currentParent == null) {
            this.setParent(newClassDeclarationParent);
            this.setToken(newClassDeclaration.getToken());
        } else {
            boolean differentParents = !currentParent.equals(newClassDeclarationParent);

            if (differentParents) {
                throw new MultipleInheritanceException(newClassDeclaration.getLine(), newClassDeclaration.getColumn(),
                        this.getName(), newClassDeclarationParent, currentParent, this.getLine());
            }
        }


    }

    public boolean attributeAlreadyExists(String attributeName){
        return this.searchAttribute(attributeName) != null;
    }

    public void insertMethod(Method newMethod){
        newMethod.setPosition(this.getMethods().size());
        this.getMethods().put(newMethod.getToken().getLexeme(), newMethod);
    }

    public void insertAttribute(Attribute newAttribute){
        newAttribute.setPosition(this.getAttributes().size());
        this.getAttributes().put(newAttribute.getToken().getLexeme(), newAttribute);
    }

    public List<Attribute> getAttributesInOrder() {
        List<Attribute> attributes = new ArrayList<>(this.getAttributes().values());
        attributes.sort(Comparator.comparing(Attribute::getPosition));
        return attributes;
    }

    public List<Method> getMethodsInOrder() {
        List<Method> methods = new ArrayList<>(this.getMethods().values());
        methods.sort(Comparator.comparing(Method::getPosition));
        return methods;
    }
}
