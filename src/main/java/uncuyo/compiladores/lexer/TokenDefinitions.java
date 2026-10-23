
package uncuyo.compiladores.lexer;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Define y administra los elementos léxicos reconocidos por el analizador
 * léxico, incluyendo palabras reservadas, símbolos especiales, operadores,
 * símbolos no definidos y tipos primitivos.
 *
 * <p>Las definiciones se almacenan en maps que relacionan cada lexema con
 * su correspondiente tipo de token ({@link TokenType}).</p>
 */
public class TokenDefinitions {

    /**
     * Map que relaciona las palabras reservadas del lenguaje con sus
     * respectivos tipos de token.
     */
    private final Map<String, TokenType> keywords = new HashMap<>();

    /**
     * Map que relaciona los símbolos especiales de un único carácter
     * con sus respectivos tipos de token.
     */
    private final Map<Character, TokenType> specialSymbols = new HashMap<>();

    /**
     * Map que relaciona los operadores del lenguaje con sus
     * respectivos tipos de token.
     */
    private final Map<String, TokenType> operators = new HashMap<>();

    /**
     * Map que relaciona los símbolos no definidos del lenguaje con
     * el tipo de token {@link TokenType#UNDEFINED}.
     */
    private final Map<Character, TokenType> undefinedSimbols = new HashMap<>();

    /**
     * Map que relaciona los nombres de los tipos primitivos del lenguaje
     * con sus respectivos tipos de token.
     */
    private final Map<String, TokenType> primitiveTypes = new HashMap<>();

    /**
     * Inicializa las palabras reservadas del lenguaje y las asocia con
     * sus respectivos tipos de token.
     *
     * <p>Incluye palabras como {@code class}, {@code if}, {@code else},
     * {@code fn}, {@code ret}, {@code while}, {@code new}, {@code self},
     * entre otras. También contempla las constantes booleanas
     * {@code true} y {@code false}.</p>
     */
    private void initKeywords() {
        keywords.put("class", TokenType.KW_CLASS);
        keywords.put("if", TokenType.KW_IF);
        keywords.put("impl", TokenType.KW_IMPL);
        keywords.put("else", TokenType.KW_ELSE);
        keywords.put("fn", TokenType.KW_FN);
        keywords.put("ret", TokenType.KW_RET);
        keywords.put("while", TokenType.KW_WHILE);
        keywords.put("new", TokenType.KW_NEW);
        keywords.put("nil", TokenType.KW_NIL);
        keywords.put("st", TokenType.KW_ST);
        keywords.put("self", TokenType.KW_SELF);
        keywords.put("pub", TokenType.KW_PUB);
        keywords.put("for", TokenType.KW_FOR);
        keywords.put("in", TokenType.KW_IN);
        keywords.put("true", TokenType.BOOLEAN_CONSTANT);
        keywords.put("false", TokenType.BOOLEAN_CONSTANT);
        keywords.put("void", TokenType.KW_VOID);
    }

    /**
     * Inicializa los símbolos especiales del lenguaje y los asocia con
     * sus respectivos tipos de token.
     *
     * <p>Incluye llaves, paréntesis, corchetes, punto y coma, coma,
     * punto y dos puntos.</p>
     */
    private void initSpecialSymbols() {
        specialSymbols.put('{', TokenType.SPECIAL_SYMBOL_LCB);
        specialSymbols.put('}', TokenType.SPECIAL_SYMBOL_RCB);
        specialSymbols.put('(', TokenType.SPECIAL_SYMBOL_PO);
        specialSymbols.put(')', TokenType.SPECIAL_SYMBOL_PC);
        specialSymbols.put(';', TokenType.SPECIAL_SYMBOL_S);
        specialSymbols.put(',', TokenType.SPECIAL_SYMBOL_CM);
        specialSymbols.put('.', TokenType.SPECIAL_SYMBOL_P);
        specialSymbols.put('[', TokenType.SPECIAL_SYMBOL_LB);
        specialSymbols.put(']', TokenType.SPECIAL_SYMBOL_RB);
        specialSymbols.put(':', TokenType.SPECIAL_SYMBOL_CL);
    }

    /**
     * Inicializa los operadores del lenguaje y los asocia con sus
     * respectivos tipos de token.
     *
     * <p>Contempla operadores de asignación, aritméticos, relacionales,
     * lógicos y de incremento y decremento. Los símbolos {@code &} y
     * {@code |} se clasifican como no definidos cuando aparecen
     * individualmente.</p>
     */
    private void initOperators() {
        // Operador de asignación
        operators.put("=", TokenType.OPERATOR_ASSIGN);
        operators.put("&", TokenType.UNDEFINED);
        operators.put("|", TokenType.UNDEFINED);

        // Operadores aritméticos
        operators.put("+", TokenType.OPERATOR_ADD);  // Suma
        operators.put("-", TokenType.OPERATOR_SUB);  // Resta
        operators.put("*", TokenType.OPERATOR_MUL);  // Multiplicación
        operators.put("/", TokenType.OPERATOR_DIV);  // División

        // Operadores relacionales
        operators.put("==", TokenType.OPERATOR_EQ);  // Igualdad
        operators.put("!=", TokenType.OPERATOR_NEQ); // Diferente
        operators.put("<", TokenType.OPERATOR_LT);   // Menor que
        operators.put(">", TokenType.OPERATOR_GT);   // Mayor que
        operators.put("<=", TokenType.OPERATOR_LTE); // Menor o igual que
        operators.put(">=", TokenType.OPERATOR_GTE); // Mayor o igual que

        // Operadores lógicos
        operators.put("&&", TokenType.OPERATOR_AND); // AND lógico
        operators.put("||", TokenType.OPERATOR_OR);  // OR lógico
        operators.put("!", TokenType.OPERATOR_NOT);  // NOT lógico

        // Operadores de incremento y decremento
        operators.put("++", TokenType.OPERATOR_INC); // Incremento
        operators.put("--", TokenType.OPERATOR_DEC); // Decremento

    }

