package uncuyo.compiladores.syntactic;

import uncuyo.compiladores.exceptions.LexerFileReadException;
import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;

import java.io.IOException;


public class SyntacticAnalyzer {

    private Lexer lexer;
    private Token currentToken;

    private void nextToken() throws LexerFileReadException, LexicalException {
        currentToken = lexer.getNextToken();
    }

    private boolean checkLexeme(String... lexemes) {
        for (String lex : lexemes) {
            if (currentToken.getLexeme().equals(lex)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkType(TokenType... types) {
        for (TokenType type : types) {
            if (currentToken.getTokenType() == type) {
                return true;
            }
        }
        return false;
    }

    private void matchLexeme(String expected) throws LexerFileReadException, LexicalException, SyntacticException {
        if (currentToken.getLexeme().equals(expected)) {
            nextToken();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '" + expected + "', pero se encontro '" +
                            currentToken.getLexeme() + "'");
        }
    }

    private void matchTokenType(TokenType expectedTokenType) throws LexerFileReadException, LexicalException, SyntacticException {

        if (currentToken.getTokenType().equals(expectedTokenType)){
            nextToken();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba " + expectedTokenType + ", pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }

    }

    /* ⟨program⟩ ::= ⟨Lista-Definiciones⟩ ⟨Start⟩ */
    public void program() throws Exception {
        listaDefiniciones();
        start();
        matchTokenType(TokenType.EOF);
        System.out.println("CORRECTO: ANALISIS SINTACTICO");
    }

    /*
        ⟨Start⟩ ::= start ⟨Bloque-Método⟩
    INCOMPLETO
    */
    public void start() throws LexerFileReadException, LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        //DUDA CON START
        //bloqueMetodo();
    }

    /* ⟨Lista-Definiciones⟩ ::= ⟨class⟩ ⟨Lista-Definiciones⟩
            | ⟨Impl⟩ ⟨Lista-Definiciones⟩
            | λ
    INCOMPLETO
    */
    public void listaDefiniciones() throws Exception {
        if (checkType(TokenType.KW_CLASS)){
            class_();
            listaDefiniciones();
        } else if (checkType(TokenType.KW_IMPL)) {
            impl();
            listaDefiniciones();
        } else if (checkType(TokenType.IDENTIFIER_VAR_METHOD)){
            //DUDA CON START
            // Caso de lambda
            // Siguientes de listaDefiniciones, cierro
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'class', 'impl' o 'start', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨class⟩ ::= class idclass ⟨classF⟩
    */
    public void class_() throws Exception {
        matchTokenType(TokenType.KW_CLASS);
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        classF();
    }

    /* 	⟨classF⟩ ::= { ⟨AtributoK⟩ }
            | ⟨Herencia⟩ { ⟨AtributoK⟩ }
    INCOMPLETO
    */
    public void classF() throws Exception {
        if(checkType(TokenType.SPECIAL_SYMBOL_LCB)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
            //atributoK();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
        } else if (checkType(TokenType.SPECIAL_SYMBOL_CL)) {
            herencia();
            matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
            //atributoK();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '{' o ':', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨AtributoK⟩ ::= ⟨Atributo⟩ ⟨AtributoK⟩
            | λ
     */

    /*
        ⟨Impl ⟩ ::= impl idclass { ⟨MiembroK⟩ }
    INCOMPLETO
     */
    public void impl() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_IMPL);
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        // MiembroK()
        matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
    }

    /*
        ⟨Herencia⟩ ::= : ⟨Tipo⟩
     */
    public void herencia() throws Exception {
        matchTokenType(TokenType.SPECIAL_SYMBOL_CL);
        tipo();
    }

    /*
    ⟨MiembroK⟩ ::= ⟨Miembro⟩ ⟨MiembroK⟩
    | λ

    ⟨Miembro⟩ ::= ⟨Método⟩
    | ⟨Constructor⟩

    ⟨Constructor ⟩ ::= . ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩

    ⟨Atributo⟩ ::= ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;
    | ⟨Visibilidad⟩ ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;

    ⟨Método⟩ ::= fn ⟨MétodoF⟩
        | ⟨Forma-Método⟩ fn ⟨MétodoF⟩

    ⟨MétodoF⟩ ::= idMetAt ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩
        | ⟨Tipo-Método⟩ idMetAt ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩

    */
    /*
        ⟨Visibilidad ⟩ ::= pub
    */
    public void visibilidad() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_PUB);
    }

    /*
        ⟨Forma-Método⟩ ::= st
     */
    public void formaMetodo() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_ST);
    }

    /*
    ⟨Bloque-Método⟩ ::= { ⟨Decl-Var-LocalesK⟩ ⟨SentenciaK⟩ }

    ⟨Decl-Var-LocalesK⟩ ::= ⟨Decl-Var-Locales⟩ ⟨Decl-Var-LocalesK⟩
    | λ

    ⟨Decl-Var-Locales⟩ ::= ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;
    
    */
    /*
        ⟨Lista-Declaración-Variables⟩ ::= idMetAt ⟨Lista-Declaración-VariablesF⟩
    */
    public void listaDeclaracionVariables() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        listaDeclaracionVariablesF();
    }
    
    /*
        ⟨Lista-Declaración-VariablesF⟩ ::= , ⟨Lista-Declaración-Variables⟩
            | λ
    */
    public void listaDeclaracionVariablesF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_CM)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_CM);
            listaDeclaracionVariables();
        } else if (checkType(TokenType.STRING_CONSTANT, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT,
                TokenType.IDENTIFIER_CLASS, TokenType.SPECIAL_SYMBOL_S, TokenType.KW_WHILE, TokenType.KW_FOR,
                TokenType.KW_IF, TokenType.KW_RET, TokenType.SPECIAL_SYMBOL_PO, TokenType.SPECIAL_SYMBOL_LCB
                TokenType.IDENTIFIER_VAR_METHOD, TokenType.KW_SELF, TokenType.SPECIAL_SYMBOL_RCB)) {
            // Siguientes de listaDeclaracionVariablesF, cierro
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ',', string, booleano, entero, un identificador de clase, ';' , " +
                            "'while', 'for', 'if', 'ret', '(' , '{' , identificador de variable o método, " +
                            "'self' o '}', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    
    /*
	⟨Argumentos-Formales⟩::= ( ⟨Argumentos-FormalesF⟩

	⟨Argumentos-FormalesF⟩ ::= )
		| ⟨Lista-Argumentos-Formales⟩ )

    */
    /*
        ⟨Lista-Argumentos-Formales⟩ ::= ⟨Argumento-Formal ⟩ ⟨Lista-Argumentos-FormalesF⟩
    */
    public void listaArgumentosFormales() throws LexicalException, SyntacticException {
        argumentoFormal();
        listaArgumentosFormalesF();
    }
    
    /*
        ⟨Lista-Argumentos-FormalesF⟩ ::=  , ⟨Lista-Argumentos-Formales⟩
            | λ
    */
    public void listaArgumentosFormalesF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_CM)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_CM);
            listaArgumentosFormales();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_PC)) {
            // Siguientes de listaArgumentosFormalesF, cierro
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ',' o ')', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    
    /*
        ⟨Argumento-Formal ⟩ ::= ⟨Tipo⟩ idMetAt
    */
    public void argumentoFormal() throws LexicalException, SyntacticException {
        tipo();
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
    }

    /*
        ⟨Tipo-Método⟩ ::= ⟨Tipo⟩
            | void
    */
    public void tipoMetodo() throws LexicalException, SyntacticException {
        //DUDA: CÓMO HACEMOS CON ARRAY? ya que puede venir idclass o Array, Array tmb sería un idclass
        if(checkType(TokenType.STRING_CONSTANT, TokenType.BOOLEAN_CONSTANT,
                TokenType.INT_CONSTANT, TokenType.IDENTIFIER_CLASS)) {
            tipo();
        } else if (checkType(TokenType.KW_VOID)) {
            matchTokenType(TokenType.KW_VOID);
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int', 'void' o un identificador de clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨Tipo⟩ ::= ⟨Tipo-Primitivo⟩
            | ⟨Tipo-Referencia⟩
            | ⟨Tipo-Arreglo⟩
    */
    public void tipo() throws LexicalException, SyntacticException {
        if(checkType(TokenType.STRING_CONSTANT, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT)) {
            tipoPrimitivo();
        } else if (checkType(TokenType.IDENTIFIER_CLASS)) {
            tipoReferencia();
            //DUDA: QUE HACEMOS CON ARRAY SI ES IDCLASS
        } else if (checkLexeme("Array")) {
            tipoArreglo();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int', identificador de clase o 'Array', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨Tipo-Primitivo⟩ ::= Str
            | Bool
            | Int
    */
    public void tipoPrimitivo() throws LexicalException, SyntacticException {
        //DUDA: SERIA IDCLASS? O COMPARAMOS EL LEXEMA
        if(checkType(TokenType.STRING_CONSTANT)) {
            matchTokenType(TokenType.STRING_CONSTANT);
        } else if(checkType(TokenType.BOOLEAN_CONSTANT)) {
            matchTokenType(TokenType.BOOLEAN_CONSTANT);
        } else if (checkType(TokenType.INT_CONSTANT)) {
            matchTokenType(TokenType.INT_CONSTANT);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool' o 'Int', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨Tipo-Referencia⟩ ::= idclass
    */
    public void tipoReferencia() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_CLASS);
    }
    /*
        ⟨Tipo-Arreglo⟩ ::= Array ⟨Tipo-Primitivo⟩
    */
    public void tipoArreglo() throws LexicalException, SyntacticException {
        // DUDA: MATCHEO CON LEXEMA O IDCLASS?
        matchLexeme("Array");
        tipoPrimitivo();
    }

    /*

    ⟨SentenciaK⟩ ::= ⟨Sentencia⟩ ⟨SentenciaK⟩
        | λ
    */

    /*
    ⟨Sentencia⟩ ::= ;
        | ⟨Asignación⟩;
        | ⟨Sentencia-Simple⟩;
        | while ( ⟨Expresión⟩ ) ⟨Sentencia⟩
        | for ( ⟨Tipo-Primitivo⟩ idMetAt in idMetAt ) ⟨Sentencia⟩
        | ⟨Bloque⟩
        | if (⟨Expresión⟩) ⟨Sentencia⟩ ⟨SentenciaF1⟩
        | ret ⟨SentenciaF2⟩
    */
    public void sentencia() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_S)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if(checkType(TokenType.SPECIAL_SYMBOL_PO)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        } else if (checkType(TokenType.KW_WHILE)) {
            matchTokenType(TokenType.KW_WHILE);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
            expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
            sentencia();
        } else if (checkType(TokenType.KW_FOR)) {
            matchTokenType(TokenType.KW_FOR);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
            tipoPrimitivo();
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            matchTokenType(TokenType.KW_IN);
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
            sentencia();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LCB)) {
            bloque();
        } else if (checkType(TokenType.KW_IF)) {
            matchTokenType(TokenType.KW_IF);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
            expresion();
            sentencia();
            sentenciaF1();
        } else if (checkType(TokenType.KW_RET)) {
            matchTokenType(TokenType.KW_RET);
            sentenciaF2();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ';' , 'while', 'for', 'if', 'ret' , '{', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    /*
	⟨SentenciaF1⟩ ::= else ⟨Sentencia⟩
		| λ
    */
    public void sentenciaF1() throws LexicalException, SyntacticException {
        // DUDA: AMBIGUEDAD? CON ELSE PODEMOS IR A 2 CAMINOS DISTINTOS
        if (checkType(TokenType.KW_ELSE)) {
            matchTokenType(TokenType.KW_ELSE);
            sentencia();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_S, TokenType.KW_WHILE, TokenType.KW_FOR,
                TokenType.KW_IF, TokenType.KW_RET, TokenType.SPECIAL_SYMBOL_PO, TokenType.SPECIAL_SYMBOL_LCB,
                TokenType.IDENTIFIER_VAR_METHOD, TokenType.KW_SELF, TokenType.SPECIAL_SYMBOL_RCB)) {
            // Siguientes de sentenciaF1, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ';' , 'while', 'for', 'if', 'ret', '(' , '{' , 'id', 'self' , '}', 'else', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
	⟨SentenciaF2⟩ ::= ;
		| ⟨Expresión⟩;
    */
    public void sentenciaF2() throws LexicalException, SyntacticException {
        if(checkType(TokenType.SPECIAL_SYMBOL_S)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if (checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB, TokenType.OPERATOR_NOT,
            TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC, TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT,
                    TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT, TokenType.SPECIAL_SYMBOL_PO,
                    TokenType.KW_SELF, TokenType.IDENTIFIER_CLASS, TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {
            expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ';' '+', '-', '!', '++', '--', 'nil', 'true', 'false', entero, string, '(', 'self', identificador de clase, 'new' " +
                            "o identificador de variable o metodo, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    /*
        ⟨Bloque⟩ ::= { ⟨SentenciaK⟩ }
    INCOMPLETO
    */
    public void bloque() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        // sentenciaK();
        matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
    }

    /*
    ⟨Asignación⟩ ::= ⟨AccesoVar-Simple⟩ = ⟨Expresión⟩
        | ⟨AccesoSelf-Simple⟩ = ⟨Expresión⟩
    */
    public void asignacion() throws LexicalException, SyntacticException {
        if (checkType(TokenType.IDENTIFIER_VAR_METHOD)) {
            accesoVarSimple();
            matchTokenType(TokenType.OPERATOR_ASSIGN);
            expresion();
        } else if (checkType(TokenType.KW_SELF)) {
            accesoSelfSimple();
            matchTokenType(TokenType.OPERATOR_ASSIGN);
            expresion();
        }
    }

     /*
	⟨AccesoVar-Simple⟩ ::= id ⟨AccesoVar-SimpleF⟩
     */
    public void accesoVarSimple() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        accesoVarSimpleF();
    }

    /*
    ⟨AccesoVar-SimpleF⟩ ::= ⟨Encadenado-SimpleK⟩
        | [ ⟨Expresión⟩ ]
     */
    public void accesoVarSimpleF() throws LexicalException, SyntacticException {
        if(checkType(TokenType.SPECIAL_SYMBOL_P)) {
            encadenadoSimpleK();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
        } else if (checkType(TokenType.OPERATOR_ASSIGN)) {
            // Primeros(⟨Encadenado-SimpleK⟩) contiene λ, por lo tanto checkeo siguientes
            // Siguientes de accesoVarSimpleF, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '=', '.', '[' pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨AccesoSelf-Simple⟩ ::= self ⟨Encadenado-SimpleK⟩
    */
    public void accesoSelfSimple() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_SELF);
        encadenadoSimpleK();
    }

    /*
    ⟨Encadenado-SimpleK⟩ ::= ⟨Encadenado-Simple⟩ ⟨Encadenado-SimpleK⟩
        | λ
    */
    public void encadenadoSimpleK() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
            encadenadoSimple();
            encadenadoSimpleK();
        } else if (checkType(TokenType.OPERATOR_ASSIGN)) {
            // Siguientes de encadenadoSimpleK, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '=', '.', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨Encadenado-Simple⟩ ::= . id
    */
    public void encadenadoSimple() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
    }

    /*
    ⟨Sentencia-Simple⟩ ::= (⟨Expresión⟩)
    */
    public void sentenciaSimple() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        expresion();
        matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
    }

    /*
    ⟨Expresión⟩ ::= ⟨ExpOr⟩
    */
    public void expresion() throws LexicalException, SyntacticException {
        expOrR();
    }
    /*
	⟨ExpOr⟩ ::= ⟨ExpAnd⟩ ⟨ExpOrR⟩
    */
    public void expOr() throws LexicalException, SyntacticException {
        expAnd();
        expOrR();
    }

    /*
    ⟨ExpOrR⟩ ::= || ⟨ExpAnd⟩ ⟨ExpOrR⟩
        | λ
    */
    public void expOrR() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_OR)) {
            matchTokenType(TokenType.OPERATOR_OR);
            expAnd();
            expOrR();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_PC,TokenType.SPECIAL_SYMBOL_S,
                TokenType.SPECIAL_SYMBOL_RB, TokenType.SPECIAL_SYMBOL_CM)) {
            // Siguientes de expOrR, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
	⟨ExpAnd⟩ ::= ⟨ExpIgual⟩ ⟨ExpAndR⟩
    */
    public void expAnd() throws LexicalException, SyntacticException {
        expIgual();
        expAndR();
    }

    /*
    ⟨ExpAndR⟩ ::= && ⟨ExpIgual ⟩ ⟨ExpAndR⟩
        | λ
    */
    public void expAndR() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_AND)) {
            matchTokenType(TokenType.OPERATOR_AND);
            expIgual();
            expAndR();
        } else if (checkType(TokenType.OPERATOR_OR, TokenType.SPECIAL_SYMBOL_PC,
                TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB, TokenType.SPECIAL_SYMBOL_CM)) {
            // Siguientes de expAndR, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
	⟨ExpIgual ⟩ ::= ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩
    */
    public void expIgual() throws LexicalException, SyntacticException {
        expCompuesta();
        expIgualR();
    }

    /*
    ⟨ExpIgualR⟩ ::= ⟨OpIgual⟩ ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩
        | λ
    */
    public void expIgualR() throws LexicalException, SyntacticException {
        if(checkType(TokenType.OPERATOR_EQ, TokenType.OPERATOR_NEQ)) {
            opIgual();
            expCompuesta();
            expIgualR();
        } else if (checkType(TokenType.OPERATOR_AND, TokenType.OPERATOR_OR, TokenType.SPECIAL_SYMBOL_PC,
                TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB, TokenType.SPECIAL_SYMBOL_CM)) {
            // Siguientes de expIgualR, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
	⟨ExpCompuesta⟩ ::= ⟨ExpAd⟩ ⟨ExpCompuestaF⟩
    */
    public void expCompuesta() throws LexicalException, SyntacticException {
        expAd();
        expCompuestaF();
    }

    /*
	⟨ExpCompuestaF⟩ ::= ⟨OpCompuesto⟩ ⟨ExpAd⟩
		| λ
    */
    public void expCompuestaF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_GT, TokenType.OPERATOR_LT, TokenType.OPERATOR_GTE, TokenType.OPERATOR_LTE)) {
            opCompuesto();
            expAd();
        } else if (checkType(TokenType.OPERATOR_EQ, TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND,
                TokenType.OPERATOR_OR, TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {
            // Siguientes de expCompuestaF, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨ExpAd ⟩ ::= ⟨ExpMul⟩ ⟨ExpAdR⟩
    */
    public void expAd() throws LexicalException, SyntacticException {
        expMul();
        expAdR();
    }

    /*
    ⟨ExpAdR⟩ ::= ⟨OpAd⟩ ⟨ExpMul⟩ ⟨ExpAdR⟩
        | λ
    */
    public void expAdR() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB)) {
            opAd();
            expMul();
            expAdR();
        } else if (checkType(TokenType.OPERATOR_LT, TokenType.OPERATOR_GT, TokenType.OPERATOR_LTE,
                TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ, TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND,
                TokenType.OPERATOR_OR, TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {
            // Siguientes de expAdR, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    /*
    ⟨ExpMul⟩ ::= ⟨ExpUn⟩ ⟨ExpMulR⟩
    */
    public void expMul() throws LexicalException, SyntacticException {
        expUn();
        expMulR();
    }

    /*
    ⟨ExpMulR⟩ ::= ⟨OpMul⟩ ⟨ExpUn⟩ ⟨ExpMulR⟩
        | λ
    */
    public void expMulR() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV)) {
            opMul();
            expUn();
            expMulR();
        } else if (checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT,
            TokenType.OPERATOR_GT, TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR, TokenType.SPECIAL_SYMBOL_PC,
                TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB, TokenType.SPECIAL_SYMBOL_CM)) {
            // Siguientes de expMulR, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨ExpUn⟩ ::= ⟨OpUnario⟩ ⟨ExpUn⟩
        | ⟨Operando⟩
    */
    public void expUn() throws LexicalException, SyntacticException {
        if(checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB, TokenType.OPERATOR_NOT,
                TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC)) {
            opUnario();
            expUn();
        } else if (checkType(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT,
                    TokenType.SPECIAL_SYMBOL_PO, TokenType.KW_SELF, TokenType.IDENTIFIER_CLASS,
                    TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {
            operando();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '+', '-', '!', '++', '--', 'nil', 'true', 'false', entero, string, '(', 'self', identificador de clase, 'new' " +
                            "o identificador de variable o metodo, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    /*
    ⟨OpIgual ⟩ ::= ==
        | !=
    */
    public void opIgual() throws LexicalException, SyntacticException {
        if(checkType(TokenType.OPERATOR_EQ)) {
            matchTokenType(TokenType.OPERATOR_EQ);
        } else if (checkType(TokenType.OPERATOR_NEQ)) {
            matchTokenType(TokenType.OPERATOR_NEQ);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '==' o '!=', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨OpCompuesto⟩ ::= <
        | >
        | <=
        | >=
    */
    public void opCompuesto() throws LexicalException, SyntacticException {
        if(checkType(TokenType.OPERATOR_LT)) {
            matchTokenType(TokenType.OPERATOR_LT);
        } else if (checkType(TokenType.OPERATOR_GT)) {
            matchTokenType(TokenType.OPERATOR_GT);
        } else if (checkType(TokenType.OPERATOR_LTE)) {
            matchTokenType(TokenType.OPERATOR_LTE);
        } else if (checkType(TokenType.OPERATOR_GTE)) {
            matchTokenType(TokenType.OPERATOR_GTE);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '<', '>', '<=' o '>=', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨OpAd ⟩ ::= +
        | -

    */
    public void opAd() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_ADD)) {
            matchTokenType(TokenType.OPERATOR_ADD);
        } else if (checkType(TokenType.OPERATOR_SUB)) {
            matchTokenType(TokenType.OPERATOR_SUB);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '+' o '-', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨OpUnario⟩ ::= 	+
        | -
        | !
        | ++
        | --
    */
    public void opUnario() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_ADD)) {
            matchTokenType(TokenType.OPERATOR_ADD);
        } else if (checkType(TokenType.OPERATOR_SUB)) {
            matchTokenType(TokenType.OPERATOR_SUB);
        } else if (checkType(TokenType.OPERATOR_NOT)) {
            matchTokenType(TokenType.OPERATOR_NOT);
        } else if (checkType(TokenType.OPERATOR_INC)) {
            matchTokenType(TokenType.OPERATOR_INC);
        } else if (checkType(TokenType.OPERATOR_DEC)) {
            matchTokenType(TokenType.OPERATOR_DEC);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '+', '-', '!', '++' o '--', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨OpMul ⟩ ::= *
        | /
    */
    public void opMul() throws LexicalException, SyntacticException {
        if (checkType(TokenType.OPERATOR_MUL)) {
            matchTokenType(TokenType.OPERATOR_MUL);
        } else if (checkType(TokenType.OPERATOR_DIV)) {
            matchTokenType(TokenType.OPERATOR_DIV);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '*' o '/', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨Operando⟩ ::= ⟨Literal⟩
            | ⟨Primario⟩
    INCOMPLETO
    */
    public void operando() throws LexicalException, SyntacticException {
        if(checkType(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT)) {
            literal();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_PO, TokenType.KW_SELF, TokenType.IDENTIFIER_CLASS,
                TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {
            //primario();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'nil', 'true', 'false', entero, string, '(', 'self', identificador de clase, 'new' " +
                            "o identificador de variable o metodo, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨Literal⟩ ::= nil
        | true
        | false
        | intLiteral
        | StrLiteral
    */
    public void literal() throws LexicalException, SyntacticException {
        if (checkType(TokenType.KW_NIL)){
            matchTokenType(TokenType.KW_NIL);
        } else if (checkType(TokenType.BOOLEAN_CONSTANT)) {
            matchTokenType(TokenType.BOOLEAN_CONSTANT);
        } else if (checkType(TokenType.INT_CONSTANT)) {
            matchTokenType(TokenType.INT_CONSTANT);
        } else if (checkType(TokenType.STRING_CONSTANT)) {
            matchTokenType(TokenType.STRING_CONSTANT);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'nil', 'true', 'false', un entero o un string, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*

    ⟨Primario⟩ ::= ⟨ExpresionParentizada⟩
        | ⟨AccesoSelf⟩
        | ⟨Llamada-Método-Estático⟩
        | ⟨Llamada-Conclassor⟩
        | id ⟨VarOMetodo⟩

	⟨VarOMetodo⟩ ::= ⟨AccesoVar⟩
		| ⟨Llamada-Método⟩

	⟨ExpresionParentizada⟩ ::= ( ⟨Expresión⟩ ) ⟨ExpresionParentizadaF⟩

	⟨ExpresionParentizadaF⟩ ::=  λ
        | ⟨Encadenado⟩

	⟨AccesoSelf⟩ ::= self ⟨AccesoSelfF⟩

	⟨AccesoSelfF⟩::= λ
        | ⟨Encadenado⟩

	⟨AccesoVar⟩ ::= λ
        | ⟨Encadenado⟩
        | [ ⟨Expresión⟩ ] ⟨AccesoVarF⟩

    ⟨AccesoVarF⟩ ::= λ
        | ⟨Encadenado⟩

    ⟨Llamada-Método⟩ ::= ⟨Argumentos-Actuales⟩ ⟨Llamada-MétodoF⟩

    ⟨Llamada-MétodoF⟩ ::= λ
        | ⟨Encadenado⟩

    ⟨Llamada-Método-Estático⟩ ::= idclass . ⟨Llamada-Método⟩ ⟨Llamada-Método-EstáticoF⟩

    ⟨Llamada-Método-EstáticoF⟩ ::= λ
        | ⟨Encadenado⟩

    ⟨Llamada-Conclassor⟩ ::= new ⟨Llamada-ConclassorF1⟩

    ⟨Llamada-ConclassorF1⟩ ::= idclass ⟨Argumentos-Actuales⟩ ⟨Llamada-ConclassorF2⟩
        | ⟨Tipo-Primitivo⟩ [ ⟨Expresión⟩ ] ⟨Llamada-ConclassorF2⟩

    ⟨Llamada-ConclassorF2⟩ ::= λ
        | ⟨Encadenado⟩

	⟨Argumentos-Actuales⟩ ::= ( ⟨Argumentos-ActualesF⟩

	⟨Argumentos-ActualesF⟩ ::= )
		| ⟨Lista-Expresiones⟩ )

	⟨Lista-Expresiones⟩ ::= ⟨Expresión⟩ ⟨Lista-ExpresionesF⟩

	⟨Lista-ExpresionesF⟩ ::= λ
		| , ⟨Lista-Expresiones⟩

	⟨Encadenado⟩ ::= . ⟨EncadenadoF⟩

	⟨EncadenadoF⟩ ::= ⟨Llamada-Método⟩
		| ⟨AccesoVar⟩

     */

}
