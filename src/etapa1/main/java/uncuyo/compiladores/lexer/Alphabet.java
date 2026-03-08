package uncuyo.compiladores.lexer;

import java.util.HashSet;

public class Alphabet {
    static public HashSet<Character> alphabet = new HashSet<>();
    static {
        String chars =
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            "+-*/=<>!&|;:,.$#%@^" +
            "(){}[]" +
            "_";

        for (char c : chars.toCharArray()) {
            alphabet.add(c);
        }

        alphabet.add(' ');
        alphabet.add('\n');
        alphabet.add('\t');
    }

    public static boolean isValidCharacter(char c){
        return alphabet.contains(c);
    }

}
