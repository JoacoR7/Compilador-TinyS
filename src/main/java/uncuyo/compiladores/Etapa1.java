package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;

import java.io.*;
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
            do {
                token = lexer.getNextToken();
                tokenList.add(token);

            } while (token.getTokenType() != TokenType.EOF);
            if(args.length == 1) {
                System.out.println("CORRECTO: ANALISIS LEXICO");
                System.out.println("| TOKEN | LEXEMA | NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) |");
                for(Token tokenItem : tokenList) {
                    System.out.println("| " + tokenItem.getTokenType().toString() + " | " + tokenItem.getLexeme()
                            + " | " + tokenItem.getRowIndex() + " (" + tokenItem.getColumnIndex() + ") |");
                }
            } else if (args.length == 2) {
                File file = new File(args[1]);
                try {
                    if(!file.exists()) {
                        if(file.createNewFile()) {
                            System.out.println("Archivo creado: " + file.getAbsolutePath());
                        } else {
                            System.out.println("No se pudo crear el archivo");
                        }
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write("CORRECTO: ANALISIS LEXICO");
                    writer.newLine();
                    writer.write("| TOKEN | LEXEMA | NÚMERO DE LÍNEA (NÚMERO DE COLUMNA) |");
                    System.out.println("Archivo modificado: " + file.getAbsolutePath());
                    for(Token tokenItem : tokenList) {
                        writer.newLine();
                        writer.write("| " + tokenItem.getTokenType().toString() + " | " + tokenItem.getLexeme()
                                + " | " + tokenItem.getRowIndex() + " (" + tokenItem.getColumnIndex() + ") |");
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
