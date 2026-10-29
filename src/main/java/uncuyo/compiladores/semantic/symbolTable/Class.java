package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.*;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonSerializable;

import java.util.*;

/**
 * Entrada de una clase TinyS que reúne sus declaraciones e implementaciones.
 *
 * <p>Conserva el token del nombre, el nombre del padre,
 * los miembros y los flags de consolidación. El constructor se representa
 * con {@link Method} y se almacena separado del map de métodos. Los maps
 * son mutables y mantienen el orden de inserción.</p>
 */
public class Class implements JsonSerializable {

    private Token token;
    private LinkedHashMap<String, Attribute> attributes = new LinkedHashMap<>();
    private LinkedHashMap<String, Method> methods = new LinkedHashMap<>();
    private Method constructor;
    private String parent;
    private boolean foundClass;
    private boolean foundImpl;
    private boolean circularInheritanceChecked;
    private boolean atributesConsolidationChecked;
    private boolean methodsConsolidationChecked;

    public Class() {
    }

    public Class(Token token) {
        this.token = token;
    }

    public Class(Token token, LinkedHashMap<String, Attribute> attributes, LinkedHashMap<String, Method> methods, String parent) {
        this.token = token;
        this.attributes = attributes;
        this.methods = methods;
        this.parent = parent;
    }

