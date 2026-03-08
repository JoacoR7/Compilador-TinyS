package uncuyo.compiladores.lexerTester;


import org.junit.jupiter.api.Test;
import uncuyo.compiladores.lexer.Lexer;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class lexerTest {

    @Test
    public void testString() throws FileNotFoundException {
        Lexer lexer = new Lexer("testFiles/lexerFiles/strings.s");
        assertDoesNotThrow(lexer::getAllTokens);
    }
}
