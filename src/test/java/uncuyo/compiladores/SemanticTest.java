package uncuyo.compiladores;

import org.junit.jupiter.api.Test;
import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;
import uncuyo.compiladores.syntactic.SyntacticAnalyzer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SemanticTest {

    private SemanticAnalyzer semanticAnalyzer;
    private Lexer lexer;
    private FileUtils fileUtils;

    /*********************************************
                    Casos de error
    *********************************************/
    @Test
    public void errorCases() throws FileNotFoundException, LexicalException {
        fileUtils = new FileUtils("testFiles/semanticFiles/error/");
        List<String> testFiles = fileUtils.traverseFiles();
        HashMap<String, String> testsPassed = new HashMap<>();
        HashMap<String, String> testsFailed = new HashMap<>();
        for (String testFile : testFiles) {
            try {
                lexer = new Lexer(testFile);
                semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
                SemanticException ex = assertThrows(
                        SemanticException.class,
                        semanticAnalyzer::program
                );

                StringBuilder sb = new StringBuilder();
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));

                sb.append("------------------------------------------------------------------\n");
                sb.append("TEST FILE: ").append(testFile).append("\n");
                sb.append(ex.getMessage()).append("\n\n").append(sw.toString()).append("\n");
                sb.append("------------------------------------------------------------------\n\n");

                testsPassed.put(testFile, sb.toString());

            } catch (AssertionError | Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                testsFailed.put(testFile, sw.toString());
            }
        }
        System.out.println("[ERROR TESTS]");
        System.out.println("Total tests: " + testFiles.size());
        System.out.println("Total tests passed: " + testsPassed.size());
        for (Map.Entry<String, String> testPassed : testsPassed.entrySet()) {
            System.out.println(testPassed.getValue());
        }
        if (!testsFailed.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            sb.append("TOTAL TEST FAILED: ").append(testsFailed.size()).append("\n");
            sb.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");

            for (Map.Entry<String, String> failedTest : testsFailed.entrySet()) {
                sb.append("------------------------------------------------------------------\n");
                sb.append("TEST FILE: ").append(failedTest.getKey()).append("\n");
                sb.append(failedTest.getValue()).append("\n");
                sb.append("------------------------------------------------------------------\n\n");
            }

            fail(sb.toString());
        }
    }

    /*********************************************
                    Casos de éxito
     *********************************************/
    @Test
    public void successCases() throws FileNotFoundException, LexicalException {
        fileUtils = new FileUtils("testFiles/semanticFiles/success/");
        List<String> testPassed = new ArrayList<>();
        HashMap<String, String> testFailed = new HashMap<>();
        List<String> testFiles = fileUtils.traverseFiles();
        for (String testFile : testFiles) {
            try {
                lexer = new Lexer(testFile);
                semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
                assertDoesNotThrow(semanticAnalyzer::program);
                testPassed.add(testFile);
            } catch (AssertionError | Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                testFailed.put(testFile, sw.toString());
            }
        }
        System.out.println("[SUCCESS TESTS]");
        System.out.println("Total tests: " + testFiles.size());
        System.out.println("Total tests passed: " + testPassed.size());
        if (!testFailed.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            sb.append("TOTAL TEST FAILED: ").append(testFailed.size()).append("\n");
            sb.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");

            for (Map.Entry<String, String> failedTest : testFailed.entrySet()) {
                sb.append("------------------------------------------------------------------\n");
                sb.append("TEST FILE: ").append(failedTest.getKey()).append("\n");
                sb.append(failedTest.getValue()).append("\n");
                sb.append("------------------------------------------------------------------\n\n");
            }

            fail(sb.toString());
        }
    }
}
