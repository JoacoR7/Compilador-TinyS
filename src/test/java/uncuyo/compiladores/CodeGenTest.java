package uncuyo.compiladores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

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
        Path sourceDirectory = Path.of("tc", "tinyS", category);
        Path outputDirectory = Path.of("tc", "tinyS", "mipsFiles", category);
        Files.createDirectories(outputDirectory);

        TestCaseRunner.run("CODEGEN", category, sourceDirectory, source -> {
            Lexer lexer = new Lexer(source.toString());
            try {
                SemanticAnalyzer analyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
                analyzer.program();

                String fileName = source.getFileName().toString();
                Path outputFile = outputDirectory.resolve(fileName.substring(0, fileName.length() - 2) + ".asm");
                analyzer.getAst().getCodeGenerator().writeOutput(outputFile.toString());
            } finally {
                lexer.close();
            }
        });
    }
}
