package uncuyo.compiladores.syntactic;

import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;

import java.io.IOException;


public class SyntacticAnalyzer {

    private Lexer lexer;
    private Token currentToken;

    private void nextToken() throws LexicalException, IOException {
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

    private void matchLexeme(String expected) throws Exception {
        if (currentToken.getLexeme().equals(expected)) {
            nextToken();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '" + expected + "' pero se encontro '" +
                            currentToken.getLexeme() + "'");
        }
    }

    private void matchTokenType(TokenType expectedTokenType) throws Exception {

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
    public void program(){
        //listaDefiniciones();
        //start();
    }

    /* ⟨Start⟩ ::= start ⟨Bloque-Método⟩ */
    public void start() throws Exception {
        matchLexeme("start");
        //bloqueMetodo();
    }

    /* ⟨Lista-Definiciones⟩ ::= ⟨class⟩ ⟨Lista-Definiciones⟩
            | ⟨Impl⟩ ⟨Lista-Definiciones⟩
            | λ
    */
    public void listaDefiniciones() throws SyntacticException {
        if (checkLexeme("class")){
            // class();
            // listaDefiniciones();
        } else if (checkLexeme("impl")) {
            // impl()
            // listaDefiniciones();
        }else if (checkLexeme("start")){
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
    public void class1() throws Exception {
        matchLexeme("class");
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        //classF();
    }

    /* 	⟨classF⟩ ::= { ⟨AtributoK⟩ }
            | ⟨Herencia⟩ { ⟨AtributoK⟩ }
    */
    public void classF() throws Exception {
        if (checkLexeme("{")){
            matchLexeme("{");
            // atributoK();
            matchLexeme("}");
        } else if (checkLexeme(":")) {
            // herencia();
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

    public void impl() throws Exception {
        matchLexeme("impl");
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        matchLexeme("{");
        // MiembroK()
        matchLexeme("}");
    }

    /*
        ⟨Herencia⟩ ::= : ⟨Tipo⟩
     */

    public void herencia() throws Exception {
        matchLexeme(":");
        //tipo()
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

    ⟨Visibilidad ⟩ ::= pub

    ⟨Forma-Método⟩ ::= st

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

    ⟨Tipo-Método⟩ ::= ⟨Tipo⟩
    | void

    */

    /*
        ⟨Tipo⟩ ::= ⟨Tipo-Primitivo⟩
            | ⟨Tipo-Referencia⟩
            | ⟨Tipo-Arreglo⟩
    */
    public void tipo() throws Exception {
        if(checkLexeme("Str", "Bool", "Int")) {
            tipoPrimitivo();
        } else if (checkType(TokenType.IDENTIFIER_CLASS)) {
            tipoReferencia();
        } else if (checkLexeme("Array")) {
            tipoArreglo();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int', identificador de clase o 'Array' pero se encontró '" +
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
        if(checkLexeme("Str")) {
            matchLexeme("Str");
        } else if(checkLexeme("Bool")) {
            matchLexeme("Bool");
        } else if (checkLexeme("Int")) {
            matchLexeme("Int");
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool' o 'Int', pero se encontró '" +
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

    ⟨Expresión⟩ ::= ⟨ExpOr ⟩

	⟨ExpOr ⟩ ::= ⟨ExpAnd⟩ ⟨ExpOrR⟩

    ⟨ExpOrR⟩ ::= || ⟨ExpAnd ⟩ ⟨ExpOrR⟩
        | λ

	⟨ExpAnd ⟩ ::= ⟨ExpIgual⟩ ⟨ExpAndR⟩

    ⟨ExpAndR⟩ ::= && ⟨ExpIgual ⟩ ⟨ExpAndR⟩
        | λ

	⟨ExpIgual ⟩ ::= ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩

    ⟨ExpIguaR⟩ ::= ⟨OpIgual ⟩ ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩
        | λ

	⟨ExpCompuesta⟩ ::= ⟨ExpAd ⟩ ⟨ExpCompuestaF⟩

	⟨ExpCompuestaF⟩ ::= ⟨OpCompuesto⟩ ⟨ExpAd ⟩
		| λ

    ⟨ExpAd ⟩ ::= ⟨ExpMul ⟩ ⟨ExpAdR⟩

    ⟨ExpAdR⟩ ::= ⟨OpAd ⟩ ⟨ExpMul ⟩ ⟨ExpAdR⟩
        | λ

    ⟨ExpMul ⟩ ::= ⟨ExpUn⟩ ⟨ExpMulR⟩

    ⟨ExpMulR⟩ ::= ⟨OpMul ⟩ ⟨ExpUn⟩ ⟨ExpMulR⟩
    | λ

    ⟨ExpUn⟩ ::= ⟨OpUnario⟩ ⟨ExpUn⟩
    | ⟨Operando⟩

    ⟨OpIgual ⟩ ::= ==
    | !=

    ⟨OpCompuesto⟩ ::= <
    | >
    | <=
    | >=

    ⟨OpAd ⟩ ::= +
    | -

    ⟨OpUnario⟩ ::= 	+
    | -
    | !
    | ++
    | --

    ⟨OpMul ⟩ ::= *
    | /

        ⟨Operando⟩ ::= ⟨Literal ⟩
    | ⟨Primario⟩

    ⟨Literal ⟩ ::= nil
    | true
    | false
    | intLiteral
    | StrLiteral

    ⟨Primario⟩ ::= ⟨ExpresionParentizada⟩  -> (
    | ⟨AccesoSelf ⟩  -> self
    | ⟨AccesoVar ⟩  -> id
    | ⟨Llamada-Método⟩  -> id
    | ⟨Llamada-Método-Estático⟩ -> idclass
    | ⟨Llamada-Conclassor⟩ -> new

    ⟨Primario⟩ ::= ⟨ExpresionParentizada⟩
    | ⟨AccesoSelf ⟩
    | ⟨Llamada-Método-Estático⟩
    | ⟨Llamada-Conclassor⟩
    | id ⟨VarOMetodo⟩

	⟨VarOMetodo⟩ ::= ⟨AccesoVar ⟩
		| ⟨Llamada-Método⟩


	⟨ExpresionParentizada⟩ ::= ( ⟨Expresión⟩ ) ⟨ExpresionParentizadaF⟩

	⟨ExpresionParentizadaF⟩ ::=  λ
		| ⟨Encadenado⟩

	⟨AccesoSelf ⟩ ::= self ⟨AccesoSelfF⟩

    ⟨AccesoSelfF⟩::= λ
        | ⟨Encadenado⟩

    ⟨AccesoVar ⟩ ::= id
    | id ⟨Encadenado⟩
    | id [ ⟨Expresión⟩ ]
    | id [ ⟨Expresión⟩ ] ⟨Encadenado⟩

        ⟨AccesoVar ⟩ ::= λ
    | ⟨Encadenado⟩
    | [ ⟨Expresión⟩ ]
    | [ ⟨Expresión⟩ ] ⟨Encadenado⟩

        ⟨AccesoVar ⟩ ::= λ
    | ⟨Encadenado⟩
    | [ ⟨Expresión⟩ ] ⟨AccesoVarF⟩

        ⟨AccesoVarF⟩ ::= λ
            | ⟨Encadenado⟩

    ⟨Llamada-Método⟩ ::= id ⟨Argumentos-Actuales⟩
    | id ⟨Argumentos-Actuales⟩ ⟨Encadenado⟩

    ⟨Llamada-Método⟩ ::= ⟨Argumentos-Actuales⟩
    | ⟨Argumentos-Actuales⟩ ⟨Encadenado⟩

    ⟨Llamada-Método⟩ ::= ⟨Argumentos-Actuales⟩ ⟨Llamada-MétodoF⟩

    ⟨Llamada-MétodoF⟩ ::= λ
    | ⟨Encadenado⟩

    ⟨Llamada-Método-Estático⟩ ::= idclass . ⟨Llamada-Método⟩ ⟨Llamada-Método-EstáticoF⟩

    ⟨Llamada-Método-EstáticoF⟩ ::= λ
    | ⟨Encadenado⟩

        ⟨Llamada-Conclassor⟩ ::= new idclass ⟨Argumentos-Actuales⟩ ⟨Llamada-ConclassorF⟩
            | new ⟨Tipo-Primitivo⟩ [ ⟨Expresión⟩ ] ⟨Llamada-ConclassorF⟩

    ⟨Llamada-ConclassorF⟩ ::= λ
        | ⟨Encadenado⟩

	⟨Argumentos-Actuales⟩ ::= ( ⟨Argumentos-ActualesF⟩

	⟨Argumentos-ActualesF⟩ ::= )
		| ⟨Lista-Expresiones⟩ )

	⟨Lista-Expresiones⟩ ::= ⟨Expresión⟩ ⟨Lista-ExpresionesF⟩

	⟨Lista-ExpresionesF⟩ ::= λ
		| , ⟨Lista-Expresiones⟩

	⟨Encadenado⟩ ::= . ⟨EncadenadoF⟩

	⟨EncadenadoF⟩ ::= ⟨Llamada-Método⟩
		| ⟨AccesoVar ⟩

     */

}
