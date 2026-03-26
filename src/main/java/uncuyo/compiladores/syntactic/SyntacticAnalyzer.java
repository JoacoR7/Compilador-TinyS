package uncuyo.compiladores.syntactic;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;

import java.io.IOException;


public class SyntacticAnalyzer {

    private Lexer lexer;
    private Token currentToken;

    private void nextToken() throws LexicalException, IOException {
        currentToken = lexer.getNextToken();
    }

    private boolean checkLexeme(String... lexemes) {
        for (String lex : lexemes) {
            if (currentToken.getLexeme().equals(lex)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkType(TokenType... types) {
        for (TokenType type : types) {
            if (currentToken.getTokenType() == type) {
                return true;
            }
        }
        return false;
    }

    private void matchLexeme(String expected) throws Exception {
        if (currentToken.getLexeme().equals(expected)) {
            nextToken();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '" + expected + "' pero se encontro '" +
                            currentToken.getLexeme() + "'");
        }
    }

    private void matchTokenType(TokenType expectedTokenType) throws Exception {

        if (currentToken.getTokenType().equals(expectedTokenType)){
            nextToken();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba " + expectedTokenType + " pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }

    }

    /* ⟨program⟩ ::= ⟨Lista-Definiciones⟩ ⟨Start⟩ */
    public void program(){
        //listaDefiniciones();
        //start();
    }

    /* ⟨Start⟩ ::= start ⟨Bloque-Método⟩ */
    public void start() throws Exception {
        matchLexeme("start");
        //bloqueMetodo();
    }

    /* ⟨Lista-Definiciones⟩ ::= ⟨class⟩ ⟨Lista-Definiciones⟩
            | ⟨Impl⟩ ⟨Lista-Definiciones⟩
            | λ
    */
    public void listaDefiniciones() throws SyntacticException {
        if (checkLexeme("class")){
            // class();
            // listaDefiniciones();
        } else if (checkLexeme("impl")) {
            // impl()
            // listaDefiniciones();
        }else if (checkLexeme("start")){
            // Caso de lambda
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'class', 'impl' o 'start' pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /* 	⟨class⟩ ::= class idclass ⟨classF⟩ */
    public void class1() throws Exception {
        matchLexeme("class");
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        //classF();
    }

    /* 	⟨classF⟩ ::= { ⟨AtributoK⟩ }
            | ⟨Herencia⟩ { ⟨AtributoK⟩ }
    */
    public void classF() throws Exception {
        if (checkLexeme("{")){
            matchLexeme("{");
            // atributoK();
            matchLexeme("}");
        } else if (checkLexeme(":")) {
            // herencia();
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '{' o ':' pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨AtributoK⟩ ::= ⟨Atributo⟩ ⟨AtributoK⟩
            | λ
     */

    /*
        ⟨Impl ⟩ ::= impl idclass { ⟨MiembroK⟩ }
     */

    public void impl() throws Exception {
        matchLexeme("impl");
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        matchLexeme("{");
        // MiembroK()
        matchLexeme("}");
    }

    /*
        ⟨Herencia⟩ ::= : ⟨Tipo⟩
     */

    public void herencia() throws Exception {
        matchLexeme(":");
        //tipo()
    }

}
