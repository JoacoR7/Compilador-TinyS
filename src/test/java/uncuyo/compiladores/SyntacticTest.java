package uncuyo.compiladores;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.syntactic.SyntacticAnalyzer;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SyntacticTest {
    @Test
    public void successCases() throws IOException {
        runCases("success");
    }

    @Test
    public void errorCases() throws IOException {
        runCases("error");
    }

    private void runCases(String category) throws IOException {
        TestCaseRunner.run("SYNTACTIC", category, Path.of("tc", "etapa2", category), source -> {
            Lexer lexer = new Lexer(source.toString());
            try {
                SyntacticAnalyzer analyzer = new SyntacticAnalyzer(lexer, lexer.getNextToken());
                if (category.equals("error")) {
                    assertThrows(SyntacticException.class, analyzer::program);
                } else {
                    analyzer.program();
                }
            } finally {
                lexer.close();
            }
        });
    }
}
