package uncuyo.compiladores;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uncuyo.compiladores.SemanticTestSupport.analyze;

public class SemanticSentencesTest {
    @Test
    public void successCases() throws IOException {
        TestCaseRunner.run("SEMANTIC SENTENCES", "success", Path.of("tc", "etapa4", "success"),
                source -> analyze(source, SemanticAnalyzer::analyzeSentences));
    }

    @Test
    public void errorCases() throws IOException {
        TestCaseRunner.run("SEMANTIC SENTENCES", "error", Path.of("tc", "etapa4", "error"), source -> {
            assertDoesNotThrow(() -> analyze(source, SemanticAnalyzer::analyzeDeclarations),
                    "El caso debe superar el chequeo de declaraciones");
            assertThrows(SemanticException.class,
                    () -> analyze(source, SemanticAnalyzer::analyzeSentences));
        });
    }
}
