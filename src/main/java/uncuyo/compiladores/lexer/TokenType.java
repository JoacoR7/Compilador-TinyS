package uncuyo.compiladores.lexer;

public enum TokenType {
    /** Keyword 'class' **/
    KW_CLASS,
    /** Keyword 'if' **/
    KW_IF,
    /** Keyword 'impl' **/
    KW_IMPL,
    /** Keyword 'else' **/
    KW_ELSE,
    /** Keyword 'fn' **/
    KW_FN,
    /** Keyword 'ret' **/
    KW_RET,
    /** Keyword 'while' **/
    KW_WHILE,
    /** Keyword 'new' **/
    KW_NEW,
    /** Keyword 'nil' **/
    KW_NIL,
    /** Keyword 'st' **/
    KW_ST,
    /** Keyword 'self' **/
    KW_SELF,
    /** Keyword 'pub' **/
    KW_PUB,
    /** Keyword 'div' **/
    KW_DIV,
    /** Keyword 'for' **/
    KW_FOR,
    /** Keyword 'in' **/
    KW_IN,
    /** Keyword 'void' **/
    KW_VOID,

    /** Class identifier **/
    IDENTIFIER_CLASS,
    /** Variable or method identifier **/
    IDENTIFIER_VAR_METHOD,
    /**  **/
    /** Primitive class: integer type */
    INT_CLASS,
    /** Primitive class: boolean type */
    BOOL_CLASS,
    /** Primitive class: string type */
    STR_CLASS,
    /** Primitive class: array type */
    ARRAY_CLASS,

    /** Integer constant **/
    INT_CONSTANT,
    /** String constant **/
    STRING_CONSTANT,
    /** Boolean constant **/
    BOOLEAN_CONSTANT,

    /** Special symbol '{' **/
    SPECIAL_SYMBOL_LCB, //Left curly brace {
    /** Special symbol '}' **/
    SPECIAL_SYMBOL_RCB, //Right curly brace }
    /** Special symbol '(' **/
    SPECIAL_SYMBOL_PO, //Parenthesis open (
    /** Special symbol ')' **/
    SPECIAL_SYMBOL_PC, //Parenthesis close )
    /** Special symbol ';'**/
    SPECIAL_SYMBOL_S, //Semicolon ;
    /** Special symbol ',' **/
    SPECIAL_SYMBOL_CM, //Comma ,
    /** Special symbol ':' **/
    SPECIAL_SYMBOL_CL, // :
    /** Special symbol '.' **/
    SPECIAL_SYMBOL_P, //Punto .
    /** Special symbol '[' **/
    SPECIAL_SYMBOL_LB, // LEFT BRACKET [
    /** Special symbol ']' **/
    SPECIAL_SYMBOL_RB, // RIGHT BRACKT ]

    /** Operator '=' **/
    OPERATOR_ASSIGN, // =
    /** Operator '+' **/
    OPERATOR_ADD, // +
    /** Operator '-' **/
    OPERATOR_SUB, // -
    /** Operator '*' **/
    OPERATOR_MUL, // *
    /** Operator '/' **/
    OPERATOR_DIV, // /
    /** Operator '==' **/
    OPERATOR_EQ, // ==
    /** Operator '!=' **/
    OPERATOR_NEQ, // !=
    /** Operator '<' **/
    OPERATOR_LT, // <
    /** Operator '>' **/
    OPERATOR_GT, // >
    /** Operator '<=' **/
    OPERATOR_LTE, // <=
    /** Operator '>=' **/
    OPERATOR_GTE, // >=
    /** Operator '&&' **/
    OPERATOR_AND, // &&
    /** Operator '||' **/
    OPERATOR_OR, // ||
    /** Operator '!' **/
    OPERATOR_NOT, // !
    /** Operator '++' **/
    OPERATOR_INC, // ++
    /** Operator '--' **/
    OPERATOR_DEC, // --

    /** Undefined token **/
    UNDEFINED,
    /** EOF token**/
    EOF
}