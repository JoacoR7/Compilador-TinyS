package uncuyo.compiladores.semantic;

import uncuyo.compiladores.exceptions.LexerFileReadException;
import uncuyo.compiladores.exceptions.LexicalException;
import uncuyo.compiladores.exceptions.SemanticException;
import uncuyo.compiladores.exceptions.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenType;


public class SemanticAnalyzer {

    private final Lexer lexer;
    private Token currentToken;
    private final SymbolTable symbolTable = new SymbolTable();

    public SemanticAnalyzer(Lexer lexer, Token token) {
        this.lexer = lexer;
        this.currentToken = token;
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

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
    public void program() throws LexicalException, SyntacticException {
        listaDefiniciones();
        start();
        matchTokenType(TokenType.EOF);
    }

    /*
        ⟨Start⟩ ::= start ⟨Bloque-Método⟩
    */
    public void start() throws LexerFileReadException, LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        bloqueMetodo();
    }

    /* ⟨Lista-Definiciones⟩ ::= ⟨class⟩ ⟨Lista-Definiciones⟩
            | ⟨Impl⟩ ⟨Lista-Definiciones⟩
            | λ
    */
    public void listaDefiniciones() throws LexicalException, SyntacticException {
        if (checkType(TokenType.KW_CLASS)){
            class_();
            listaDefiniciones();
        } else if (checkType(TokenType.KW_IMPL)) {
            impl();
            listaDefiniciones();
        } else if (checkType(TokenType.IDENTIFIER_VAR_METHOD)){
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
        class idclass ⟨classF⟩ {idclass.padre = classF.tipo, TS.guardar(HTClass , idclass.lex)}
            INCOMPLETO FALTA AGREGAR ACCIONES SEMÁNTICAS
	    */
    public void class_() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_CLASS);

        Token auxToken = Token.copy(currentToken);
        matchTokenType(TokenType.IDENTIFIER_CLASS);

        Class currentClass = new Class(auxToken);
        symbolTable.setCurrentClass(currentClass);

        Type type = classF();
        currentClass.setParent(type.getLexeme());
        symbolTable.saveClass(currentClass);
    }

    /*
        ⟨classF⟩ ::= {classF.tipo = Object} { ⟨AtributoK⟩ }
            | ⟨Herencia⟩ {classF.tipo = Herencia.tipo} { ⟨AtributoK⟩ }
    */
    public Type classF() throws LexicalException, SyntacticException {
        Type type = null;

        if(checkType(TokenType.SPECIAL_SYMBOL_LCB)) {
            type = new Type(TokenType.OBJECT_CLASS, "Object");
            matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
            atributoK();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
        } else if (checkType(TokenType.SPECIAL_SYMBOL_CL)) {
            type = herencia();
            matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
            atributoK();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '{' o ':', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return type;
    }

    /*
        ⟨AtributoK⟩ ::= ⟨Atributo⟩ ⟨AtributoK⟩
            | λ
     */
    public void atributoK() throws LexicalException, SyntacticException {
        if (checkType(TokenType.STR_CLASS, TokenType.BOOL_CLASS, TokenType.INT_CLASS,
                TokenType.IDENTIFIER_CLASS, TokenType.ARRAY_CLASS, TokenType.KW_PUB)) {
            atributo();
            atributoK();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_RCB)) {
            // Siguientes atributoK, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'pub', '}' 'Str', 'Bool', 'Int', 'Array' u otros identificadores de clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨Impl ⟩ ::= impl idclass { ⟨MiembroK⟩ }
     */
    public void impl() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_IMPL);
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        miembroK();
        matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
    }

