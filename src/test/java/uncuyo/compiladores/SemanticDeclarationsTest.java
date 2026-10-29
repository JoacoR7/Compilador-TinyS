package uncuyo.compiladores;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static uncuyo.compiladores.SemanticTestSupport.analyze;

public class SemanticDeclarationsTest {
    @Test
    public void successCases() throws IOException {
        TestCaseRunner.run("SEMANTIC DECLARATIONS", "success", Path.of("tc", "etapa3", "success"),
                source -> analyze(source, SemanticAnalyzer::analyzeDeclarations));
    }

    @Test
    public void errorCases() throws IOException {
        TestCaseRunner.run("SEMANTIC DECLARATIONS", "error", Path.of("tc", "etapa3", "error"),
                source -> assertThrows(SemanticException.class,
                        () -> analyze(source, SemanticAnalyzer::analyzeDeclarations)));
    }
}
