package uncuyo.compiladores.semantic;

import uncuyo.compiladores.exceptions.lexical.LexerFileReadException;
import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.exceptions.semantic.symbolTableException.ConstructorRedefinitionException;
import uncuyo.compiladores.exceptions.syntactic.MethodOutsideImplException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.lexer.Token;
import uncuyo.compiladores.lexer.TokenDefinitions;
import uncuyo.compiladores.lexer.TokenType;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.*;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.AssignmentNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.SentenceNode;
import uncuyo.compiladores.semantic.symbolTable.*;
import uncuyo.compiladores.semantic.symbolTable.Class;

import java.util.LinkedList;


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

    private void matchTokenType(TokenType expectedTokenType) throws LexerFileReadException, LexicalException, SyntacticException {

        if (currentToken.getTokenType().equals(expectedTokenType)){
            nextToken();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba " + TokenDefinitions.getLexeme(expectedTokenType) + ", pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }

    }

    /* ⟨program⟩ ::= ⟨Lista-Definiciones⟩ ⟨Start⟩ */
    public void program() throws LexicalException, SyntacticException {
        listaDefiniciones();
        start();
        symbolTable.setStart(symbolTable.getCurrentMethod());
        matchTokenType(TokenType.EOF);
        symbolTable.consolidate();
    }

    /*
        ⟨Start⟩ ::= start ⟨Bloque-Método⟩
    */
    public void start() throws LexerFileReadException, LexicalException, SyntacticException {
        Token auxToken = currentToken;
        if (checkLexeme("start")){
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        } else {
            throw new MethodOutsideImplException(auxToken);
        }
        Type type = new Type(TokenType.KW_VOID , "void");
        Method startMethod = new Method(auxToken, type, false);
        symbolTable.setCurrentMethod(startMethod);
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

        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        Class currentClass = symbolTable.searchAndCopyClass(auxToken.getLexeme());

        boolean classAlreadyExists = currentClass != null;

        if (!classAlreadyExists){
            currentClass = new Class(auxToken);
        }

        symbolTable.setCurrentClass(currentClass);

        Type type = classF();
        currentClass.setParent(type.getLexeme());
        currentClass.setFoundClass(true);

        if (classAlreadyExists){
            symbolTable.mergeClassDeclarations(symbolTable.searchOriginalClass(currentClass.getName()));
        }else {
            symbolTable.saveClass(currentClass);
        }
    }

    /*
        ⟨classF⟩ ::= {classF.tipo = Object} { ⟨AtributoK⟩ }
            | ⟨Herencia⟩ {classF.tipo = Herencia.tipo} { ⟨AtributoK⟩ }
    */
    public Type classF() throws LexicalException, SyntacticException {
        Type type;

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

        Token auxToken = currentToken;

        matchTokenType(TokenType.IDENTIFIER_CLASS);

        Class currentClass = symbolTable.searchAndCopyClass(auxToken.getLexeme());

        boolean classAlreadyExists = currentClass != null;

        if (!classAlreadyExists){
            currentClass = new Class(auxToken);
        }

        symbolTable.setCurrentClass(currentClass);

        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        miembroK();
        matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);

        symbolTable.getCurrentClass().setFoundImpl(true);
        if (classAlreadyExists){
            symbolTable.mergeClassImplements(symbolTable.searchOriginalClass(currentClass.getName()));
        }else {
            symbolTable.saveClass(currentClass);
        }
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
        Token auxToken = currentToken;
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        Class currentClass = symbolTable.getCurrentClass();
        if (currentClass.getConstructor() != null) {
            throw new ConstructorRedefinitionException(auxToken.getRowIndex(), auxToken.getColumnIndex(),
                    currentClass.getName(), currentClass.getConstructor().getToken());
        }
        Method constructor = new Method(auxToken, new Type(TokenType.KW_CLASS, currentClass.getName()), false);
        symbolTable.getCurrentClass().setConstructor(constructor);
        symbolTable.setCurrentMethod(constructor);
        argumentosFormales();
        bloqueMetodo();
    }

    /*

    ⟨Atributo⟩ ::= ⟨Tipo⟩ {ListaDeclaracionVariables.tipo = Tipo.tipo, ListaDeclaracionVariables.privado = true } ⟨Lista-Declaración-Variables⟩ ;
        | pub ⟨Tipo⟩ {ListaDeclaracionVariables.tipo = Tipo.tipo, ListaDeclaracionVariables.privado = false}  ⟨Lista-Declaración-Variables⟩ ;
    */
    public void atributo() throws LexicalException, SyntacticException {
        Type type;
        if (checkType(TokenType.IDENTIFIER_CLASS, TokenType.STR_CLASS, TokenType.BOOL_CLASS,
                TokenType.INT_CLASS, TokenType.ARRAY_CLASS)) {
            type = tipo();
            listaDeclaracionVariables(type , true, true);
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if (checkType(TokenType.KW_PUB)) {
            matchTokenType(TokenType.KW_PUB);
            type = tipo();
            listaDeclaracionVariables(type , false, true);
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
        | st fn ⟨MétodoF⟩
    */
    public void metodo() throws LexicalException, SyntacticException {
        boolean isStatic = false;
        if(checkType(TokenType.KW_FN)){
            matchTokenType(TokenType.KW_FN);
            metodoF(isStatic);
        }else if (checkType(TokenType.KW_ST)){
            matchTokenType(TokenType.KW_ST);
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
        Type type;
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
        Type tipo = tipo();
        listaDeclaracionVariables(tipo , true, false);
        matchTokenType(TokenType.SPECIAL_SYMBOL_S);
    }

    /*
        ⟨Lista-Declaración-Variables⟩ ::= idMetAt {if(TS.yaExiste(ClaseActual, idMetAt.lex)) then Error
         else TS.guardar(ClaseActual, idMetAt.lex, ListaDeclaracionVariables.tipo) }
         {ListaDeclaracionVariablesF.tipo = ListaDeclaracionVariables.tipo, ListaDeclaracionVariablesF.privado = ListaDeclaracionVariables.privado} ⟨Lista-Declaración-VariablesF⟩    */
    public void listaDeclaracionVariables(Type type , boolean isPrivate, boolean isClass) throws LexicalException, SyntacticException {
        Token auxToken = currentToken;

        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);

        if (isClass) {

            if(symbolTable.getCurrentClass().attributeAlreadyExists(auxToken.getLexeme())){
                throw new SemanticException(auxToken.getRowIndex(),
                        auxToken.getColumnIndex(),
                        "El atributo '"+ auxToken.getLexeme() + "' ya ha sido definido dentro de la clase '" +
                                symbolTable.getCurrentClass().getToken().getLexeme() + "'");
            }

            Attribute attribute = new Attribute(auxToken, type, isPrivate);
            symbolTable.getCurrentClass().insertAttribute(attribute);
        } else {
            Variable variable = new Variable(auxToken, type);
            getSymbolTable().getCurrentMethod().insertVariable(variable);
        }

        listaDeclaracionVariablesF(type, isPrivate, isClass);
    }
    
    /*
        ⟨Lista-Declaración-VariablesF⟩ ::= , {ListaDeclaracionVariables.tipo = ListaDeclaracionVariablesF.tipo, ListaDeclaracionVariables.privado = ListaDeclaracionVariablesF.privado } ⟨Lista-Declaración-Variables⟩
            | λ
    */
    public void listaDeclaracionVariablesF(Type type, boolean isPrivate, boolean isClass) throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_CM)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_CM);
            listaDeclaracionVariables(type , isPrivate, isClass);
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
        Type type;
        if(checkType(TokenType.STR_CLASS, TokenType.BOOL_CLASS,
                TokenType.INT_CLASS, TokenType.ARRAY_CLASS, TokenType.IDENTIFIER_CLASS)) {
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
        Type type;

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
        matchTokenType(TokenType.ARRAY_CLASS);
        Type subType = tipoPrimitivo();
        return new ArrayType(subType);
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
    public SentenceNode sentencia() throws LexicalException, SyntacticException {
        SentenceNode sentenceNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_S)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if (checkType(TokenType.IDENTIFIER_VAR_METHOD, TokenType.KW_SELF)) {
            sentenceNode = asignacion();
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
        return sentenceNode;
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
    public AssignmentNode asignacion() throws LexicalException, SyntacticException {

        AssignmentNode assignmentNode;
        ExpressionNode expressionNode;
        if (checkType(TokenType.IDENTIFIER_VAR_METHOD)) {
            SimpleAccessNode simpleAccessNode = accesoVarSimple();
            matchTokenType(TokenType.OPERATOR_ASSIGN);
            expressionNode = expresion();
            assignmentNode = new AssignmentNode(simpleAccessNode, expressionNode);

        } else if (checkType(TokenType.KW_SELF)) {
            SimpleAccessNode simpleAccessNode = accesoSelfSimple();
            matchTokenType(TokenType.OPERATOR_ASSIGN);
            expressionNode = expresion();
            assignmentNode = new AssignmentNode(simpleAccessNode, expressionNode);
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'self' o un identificador de variable, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return assignmentNode;
    }

     /*
	⟨AccesoVar-Simple⟩ ::= id ⟨AccesoVar-SimpleF⟩
     */
    public SimpleAccessNode accesoVarSimple() throws LexicalException, SyntacticException {
        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        return accesoVarSimpleF(auxToken);
    }

    /*
    ⟨AccesoVar-SimpleF⟩ ::= ⟨Encadenado-SimpleK⟩
        | [ ⟨Expresión⟩ ]
     */

    public SimpleAccessNode accesoVarSimpleF(Token token) throws LexicalException, SyntacticException {
        SimpleAccessNode simpleAccessNode = null;
        if(checkType(TokenType.SPECIAL_SYMBOL_P)) {
            simpleAccessNode = new SimpleVariableNode(token, null);
            SimpleVariableNode simpleVariableNode = (SimpleVariableNode) simpleAccessNode;
            simpleVariableNode.setSimpleChainedNode(encadenadoSimpleK());
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            simpleAccessNode = new SimpleArrayNode(token, expresion());
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
        } else if (checkType(TokenType.OPERATOR_ASSIGN)) {
            // Primeros(⟨Encadenado-SimpleK⟩) contiene λ, por lo tanto checkeo siguientes
            // Siguientes de accesoVarSimpleF, cierro
            simpleAccessNode = new SimpleVariableNode(token, null);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '=', '.', '[' pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return simpleAccessNode;
    }

    /*
    ⟨AccesoSelf-Simple⟩ ::= self ⟨Encadenado-SimpleK⟩
    */
    public SimpleSelfNode accesoSelfSimple() throws LexicalException, SyntacticException {
        Token auxToken = currentToken;
        matchTokenType(TokenType.KW_SELF);
        return new SimpleSelfNode(encadenadoSimpleK());
    }

    /*
    ⟨Encadenado-SimpleK⟩ ::= ⟨Encadenado-Simple⟩ ⟨Encadenado-SimpleK⟩
        | λ
    */
    public SimpleChainedNode encadenadoSimpleK() throws LexicalException, SyntacticException {
        SimpleChainedNode simpleChainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
            simpleChainedNode = encadenadoSimple();
            simpleChainedNode.setSimpleChainedNode(encadenadoSimpleK());
        } else if (checkType(TokenType.OPERATOR_ASSIGN)) {
            // Siguientes de encadenadoSimpleK, cierro
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '=', '.', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return simpleChainedNode;
    }

    /*
    ⟨Encadenado-Simple⟩ ::= . id
    */
    public SimpleChainedNode encadenadoSimple() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        return new SimpleChainedNode(auxToken,null,
                symbolTable.getCurrentClass().getName(),
                symbolTable.getCurrentMethod().getName());
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
    public ExpressionNode expresion() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expOr();
        System.out.println(expressionNode.toJson(""));
        return expressionNode;

    }
    /*
	⟨ExpOr⟩ ::= ⟨ExpAnd⟩ ⟨ExpOrR⟩
    */
    public ExpressionNode expOr() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expAnd();
        BinaryExpressionNode binaryExpressionNode = expOrR(expressionNode);
        return binaryExpressionNode == null ? expressionNode : binaryExpressionNode;
    }

    /*
    ⟨ExpOrR⟩ ::= || ⟨ExpAnd⟩ ⟨ExpOrR⟩
        | λ
    */
    public BinaryExpressionNode expOrR(ExpressionNode expressionNodeL) throws LexicalException, SyntacticException {
        BinaryExpressionNode binaryExpressionNode = null;
        if (checkType(TokenType.OPERATOR_OR)) {
            Token auxToken = currentToken;
            matchTokenType(TokenType.OPERATOR_OR);
            ExpressionNode expressionNodeR = expAnd();
            binaryExpressionNode = new BinaryExpressionNode(auxToken, expressionNodeL, expressionNodeR);
            BinaryExpressionNode binaryExpressionNodeAux = expOrR(binaryExpressionNode);
            if (binaryExpressionNodeAux != null) {
                binaryExpressionNode = binaryExpressionNodeAux;
            }
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
        return binaryExpressionNode;
    }

    /*
	⟨ExpAnd⟩ ::= ⟨ExpIgual⟩ ⟨ExpAndR⟩
    */
    public ExpressionNode expAnd() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expIgual();
        BinaryExpressionNode binaryExpressionNode = expAndR(expressionNode);
        return binaryExpressionNode == null ? expressionNode : binaryExpressionNode;
    }

    /*
    ⟨ExpAndR⟩ ::= && ⟨ExpIgual ⟩ ⟨ExpAndR⟩
        | λ
    */
    public BinaryExpressionNode expAndR(ExpressionNode expressionNodeL) throws LexicalException, SyntacticException {
        BinaryExpressionNode binaryExpressionNode = null;
        if (checkType(TokenType.OPERATOR_AND)) {
            Token binaryOperationToken = currentToken;
            matchTokenType(TokenType.OPERATOR_AND);
            ExpressionNode expressionNodeR = expIgual();
            binaryExpressionNode = new BinaryExpressionNode(binaryOperationToken, expressionNodeL, expressionNodeR);
            BinaryExpressionNode binaryExpressionNodeAux = expAndR(binaryExpressionNode);
            if (binaryExpressionNodeAux != null){
                binaryExpressionNode = binaryExpressionNodeAux;
            }
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
        return binaryExpressionNode;
    }

    /*
	⟨ExpIgual ⟩ ::= ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩
    */
    public ExpressionNode expIgual() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expCompuesta();
        BinaryExpressionNode binaryExpressionNode = expIgualR(expressionNode);
        return binaryExpressionNode == null ? expressionNode : binaryExpressionNode;
    }

    /*
    ⟨ExpIgualR⟩ ::= ⟨OpIgual⟩ ⟨ExpCompuesta⟩ ⟨ExpIgualR⟩
        | λ
    */
    public BinaryExpressionNode expIgualR(ExpressionNode expressionNodeL) throws LexicalException, SyntacticException {
        BinaryExpressionNode binaryExpressionNode = null;
        if(checkType(TokenType.OPERATOR_EQ, TokenType.OPERATOR_NEQ)) {
            Token binaryOperationToken = opIgual();
            ExpressionNode expressionNodeR = expCompuesta();
            binaryExpressionNode = new BinaryExpressionNode(binaryOperationToken, expressionNodeL, expressionNodeR);
            BinaryExpressionNode binaryExpressionNodeAux = expIgualR(binaryExpressionNode);
            if (binaryExpressionNodeAux != null) {
                binaryExpressionNode = binaryExpressionNodeAux;
            }
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
        return binaryExpressionNode;
    }

    /*
	⟨ExpCompuesta⟩ ::= ⟨ExpAd⟩ ⟨ExpCompuestaF⟩
    */
    public ExpressionNode expCompuesta() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expAd();
        BinaryExpressionNode binaryExpressionNode = expCompuestaF(expressionNode);
        return binaryExpressionNode == null ? expressionNode : binaryExpressionNode;
    }

    /*
	⟨ExpCompuestaF⟩ ::= ⟨OpCompuesto⟩ ⟨ExpAd⟩
		| λ
    */
    public BinaryExpressionNode expCompuestaF(ExpressionNode expressionNodeL) throws LexicalException, SyntacticException {
        BinaryExpressionNode binaryExpressionNode = null;
        if (checkType(TokenType.OPERATOR_GT, TokenType.OPERATOR_LT, TokenType.OPERATOR_GTE, TokenType.OPERATOR_LTE)) {
            Token binaryOperationToken = opCompuesto();
            ExpressionNode expressionNodeR = expAd();
            binaryExpressionNode = new BinaryExpressionNode(binaryOperationToken, expressionNodeL, expressionNodeR);
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
        return binaryExpressionNode;
    }

    /*
    ⟨ExpAd ⟩ ::= ⟨ExpMul⟩ ⟨ExpAdR⟩
    */
    public ExpressionNode expAd() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expMul();
        BinaryExpressionNode binaryExpressionNode = expAdR(expressionNode);
        return binaryExpressionNode == null ? expressionNode : binaryExpressionNode;
    }

    /*
    ⟨ExpAdR⟩ ::= ⟨OpAd⟩ ⟨ExpMul⟩ ⟨ExpAdR⟩
        | λ
    */
    public BinaryExpressionNode expAdR(ExpressionNode expressionNodeL) throws LexicalException, SyntacticException {
        BinaryExpressionNode binaryExpressionNode = null;
        if (checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB)) {
            Token binaryOperationToken = opAd();
            ExpressionNode expressionNodeR = expMul();
            binaryExpressionNode = new BinaryExpressionNode(binaryOperationToken, expressionNodeL, expressionNodeR);
            BinaryExpressionNode binaryExpressionNodeAux = expAdR(binaryExpressionNode);
            if (binaryExpressionNodeAux != null) {
                binaryExpressionNode = binaryExpressionNodeAux;
            }
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
        return binaryExpressionNode;
    }

    /*
    ⟨ExpMul⟩ ::= ⟨ExpUn⟩ ⟨ExpMulR⟩
    */
    public ExpressionNode expMul() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expUn();
        BinaryExpressionNode binaryExpressionNode = expMulR(expressionNode);
        return binaryExpressionNode == null ? expressionNode : binaryExpressionNode;
    }

    /*
    ⟨ExpMulR⟩ ::= ⟨OpMul⟩ ⟨ExpUn⟩ ⟨ExpMulR⟩
        | λ
    */
    public BinaryExpressionNode expMulR(ExpressionNode expressionNodeL) throws LexicalException, SyntacticException {
        BinaryExpressionNode binaryExpressionNode = null;
        if (checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV)) {
            Token binaryOperationToken = opMul();
            ExpressionNode expressionNodeR = expUn();
            binaryExpressionNode = new BinaryExpressionNode(binaryOperationToken, expressionNodeL, expressionNodeR);
            BinaryExpressionNode binaryExpressionNodeAux = expMulR(binaryExpressionNode);
            if (binaryExpressionNodeAux != null) {
                binaryExpressionNode = binaryExpressionNodeAux;
            }
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
        return binaryExpressionNode;
    }

    /*
    ⟨ExpUn⟩ ::= ⟨OpUnario⟩ ⟨ExpUn⟩
        | ⟨Operando⟩
    */
    public ExpressionNode expUn() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode;
        if(checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB, TokenType.OPERATOR_NOT,
                TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC)) {

            Token unaryOperationToken = opUnario();
            expressionNode = new UnaryExpressionNode(expUn(), unaryOperationToken);
        } else if (checkType(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT,
                    TokenType.SPECIAL_SYMBOL_PO, TokenType.KW_SELF, TokenType.IDENTIFIER_CLASS,
                    TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {
            expressionNode = operando();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '+', '-', '!', '++', '--', 'nil', 'true', 'false', entero, string, '(', 'self', identificador de clase, 'new' " +
                            "o identificador de variable o metodo, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return expressionNode;
    }

    /*
    ⟨OpIgual ⟩ ::= ==
        | !=
    */
    public Token opIgual() throws LexicalException, SyntacticException {
        Token auxToken = currentToken;
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
        return auxToken;
    }

    /*
    ⟨OpCompuesto⟩ ::= <
        | >
        | <=
        | >=
    */
    public Token opCompuesto() throws LexicalException, SyntacticException {
        Token auxToken = currentToken;
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
        return auxToken;
    }

    /*
    ⟨OpAd ⟩ ::= +
        | -

    */
    public Token opAd() throws LexicalException, SyntacticException {
        Token auxToken = currentToken;
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
        return auxToken;
    }

    /*
    ⟨OpUnario⟩ ::= 	+
        | -
        | !
        | ++
        | --
    */
    public Token opUnario() throws LexicalException, SyntacticException {
        Token auxToken = currentToken;
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
        return auxToken;
    }

    /*
    ⟨OpMul ⟩ ::= *
        | /
    */
    public Token opMul() throws LexicalException, SyntacticException {
        Token auxToken = currentToken;
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
        return auxToken;
    }

    /*
        ⟨Operando⟩ ::= ⟨Literal⟩
            | ⟨Primario⟩
    */
    public ExpressionNode operando() throws LexicalException, SyntacticException {

        ExpressionNode expressionNode;
        if(checkType(TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT)) {
            expressionNode = literal();
        } else if (checkType(TokenType.SPECIAL_SYMBOL_PO, TokenType.KW_SELF, TokenType.IDENTIFIER_CLASS,
                TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {
            expressionNode = primario();
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'nil', 'true', 'false', entero, string, '(', 'self', identificador de clase, 'new' " +
                            "o identificador de variable o metodo, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return expressionNode;
    }

    /*
    ⟨Literal⟩ ::= nil
        | true
        | false
        | intLiteral
        | StrLiteral
    */
    public LiteralNode literal() throws LexicalException, SyntacticException {

        Token auxToken = currentToken;
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
        return new LiteralNode(auxToken);
    }

    /*

    ⟨Primario⟩ ::= ⟨ExpresionParentizada⟩
        | ⟨AccesoSelf⟩
        | ⟨Llamada-Método-Estático⟩
        | ⟨Llamada-Conclassor⟩
        | id ⟨VarOMetodo⟩
    */

    public ExpressionNode primario() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode;
        if (checkType(TokenType.SPECIAL_SYMBOL_PO)) {
            expressionNode = expresionParentizada();
        } else if (checkType(TokenType.KW_SELF)) {
            expressionNode = accesoSelf();
        } else if (checkType(TokenType.IDENTIFIER_CLASS)) {
            expressionNode = llamadaMetodoEstatico();
        } else if (checkType(TokenType.KW_NEW)) {
            expressionNode = llamadaConclassor();
        } else if (checkType(TokenType.IDENTIFIER_VAR_METHOD)) {
            Token auxToken = currentToken;
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            expressionNode = varOMetodo(auxToken);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '(', 'self', identificador de clase, 'new', o un identificador de variable o método, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return expressionNode;
    }

    /*
	⟨VarOMetodo⟩ ::= ⟨AccesoVar⟩
		| ⟨Llamada-Método⟩
    */
    public ChainedNode varOMetodo(Token token) throws SyntacticException, LexicalException {
        ChainedNode chainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_LB, TokenType.SPECIAL_SYMBOL_P)){
            chainedNode = accesoVar(token);
        }else if (checkType(TokenType.SPECIAL_SYMBOL_PO)){
            chainedNode = llamadaMetodo(token);
        } else if (checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {
            chainedNode = new VariableNode(token, null, symbolTable.getCurrentClass().getName(), symbolTable.getCurrentMethod().getName());
        } else{
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '[','.','(','*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return chainedNode;
    }

    /*
    ⟨ExpresionParentizada⟩ ::= ( ⟨Expresión⟩ ) ⟨ExpresionParentizadaF⟩
    */
    public ParenthesizedExpressionNode expresionParentizada() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        ExpressionNode expressionNode = expresion();
        matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        ChainedNode chainedNode = expresionParentizadaF();
        return new ParenthesizedExpressionNode(expressionNode, chainedNode);
    }

    /*
	⟨ExpresionParentizadaF⟩ ::=  λ
        | ⟨Encadenado⟩
    */
    public ChainedNode expresionParentizadaF() throws LexicalException, SyntacticException {
        ChainedNode chainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_P)){
            chainedNode = encadenado();
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
        return chainedNode;
    }

    /*
    ⟨AccesoSelf⟩ ::= self ⟨AccesoSelfF⟩
    */
    public SelfNode accesoSelf() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_SELF);
        return new SelfNode(accesoSelfF());
    }

    /*
	⟨AccesoSelfF⟩::= λ
        | ⟨Encadenado⟩
    */
    public ChainedNode accesoSelfF() throws LexicalException, SyntacticException {
        ChainedNode chainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_P)){
            chainedNode = encadenado();
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
        return chainedNode;
    }

    /*
	⟨AccesoVar⟩ ::= λ
        | ⟨Encadenado⟩
        | [ ⟨Expresión⟩ ] ⟨AccesoVarF⟩
    */
    public ChainedNode accesoVar(Token token) throws SyntacticException, LexicalException {
        ChainedNode chainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
            chainedNode = new VariableNode(token, null, symbolTable.getCurrentClass().getName(), symbolTable.getCurrentMethod().getName());
            chainedNode.setChainedNode(encadenado());
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            ExpressionNode expressionNode = expresion();
            chainedNode = new ArrayNode(token, null, expressionNode);
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
            chainedNode.setChainedNode(accesoVarF());
        } else if (checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {
            chainedNode = new VariableNode(token, null, symbolTable.getCurrentClass().getName(), symbolTable.getCurrentMethod().getName());
        } else{
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba '.', '*', '/', '+', '-', '<', '>', '<=', '>=', '==', '!=', '&&', '||', ')' , ';' , ']', ',', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }

        return chainedNode;
    }

    /*
    ⟨AccesoVarF⟩ ::= λ
        | ⟨Encadenado⟩
    */
     public ChainedNode accesoVarF() throws SyntacticException, LexicalException {
         ChainedNode chainedNode = null;

         if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
             chainedNode = encadenado();
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
         return chainedNode;
     }

     /*
    ⟨Llamada-Método⟩ ::= ⟨Argumentos-Actuales⟩ ⟨Llamada-MétodoF⟩
    */
    public ChainedCallNode llamadaMetodo(Token token) throws LexicalException, SyntacticException {
        LinkedList<ExpressionNode> expressionNodes = argumentosActuales();
        ChainedNode chainedNode = llamadaMetodoF();
        return new ChainedCallNode(token, chainedNode, expressionNodes);
    }

    /*
    ⟨Llamada-MétodoF⟩ ::= λ
        | ⟨Encadenado⟩
    */
    public ChainedNode llamadaMetodoF() throws SyntacticException, LexicalException {
        ChainedNode chainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_P)) {
            chainedNode = encadenado();
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

        return chainedNode;
    }

    /*
    ⟨Llamada-Método-Estático⟩ ::= idclass . id ⟨Llamada-Método⟩
    */
    public ChainedCallNode llamadaMetodoEstatico() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        return llamadaMetodo(auxToken);
    }
    
    /*
        ⟨Llamada-Conclassor⟩ ::= new ⟨Llamada-ConclassorF1⟩
    */
    public ConclassorCallNode llamadaConclassor() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.KW_NEW);
        return llamadaConclassorF1();
    }
    
    /*
    ⟨Llamada-ConclassorF1⟩ ::= idclass ⟨Argumentos-Actuales⟩ ⟨Llamada-ConclassorF2⟩
        | ⟨Tipo-Primitivo⟩ [ ⟨Expresión⟩ ] ⟨Llamada-ConclassorF2⟩
    */
    public ConclassorCallNode llamadaConclassorF1() throws LexicalException, SyntacticException {
        ConclassorCallNode conclassorCallNode;
        if (checkType(TokenType.IDENTIFIER_CLASS)){
            Token auxToken = currentToken;
            matchTokenType(TokenType.IDENTIFIER_CLASS);
            Type type = new Type(auxToken.getTokenType(), auxToken.getLexeme());
            LinkedList<ExpressionNode> expressionNodes = argumentosActuales();
            ChainedNode chainedNode = llamadaConclassorF2();
            conclassorCallNode = new ClassConstructorNode(type, expressionNodes, chainedNode);
        } else if (checkType(TokenType.INT_CLASS, TokenType.BOOL_CLASS, TokenType.STR_CLASS)) {
            Type type = tipoPrimitivo();
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            ExpressionNode expressionNode = expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
            ChainedNode chainedNode = llamadaConclassorF2();
            conclassorCallNode = new ArrayConstructorNode(type, chainedNode, expressionNode);
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int' o un identificador de clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }

        return conclassorCallNode;
    }

    /*
    ⟨Llamada-ConclassorF2⟩ ::= λ
        | ⟨Encadenado⟩
    */
    public ChainedNode llamadaConclassorF2() throws LexicalException, SyntacticException {
        ChainedNode chainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_P)){
            chainedNode = encadenado();
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
        return chainedNode;
    }
    
    /*
	⟨Argumentos-Actuales⟩ ::= ( ⟨Argumentos-ActualesF⟩
    */
    public LinkedList<ExpressionNode> argumentosActuales() throws SyntacticException, LexicalException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        return argumentosActualesF();
    }
    
    /*
	⟨Argumentos-ActualesF⟩ ::= )
		| ⟨Lista-Expresiones⟩ )
    */
    public LinkedList<ExpressionNode> argumentosActualesF() throws SyntacticException, LexicalException {
        LinkedList<ExpressionNode> expressionNodes = new LinkedList<>();
        if(checkType(TokenType.SPECIAL_SYMBOL_PC)){
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        } else if (checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB,
                TokenType.OPERATOR_NOT, TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC,
                TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT, TokenType.INT_CONSTANT,
                TokenType.STRING_CONSTANT, TokenType.SPECIAL_SYMBOL_PO, TokenType.KW_SELF,
                TokenType.IDENTIFIER_CLASS, TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {

            listaExpresiones(expressionNodes);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ')', '+', '-', '!', '++', '--', 'nil', '(', 'self', 'new', booleano, entero, string, una variable o una clase, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }

        return expressionNodes;
    }

    /*
	⟨Lista-Expresiones⟩ ::= ⟨Expresión⟩ ⟨Lista-ExpresionesF⟩
    */
    public void listaExpresiones(LinkedList<ExpressionNode> expressionNodes) throws SyntacticException, LexicalException {
        expressionNodes.add(expresion());
        listaExpresionesF(expressionNodes);
    }

    /*
	⟨Lista-ExpresionesF⟩ ::= λ
		| , ⟨Lista-Expresiones⟩
    */
    public void listaExpresionesF(LinkedList<ExpressionNode> expressionNodes) throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_CM)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_CM);
            listaExpresiones(expressionNodes);
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
    public ChainedNode encadenado() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        return encadenadoF(auxToken);
    }
    /*
        ⟨EncadenadoF⟩ ::= ⟨Llamada-Método⟩
            | ⟨AccesoVar⟩
     */
    public ChainedNode encadenadoF(Token token) throws LexicalException, SyntacticException {
        ChainedNode chainedNode = null;
        if (checkType(TokenType.SPECIAL_SYMBOL_PO)) {
            chainedNode = llamadaMetodo(token);
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB, TokenType.SPECIAL_SYMBOL_P)) {
            chainedNode = accesoVar(token);
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
        return chainedNode;
    }

}
