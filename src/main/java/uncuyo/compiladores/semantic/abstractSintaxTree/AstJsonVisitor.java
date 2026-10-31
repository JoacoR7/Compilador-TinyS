package uncuyo.compiladores.semantic.abstractSintaxTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.JsonWriter;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ArrayConstructorNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ArrayNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.BinaryExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ChainedCallNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ChainedNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ClassConstructorNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ConclassorCallNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.LiteralNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.OperandNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ParenthesizedExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SelfNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleAccessNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleArrayNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleChainedNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleSelfNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleVariableNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.UnaryExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.VariableNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.AssignmentNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.BlockNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.ForNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.IfElseNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.ReturnNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.SentenceNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.SimpleSentenceNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.WhileNode;
import uncuyo.compiladores.semantic.symbolTable.Class;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.SymbolTable;
import uncuyo.compiladores.semantic.symbolTable.Type;

/**
 * Exporta el AST completo junto con los datos de la tabla de símbolos.
 * Incluye clases de usuario y predefinidas, los
 * cuerpos disponibles y el método {@code start}. Una entrada nula en una
 * lista de sentencias se representa como {@code EmptySentence}.
 */
public final class AstJsonVisitor implements Visitor<Object> {
    private static final Set<String> PREDEFINED = Set.of("Object", "IO", "Int", "Str", "Bool", "Array");
    private final AST ast;
    private final SymbolTable table;

    /**
     * Crea un exportador para un AST y su tabla de símbolos.
     *
     * @param ast árbol cuyos cuerpos se exportan
     * @param table tabla consolidada con las declaraciones del programa
     */
    public AstJsonVisitor(AST ast, SymbolTable table) {
        this.ast = ast;
        this.table = table;
    }

    /**
     * Serializa las clases de la tabla y el cuerpo de {@code start}.
     * Los métodos sin cuerpo disponible se exportan con {@code body: null}.
     *
     * @param indent sangría inicial para el documento
     * @return documento JSON completo del programa
     */
    public String export(String indent) {
        List<Object> classes = new ArrayList<>();
        for (Class current : table.getClasses().values()) {
            List<Object> attributes = new ArrayList<>();
            current.getAttributes().values().forEach(attribute -> attributes.add(JsonWriter.object(
                    "name", attribute.getName(), "type", typeName(attribute.getType()),
                    "private", attribute.isPrivate())));
            List<Object> methods = new ArrayList<>();
            current.getMethods().values().forEach(method -> methods.add(
                    method(method, findBody(current, method.getName()))));
            classes.add(JsonWriter.object("name", current.getName(), "parent", current.getParent(),
                    "predefined", PREDEFINED.contains(current.getName()), "attributes", attributes,
                    "constructor", method(current.getConstructor(), findBody(current, ".")),
                    "methods", methods));
        }
        return JsonWriter.write(JsonWriter.object("classes", classes,
                "startMethod", method(table.getStart(), ast.getStartBlockNode())), indent);
    }

    /**
     * Busca el cuerpo de un método o constructor en una clase y sus ancestros.
     *
     * @param current clase desde la que comienza la búsqueda
     * @param name nombre del método; {@code .} identifica al constructor
     * @return primer bloque coincidente, o {@code null} si no existe
     */
    private BlockNode findBody(Class current, String name) {
        while (current != null) {
            for (BlockNode block : ast.getBlockNodes()) {
                if (Objects.equals(block.getClassName(), current.getName())
                        && Objects.equals(block.getMethodName(), name)) {
                    return block;
                }
            }
            current = table.getClasses().get(current.getParent());
        }
        return null;
    }

    /**
     * Construye la representación de una firma, sus variables y su cuerpo.
     *
     * @param method declaración que se exporta; puede ser {@code null}
     * @param body bloque asociado, o {@code null} si no hay implementación disponible
     * @return mapa del método, o {@code null} si no hay declaración
     */
    private Object method(Method method, BlockNode body) {
        if (method == null) {
            return null;
        }
        List<Object> parameters = new ArrayList<>();
        method.getParameters().values().forEach(parameter -> parameters.add(JsonWriter.object(
                "name", parameter.getLexeme(), "type", typeName(parameter.getType()),
                "position", parameter.getPosition())));
        List<Object> variables = new ArrayList<>();
        method.getVariables().values().forEach(variable -> variables.add(JsonWriter.object(
                "name", variable.getName(), "type", typeName(variable.getType()),
                "position", variable.getPosition())));
        return JsonWriter.object("name", method.getName(), "returnType", typeName(method.getType()),
                "static", method.isStatic(), "parameters", parameters, "variables", variables,
                "body", render(body));
    }

