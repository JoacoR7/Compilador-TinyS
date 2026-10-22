package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.codeGen.CodeGenVisitor;
import uncuyo.compiladores.codeGen.CodeGenerator;
import uncuyo.compiladores.semantic.JsonSerializable;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.BlockNode;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.SymbolTable;

import java.util.LinkedList;

public class AST implements JsonSerializable {
    private Class currentClass;
    private Method currentMethod;
    private LinkedList<BlockNode> blockNodes = new LinkedList<>();
    private BlockNode startBlockNode;
    private static SymbolTable symbolTable;
    private CodeGenerator codeGenerator;

    public AST() {
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

    public LinkedList<BlockNode> getBlockNodes() {
        return blockNodes;
    }

    public void setBlockNodes(LinkedList<BlockNode> blockNodes) {
        this.blockNodes = blockNodes;
    }

    public BlockNode getStartBlockNode() {
        return startBlockNode;
    }

    public void setStartBlockNode(BlockNode startBlockNode) {
        this.startBlockNode = startBlockNode;
    }

    public static SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public static void setSymbolTable(SymbolTable symbolTable) {
        AST.symbolTable = symbolTable;
    }

    public void consolidate() {
        TypeVisitor typeVisitor = new TypeVisitor();
        for (BlockNode blockNode : blockNodes) {
            typeVisitor.visit(blockNode);
        }
        typeVisitor.visit(startBlockNode);
    }

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

    public CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }


    @Override
    public String toJson(String indent) {

        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "\t";

        sb.append(indent).append("{\n");
        int total = this.getBlockNodes().size();
        if (total > 0 ){
            sb.append(innerIndent).append("\"blockNodes\": [\n");

            int i = 0;

            for (BlockNode b : this.getBlockNodes()) {
                sb.append(b.toJson(innerIndent + "\t"));

                if (++i < total) sb.append(",");
                sb.append("\n");
            }
            sb.append(innerIndent).append("],\n");

        }else {
            sb.append(innerIndent).append("\"blockNodes\": [],\n");
        }

        sb.append(innerIndent).append("\"startMethod\": \n");
        sb.append(this.getStartBlockNode().toJson(innerIndent)).append("\n");

        sb.append(indent).append("}");

        return sb.toString();
    }
}
