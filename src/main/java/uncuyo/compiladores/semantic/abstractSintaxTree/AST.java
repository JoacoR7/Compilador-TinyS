package uncuyo.compiladores.semantic.abstractSintaxTree;

import java.util.LinkedList;

import uncuyo.compiladores.codeGen.CodeGenVisitor;
import uncuyo.compiladores.codeGen.CodeGenerator;
import uncuyo.compiladores.semantic.JsonSerializable;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.BlockNode;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.SymbolTable;

/**
 * Árbol sintáctico abstracto de los cuerpos de un programa TinyS.
 * Conserva los bloques raíz de métodos y constructores, y el bloque de
 * {@code start} por separado. La tabla de símbolos es compartida entre
 * instancias y debe establecerse antes del chequeo, la exportación o la
 * generación de código. Los visitantes operan sobre los nodos almacenados.
 */
public class AST implements JsonSerializable {
    private Class currentClass;
    private Method currentMethod;
    private LinkedList<BlockNode> blockNodes = new LinkedList<>();
    private BlockNode startBlockNode;
    private static SymbolTable symbolTable;
    private CodeGenerator codeGenerator;

    /**
     * Crea un AST sin cuerpos registrados y con una lista de bloques vacía.
     */
    public AST() {
    }

    /**
     * Obtiene la clase Java guardada como contexto auxiliar del AST.
     *
     * @return la clase Java guardada como contexto auxiliar del AST
     */
    public Class getCurrentClass() {
        return currentClass;
    }

    /**
     * Establece la clase guardada como contexto auxiliar del AST.
     *
     * @param currentClass la clase guardada como contexto auxiliar del AST
     */
    public void setCurrentClass(Class currentClass) {
        this.currentClass = currentClass;
    }

    /**
     * Obtiene el método guardado como contexto del AST.
     *
     * @return el método guardado como contexto del AST
     */
    public Method getCurrentMethod() {
        return currentMethod;
    }

    /**
     * Establece el método guardado como contexto del AST.
     *
     * @param currentMethod el método guardado como contexto del AST
     */
    public void setCurrentMethod(Method currentMethod) {
        this.currentMethod = currentMethod;
    }

    /**
     * Obtiene la lista de bloques raíz de métodos y constructores.
     *
     * @return la lista de bloques raíz de métodos y constructores
     */
    public LinkedList<BlockNode> getBlockNodes() {
        return blockNodes;
    }

    /**
     * Establece la lista de bloques raíz de métodos y constructores.
     *
     * @param blockNodes la lista de bloques raíz de métodos y constructores
     */
    public void setBlockNodes(LinkedList<BlockNode> blockNodes) {
        this.blockNodes = blockNodes;
    }

    /**
     * Obtiene el bloque de {@code start}.
     *
     * @return el bloque de {@code start}
     */
    public BlockNode getStartBlockNode() {
        return startBlockNode;
    }

    /**
     * Establece el bloque de {@code start}.
     *
     * @param startBlockNode el bloque de {@code start}
     */
    public void setStartBlockNode(BlockNode startBlockNode) {
        this.startBlockNode = startBlockNode;
    }

    /**
     * Obtiene la tabla de símbolos compartida por los AST.
     *
     * @return la tabla de símbolos compartida por los AST
     */
    public static SymbolTable getSymbolTable() {
        return symbolTable;
    }

    /**
     * Establece la tabla de símbolos compartida por los AST.
     *
     * @param symbolTable la tabla de símbolos compartida por los AST
     */
    public static void setSymbolTable(SymbolTable symbolTable) {
        AST.symbolTable = symbolTable;
    }

    /**
     * Comprueba los cuerpos con un mismo {@link TypeVisitor} y luego visita {@code start}.
     * Requiere una tabla de símbolos consolidada y los cuerpos ya construidos.
     *
     * @throws uncuyo.compiladores.exceptions.semantic.SemanticException si falla el chequeo de un cuerpo
     */
    public void consolidate() {
        TypeVisitor typeVisitor = new TypeVisitor();
        for (BlockNode blockNode : blockNodes) {
            typeVisitor.visit(blockNode);
        }
        typeVisitor.visit(startBlockNode);
    }

    /**
     * Genera código para los bloques registrados y finalmente para {@code start}.
     * Emite una virtual table cada vez que cambia la clase de los bloques
     * recorridos y conserva el generador resultante. Requiere el AST chequeado
     * y los bloques de cada clase agrupados en la lista.
     */
    public void generate() {
        CodeGenVisitor codeGenVisitor = new CodeGenVisitor();
        String currentClassName = null;
        for (BlockNode blockNode : blockNodes) {
            if (!blockNode.getClassName().equals(currentClassName)) {
                currentClassName = blockNode.getClassName();
                codeGenVisitor.getCodeGenerator().generateVTable(AST.getSymbolTable().getClasses().get(currentClassName));
            }
            codeGenVisitor.visit(blockNode);
        }
        codeGenVisitor.visit(startBlockNode);
        this.codeGenerator = codeGenVisitor.getCodeGenerator();
    }

    /**
     * Obtiene el generador de código, o {@code null} si aún no se ejecutó {@code generate()}.
     *
     * @return el generador de código, o {@code null} si aún no se ejecutó {@code generate()}
     */
    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }


    /**
     * Exporta las clases y el método {@code start} mediante {@link AstJsonVisitor}.
     *
     * @param indent sangría inicial para el documento JSON
     * @return representación JSON completa del programa
     */
    @Override
    public String toJson(String indent) {
        return new AstJsonVisitor(this, symbolTable).export(indent);
    }
}
