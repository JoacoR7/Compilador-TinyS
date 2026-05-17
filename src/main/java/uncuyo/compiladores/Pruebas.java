package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;
import uncuyo.compiladores.semantic.symbolTable.*;
import uncuyo.compiladores.semantic.symbolTable.Class;

import java.io.IOException;

public class Pruebas {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {
        int a;
        (a) = 3;
        Lexer lexer = new Lexer("C:\\Users\\AMD\\Desktop\\Compiladores2026\\testFiles\\semanticFiles\\error\\classNotDeclarated.s");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
        semanticAnalyzer.program();
        SymbolTable symbolTable = semanticAnalyzer.getSymbolTable();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\"classes\": [\n");

        int i = 0;
        int total = symbolTable.getClasses().size();

        for (Class c : symbolTable.getClasses().values()) {
            sb.append(c.toJson("\t"));

            if (++i < total) sb.append(",");
            sb.append("\n");
        }

        sb.append("]\n}");

        System.out.println(sb.toString());
    }
}
