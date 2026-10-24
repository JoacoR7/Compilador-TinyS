package uncuyo.compiladores;

import org.junit.jupiter.api.Test;
import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.lexer.Lexer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class LexerTest {

    private Lexer lexer;
    private FileUtils fileUtils;

    @Test
    public void errorCases() throws FileNotFoundException {
        fileUtils = new FileUtils("tc/error/");
        List<String> testFiles = fileUtils.traverseFiles();
        HashMap<String, String> testsPassed = new HashMap<>();
        HashMap<String, String> testsFailed = new HashMap<>();

        for (String testFile : testFiles) {
            try {
                lexer = new Lexer(testFile);
                LexicalException ex = assertThrows(LexicalException.class, lexer::getAllTokens);

                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));

                StringBuilder sb = new StringBuilder();
                sb.append("------------------------------------------------------------------\n");
                sb.append("TEST FILE: ").append(testFile).append("\n");
                sb.append(ex.getMessage()).append("\n\n").append(sw).append("\n");
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
        failIfAnyTestFailed(testsFailed);
    }

    @Test
    public void successCases() throws FileNotFoundException {
        fileUtils = new FileUtils("tc/success/");
        List<String> testsPassed = new ArrayList<>();
        HashMap<String, String> testsFailed = new HashMap<>();
        List<String> testFiles = fileUtils.traverseFiles();

        for (String testFile : testFiles) {
            try {
                lexer = new Lexer(testFile);
                assertDoesNotThrow(lexer::getAllTokens);
                testsPassed.add(testFile);
            } catch (AssertionError | Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                testsFailed.put(testFile, sw.toString());
            }
        }

        System.out.println("[SUCCESS TESTS]");
        System.out.println("Total tests: " + testFiles.size());
        System.out.println("Total tests passed: " + testsPassed.size());
        failIfAnyTestFailed(testsFailed);
    }

    private void failIfAnyTestFailed(HashMap<String, String> testsFailed) {
        if (testsFailed.isEmpty()) {
            return;
        }

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
