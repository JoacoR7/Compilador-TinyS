package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

import java.io.IOException;
import java.util.Optional;

public class Pruebas {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {
        Lexer lexer = new Lexer("C:\\Users\\victo\\IdeaProjects\\Compilador\\testFiles\\semanticFiles\\error\\duplicatedAttribute.s");
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
        semanticAnalyzer.program();
    }
}