    /*
        ⟨Herencia⟩ ::= : ⟨Tipo⟩ {Herencia.tipo = Tipo.tipo}
     */
    public Type herencia() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_CL);
        return tipo();
    }

    /*
        ⟨MiembroK⟩ ::= ⟨Miembro⟩ ⟨MiembroK⟩
            | λ
    */
    public void miembroK() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_P, TokenType.KW_ST, TokenType.KW_FN)) {
            miembro();
            miembroK();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_RCB)) {
            // Siguientes de miembroK, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'st', 'fn', '.' o '}', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    /*
        ⟨Miembro⟩ ::= ⟨Método⟩
            | ⟨Constructor⟩
    */
    public void miembro() throws LexicalException, SyntacticException {
        if (checkType(TokenType.KW_ST, TokenType.KW_FN)) {
            metodo();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
            constructor();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'st', 'fn', o '.', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    /*
        ⟨Constructor ⟩ ::= . ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩
    */
    public void constructor() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        argumentosFormales();
        bloqueMetodo();
    }

    /*

    ⟨Atributo⟩ ::= ⟨Tipo⟩ {ListaDeclaracionVariables.tipo = Tipo.tipo, ListaDeclaracionVariables.privado = true } ⟨Lista-Declaración-Variables⟩ ;
        | ⟨Visibilidad⟩ ⟨Tipo⟩ {ListaDeclaracionVariables.tipo = Tipo.tipo, ListaDeclaracionVariables.privado = false}  ⟨Lista-Declaración-Variables⟩ ;
    */
    public void atributo() throws LexicalException, SyntacticException {
        Type type = null;
        if (checkType(TokenType.IDENTIFIER_CLASS, TokenType.STR_CLASS, TokenType.BOOL_CLASS,
                TokenType.INT_CLASS, TokenType.ARRAY_CLASS)) {
            type = tipo();
            listaDeclaracionVariables(type , true);
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if (checkType(TokenType.KW_PUB)) {
            visibilidad();
            type = tipo();
            listaDeclaracionVariables(type , false);
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba un identificador de clase o 'pub', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨Método⟩ ::= fn ⟨MétodoF⟩
        | ⟨Forma-Método⟩ fn ⟨MétodoF⟩
    */
    public void metodo() throws LexicalException, SyntacticException {
        boolean isStatic = false;
        if(checkType(TokenType.KW_FN)){
            matchTokenType(TokenType.KW_FN);
            metodoF(isStatic);
        }else if (checkType(TokenType.KW_ST)){
            formaMetodo();
            isStatic = true;
            matchTokenType(TokenType.KW_FN);
            metodoF(isStatic);
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'fn' o 'st', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        symbolTable.getCurrentClass().insertMethod(symbolTable.getCurrentMethod());
    }

    /*
        ⟨MétodoF⟩ ::= {MétodoF.tipo = void} idMetAt ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩
            | ⟨Tipo-Método⟩ {MétodoF.tipo = Tipo-Método.tipo} idMetAt ⟨Argumentos-Formales⟩ ⟨Bloque-Método⟩
    */
    public void metodoF(boolean isStatic) throws LexicalException, SyntacticException {
        Type type = null;
        if(checkType(TokenType.IDENTIFIER_VAR_METHOD)){
            type = new Type(TokenType.KW_VOID, "void");
            Token auxToken = currentToken;
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            Method currentMethod = new Method(auxToken, type, isStatic);
            symbolTable.setCurrentMethod(currentMethod);
            argumentosFormales();
            bloqueMetodo();
        }else if (checkType(TokenType.KW_VOID, TokenType.IDENTIFIER_CLASS,
                TokenType.INT_CLASS, TokenType.BOOL_CLASS, TokenType.STR_CLASS, TokenType.ARRAY_CLASS)){
            type = tipoMetodo();
            Token auxToken = currentToken;
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            Method currentMethod = new Method(auxToken, type, isStatic);
            currentMethod.setType(type);
            symbolTable.setCurrentMethod(currentMethod);
            argumentosFormales();
            bloqueMetodo();
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int', 'Array', 'void', una clase, variable o un metodo , pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

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
    */
    public void bloqueMetodo() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        declVarLocalesK();
        sentenciaK();
        matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
    }

    /*
        ⟨Decl-Var-LocalesK⟩ ::= ⟨Decl-Var-Locales⟩ ⟨Decl-Var-LocalesK⟩
            | λ
    */
    public void declVarLocalesK() throws LexicalException, SyntacticException {
        if (checkType(TokenType.STR_CLASS, TokenType.BOOL_CLASS, TokenType.INT_CLASS, TokenType.ARRAY_CLASS,
                TokenType.IDENTIFIER_CLASS)) {
            declVarLocales();
            declVarLocalesK();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_S, TokenType.KW_WHILE, TokenType.KW_FOR,
                TokenType.KW_IF, TokenType.KW_RET, TokenType.SPECIAL_SYMBOL_PO, TokenType.SPECIAL_SYMBOL_LCB,
                TokenType.IDENTIFIER_VAR_METHOD, TokenType.KW_SELF, TokenType.SPECIAL_SYMBOL_RCB)) {
            // Siguientes de declVarLocalesK, cierro
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba string, booleano, entero, un identificador de clase, ';' , " +
                            "'while', 'for', 'if', 'ret', '(' , '{' , identificador de variable o método, " +
                            "'self' o '}', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
        ⟨Decl-Var-Locales⟩ ::= ⟨Tipo⟩ ⟨Lista-Declaración-Variables⟩ ;
    */
    public void declVarLocales() throws LexicalException, SyntacticException {
        tipo();
        listaDeclaracionVariables(null , false); // INCOMPLETO
        matchTokenType(TokenType.SPECIAL_SYMBOL_S);
    }

    /*
        ⟨Lista-Declaración-Variables⟩ ::= idMetAt {if(TS.yaExiste(ClaseActual, idMetAt.lex)) then Error
         else TS.guardar(ClaseActual, idMetAt.lex, ListaDeclaracionVariables.tipo) }
         {ListaDeclaracionVariablesF.tipo = ListaDeclaracionVariables.tipo, ListaDeclaracionVariablesF.privado = ListaDeclaracionVariables.privado} ⟨Lista-Declaración-VariablesF⟩    */
    public void listaDeclaracionVariables(Type type , boolean isPrivate) throws LexicalException, SyntacticException {
        Token auxToken = Token.copy(currentToken);

        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);

        if(symbolTable.getCurrentClass().attributeAlreadyExists(auxToken.getLexeme())){
            throw new SemanticException(auxToken.getRowIndex(),
                    auxToken.getColumnIndex(),
                    "El atributo '"+ auxToken.getLexeme() + "' ya ha sido definido dentro de la clase '" +
                            symbolTable.getCurrentClass().getToken().getLexeme() + "'");
        }
        symbolTable.getCurrentClass().saveAttribute(auxToken, type, isPrivate);
        listaDeclaracionVariablesF(type, isPrivate);
    }
    
    /*
        ⟨Lista-Declaración-VariablesF⟩ ::= , {ListaDeclaracionVariables.tipo = ListaDeclaracionVariablesF.tipo, ListaDeclaracionVariables.privado = ListaDeclaracionVariablesF.privado } ⟨Lista-Declaración-Variables⟩
            | λ
    */
    public void listaDeclaracionVariablesF(Type type, boolean isPrivate) throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_CM)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_CM);
            listaDeclaracionVariables(type , isPrivate);
        } else if (checkType(TokenType.STRING_CONSTANT, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT,
                TokenType.IDENTIFIER_CLASS, TokenType.SPECIAL_SYMBOL_S, TokenType.KW_WHILE, TokenType.KW_FOR,
                TokenType.KW_IF, TokenType.KW_RET, TokenType.SPECIAL_SYMBOL_PO, TokenType.SPECIAL_SYMBOL_LCB,
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
	*/
    public void argumentosFormales() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        argumentosFormalesF();
    }

    /*
	⟨Argumentos-FormalesF⟩ ::= )
		| ⟨Lista-Argumentos-Formales⟩ )
    */
    public void argumentosFormalesF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_PC)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        } else if (checkType(TokenType.STR_CLASS, TokenType.BOOL_CLASS, TokenType.INT_CLASS, TokenType.ARRAY_CLASS,
                TokenType.IDENTIFIER_CLASS)) {
                listaArgumentosFormales();
                matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ')', o un identificador de clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

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
        Type type = tipo();
        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        symbolTable.getCurrentMethod().insertParameter(type, auxToken.getLexeme());
    }

    /*
        ⟨Tipo-Método⟩ ::= ⟨Tipo⟩
            | void
    */
    public Type tipoMetodo() throws LexicalException, SyntacticException {
        Type type = null;
        if(checkType(TokenType.STR_CLASS, TokenType.BOOL_CLASS,
                TokenType.INT_CLASS, TokenType.IDENTIFIER_CLASS)) {
            type = tipo();
        } else if (checkType(TokenType.KW_VOID)) {
            matchTokenType(TokenType.KW_VOID);
            type = new Type(TokenType.KW_VOID, "void");
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int', 'void' o un identificador de clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return type;
    }

    /*
        ⟨Tipo⟩ ::= ⟨Tipo-Primitivo⟩
            | ⟨Tipo-Referencia⟩
            | ⟨Tipo-Arreglo⟩
    */
    public Type tipo() throws LexicalException, SyntacticException {
        Type type = null;

        if(checkType(TokenType.STR_CLASS, TokenType.BOOL_CLASS, TokenType.INT_CLASS)) {
            type =  tipoPrimitivo();
        } else if (checkType(TokenType.IDENTIFIER_CLASS)) {
            type = tipoReferencia();
        } else if (checkType(TokenType.ARRAY_CLASS)) {
            type = tipoArreglo();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int', identificador de clase o 'Array', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return type;
    }

    /*
        ⟨Tipo-Primitivo⟩ ::= Str {TipoPrimitivo.tipo = STR_CLASS}
            | Bool {TipoPrimitivo.tipo = BOOL_CLASS}
            | Int {TipoPrimitivo.tipo = INT_CLASS}
    */
    public Type tipoPrimitivo() throws LexicalException, SyntacticException {
        Type type = new Type(currentToken.getTokenType(), currentToken.getLexeme());

        if(checkType(TokenType.STR_CLASS)) {
            matchTokenType(TokenType.STR_CLASS);
        } else if(checkType(TokenType.BOOL_CLASS)) {
            matchTokenType(TokenType.BOOL_CLASS);
        } else if (checkType(TokenType.INT_CLASS)) {
            matchTokenType(TokenType.INT_CLASS);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool' o 'Int', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return type;
    }

    /*
        ⟨Tipo-Referencia⟩ ::= idclass {TipoReferencia.tipo = idclass}
    */
    public Type tipoReferencia() throws LexicalException, SyntacticException {
        Type type = new Type(currentToken.getTokenType(), currentToken.getLexeme());
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        return type;
    }
    /*
        ⟨Tipo-Arreglo⟩ ::= Array ⟨Tipo-Primitivo⟩ {TipoArreglo.tipo = TipoPrimitivo.tipo}
    */
    public Type tipoArreglo() throws LexicalException, SyntacticException {
        // INCOMPLETO , DEBO CREAR LA CLASE ARRAY Y ASIGNARLE EL SUBTIPO?
        matchTokenType(TokenType.ARRAY_CLASS);
        Type subType = tipoPrimitivo();
        ArrayType type = new ArrayType(subType);
        return type;
    }

    /*
        ⟨SentenciaK⟩ ::= ⟨Sentencia⟩ ⟨SentenciaK⟩
            | λ
    */
    public void sentenciaK() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_S, TokenType.KW_WHILE, TokenType.KW_FOR,
                TokenType.KW_IF, TokenType.KW_RET, TokenType.SPECIAL_SYMBOL_PO, TokenType.SPECIAL_SYMBOL_LCB,
                TokenType.IDENTIFIER_VAR_METHOD, TokenType.KW_SELF)) {
            sentencia();
            sentenciaK();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_RCB)) {
            // Siguientes de sentenciaK, cierro
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ',', 'while', 'for', 'if', 'ret', '(' , '{' , identificador de variable o método, " +
                            "'self' o '}', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

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
        } else if (checkType(TokenType.IDENTIFIER_VAR_METHOD, TokenType.KW_SELF)) {
            asignacion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if(checkType(TokenType.SPECIAL_SYMBOL_PO)) {
            sentenciaSimple();
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
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
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
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
    */
    public void bloque() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        sentenciaK();
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
        expOr();
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
    */
    public void operando() throws LexicalException, SyntacticException {
        if(checkType(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT)) {
            literal();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_PO, TokenType.KW_SELF, TokenType.IDENTIFIER_CLASS,
                TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {
            primario();
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
    */

    public void primario() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_PO)) {
            expresionParentizada();
        } else if (checkType(TokenType.KW_SELF)) {
            accesoSelf();
        } else if (checkType(TokenType.IDENTIFIER_CLASS)) {
            llamadaMetodoEstatico();
        } else if (checkType(TokenType.KW_NEW)) {
            llamadaConclassor();
        } else if (checkType(TokenType.IDENTIFIER_VAR_METHOD)) {
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            varOMetodo();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '(', 'self', identificador de clase, 'new', o un identificador de variable o método, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
	⟨VarOMetodo⟩ ::= ⟨AccesoVar⟩
		| ⟨Llamada-Método⟩
    */
    public void varOMetodo() throws SyntacticException, LexicalException {
        if (checkType(TokenType.SPECIAL_SYMBOL_LB, TokenType.SPECIAL_SYMBOL_P)){
            accesoVar();
        }else if (checkType(TokenType.SPECIAL_SYMBOL_PO)){
            llamadaMetodo();
        } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {

            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '[','.','(','*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );

        }
    }

    /*
    ⟨ExpresionParentizada⟩ ::= ( ⟨Expresión⟩ ) ⟨ExpresionParentizadaF⟩
    */
    public void expresionParentizada() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        expresion();
        matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        expresionParentizadaF();
    }

    /*
	⟨ExpresionParentizadaF⟩ ::=  λ
        | ⟨Encadenado⟩
    INCOMPLETO
    */
    public void expresionParentizadaF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_P)){
            encadenado();
        } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {

            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '.', '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨AccesoSelf⟩ ::= self ⟨AccesoSelfF⟩
    */
    public void accesoSelf() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_SELF);
        accesoSelfF();
    }

    /*
	⟨AccesoSelfF⟩::= λ
        | ⟨Encadenado⟩}
    INCOMPLETO
    */
    public void accesoSelfF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_P)){
            encadenado();
        } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {

            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '.', '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
	⟨AccesoVar⟩ ::= λ
        | ⟨Encadenado⟩
        | [ ⟨Expresión⟩ ] ⟨AccesoVarF⟩
    */
    public void accesoVar() throws SyntacticException, LexicalException {
        if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
            encadenado();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
            accesoVarF();
        } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {

            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '.', '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨AccesoVarF⟩ ::= λ
        | ⟨Encadenado⟩
    INCOMPLETO
    */
     public void accesoVarF() throws SyntacticException, LexicalException {
         // REVISAR AMBIGUEDAD CON TokenType.SPECIAL_SYMBOL_P
         if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
             encadenado();
         } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                 TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                 TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                 TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                 TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                 TokenType.SPECIAL_SYMBOL_CM, TokenType.SPECIAL_SYMBOL_P)) {

             throw new SyntacticException(currentToken.getRowIndex(),
                     currentToken.getColumnIndex(),
                     "Se esperaba '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', '.', pero se encontro '" +
                             currentToken.getLexeme() + "'"
             );
         }
     }

     /*
    ⟨Llamada-Método⟩ ::= ⟨Argumentos-Actuales⟩ ⟨Llamada-MétodoF⟩
    */
    public void llamadaMetodo() throws LexicalException, SyntacticException {
        argumentosActuales();
        llamadaMetodoF();
    }

    /*
    ⟨Llamada-MétodoF⟩ ::= λ
        | ⟨Encadenado⟩
    INCOMPLETO
    */
    public void llamadaMetodoF() throws SyntacticException, LexicalException {
        // REVISAR AMBIGUEDAD CON TokenType.SPECIAL_SYMBOL_P
        if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
            encadenado();
        } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM, TokenType.SPECIAL_SYMBOL_P)) {

            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', '.', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨Llamada-Método-Estático⟩ ::= idclass . id ⟨Llamada-Método⟩ ⟨Llamada-Método-EstáticoF⟩
    */
    public void llamadaMetodoEstatico() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        llamadaMetodo();
        llamadaMetodoEstaticoF();
    }

    /*
    ⟨Llamada-Método-EstáticoF⟩ ::= λ
        | ⟨Encadenado⟩
    */
    public void llamadaMetodoEstaticoF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_P)){
            encadenado();
        } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {
            // Siguientes de llamadaMetodoEstaticoF, cierro

            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '.', '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    
    /*
        ⟨Llamada-Conclassor⟩ ::= new ⟨Llamada-ConclassorF1⟩
    */
    public void llamadaConclassor() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_NEW);
        llamadaConclassorF1();
    }
    
    /*
    ⟨Llamada-ConclassorF1⟩ ::= idclass ⟨Argumentos-Actuales⟩ ⟨Llamada-ConclassorF2⟩
        | ⟨Tipo-Primitivo⟩ [ ⟨Expresión⟩ ] ⟨Llamada-ConclassorF2⟩
    */
    public void llamadaConclassorF1() throws LexicalException, SyntacticException {
        if (checkType(TokenType.IDENTIFIER_CLASS)){
            matchTokenType(TokenType.IDENTIFIER_CLASS);
            argumentosActuales();
            llamadaConclassorF2();
        } else if (checkType(TokenType.INT_CLASS, TokenType.BOOL_CLASS, TokenType.STR_CLASS)) {
            tipoPrimitivo();
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
            llamadaConclassorF2();
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int' o un identificador de clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
    ⟨Llamada-ConclassorF2⟩ ::= λ
        | ⟨Encadenado⟩
    */
    public void llamadaConclassorF2() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_P)){
            encadenado();
        } else if (!checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {

            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '.', '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    
    /*
	⟨Argumentos-Actuales⟩ ::= ( ⟨Argumentos-ActualesF⟩
    */
    public void argumentosActuales() throws SyntacticException, LexicalException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        argumentosActualesF();
    }
    
    /*
	⟨Argumentos-ActualesF⟩ ::= )
		| ⟨Lista-Expresiones⟩ )
    */
    public void argumentosActualesF() throws SyntacticException, LexicalException {
        if(checkType(TokenType.SPECIAL_SYMBOL_PC)){
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        } else if (checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB,
                TokenType.OPERATOR_NOT, TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC,
                TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT,
                TokenType.STRING_CONSTANT, TokenType.SPECIAL_SYMBOL_PO, TokenType.KW_SELF,
                TokenType.IDENTIFIER_CLASS, TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {

            listaExpresiones();
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ')', '+', '-', '!', '++', '--', 'nil', '(', 'self', 'new', booleano, entero, string, una variable o una clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /*
	⟨Lista-Expresiones⟩ ::= ⟨Expresión⟩ ⟨Lista-ExpresionesF⟩
    */
    public void listaExpresiones() throws SyntacticException, LexicalException {
        expresion();
        listaExpresionesF();
    }

    /*
	⟨Lista-ExpresionesF⟩ ::= λ
		| , ⟨Lista-Expresiones⟩
    */
    public void listaExpresionesF() throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_CM)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_CM);
            listaExpresiones();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_PC)) {
            // Siguientes de listaExpresionesF, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ',' o ')', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }
    
    /*
        ⟨Encadenado⟩ ::= . id ⟨EncadenadoF⟩
	*/
    public void encadenado() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        encadenadoF();
    }
    /*
        ⟨EncadenadoF⟩ ::= ⟨Llamada-Método⟩
            | ⟨AccesoVar⟩
     */
    public void encadenadoF() throws LexicalException, SyntacticException {
        // DUDA, AMBIGUEDAD CON '.'
        if (checkType(TokenType.SPECIAL_SYMBOL_PO)) {
            llamadaMetodo();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB, TokenType.SPECIAL_SYMBOL_P)) {
            accesoVar();
        } else if (checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB,
                TokenType.OPERATOR_LT, TokenType.OPERATOR_GT, TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR, TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S,
                TokenType.SPECIAL_SYMBOL_RB, TokenType.SPECIAL_SYMBOL_CM, TokenType.SPECIAL_SYMBOL_P)) {
            // Siguientes de encadenadoF, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '(', '[', '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')', ';', ']', ',', o '.', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

}
