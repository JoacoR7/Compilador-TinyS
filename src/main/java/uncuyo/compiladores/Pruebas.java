package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.Attribute;
import uncuyo.compiladores.semantic.Class;
import uncuyo.compiladores.semantic.SemanticAnalyzer;
import uncuyo.compiladores.semantic.SymbolTable;

import java.io.IOException;
import java.util.Optional;

public class Pruebas {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {
        Lexer lexer = new Lexer("C:\\Users\\AMD\\Desktop\\Compiladores2026\\testFiles\\semanticFiles\\success\\mergeClass.s");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
        semanticAnalyzer.program();
        SymbolTable symbolTable = semanticAnalyzer.getSymbolTable();
        StringBuilder sb = new StringBuilder();
        sb.append("classes: [\n");
        for(Class currentClass : symbolTable.getClasses().values()){
            sb.append("\tname: ").append(currentClass.getToken().getLexeme()).append(",\n");
            sb.append("\tparent: ").append(currentClass.getParent()).append(",\n");
            sb.append("\tattributes: [\n");
            for (Attribute currentAttribute : currentClass.getAttributes().values()){
                sb.append("\t\t").append("{name: ").append(currentAttribute.getToken().getLexeme()).append(", ");
                sb.append("type: ").append(currentAttribute.getType().getLexeme()).append(", ");
                sb.append("private: ").append(currentAttribute.isPrivate()).append(", ");
                sb.append("position: ").append(currentAttribute.getPosition()).append("},\n");
            }
            sb.append("\t]\n");
        }
        sb.append("]\n");
        System.out.println(sb.toString());
    }
}
