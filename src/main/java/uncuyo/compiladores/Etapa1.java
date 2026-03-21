package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Etapa1 {
    public static void main(String[] args) throws IOException, LexicalException {

        if(args.length < 1) {
            System.out.println("ERROR: Debe especificar el archivo fuente");
        } else {
            Lexer lexer = new Lexer(args[0]);
            List<Token> tokenList = new ArrayList<>();
            Token token;
            while ((token = lexer.getNextToken()) != null) {
                tokenList.add(token);
            }
            if(args.length == 1) {
                System.out.println("CORRECTO: ANALISIS LEXICO");
                System.out.println("| TOKEN | LEXEMA | NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) |");
                for(Token tokenItem : tokenList) {
                    System.out.println("| " + tokenItem.getTokenType().toString() + " | " + tokenItem.getLexeme()
                            + " | " + tokenItem.getRowIndex() + " (" + tokenItem.getColumnIndex() + ") |");
                }
            }
        }
    }
}
