package uncuyo.compiladores.exceptions.semantic.symbolTableException;

import uncuyo.compiladores.lexer.Token;

public class InheritedAttributeRedeclaration extends SymbolTableException {
    public InheritedAttributeRedeclaration(Token currentAttributeToken, Token originalAttributeToken,
                                           String currentClassName, String originalClassName) {
        super(currentAttributeToken.getRowIndex(), currentAttributeToken.getColumnIndex(),
                "El atributo '" + currentAttributeToken.getLexeme() + "' definido en la clase '" + currentClassName +
                        "' ya fue definido anteriormente en la superclase '" + originalClassName + "' en la línea " +
                        originalAttributeToken.getRowIndex() + "(columna " + originalAttributeToken.getColumnIndex() + ").");
    }
}