    /**
     * Método para copiar una clase, pero sin atributos ni métodos.
     *
     * @param other entrada original de la tabla de símbolos
     */
    public Class(Class other) {
        this.token = other.token;
        this.parent = other.parent;
        this.foundClass = other.foundClass;
        this.foundImpl = other.foundImpl;
        this.attributes = new LinkedHashMap<>();
        this.methods = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(LinkedHashMap<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public LinkedHashMap<String, Method> getMethods() {
        return methods;
    }

    public void setMethods(LinkedHashMap<String, Method> methods) {
        this.methods = methods;
    }

    public Method getConstructor() {
        return constructor;
    }

    public void setConstructor(Method constructor) {
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

    public boolean isCircularInheritanceChecked() {
        return circularInheritanceChecked;
    }

    public void setCircularInheritanceChecked(boolean circularInheritanceChecked) {
        this.circularInheritanceChecked = circularInheritanceChecked;
    }

    public boolean isAtributesConsolidationChecked() {
        return atributesConsolidationChecked;
    }

    public void setAtributesConsolidationChecked(boolean atributesConsolidationChecked) {
        this.atributesConsolidationChecked = atributesConsolidationChecked;
    }

    public boolean isMethodsConsolidationChecked() {
        return methodsConsolidationChecked;
    }

    public void setMethodsConsolidationChecked(boolean methodsConsolidationChecked) {
        this.methodsConsolidationChecked = methodsConsolidationChecked;
    }

    /**
     * Busca un atributo en los miembros actualmente registrados en la clase.
     *
     * @param attributeName nombre del atributo
     * @return atributo encontrado, o {@code null} si no está en el mapa
     */
    public Attribute searchAttribute(String attributeName){
        return attributes.getOrDefault(attributeName, null);
    }

    /**
     * Integra otra declaración {@code class} en esta entrada.
     * Primero incorpora sus atributos y luego valida el padre.
     *
     * @param newClassDeclaration parte reconocida para la misma clase
     * @throws SemanticException si se repite un atributo
     * @throws PrimitiveInheritanceException si el nuevo padre es un tipo prohibido
     * @throws MultipleInheritanceException si las partes declaran padres diferentes
     */
    public void mergeDeclarations(Class newClassDeclaration) {
        mergeAttributes(newClassDeclaration);
        validateAndSetParent(newClassDeclaration);
        this.setFoundClass(newClassDeclaration.isFoundClass());
    }

    /**
     * Integra el constructor y los métodos de otro {@code impl}.
     * Si este impl todavía no tiene constructor, toma el del nuevo impl.
     *
     * @param newClassDeclaration implementación reconocida para la misma clase
     * @throws ConstructorRedefinitionException si ambas partes tienen constructor
     * @throws SemanticException si un método nuevo ya está registrado
     */
    public void mergeImplements(Class newClassDeclaration) {
        if(newClassDeclaration.getConstructor() != null && this.getConstructor() != null){
            Token newClassDeclarationConstructorToken = newClassDeclaration.getConstructor().getToken();
            throw new ConstructorRedefinitionException(newClassDeclarationConstructorToken.getRowIndex(),
                    newClassDeclarationConstructorToken.getColumnIndex(), getName(), getConstructor().getToken());
        } else if (this.getConstructor() == null && newClassDeclaration.getConstructor() != null) {
            this.setConstructor(newClassDeclaration.getConstructor());
        }

        mergeMethods(newClassDeclaration);
        this.setFoundImpl(newClassDeclaration.isFoundImpl());
    }

    /**
     * Agrega atributos de otra declaración y rechaza los nombres ya registrados.
     *
     * @param newClassDeclaration declaración que aporta los atributos
     * @throws SemanticException si un atributo repite un nombre existente
     */
    private void mergeAttributes(Class newClassDeclaration) {
        for (Map.Entry<String, Attribute> entry : newClassDeclaration.getAttributes().entrySet()) {

            Attribute attribute = entry.getValue();

            if (this.getAttributes().containsKey(attribute.getName())) {
                throw new SemanticException(
                        attribute.getToken().getRowIndex(),
                        attribute.getToken().getColumnIndex(),
                        "El atributo '" + attribute.getName() +
                                "' ya esta definido en la clase '" +
                                this.getToken().getLexeme() + "'"
                );
            }

            this.insertAttribute(attribute);
        }
    }

    /**
     * Agrega métodos de otra implementación y rechaza los nombres ya registrados.
     *
     * @param newClassDeclaration declaración que aporta los métodos
     * @throws SemanticException si un método repite un nombre existente
     */
    private void mergeMethods(Class newClassDeclaration) {
        for (Map.Entry<String, Method> entry : newClassDeclaration.getMethods().entrySet()) {

            Method method = entry.getValue();

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
    }

    /**
     * Rechaza la herencia de {@code Int}, {@code Bool}, {@code Str} o {@code Array}.
     * Un padre {@code null} se admite mientras sólo se haya reconocido un {@code impl}.
     *
     * @throws PrimitiveInheritanceException si el padre pertenece a esos tipos
     */
    public void validateParent() {
        String newClassDeclarationParent = this.getParent();
        List<String> primitiveTypes = List.of("Int", "Bool", "Str", "Array");

        if (newClassDeclarationParent != null && primitiveTypes.contains(newClassDeclarationParent)) {
            throw new PrimitiveInheritanceException(this.getLine(),
                    this.getColumn(), newClassDeclarationParent);
        }
    }

    /**
     * Valida el padre de otra declaración y lo adopta si esta entrada todavía no tiene uno.
     * Cuando adopta el padre, también toma el token de la declaración recibida.
     * Si la nueva declaración no declara padre, conserva el estado actual.
     *
     * @param newClassDeclaration declaración cuyo padre se integrará
     * @throws PrimitiveInheritanceException si el padre está prohibido
     * @throws MultipleInheritanceException si difiere del padre ya registrado
     */
    public void validateAndSetParent(Class newClassDeclaration) {
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

    /**
     * Indica si el map actual contiene un atributo con el nombre solicitado.
     *
     * @param attributeName nombre que se consultará
     * @return {@code true} si ya está registrado
     */
    public boolean attributeAlreadyExists(String attributeName){
        return this.searchAttribute(attributeName) != null;
    }

    /**
     * Asigna al método la clase y una posición basada en el tamaño del map, y lo guarda.
     *
     * @param newMethod método que se registrará
     */
    public void insertMethod(Method newMethod){
        newMethod.setPosition(this.getMethods().size());
        newMethod.setClassName(this.getName());
        this.getMethods().put(newMethod.getToken().getLexeme(), newMethod);
    }

    /**
     * Asigna al atributo la clase y una posición basada en el tamaño del map, y lo guarda.
     *
     * @param newAttribute atributo que se registrará
     */
    public void insertAttribute(Attribute newAttribute){
        newAttribute.setPosition(this.getAttributes().size());
        newAttribute.setClassName(this.getName());
        this.getAttributes().put(newAttribute.getToken().getLexeme(), newAttribute);
    }

    /**
     * Rechaza la redeclaración de atributos del padre y agrega sus entradas heredadas.
     *
     * <p>Copia primero los atributos del padre, conservando sus posiciones y clase
     * de origen, y luego agrega los propios con posiciones consecutivas. Utiliza
     * los miembros disponibles en el padre sin consolidarlo recursivamente.</p>
     *
     * @param parentClass entrada del padre con los atributos que se heredarán
     * @throws InheritedAttributeRedeclaration si un atributo propio repite uno del padre
     */
    public void checkAndSetInheritedAttributes(Class parentClass) {
        LinkedHashMap<String, Attribute> parentAttributes = parentClass.getAttributes();
        LinkedHashMap<String, Attribute> currentAttributes = this.getAttributes();

        for (Attribute attribute : currentAttributes.values()) {
            if (parentAttributes.containsKey(attribute.getName())) {
                Attribute originalAttribute = parentAttributes.get(attribute.getName());
                throw new InheritedAttributeRedeclaration(
                        attribute.getToken(),
                        originalAttribute.getToken(),
                        this.getName(),
                        originalAttribute.getClassName()
                );
            }
        }

        LinkedHashMap<String, Attribute> newAttributeList = new LinkedHashMap<>();
        for (Attribute attribute : parentAttributes.values()) {
            Attribute inheritedAttribute = new Attribute(attribute);
            inheritedAttribute.setInherited(true);
            newAttributeList.put(attribute.getName(), inheritedAttribute);

        }

        for (Attribute attribute : currentAttributes.values()) {
            attribute.setPosition(newAttributeList.size());
            newAttributeList.put(attribute.getToken().getLexeme(), attribute);
        }

        this.setAttributes(newAttributeList);
        this.setAtributesConsolidationChecked(true);
    }

    /**
     * Comprueba las firmas coincidentes y agrega los métodos heredados.
     *
     * <p>Compara los lexemas de los tipos de retorno, la cantidad de parámetros y
     * los lexemas de sus tipos en orden. No compara el modificador estático ni
     * el subtipo de los arreglos. Copia los métodos del padre conservando sus
     * posiciones; ante una redefinición, marca esa copia y actualiza su clase
     * asociada, sin reemplazar sus parámetros o variables por los de la hija.</p>
     *
     * @param parentClass entrada del padre con los métodos que se heredarán
     * @throws InvalidOverrideException si difiere alguno de los componentes de firma comprobados
     */
    public void checkAndSetInheritedMethods(Class parentClass) {
        LinkedHashMap<String, Method> parentMethods = parentClass.getMethods();
        LinkedHashMap<String, Method> currentMethods = this.getMethods();

        for (Method method : currentMethods.values()) {
            if (parentMethods.containsKey(method.getName())) {
                Method originalMethod = parentMethods.get(method.getName());
                if (!originalMethod.getType().getLexeme().equals(method.getType().getLexeme())) {
                    String description = "El metodo '" + originalMethod.getName() + "' no puede retornar '" +
                            method.getType().getLexeme() + "', debe retornar '" + originalMethod.getType().getLexeme() + "'";
                    throw new InvalidOverrideException(method.getToken(), description);
                } else if (originalMethod.getParameters().size() != method.getParameters().size()) {
                    String description = "Se encontraron " + method.getParameters().size() + " parametros, se esperaban " +
                            originalMethod.getParameters().size() + " parametros";
                    throw new InvalidOverrideException(method.getToken(), description);
                } else {
                    List<Parameter> originalParameters = new ArrayList<>(originalMethod.getParameters().values());
                    List<Parameter> currentParameters = new ArrayList<>(method.getParameters().values());

                    for (int i = 0; i < originalParameters.size(); i++) {
                        Parameter originalParameter = originalParameters.get(i);
                        Parameter currentParameter = currentParameters.get(i);
                        if (!originalParameter.getType().getLexeme().equals(currentParameter.getType().getLexeme())) {
                            String description = "Se esperaba parametro de tipo '" + originalParameter.getType().getLexeme() +
                                    "', pero se encontro de tipo '" + currentParameter.getType().getLexeme() + "'";
                            throw new InvalidOverrideException(method.getToken(), description);
                        }
                    }
                }
            }
        }

        LinkedHashMap<String, Method> newMethodList = new LinkedHashMap<>();

        for (Method method : parentMethods.values()) {
            Method inheritedMethod = new Method(method);
            newMethodList.put(method.getName(), inheritedMethod);

        }

        for (Method method : currentMethods.values()) {
            if (!newMethodList.containsKey(method.getName())) {
                method.setPosition(newMethodList.size());
                method.setClassName(this.getName());
                newMethodList.put(method.getToken().getLexeme(), method);
            } else {
                newMethodList.get(method.getToken().getLexeme()).setOverride(true);
                newMethodList.get(method.getToken().getLexeme()).setClassName(this.getName());
            }
        }

        this.setMethods(newMethodList);
        this.setMethodsConsolidationChecked(true);
    }

    /**
     * Exporta la declaración de clase, sus atributos, constructor y métodos.
     *
     * @param indent sangría inicial
     * @return representación JSON de la entrada
     */
    @Override
    public String toJson(String indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent).append("{\n");

        sb.append(indent).append("\t\"name\": \"")
                .append(getToken().getLexeme()).append("\",\n");

        sb.append(indent).append("\t\"parent\": \"")
                .append(getParent()).append("\",\n");

        sb.append(indent).append("\t\"foundClass\": ")
                .append(isFoundClass()).append(",\n");

        sb.append(indent).append("\t\"foundImpl\": ")
                .append(isFoundImpl()).append(",\n");

        if (constructor != null) {
            sb.append(indent).append("\t\"constructor\": \n")
                    .append(constructor.toJson(indent+"\t\t")).append(",\n");
        }

        // ATTRIBUTES
        int total = getAttributes().size();
        int i = 0;
        if (total > 0){
            sb.append(indent).append("\t\"attributes\": [\n");

            for (Map.Entry<String, Attribute> entry : this.getAttributes().entrySet()) {
                Attribute a = entry.getValue();
                sb.append(a.toJson(indent + "\t\t"));

                if (++i < total) sb.append(",");
                sb.append("\n");
            }

            sb.append(indent).append("\t],\n");
        }else {
            sb.append(indent).append("\t\"attributes\": [],\n");
        }


        // METHODS
        total = getMethods().size();
        if (total > 0){
            sb.append(indent).append("\t\"methods\": [\n");

            i = 0;

            for (Map.Entry<String, Method> entry : this.getMethods().entrySet()) {
                Method m = entry.getValue();

                sb.append(m.toJson(indent + "\t\t"));

                if (++i < total) sb.append(",");
                sb.append("\n");
            }

            sb.append(indent).append("\t]\n");
        }else {
            sb.append(indent).append("\t\"methods\": []\n");
        }


        sb.append(indent).append("}");

        return sb.toString();
    }

    public boolean isSubtypeOf(Class possibleParent, Map<String, Class> classes) {

        Class current = this;

        while (current != null) {

            if (current.getName().equals(possibleParent.getName())) {
                return true;
            }

            String parentName = current.getParent();

            if (parentName == null || parentName.equals("Object")) {
                break;
            }

            current = classes.get(parentName);
        }

        return false;
    }

    public Method getMethodInHierarchy(String methodName, Map<String, Class> classes) {

        Class current = this;

        while (current != null) {

            Method method = current.getMethods().get(methodName);

            if (method != null) {
                return method;
            }

            String parentName = current.getParent();

            if (parentName == null || parentName.equals("Object")) {
                break;
            }

            current = classes.get(parentName);
        }

        return null;
    }
}
