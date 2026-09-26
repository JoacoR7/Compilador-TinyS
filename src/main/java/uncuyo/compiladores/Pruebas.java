package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.symbolTable.*;
import uncuyo.compiladores.semantic.symbolTable.Class;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class Pruebas {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {
        Map<String, String> env = System.getenv();
        String inputPath = env.get("TEST_PATH") + "\\codeGenFiles\\success\\literals.s";
        Lexer lexer = new Lexer(inputPath);
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
        System.out.println("###### CODIGO GENERADO ######");
        String codigo = ast.getCodeGenerator().generate();
        String fileName = Paths.get(inputPath).getFileName().toString();
        String outputName = fileName.replaceFirst("\\.s$", ".asm");

        String outputPath = Paths.get(
                env.get("TEST_PATH"),
                "codeGenFiles",
                "pruebas",
                outputName
        ).toString();

        ast.getCodeGenerator().writeOutput(outputPath);
        System.out.println(codigo);
    }
}
