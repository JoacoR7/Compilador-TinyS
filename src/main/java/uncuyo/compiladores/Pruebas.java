package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;

import java.io.IOException;
import java.util.Optional;

public class Pruebas {
    public static void main(String[] args) throws IOException, LexicalException {
        Lexer lexer = new Lexer("C:\\Users\\AMD\\Desktop\\Compiladores2026\\src\\etapa1\\main\\java\\uncuyo\\compiladores\\prueba.txt");
        Token token;
        while ((token = lexer.getNextToken()) != null) {
            System.out.println(token);
        }
    }
}
