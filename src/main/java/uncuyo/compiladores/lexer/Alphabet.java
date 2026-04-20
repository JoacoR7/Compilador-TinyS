package uncuyo.compiladores.lexer;

import java.util.HashSet;

/**
 * Clase que representa el alfabeto de TinyS
 *
 * @author Victor Ramirez
 * @author Joaquin Ruiz
 */
public class Alphabet {
    static public HashSet<Character> alphabet = new HashSet<>();
    static {
        String chars =
            "abcdefghijklmnñopqrstuvwxyz" +
            "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ" +
            "0123456789" +
            "+-*/=<>!&|;:,.$#%@^%¿?¡!" +
            "(){}[]" +
            "_";

        for (char c : chars.toCharArray()) {
            alphabet.add(c);
        }

        alphabet.add(' ');
        alphabet.add('\n');
        alphabet.add('\t');
    }

    /**
     * Método estático que verifica si un caracter pertenece al alfabeto
     * @param c caracter a analizar
     * @return true si el caracter pertenece al alfabeto, false en caso contrario
     */
    public static boolean isValidCharacter(char c){
        return alphabet.contains(c);
    }

}