    /**
     * Obtiene el nombre semántico de un tipo cuando está disponible.
     *
     * @param type tipo que se consulta; puede ser {@code null}
     * @return nombre del tipo, o {@code null} si no hay tipo
     */
    private static String typeName(Type type) {
        return type == null ? null : type.getTypeName();
    }

    /**
     * Aplica el visitante a un hijo opcional del AST.
     *
     * @param node nodo hijo, o {@code null} si está ausente
     * @return representación del nodo, o {@code null} si está ausente
     */
    private Object render(Node node) {
        return node == null ? null : node.accept(this);
    }

    /**
     * Representa una sentencia y distingue una entrada vacía de un hijo ausente.
     *
     * @param node sentencia que se exporta; {@code null} indica una sentencia vacía
     * @return representación de la sentencia o un objeto {@code EmptySentence}
     */
    private Object sentence(SentenceNode node) {
        return node == null ? JsonWriter.object("nodeType", "EmptySentence") : render(node);
    }

    /**
     * Exporta los argumentos conservando su orden.
     *
     * @param nodes lista de expresiones, que puede ser {@code null}
     * @return lista de representaciones; vacía si no hay argumentos
     */
    private List<Object> expressions(List<ExpressionNode> nodes) {
        List<Object> result = new ArrayList<>();
        if (nodes != null) {
            nodes.forEach(node -> result.add(render(node)));
        }
        return result;
    }

    /**
     * Construye un objeto con el nombre del nodo y sus metadatos disponibles.
     * Agrega ubicación si recibe un token y tipo si la expresión ya lo tiene.
     *
     * @param node nodo del que se obtiene la clase concreta y el tipo
     * @param token token de ubicación, o {@code null} si el nodo no lo conserva
     * @param fields pares alternados de nombre de propiedad y valor
     * @return mapa con los metadatos y las propiedades específicas
     */
    private Map<String, Object> node(Node node, Token token, Object... fields) {
        Map<String, Object> result = JsonWriter.object("nodeType", node.getClass().getSimpleName());
        if (token != null) {
            result.put("lexeme", token.getLexeme());
            result.put("row", token.getRowIndex());
            result.put("column", token.getColumnIndex());
        }
        if (node instanceof ExpressionNode expression && expression.getType() != null) {
            result.put("type", typeName(expression.getType()));
        }
        result.putAll(JsonWriter.object(fields));
        return result;
    }

