package uncuyo.compiladores;


import org.junit.jupiter.api.Test;
import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testString() throws FileNotFoundException {
        Lexer lexer = new Lexer("testFiles/lexerFiles/success/strings.s");
        assertDoesNotThrow(lexer::getAllTokens);
    }

    @Test
    public void testOperators() throws Exception {

        Lexer lexer = new Lexer("testFiles/lexerFiles/success/operators.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(17, tokens.size());

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

    @Test
    public void testSpecialSymbols() throws Exception {

        Lexer lexer = new Lexer("testFiles/lexerFiles/success/specialSymbols.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(10, tokens.size());

        assertEquals(new Token("{", TokenType.SPECIAL_SYMBOL_LCB, 1, 1), tokens.get(0));
        assertEquals(new Token("}", TokenType.SPECIAL_SYMBOL_RCB, 1, 2), tokens.get(1));
        assertEquals(new Token("(", TokenType.SPECIAL_SYMBOL_PO, 1, 3), tokens.get(2));
        assertEquals(new Token(")", TokenType.SPECIAL_SYMBOL_PC, 1, 4), tokens.get(3));
        assertEquals(new Token(";", TokenType.SPECIAL_SYMBOL_S, 1, 5), tokens.get(4));
        assertEquals(new Token(",", TokenType.SPECIAL_SYMBOL_CM, 1, 6), tokens.get(5));
        assertEquals(new Token(".", TokenType.SPECIAL_SYMBOL_P, 1, 7), tokens.get(6));
        assertEquals(new Token("[", TokenType.SPECIAL_SYMBOL_LB, 1, 8), tokens.get(7));
        assertEquals(new Token("]", TokenType.SPECIAL_SYMBOL_RB, 1, 9), tokens.get(8));
    }

    @Test
    public void testKeywords() throws Exception {
        Lexer lexer = new Lexer("testFiles/lexerFiles/success/keywords.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(18, tokens.size());

        assertEquals(new Token("class", TokenType.KW_CLASS, 1, 1), tokens.get(0));
        assertEquals(new Token("impl", TokenType.KW_IMPL, 1, 7), tokens.get(1));
        assertEquals(new Token("else", TokenType.KW_ELSE, 1, 12), tokens.get(2));
        assertEquals(new Token("if", TokenType.KW_IF, 1, 17), tokens.get(3));
        assertEquals(new Token("ret", TokenType.KW_RET, 1, 20), tokens.get(4));
        assertEquals(new Token("while", TokenType.KW_WHILE, 1, 24), tokens.get(5));
        assertEquals(new Token("new", TokenType.KW_NEW, 1, 30), tokens.get(6));
        assertEquals(new Token("nil", TokenType.KW_NIL, 1, 34), tokens.get(7));
        assertEquals(new Token("st", TokenType.KW_ST, 1, 38), tokens.get(8));
        assertEquals(new Token("self", TokenType.KW_SELF, 1, 41), tokens.get(9));
        assertEquals(new Token("pub", TokenType.KW_PUB, 1, 46), tokens.get(10));
        assertEquals(new Token("for", TokenType.KW_FOR, 1, 50), tokens.get(11));
        assertEquals(new Token("in", TokenType.KW_IN, 1, 54), tokens.get(12));
        assertEquals(new Token("fn", TokenType.KW_FN, 1, 57), tokens.get(13));
        assertEquals(new Token("void", TokenType.KW_VOID, 1, 60), tokens.get(14));
        assertEquals(new Token("true", TokenType.BOOLEAN_CONSTANT, 1, 65), tokens.get(15));
        assertEquals(new Token("false", TokenType.BOOLEAN_CONSTANT, 1, 70), tokens.get(16));
    }

    @Test
    public void testIdentifiers() throws Exception {
        Lexer lexer = new Lexer("testFiles/lexerFiles/success/identifiers.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(new Token("Foo", TokenType.IDENTIFIER_CLASS, 1, 1), tokens.get(0));
        assertEquals(new Token("bar", TokenType.IDENTIFIER_VAR_METHOD, 1, 5), tokens.get(1));
        assertEquals(new Token("x", TokenType.IDENTIFIER_VAR_METHOD, 1, 9), tokens.get(2));
        assertEquals(new Token("nombre1", TokenType.IDENTIFIER_VAR_METHOD, 1, 11), tokens.get(3));
        assertEquals(new Token("fooBar", TokenType.IDENTIFIER_VAR_METHOD, 1, 19), tokens.get(4));
        assertEquals(new Token("Class1", TokenType.IDENTIFIER_CLASS, 1, 26), tokens.get(5));
        assertEquals(new Token("foo_bar", TokenType.IDENTIFIER_VAR_METHOD, 1, 33), tokens.get(6));
        assertEquals(new Token("x1", TokenType.IDENTIFIER_VAR_METHOD, 1, 41), tokens.get(7));
    }

    @Test
    public void testIntegers() throws Exception {
        Lexer lexer = new Lexer("testFiles/lexerFiles/success/integers.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(new Token("0", TokenType.INT_CONSTANT, 1, 1), tokens.get(0));
        assertEquals(new Token("42", TokenType.INT_CONSTANT, 1, 3), tokens.get(1));
        assertEquals(new Token("100", TokenType.INT_CONSTANT, 1, 6), tokens.get(2));
        assertEquals(new Token("123456", TokenType.INT_CONSTANT, 1, 10), tokens.get(3));
        assertEquals(new Token("999", TokenType.INT_CONSTANT, 1, 17), tokens.get(4));
    }

    @Test
    public void testCommentsAreIgnored() throws Exception {
        Lexer lexer = new Lexer("testFiles/lexerFiles/success/comments.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(new Token("42", TokenType.INT_CONSTANT, 2, 1), tokens.get(0));
        assertEquals(new Token("\"fin\"", TokenType.STRING_CONSTANT, 5, 1), tokens.get(1));
    }

    @Test
    public void testEscapeStrings() throws Exception {
        Lexer lexer = new Lexer("testFiles/lexerFiles/success/escapeStrings.s");
        List<Token> tokens = lexer.getAllTokens();

        assertEquals(new Token("\"hola\\nmundo\"", TokenType.STRING_CONSTANT, 1, 1), tokens.get(0));
        assertEquals(new Token("\"tab\\there\"", TokenType.STRING_CONSTANT, 1, 15), tokens.get(1));
        assertEquals(new Token("\"comillas\\\"dobles\"", TokenType.STRING_CONSTANT, 1, 27), tokens.get(2));
        assertEquals(new Token("\"barra\\\\invertida\"", TokenType.STRING_CONSTANT, 1, 46), tokens.get(3));
        assertEquals(new Token("\"retorno\\rcarro\"", TokenType.STRING_CONSTANT, 1, 65), tokens.get(4));
    }

    @Test
    public void testErrorUndefinedSymbol() {
        Lexer lexer;
        try {
            lexer = new Lexer("testFiles/lexerFiles/error/undefinedSymbol.s");
            assertThrows(LexicalException.class, lexer::getAllTokens);
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado");
        }
    }

    @Test
    public void testErrorUndefinedDollarSymbol() {
        Lexer lexer;
        try {
            lexer = new Lexer("testFiles/lexerFiles/error/dollarSymbol.s");
            assertThrows(LexicalException.class, lexer::getAllTokens);
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado");
        }
    }

    @Test
    public void testErrorUnclosedString() {
        Lexer lexer;
        try {
            lexer = new Lexer("testFiles/lexerFiles/error/unclosedString.s");
            assertThrows(LexicalException.class, lexer::getAllTokens);
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado");
        }
    }

    @Test
    public void testErrorInvalidEscape() {
        Lexer lexer;
        try {
            lexer = new Lexer("testFiles/lexerFiles/error/invalidEscape.s");
            assertThrows(LexicalException.class, lexer::getAllTokens);
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado");
        }
    }

    @Test
    public void testErrorUnclosedMultilineComment() {
        Lexer lexer;
        try {
            lexer = new Lexer("testFiles/lexerFiles/error/unclosedMultilineComment.s");
            assertThrows(LexicalException.class, lexer::getAllTokens);
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado");
        }
    }

    @Test
    public void testErrorLongString() {
        Lexer lexer;
        try {
            lexer = new Lexer("testFiles/lexerFiles/error/longitudStrInvalida.s");
            assertThrows(LexicalException.class, lexer::getAllTokens);
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado");
        }
    }

    @Test
    public void testErrorGeneral() {
        Lexer lexer;
        try {
            lexer = new Lexer("testFiles/lexerFiles/error/error.s");
            assertThrows(LexicalException.class, lexer::getAllTokens);
        } catch (FileNotFoundException e) {
            fail("Archivo no encontrado");
        }
    }
}
