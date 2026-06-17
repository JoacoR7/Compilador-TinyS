package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.symbolTable.*;
import uncuyo.compiladores.semantic.symbolTable.Class;

import java.io.IOException;

public class Pruebas {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {
        int a;
        (a) = 3;
        Lexer lexer = new Lexer("C:\\Users\\victo\\IdeaProjects\\Compilador\\testFiles\\semanticFiles\\success\\startVariables.s");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
        semanticAnalyzer.program();
        SymbolTable symbolTable = semanticAnalyzer.getSymbolTable();
        AST ast = semanticAnalyzer.getAst();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n\"classes\": [\n");

        System.out.println("###### SYMBOL TABLE ######");
        System.out.println(symbolTable.toJson(""));
        System.out.println("###### AST ######");
        System.out.println(ast.toJson(""));
    }
}
