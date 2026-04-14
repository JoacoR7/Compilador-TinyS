package uncuyo.compiladores.sintactirTester;

import org.junit.jupiter.api.Test;
import uncuyo.compiladores.FileUtils;
import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.syntactic.SyntacticAnalyzer;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;

public class SyntacticTest {

    private SyntacticAnalyzer syntacticAnalyzer;
    private Lexer lexer;
    private FileUtils fileUtils;

    /*********************************************
                    Casos de error
    *********************************************/
    @Test
    public void noStartMethod() throws FileNotFoundException, LexicalException {
        fileUtils = new FileUtils("testFiles/syntacticFiles/error/");
        for (String testFile : fileUtils.traverseFiles()) {
            System.out.println(testFile);
            lexer = new Lexer(testFile);
            syntacticAnalyzer = new SyntacticAnalyzer(lexer, lexer.getNextToken());
            assertThrows(SyntacticException.class, syntacticAnalyzer::program);
        }

    }

    /*********************************************
                    Casos de éxito
     *********************************************/


    @Test
    public void successCases() throws FileNotFoundException, LexicalException {
        fileUtils = new FileUtils("testFiles/syntacticFiles/success/");
        for (String testFile : fileUtils.traverseFiles()) {
            System.out.println(testFile);
            lexer = new Lexer(testFile);
            syntacticAnalyzer = new SyntacticAnalyzer(lexer, lexer.getNextToken());
            assertDoesNotThrow(syntacticAnalyzer::program);
        }
    }
}