    /**
     * Construye la representación de una creación de arreglo.
     * Incluye la longitud y el encadenado posterior.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ArrayConstructorNode node) {
        return node(node, node.getToken(), "length", render(node.getExpression()),
                "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de un acceso indexado a un arreglo.
     * Incluye el índice y el encadenado posterior.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ArrayNode node) {
        return node(node, node.getToken(), "index", render(node.getExpression()),
                "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de una operación binaria.
     * Incluye el operador y ambos operandos.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(BinaryExpressionNode node) {
        return node(node, node.getBinaryOperatorToken(), "operator", node.getBinaryOperatorToken().getLexeme(),
                "left", render(node.getLeftNode()), "right", render(node.getRightNode()));
    }

    /**
     * Construye la representación de una llamada encadenada.
     * Incluye los argumentos en orden y la continuación.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ChainedCallNode node) {
        return node(node, node.getToken(), "arguments", expressions(node.getExpressionNodes()),
                "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de un acceso encadenado.
     * Incluye el token y la continuación del acceso.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ChainedNode node) {
        return node(node, node.getToken(), "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de una creación de instancia.
     * Incluye los argumentos del constructor y la continuación.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ClassConstructorNode node) {
        return node(node, node.getToken(), "arguments", expressions(node.getExpressionNodes()),
                "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de una expresión de construcción.
     * Incluye el token y el encadenado de la construcción.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ConclassorCallNode node) {
        return node(node, node.getToken(), "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de una expresión.
     * Incluye el tipo semántico si está disponible.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ExpressionNode node) {
        return node(node, null);
    }

    /**
     * Construye la representación de un literal.
     * Incluye el token y su categoría léxica.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(LiteralNode node) {
        return node(node, node.getToken(), "tokenType", node.getToken().getTokenType().name());
    }

    /**
     * Construye la representación de un operando.
     * Incluye el tipo semántico si está disponible.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(OperandNode node) {
        return node(node, null);
    }

    /**
     * Construye la representación de una expresión entre paréntesis.
     * Incluye la expresión agrupada y la continuación.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ParenthesizedExpressionNode node) {
        return node(node, null, "expression", render(node.getExpresion()),
                "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de una referencia a {@code self}.
     * Incluye el token de {@code self} y la continuación.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SelfNode node) {
        return node(node, node.getToken(), "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de un acceso de destino.
     * Incluye los metadatos del acceso de destino.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SimpleAccessNode node) {
        return node(node, node.getToken());
    }

    /**
     * Construye la representación de un acceso indexado de destino.
     * Incluye la expresión del índice.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SimpleArrayNode node) {
        return node(node, node.getToken(), "index", render(node.getExpressionNode()));
    }

    /**
     * Construye la representación de un atributo encadenado de destino.
     * Incluye el atributo y el siguiente atributo de destino.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SimpleChainedNode node) {
        return node(node, node.getToken(), "chained", render(node.getSimpleChainedNode()));
    }

    /**
     * Construye la representación de un acceso de destino mediante {@code self}.
     * Incluye el token y el encadenado de atributos de destino.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SimpleSelfNode node) {
        return node(node, node.getToken(), "chained", render(node.getSimpleChainedNode()));
    }

    /**
     * Construye la representación de un acceso de destino a una variable.
     * Incluye el token y el encadenado de atributos de destino.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SimpleVariableNode node) {
        return node(node, node.getToken(), "chained", render(node.getSimpleChainedNode()));
    }

    /**
     * Construye la representación de una operación unaria.
     * Incluye el operador y su operando.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(UnaryExpressionNode node) {
        return node(node, node.getUnaryOperatorToken(), "operator", node.getUnaryOperatorToken().getLexeme(),
                "expression", render(node.getExpressionNode()));
    }

    /**
     * Construye la representación de una referencia a una variable o atributo.
     * Incluye los nombres de clase y método, y el encadenado.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(VariableNode node) {
        return node(node, node.getToken(), "className", node.getClassName(),
                "methodName", node.getMethodName(), "chained", render(node.getChainedNode()));
    }

    /**
     * Construye la representación de una asignación.
     * Incluye el destino en {@code left} y el valor en {@code right}.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(AssignmentNode node) {
        return node(node, null, "left", render(node.getSimpleAccessNode()),
                "right", render(node.getExpressionNode()));
    }

    /**
     * Construye la representación de un bloque de sentencias.
     * Conserva el orden de las sentencias y representa las vacías como {@code EmptySentence}.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(BlockNode node) {
        List<Object> sentences = new ArrayList<>();
        if (node.getSentences() != null) {
            node.getSentences().forEach(statement -> sentences.add(sentence(statement)));
        }
        return node(node, null, "className", node.getClassName(), "methodName", node.getMethodName(),
                "method", node.isMethod(), "sentences", sentences);
    }

    /**
     * Construye la representación de un recorrido {@code for}.
     * Incluye el tipo de iteración, la variable, el arreglo y el cuerpo.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ForNode node) {
        return node(node, null, "variableType", typeName(node.getType()),
                "variable", render(node.getVarNode1()), "iterable", render(node.getVarNode2()),
                "body", sentence(node.getSentenceNode()));
    }

    /**
     * Construye la representación de una sentencia {@code if/else}.
     * Incluye la condición y las ramas; un {@code else} ausente se representa con {@code null}.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(IfElseNode node) {
        return node(node, node.getToken(), "condition", render(node.getExpression()),
                "then", sentence(node.getSentenceNodeIf()), "else", render(node.getSentenceNodeElse()));
    }

    /**
     * Construye la representación de una sentencia de retorno.
     * Incluye la expresión devuelta, que puede ser {@code null}.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(ReturnNode node) {
        return node(node, node.getToken(), "expression", render(node.getExpression()));
    }

    /**
     * Construye la representación de una sentencia.
     * Incluye el nombre de la clase concreta del nodo.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SentenceNode node) {
        return node(node, null);
    }

    /**
     * Construye la representación de una expresión usada como sentencia.
     * Incluye la expresión utilizada como sentencia.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(SimpleSentenceNode node) {
        return node(node, null, "expression", render(node.getExpressionNode()));
    }

    /**
     * Construye la representación de un ciclo {@code while}.
     * Incluye la condición y el cuerpo del ciclo.
     *
     * @param node nodo que se exporta
     * @return mapa de propiedades listo para ser serializado como JSON
     */
    @Override
    public Object visit(WhileNode node) {
        return node(node, node.getToken(), "condition", render(node.getExpressionNode()),
                "body", sentence(node.getSentenceNode()));
    }
}
