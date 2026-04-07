package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.syntactic.SyntacticAnalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Etapa2 {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {

        if(args.length < 1) {
            System.out.println("ERROR: Debe especificar el archivo fuente");
        } else {
            Lexer lexer = new Lexer(args[0]);
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexer, lexer.getNextToken());
            try {
                syntacticAnalyzer.program();
                System.out.println("CORRECTO: ANALISIS SINTÁCTICO");
            } catch (SyntacticException e) {
                System.out.println(e.getMessage());
            } catch (LexicalException e) {
                System.out.println("ERROR: LEXICO \n" + e.getMessage() );
            }

        }
    }
}
