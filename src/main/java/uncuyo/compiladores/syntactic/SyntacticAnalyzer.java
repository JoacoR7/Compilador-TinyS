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
                    "Se esperaba '" + expected + "' pero se encontro '" +
                            currentToken.getLexeme() + "'");
        }
    }

    private void matchTokenType(TokenType expectedTokenType) throws LexerFileReadException, LexicalException, SyntacticException {

        if (currentToken.getTokenType().equals(expectedTokenType)){
            nextToken();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba " + expectedTokenType + " pero se encontro '" +
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

    /* ⟨Start⟩ ::= start ⟨Bloque-Método⟩ */
    public void start() throws LexerFileReadException, LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        //DUDA CON START
        //bloqueMetodo();
    }

    /* ⟨Lista-Definiciones⟩ ::= ⟨class⟩ ⟨Lista-Definiciones⟩
            | ⟨Impl⟩ ⟨Lista-Definiciones⟩
            | λ
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
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'class', 'impl' o 'start' pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /* 	⟨class⟩ ::= class idclass ⟨classF⟩ */
    public void class_() throws Exception {
        matchTokenType(TokenType.KW_CLASS);
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        classF();
    }

    /* 	⟨classF⟩ ::= { ⟨AtributoK⟩ }
            | ⟨Herencia⟩ { ⟨AtributoK⟩ }
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
                    "Se esperaba '{' o ':' pero se encontro '" +
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

	⟨Lista-Declaración-Variables⟩ ::= idMetAt ⟨Lista-Declaración-VariablesF⟩

	⟨Lista-Declaración-VariablesF⟩ ::= , ⟨Lista-Declaración-Variables⟩
		| λ

	⟨Argumentos-Formales⟩::= ( ⟨Argumentos-FormalesF⟩

	⟨Argumentos-FormalesF⟩ ::= )
		| ⟨Lista-Argumentos-Formales⟩ )

	⟨Lista-Argumentos-Formales⟩ ::= ⟨Argumento-Formal ⟩ ⟨Lista-Argumentos-FormalesF⟩

    ⟨Lista-Argumentos-FormalesF⟩ ::=  , ⟨Lista-Argumentos-Formales⟩
        | λ

    ⟨Argumento-Formal ⟩ ::= ⟨Tipo⟩ idMetAt
    */

    /*
    ⟨Tipo-Método⟩ ::= ⟨Tipo⟩
        | void
    */

    public void tipoMetodo() throws Exception {
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
    public void tipo() throws Exception {
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
                    "Se esperaba 'Str', 'Bool', 'Int', identificador de clase o 'Array' pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨Tipo-Primitivo⟩ ::= Str
            | Bool
            | Int
    */
    public void tipoPrimitivo() throws Exception {
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

    public void tipoReferencia() throws Exception {
        matchTokenType(TokenType.IDENTIFIER_CLASS);
    }
    /*
        ⟨Tipo-Arreglo⟩ ::= Array ⟨Tipo-Primitivo⟩
    */

    public void tipoArreglo() throws Exception {
        matchLexeme("Array");
        tipoPrimitivo();
    }

    /*

    ⟨SentenciaK⟩ ::= ⟨Sentencia⟩ ⟨SentenciaK⟩
        | λ

    ⟨Sentencia⟩ ::= ;
        | ⟨Asignación⟩;
        | ⟨Sentencia-Simple⟩;
        | while ( ⟨Expresión⟩ ) ⟨Sentencia⟩
        | for ( ⟨Tipo-Primitivo⟩ idMetAt in idMetAt ) ⟨Sentencia⟩
        | ⟨Bloque⟩
        | if (⟨Expresión⟩) ⟨Sentencia⟩ ⟨SentenciaF1⟩
        | ret ⟨SentenciaF2⟩

	⟨SentenciaF1⟩ ::= else ⟨Sentencia⟩
		| λ

	⟨SentenciaF2⟩ ::= ;
		| ⟨Expresión⟩;

    ⟨Bloque⟩ ::= { ⟨SentenciaK⟩ }

    ⟨Asignación⟩ ::= ⟨AccesoVar-Simple⟩ = ⟨Expresión⟩
        | ⟨AccesoSelf-Simple⟩ = ⟨Expresión⟩

	⟨AccesoVar-Simple⟩ ::= id ⟨AccesoVar-SimpleF⟩

    ⟨AccesoVar-SimpleF⟩ ::= ⟨Encadenado-SimpleK⟩
        | [ ⟨Expresión⟩ ]

    ⟨AccesoSelf-Simple⟩ ::= self ⟨Encadenado-SimpleK⟩

    ⟨Encadenado-SimpleK⟩ ::= ⟨Encadenado-Simple⟩ ⟨Encadenado-SimpleK⟩
        | λ

    ⟨Encadenado-Simple⟩ ::= . id

    ⟨Sentencia-Simple⟩ ::= (⟨Expresión⟩)

    ⟨Expresión⟩ ::= ⟨ExpOr⟩

	⟨ExpOr⟩ ::= ⟨ExpAnd⟩ ⟨ExpOrR⟩

    ⟨ExpOrR⟩ ::= || ⟨ExpAnd⟩ ⟨ExpOrR⟩
        | λ

	⟨ExpAnd⟩ ::= ⟨ExpIgual⟩ ⟨ExpAndR⟩

    ⟨ExpAndR⟩ ::= && ⟨ExpIgual ⟩ ⟨ExpAndR⟩
        | λ

	⟨ExpIgual ⟩ ::= ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩

    ⟨ExpIgualR⟩ ::= ⟨OpIgual⟩ ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩
        | λ

	⟨ExpCompuesta⟩ ::= ⟨ExpAd⟩ ⟨ExpCompuestaF⟩

	⟨ExpCompuestaF⟩ ::= ⟨OpCompuesto⟩ ⟨ExpAd⟩
		| λ

    ⟨ExpAd ⟩ ::= ⟨ExpMul⟩ ⟨ExpAdR⟩

    ⟨ExpAdR⟩ ::= ⟨OpAd⟩ ⟨ExpMul⟩ ⟨ExpAdR⟩
        | λ

    ⟨ExpMul⟩ ::= ⟨ExpUn⟩ ⟨ExpMulR⟩

    ⟨ExpMulR⟩ ::= ⟨OpMul⟩ ⟨ExpUn⟩ ⟨ExpMulR⟩
        | λ

    ⟨ExpUn⟩ ::= ⟨OpUnario⟩ ⟨ExpUn⟩
        | ⟨Operando⟩

    */
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
                    "Se esperaba '<', '>', '<=' o '>=' pero se encontro '" +
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
                    "Se esperaba '+' o '-' pero se encontro '" +
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
                    "Se esperaba '+', '-', '!', '++' o '--' pero se encontro '" +
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
                    "Se esperaba '*' o '/' pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
 	⟨Operando⟩ ::= ⟨Literal⟩
        | ⟨Primario⟩

    */

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
