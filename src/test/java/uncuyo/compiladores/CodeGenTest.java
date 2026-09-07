package uncuyo.compiladores;

import org.junit.jupiter.api.Test;
import uncuyo.compiladores.codeGen.CodeGenerator;
import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CodeGenTest {

    private SemanticAnalyzer semanticAnalyzer;
    private Lexer lexer;
    private FileUtils fileUtils;

    @Test
    public void successCases() throws FileNotFoundException, LexicalException {
        fileUtils = new FileUtils("testFiles/codeGenFiles/success/");
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
        System.out.println("[CODEGEN SUCCESS TESTS]");
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

    @Test
    public void errorCases() throws FileNotFoundException, LexicalException {
        fileUtils = new FileUtils("testFiles/codeGenFiles/error/");
        List<String> testFiles = fileUtils.traverseFiles();
        HashMap<String, String> testsPassed = new HashMap<>();
        HashMap<String, String> testsFailed = new HashMap<>();
        for (String testFile : testFiles) {
            try {
                lexer = new Lexer(testFile);
                semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
                assertThrows(Exception.class, semanticAnalyzer::program);
                StringBuilder sb = new StringBuilder();
                sb.append("------------------------------------------------------------------\n");
                sb.append("TEST FILE: ").append(testFile).append("\n");
                sb.append("------------------------------------------------------------------\n\n");
                testsPassed.put(testFile, sb.toString());
            } catch (AssertionError | Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                testsFailed.put(testFile, sw.toString());
            }
        }
        System.out.println("[CODEGEN ERROR TESTS]");
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
}