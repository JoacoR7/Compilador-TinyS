package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

import java.io.IOException;

public class Etapa3 {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {

        if(args.length < 1) {
            System.out.println("ERROR: Debe especificar el archivo fuente");
        } else {
            Lexer lexer = new Lexer(args[0]);
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
            try {
                semanticAnalyzer.program();
                String output = args.length >= 2 ? args[1] : stripExtension(args[0]) + ".asm";
                semanticAnalyzer.getAst().getCodeGenerator().writeOutput(output);
                System.out.println("CORRECTO: GENERACION DE CODIGO -> " + output);
            } catch (SyntacticException e) {
                e.printStackTrace();
            } catch (LexicalException e) {
                System.out.println("ERROR: LEXICO \n" + e.getMessage() );
            }

        }
    }

    private static String stripExtension(String path) {
        return path.endsWith(".s") ? path.substring(0, path.length() - 2) : path;
    }
}
