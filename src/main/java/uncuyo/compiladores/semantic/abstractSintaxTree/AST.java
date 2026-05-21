package uncuyo.compiladores.semantic.abstractSintaxTree;

import uncuyo.compiladores.semantic.JsonSerializable;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.BlockNode;
import uncuyo.compiladores.semantic.symbolTable.Method;

import java.util.LinkedList;

public class AST implements JsonSerializable {
    private Class currentClass;
    private Method currentMethod;
    private LinkedList<BlockNode> blockNodes = new LinkedList<>();
    private BlockNode startBlockNode;

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
