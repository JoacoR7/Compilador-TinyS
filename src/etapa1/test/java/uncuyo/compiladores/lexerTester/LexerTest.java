package uncuyo.compiladores.lexerTester;


import org.junit.jupiter.api.Test;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testString() throws FileNotFoundException {
        Lexer lexer = new Lexer("testFiles/lexerFiles/strings.s");
        assertDoesNotThrow(lexer::getAllTokens);
    }

    @Test
    public void testOperators() throws Exception {

        Lexer lexer = new Lexer("testFiles/lexerFiles/operators.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(16, tokens.size());

        assertEquals(new Token("+", TokenType.OPERATOR_ADD, 1, 1), tokens.get(0));
        assertEquals(new Token("-", TokenType.OPERATOR_SUB, 1, 3), tokens.get(1));
        assertEquals(new Token("/", TokenType.OPERATOR_DIV, 1, 5), tokens.get(2));
        assertEquals(new Token("*", TokenType.OPERATOR_MUL, 1, 7), tokens.get(3));

        assertEquals(new Token("++", TokenType.OPERATOR_INC, 1, 9), tokens.get(4));
        assertEquals(new Token("--", TokenType.OPERATOR_DEC, 1, 12), tokens.get(5));

        assertEquals(new Token("==", TokenType.OPERATOR_EQ, 1, 15), tokens.get(6));
        assertEquals(new Token("<=", TokenType.OPERATOR_LTE, 1, 18), tokens.get(7));
        assertEquals(new Token(">=", TokenType.OPERATOR_GTE, 1, 21), tokens.get(8));
        assertEquals(new Token("!=", TokenType.OPERATOR_NEQ, 1, 24), tokens.get(9));

        assertEquals(new Token(">", TokenType.OPERATOR_GT, 1, 27), tokens.get(10));
        assertEquals(new Token("<", TokenType.OPERATOR_LT, 1, 29), tokens.get(11));
        assertEquals(new Token("=", TokenType.OPERATOR_ASSIGN, 1, 31), tokens.get(12));

        assertEquals(new Token("&&", TokenType.OPERATOR_AND, 1, 33), tokens.get(13));
        assertEquals(new Token("||", TokenType.OPERATOR_OR, 1, 36), tokens.get(14));
        assertEquals(new Token("!", TokenType.OPERATOR_NOT, 1, 39), tokens.get(15));
    }

}
