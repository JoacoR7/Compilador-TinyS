package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.lexer.Token;

import java.util.Iterator;
import java.util.Set;

public class CircularInheritanceException extends SymbolTableException {

    public CircularInheritanceException(Token token, String className, Set<String> visited) {
        super(
                token.getRowIndex(),
                token.getColumnIndex(),
                buildMessage(token, className, visited)
        );
    }

    private static String buildMessage(Token token, String className, Set<String> visited) {
        StringBuilder sb = new StringBuilder();
        sb.append(token.getLexeme()).append(" ---> ");
        Iterator<String> it = visited.iterator();
        while (it.hasNext()) {
            String s = it.next();
            sb.append(s);
            if (it.hasNext()) {
                sb.append(" ---> ");
            }
        }

        return "Herencia circular detectada en la clase '"
                + token.getLexeme()
                + "' con la superclase '"
                + className
                + "', camino: "
                + sb.toString();
    }
}