    /**
     * Inicializa los símbolos considerados no definidos por el lenguaje
     * y los asocia con el tipo de token {@link TokenType#UNDEFINED}.
     */
    private void initUndefinedSymbols() {
        undefinedSimbols.put('@', TokenType.UNDEFINED);
        undefinedSimbols.put('$', TokenType.UNDEFINED);
        undefinedSimbols.put('#', TokenType.UNDEFINED);
        undefinedSimbols.put('%', TokenType.UNDEFINED);
        undefinedSimbols.put('?', TokenType.UNDEFINED);
        undefinedSimbols.put('¿', TokenType.UNDEFINED);
        undefinedSimbols.put('^', TokenType.UNDEFINED);
        undefinedSimbols.put('_', TokenType.UNDEFINED);
    }

    /**
     * Inicializa los tipos primitivos del lenguaje y los asocia con
     * sus respectivos tipos de token.
     *
     * <p>Los tipos contemplados son {@code Str}, {@code Bool},
     * {@code Int} y {@code Array}.</p>
     */
    private void initPrimitiveTypes() {
        primitiveTypes.put("Str", TokenType.STR_CLASS);
        primitiveTypes.put("Bool", TokenType.BOOL_CLASS);
        primitiveTypes.put("Int", TokenType.INT_CLASS);
        primitiveTypes.put("Array", TokenType.ARRAY_CLASS);
    }

    /**
     * Construye una instancia de {@code TokenDefinitions} e inicializa
     * todas las definiciones léxicas del lenguaje.
     */
    public TokenDefinitions() {
        initKeywords();
        initSpecialSymbols();
        initOperators();
        initUndefinedSymbols();
        initPrimitiveTypes();
    }

    /**
     * Obtiene el map de palabras reservadas del lenguaje.
     *
     * @return map que relaciona cada palabra reservada con su tipo de token
     */
    public Map<String, TokenType> getKeywords() {
        return keywords;
    }

    /**
     * Obtiene el map de símbolos especiales del lenguaje.
     *
     * @return map que relaciona cada símbolo especial con su tipo de token
     */
    public Map<Character, TokenType> getSpecialSymbols() {
        return specialSymbols;
    }

    /**
     * Obtiene el map de operadores del lenguaje.
     *
     * @return map que relaciona cada operador con su tipo de token
     */
    public Map<String, TokenType> getOperators() {
        return operators;
    }

    /**
     * Obtiene el map de símbolos no definidos.
     *
     * @return map que relaciona cada símbolo no definido con su tipo de token
     */
    public Map<Character, TokenType> getUndefinedSymbols() {
        return undefinedSimbols;
    }

    /**
     * Obtiene el map de tipos primitivos del lenguaje.
     *
     * @return map que relaciona cada tipo primitivo con su tipo de token
     */
    public Map<String, TokenType> getPrimitiveTypes() {
        return primitiveTypes;
    }

    /**
     * Obtiene un lexema representativo asociado a un tipo de token.
     *
     * <p>Busca el primer lexema cuyo valor esté asociado al tipo de token
     * recibido, recorriendo las definiciones de palabras reservadas,
     * operadores, tipos primitivos, símbolos especiales y símbolos
     * no definidos. Si no encuentra un lexema asociado, devuelve el
     * nombre del tipo de token.</p>
     *
     * @param tokenType tipo de token cuyo lexema se desea obtener
     * @return primer lexema encontrado para el tipo indicado o el nombre
     *         del tipo de token si no existe una asociación
     */
    public static String getLexeme(TokenType tokenType) {
        TokenDefinitions defs = new TokenDefinitions();

        return Stream.of(
                        defs.getKeywords().entrySet().stream(),
                        defs.getOperators().entrySet().stream(),
                        defs.getPrimitiveTypes().entrySet().stream(),
                        defs.getSpecialSymbols().entrySet().stream().map(e -> Map.entry(String.valueOf(e.getKey()), e.getValue())),
                        defs.getUndefinedSymbols().entrySet().stream().map(e -> Map.entry(String.valueOf(e.getKey()), e.getValue()))
                )
                .flatMap(s -> s)
                .filter(e -> e.getValue() == tokenType)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(tokenType.name());
    }
}
