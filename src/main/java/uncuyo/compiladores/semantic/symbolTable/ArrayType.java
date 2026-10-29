package uncuyo.compiladores.semantic.symbolTable;

import uncuyo.compiladores.lexer.TokenType;

/**
 * Tipo de TinyS que conserva el tipo primitivo de los elementos de Array.
 */
public class ArrayType extends Type {
    private Type subType;

    /**
     * Crea un tipo de arreglo con el tipo de elemento indicado.
     *
     * @param subType tipo de los elementos
     */
    public ArrayType(Type subType) {
        super(TokenType.ARRAY_CLASS, "Array");
        this.subType = subType;
    }

    /**
     * Obtiene el tipo de los elementos del arreglo.
     *
     * @return tipo de elemento almacenado
     */
    public Type getSubType() {
        return subType;
    }
}
