package uncuyo.compiladores;

import org.junit.jupiter.api.Test;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CodeGenTest {

    @Test
    public void successCases() throws IOException {
        generateCases("success");
    }

    @Test
    public void errorCases() throws IOException {
        // Los errores de estos casos ocurren al ejecutar el MIPS generado.
        generateCases("error");
    }

    private void generateCases(String category) throws IOException {
        Path sourceDirectory = Path.of("testFiles", "codeGenFiles", category);
        Path outputDirectory = Path.of("testFiles", "codeGenFiles", "mipsFiles", category);
        Files.createDirectories(outputDirectory);

        List<Path> testFiles;
        try (Stream<Path> files = Files.list(sourceDirectory)) {
            testFiles = files.filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".s"))
                    .sorted()
                    .toList();
        }
        assertFalse(testFiles.isEmpty(), "No se encontraron casos en " + sourceDirectory);

        Map<Path, String> testsFailed = new LinkedHashMap<>();
        for (Path testFile : testFiles) {
            try {
                assertDoesNotThrow(() -> {
                    Lexer lexer = new Lexer(testFile.toString());
                    SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
                    semanticAnalyzer.program();

                    String fileName = testFile.getFileName().toString();
                    Path outputFile = outputDirectory.resolve(fileName.substring(0, fileName.length() - 2) + ".asm");
                    semanticAnalyzer.getAst().getCodeGenerator().writeOutput(outputFile.toString());
                }, testFile.toString());
            } catch (AssertionError | Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                testsFailed.put(testFile, sw.toString());
            }
        }

        System.out.println("[CODEGEN " + category.toUpperCase() + " TESTS]");
        System.out.println("Total tests: " + testFiles.size());
        System.out.println("Total tests passed: " + (testFiles.size() - testsFailed.size()));
        if (!testsFailed.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            sb.append("TOTAL TEST FAILED: ").append(testsFailed.size()).append("\n");
            sb.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
            for (Map.Entry<Path, String> failedTest : testsFailed.entrySet()) {
                sb.append("------------------------------------------------------------------\n");
                sb.append("TEST FILE: ").append(failedTest.getKey()).append("\n");
                sb.append(failedTest.getValue()).append("\n");
                sb.append("------------------------------------------------------------------\n\n");
            }
            fail(sb.toString());
        }
    }
}
