package uncuyo.compiladores.lexer;

public enum TokenType {
    KW_CLASS,
    KW_IF,
    KW_IMPL,
    KW_ELSE,
    KW_FN,
    KW_RET,
    KW_WHILE,
    KW_NEW,
    KW_NIL,
    KW_ST,
    KW_SELF,
    KW_PUB,
    KW_DIV,
    KW_FOR,
    KW_IN,

    IDENTIFIER_CLASS,
    IDENTIFIER_OBJECT,
    IDENTIFIER_METHOD,

    INT_CONSTANT,
    STRING_CONSTANT,
    BOOLEAN_CONSTANT,

    SPECIAL_SYMBOL_LCB, //Left curly brace {
    SPECIAL_SYMBOL_RCB, //Right curly brace }
    SPECIAL_SYMBOL_PO, //Parenthesis open (
    SPECIAL_SYMBOL_PC, //Parenthesis close )
    SPECIAL_SYMBOL_S, //Semicolon ;
    SPECIAL_SYMBOL_CM, //Comma ,
    SPECIAL_SYMBOL_CL, // :
    SPECIAL_SYMBOL_P, //Punto .
    SPECIAL_SYMBOL_LB, // LEFT BRACKET [
    SPECIAL_SYMBOL_RB, // RIGHT BRACKT ]


    OPERATOR_ASSIGN, // =
    OPERATOR_ADD, // +
    OPERATOR_SUB, // -
    OPERATOR_MUL, // *
    OPERATOR_DIV, // /
    OPERATOR_EQ, // ==
    OPERATOR_NEQ, // !=
    OPERATOR_LT, // <
    OPERATOR_GT, // >
    OPERATOR_LTE, // <=
    OPERATOR_GTE, // >=
    OPERATOR_AND, // &&
    OPERATOR_OR, // ||
    OPERATOR_NOT, // !
    OPERATOR_INC, // ++
    OPERATOR_DEC, // --

    UNDEFINED,
    EOF
}