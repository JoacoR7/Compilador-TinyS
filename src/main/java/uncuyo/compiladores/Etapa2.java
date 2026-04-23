package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.syntactic.SyntacticAnalyzer;

import java.io.IOException;

public class Etapa2 {
    public static void main(String[] args) throws IOException, LexicalException, SyntacticException {

        if(args.length < 1) {
            System.out.println("ERROR: Debe especificar el archivo fuente");
        } else {
            Lexer lexer = new Lexer(args[0]);
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexer, lexer.getNextToken());
            try {
                syntacticAnalyzer.program();
                System.out.println("CORRECTO: ANALISIS SINTACTICO");
            } catch (SyntacticException e) {
                e.printStackTrace();
            } catch (LexicalException e) {
                System.out.println("ERROR: LEXICO \n" + e.getMessage() );
            }

        }
    }
}
