package uncuyo.compiladores;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.lexer.Lexer;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LexerTest {
    @Test
    public void successCases() throws IOException {
        runCases("success");
    }

    @Test
    public void errorCases() throws IOException {
        runCases("error");
    }

    private void runCases(String category) throws IOException {
        TestCaseRunner.run("LEXER", category, Path.of("tc", "etapa1", category), source -> {
            Lexer lexer = new Lexer(source.toString());
            try {
                if (category.equals("error")) {
                    assertThrows(LexicalException.class, lexer::getAllTokens);
                } else {
                    lexer.getAllTokens();
                }
            } finally {
                lexer.close();
            }
        });
    }
}
