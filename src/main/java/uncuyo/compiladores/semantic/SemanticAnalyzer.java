package uncuyo.compiladores.semantic;

import java.util.LinkedList;

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
import uncuyo.compiladores.semantic.abstractSintaxTree.AST;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ArrayConstructorNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ArrayNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.BinaryExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ChainedCallNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ChainedNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ClassConstructorNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ConclassorCallNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.LiteralNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.ParenthesizedExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SelfNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleAccessNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleArrayNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleChainedNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleSelfNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.SimpleVariableNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.UnaryExpressionNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.expression.VariableNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.AssignmentNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.BlockNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.ForNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.IfElseNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.ReturnNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.SentenceNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.SimpleSentenceNode;
import uncuyo.compiladores.semantic.abstractSintaxTree.sentence.WhileNode;
import uncuyo.compiladores.semantic.symbolTable.ArrayType;
import uncuyo.compiladores.semantic.symbolTable.Attribute;
import uncuyo.compiladores.semantic.symbolTable.Block;
import uncuyo.compiladores.semantic.symbolTable.Class;
import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Parameter;
import uncuyo.compiladores.semantic.symbolTable.SymbolTable;
import uncuyo.compiladores.semantic.symbolTable.Type;
import uncuyo.compiladores.semantic.symbolTable.Variable;
import uncuyo.compiladores.syntactic.SyntacticAnalyzer;


public class SemanticAnalyzer {

    private enum Phase { DECLARATIONS, SENTENCES, CODE_GENERATION }

    private final Lexer lexer;
    private Token currentToken;
    private final SymbolTable symbolTable = new SymbolTable();
    private AST ast;
    private boolean declarationsOnly;

    /**
     * Prepara el análisis con el primer token ya obtenido del lector léxico.
     *
     * @param lexer lector que entregará los tokens restantes
     * @param token primer token del archivo, que todavía debe reconocerse
     */
    public SemanticAnalyzer(Lexer lexer, Token token) {
        this.lexer = lexer;
        this.currentToken = token;
    }

    /**
     * Obtiene la tabla construida por esta instancia.
     *
     * @return tabla mutable; queda consolidada si el análisis termina correctamente
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    /**
     * Obtiene el árbol construido durante el análisis de sentencias.
     *
     * @return AST de la instancia, o {@code null} en el modo de sólo declaraciones
     */
    public AST getAst() {
        return ast;
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

    /**
     * Reconoce el programa completo, consolida la TS, verifica el AST y genera código.
     * Para limitar el análisis a declaraciones debe usarse {@link #analyzeDeclarations()}.
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     * @throws SemanticException si falla un chequeo semántico
     */
    public void program() throws LexicalException, SyntacticException {
        analyzeProgram(Phase.CODE_GENERATION);
    }

    /**
     * Construye y consolida la tabla de símbolos sin crear el AST ni generar código.
     *
     * <p>Registra clases, miembros, parámetros y variables locales. Los cuerpos
     * se delegan al analizador sintáctico, de modo que esta entrada no verifica
     * asignaciones, llamadas, condiciones ni retornos. Al terminar exige EOF
     * y ejecuta {@link SymbolTable#consolidate()}.</p>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     * @throws SemanticException si se detecta un error de declaraciones
     */
    public void analyzeDeclarations() throws LexicalException, SyntacticException {
        analyzeProgram(Phase.DECLARATIONS);
    }

    /**
     * Construye y comprueba el AST, después de consolidar la TS, sin generar código.
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     * @throws SemanticException si falla el chequeo de declaraciones o sentencias
     */
    public void analyzeSentences() throws LexicalException, SyntacticException {
        analyzeProgram(Phase.SENTENCES);
    }

    /**
     * Coordina el reconocimiento, la consolidación y las fases opcionales.
     * Restablece el contexto de clase antes de reconocer {@code start} y sólo
     * verifica el AST después de completar la TS.
     *
     * @param phase última fase que se ejecutará en esta lectura
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     * @throws SemanticException si falla una comprobación semántica
     */
    private void analyzeProgram(Phase phase) throws LexicalException, SyntacticException {
        declarationsOnly = phase == Phase.DECLARATIONS;
        ast = declarationsOnly ? null : new AST();
        listaDefiniciones();
        symbolTable.setCurrentClass(null);
        symbolTable.setCurrentMethod(null);
        start();
        symbolTable.setStart(symbolTable.getCurrentMethod());
        matchTokenType(TokenType.EOF);
        symbolTable.consolidate();
        if (!declarationsOnly) {
            AST.setSymbolTable(symbolTable);
            ast.consolidate();
            if (phase == Phase.CODE_GENERATION) {
                ast.generate();
            }
        }
    }

    /**
     * Reconoce el bloque de entrada y lo registra como método actual de tipo void.
     *
     * <pre>{@code
     * Start ::= start Bloque-Método
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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
        bloqueMetodo(true);
    }

    /**
     * Reconoce las partes class e impl hasta llegar al identificador de entrada.
     *
     * <pre>{@code
     * Lista-Definiciones ::= class Lista-Definiciones | Impl Lista-Definiciones | λ
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce una parte class sobre una entrada temporal y luego la integra en la TS.
     * Conserva el token del nombre, asigna el padre y marca la presencia de la declaración.
     *
     * <pre>{@code
     * class ::= class idclass classF
     * }</pre>
     * @throws SemanticException si se repiten atributos o hay un conflicto de herencia
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce los atributos y sintetiza el padre explícito, u Object si se omite.
     *
     * <pre>{@code
     * classF ::= { AtributoK } | Herencia { AtributoK }
     * }</pre>
     * @return tipo que identifica el padre de la clase
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce e incorpora los atributos hasta la llave de cierre de class.
     *
     * <pre>{@code
     * AtributoK ::= Atributo AtributoK | λ
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce una parte impl y la integra con los miembros previamente registrados.
     * La entrada temporal permite procesar la implementación antes de su class.
     *
     * <pre>{@code
     * Impl ::= impl idclass { MiembroK }
     * }</pre>
     * @throws SemanticException si se repite un constructor o método al integrar las partes
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce el padre explícito sin comprobar todavía su existencia en la TS.
     *
     * <pre>{@code
     * Herencia ::= : Tipo
     * }</pre>
     * @return tipo declarado como padre
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
    public Type herencia() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_CL);
        return tipo();
    }

    /**
     * Reconoce los miembros de una implementación hasta su llave de cierre.
     *
     * <pre>{@code
     * MiembroK ::= Miembro MiembroK | λ
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Selecciona el reconocimiento de un método o un constructor.
     *
     * <pre>{@code
     * Miembro ::= Método | Constructor
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Registra el constructor como Method separado del mapa de métodos.
     * Conserva el token del punto y establece como tipo el nombre de la clase actual.
     *
     * <pre>{@code
     * Constructor ::= . Argumentos-Formales Bloque-Método
     * }</pre>
     * @throws ConstructorRedefinitionException si la parte actual ya tiene constructor
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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
        constructor.setClassName(getSymbolTable().getCurrentClass().getName());
        symbolTable.getCurrentClass().setConstructor(constructor);
        symbolTable.setCurrentMethod(constructor);
        argumentosFormales();
        bloqueMetodo(false);
    }

    /**
     * Propaga el tipo y la privacidad a todos los atributos de la lista declarada.
     *
     * <pre>{@code
     * Atributo ::= Tipo Lista-Declaración-Variables ;
     *            | pub Tipo Lista-Declaración-Variables ;
     * }</pre>
     * @throws SemanticException si se repite un atributo en la clase actual
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce el modificador estático y registra el método al terminar su cuerpo.
     *
     * <pre>{@code
     * Método ::= fn MétodoF | st fn MétodoF
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Crea el método actual y reconoce su firma y cuerpo.
     * La alternativa sin tipo de retorno explícito utiliza void.
     *
     * <pre>{@code
     * MétodoF ::= idMetAt Argumentos-Formales Bloque-Método
     *           | Tipo-Método idMetAt Argumentos-Formales Bloque-Método
     * }</pre>
     * @param isStatic indica si la producción anterior reconoció el modificador st
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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
            bloqueMetodo(false);
        }else if (checkType(TokenType.KW_VOID, TokenType.IDENTIFIER_CLASS,
                TokenType.INT_CLASS, TokenType.BOOL_CLASS, TokenType.STR_CLASS, TokenType.ARRAY_CLASS)){
            type = tipoMetodo();
            Token auxToken = currentToken;
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            Method currentMethod = new Method(auxToken, type, isStatic);
            currentMethod.setType(type);
            symbolTable.setCurrentMethod(currentMethod);
            argumentosFormales();
            bloqueMetodo(false);
        }else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba 'Str', 'Bool', 'Int', 'Array', 'void', una clase, variable o un metodo , pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
    }

    /**
     * Registra las variables locales y procesa el cuerpo del método actual.
     * En modo de declaraciones delega las sentencias en
     * {@link SyntacticAnalyzer#parseStatements()}; en los otros modos crea un BlockNode.
     *
     * <pre>{@code
     * Bloque-Método ::= { Decl-Var-LocalesK SentenciaK }
     * }</pre>
     * @param isStart indica si el cuerpo corresponde al bloque de entrada
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
    public void bloqueMetodo(boolean isStart) throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        declVarLocalesK();
        if (declarationsOnly) {
            SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexer, currentToken);
            currentToken = syntacticAnalyzer.parseStatements();
        } else {
            LinkedList<SentenceNode> sentenceNodes = new LinkedList<>();
            sentenciaK(sentenceNodes);
            BlockNode blockNode = new BlockNode(sentenceNodes, symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null,
                    symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start", true);
            if (isStart){
                ast.setStartBlockNode(blockNode);
            } else {
                ast.getBlockNodes().add(blockNode);
            }
        }
        matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
    }

    /**
     * Reconoce las declaraciones locales que preceden a las sentencias del método.
     *
     * <pre>{@code
     * Decl-Var-LocalesK ::= Decl-Var-Locales Decl-Var-LocalesK | λ
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Propaga el tipo declarado a la lista de variables locales del método actual.
     *
     * <pre>{@code
     * Decl-Var-Locales ::= Tipo Lista-Declaración-Variables ;
     * }</pre>
     * @throws SemanticException si una variable repite un nombre local o de parámetro
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
    public void declVarLocales() throws LexicalException, SyntacticException {
        Type tipo = tipo();
        listaDeclaracionVariables(tipo , true, false);
        matchTokenType(TokenType.SPECIAL_SYMBOL_S);
    }

    /**
     * Registra cada identificador como atributo o variable local según el contexto.
     * Guarda el token antes de consumirlo para conservar la ubicación del diagnóstico.
     *
     * <pre>{@code
     * Lista-Declaración-Variables ::= idMetAt Lista-Declaración-VariablesF
     * }</pre>
     * @param type tipo compartido por los identificadores de la lista
     * @param isPrivate privacidad de los atributos; no se aplica a variables locales
     * @param isClass true para insertar en la clase, false para insertar en el método
     * @throws SemanticException si el nombre entra en conflicto con una declaración previa
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
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

    /**
     * Propaga el contexto de inserción a los siguientes identificadores de la lista.
     *
     * <pre>{@code
     * Lista-Declaración-VariablesF ::= , Lista-Declaración-Variables | λ
     * }</pre>
     * @param type tipo de las declaraciones
     * @param isPrivate privacidad que se propagará a los atributos
     * @param isClass indica si se están registrando atributos
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce los paréntesis y los parámetros de la firma del método actual.
     *
     * <pre>{@code
     * Argumentos-Formales ::= ( Argumentos-FormalesF
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
    public void argumentosFormales() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        argumentosFormalesF();
    }

    /**
     * Distingue la lista vacía de una secuencia de parámetros formales.
     *
     * <pre>{@code
     * Argumentos-FormalesF ::= ) | Lista-Argumentos-Formales )
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Reconoce los parámetros formales respetando su orden de declaración.
     *
     * <pre>{@code
     * Lista-Argumentos-Formales ::= Argumento-Formal Lista-Argumentos-FormalesF
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
    public void listaArgumentosFormales() throws LexicalException, SyntacticException {
        argumentoFormal();
        listaArgumentosFormalesF();
    }

    /**
     * Reconoce los parámetros restantes, separados por comas.
     *
     * <pre>{@code
     * Lista-Argumentos-FormalesF ::= , Lista-Argumentos-Formales | λ
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Registra el nombre y tipo del parámetro en la siguiente posición de la firma.
     * La entrada Parameter conserva el nombre como cadena y no retiene el token.
     *
     * <pre>{@code
     * Argumento-Formal ::= Tipo idMetAt
     * }</pre>
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
    public void argumentoFormal() throws LexicalException, SyntacticException {
        Type type = tipo();
        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        symbolTable.getCurrentMethod().insertParameter(type, auxToken.getLexeme());
    }

    /**
     * Sintetiza el tipo de retorno de un método, admitiendo void.
     *
     * <pre>{@code
     * Tipo-Método ::= Tipo | void
     * }</pre>
     * @return representación del tipo de retorno
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Selecciona la representación de un tipo primitivo, de referencia o arreglo.
     *
     * <pre>{@code
     * Tipo ::= Tipo-Primitivo | Tipo-Referencia | Tipo-Arreglo
     * }</pre>
     * @return tipo reconocido
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Sintetiza un tipo primitivo a partir de su token.
     *
     * <pre>{@code
     * Tipo-Primitivo ::= Str | Bool | Int
     * }</pre>
     * @return representación del tipo primitivo
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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

    /**
     * Registra el nombre de un tipo de referencia sin resolver su declaración.
     *
     * <pre>{@code
     * Tipo-Referencia ::= idclass
     * }</pre>
     * @return tipo con el nombre de la clase reconocida
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
     */
    public Type tipoReferencia() throws LexicalException, SyntacticException {
        Type type = new Type(currentToken.getTokenType(), currentToken.getLexeme());
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        return type;
    }

    /**
     * Construye un ArrayType que conserva el tipo primitivo de sus elementos.
     *
     * <pre>{@code
     * Tipo-Arreglo ::= Array Tipo-Primitivo
     * }</pre>
     * @return representación del arreglo y su tipo de elemento
     *
     * @throws LexicalException si el analizador léxico rechaza la entrada
     * @throws SyntacticException si no se reconoce la producción esperada
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
    public void sentenciaK(LinkedList<SentenceNode> sentenceNodes) throws LexicalException, SyntacticException {
        if (checkType(TokenType.SPECIAL_SYMBOL_S, TokenType.KW_WHILE, TokenType.KW_FOR,
                TokenType.KW_IF, TokenType.KW_RET, TokenType.SPECIAL_SYMBOL_PO, TokenType.SPECIAL_SYMBOL_LCB,
                TokenType.IDENTIFIER_VAR_METHOD, TokenType.KW_SELF)) {
            sentenceNodes.add(sentencia());
            sentenciaK(sentenceNodes);
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
            sentenceNode = sentenciaSimple();
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if (checkType(TokenType.KW_WHILE)) {
            Token auxToken = currentToken;
            matchTokenType(TokenType.KW_WHILE);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
            ExpressionNode expressionNode = expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
            sentenceNode = new WhileNode(auxToken, expressionNode, sentenciaConBloque());
        } else if (checkType(TokenType.KW_FOR)) {
            matchTokenType(TokenType.KW_FOR);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
            Block previousBlock = symbolTable.getCurrentBlock();
            Block block = new Block(symbolTable.getCurrentMethod());
            symbolTable.getCurrentMethod().getBlocks().add(block);
            block.setParent(previousBlock);
            symbolTable.setCurrentBlock(block);
            Type type = tipoPrimitivo();
            VariableNode variableNode1 = new VariableNode(currentToken,null,symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null, symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
            block.getVariables().put(currentToken.getLexeme() , new Variable(currentToken,type));
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            matchTokenType(TokenType.KW_IN);
            VariableNode variableNode2 = new VariableNode(currentToken,null,symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null, symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
            matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
            sentenceNode = new ForNode(type, variableNode1, variableNode2, sentencia());
            symbolTable.setCurrentBlock(previousBlock);
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LCB)) {
            sentenceNode = bloque();
        } else if (checkType(TokenType.KW_IF)) {
            Token auxToken = currentToken;
            matchTokenType(TokenType.KW_IF);
            matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
            ExpressionNode expressionNode = expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
            sentenceNode = new IfElseNode(auxToken, expressionNode, sentenciaConBloque(), sentenciaF1());
        } else if (checkType(TokenType.KW_RET)) {
            Token auxToken = currentToken;
            matchTokenType(TokenType.KW_RET);
            sentenceNode = sentenciaF2(auxToken);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ';' , 'while', 'for', 'if', 'ret' , '{', pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return sentenceNode;
    }
    
    /**
     * Reconoce el cuerpo de una rama o ciclo dentro de un ámbito propio,
     * incluso cuando es una sentencia sin llaves o una sentencia vacía.
     */
    private SentenceNode sentenciaConBloque() throws LexicalException, SyntacticException {
        Block previousBlock = symbolTable.getCurrentBlock();
        Block block = new Block(symbolTable.getCurrentMethod());
        block.setParent(previousBlock);
        symbolTable.getCurrentMethod().getBlocks().add(block);
        symbolTable.setCurrentBlock(block);
        try {
            SentenceNode sentence = sentencia();
            // Una rama vacía sigue teniendo ámbito y debe distinguirse de un else ausente.
            return sentence != null ? sentence : new BlockNode(new LinkedList<>(),
                    symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null,
                    symbolTable.getCurrentMethod().getName(), false);
        } finally {
            symbolTable.setCurrentBlock(previousBlock);
        }
    }

    /*
	⟨SentenciaF1⟩ ::= else ⟨Sentencia⟩
		| λ
    */
    public SentenceNode sentenciaF1() throws LexicalException, SyntacticException {
        SentenceNode sentenceNode = null;
        if (checkType(TokenType.KW_ELSE)) {
            matchTokenType(TokenType.KW_ELSE);
            sentenceNode = sentenciaConBloque();
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
        return sentenceNode;
    }

    /*
	⟨SentenciaF2⟩ ::= ;
		| ⟨Expresión⟩;
    */
    public ReturnNode sentenciaF2(Token token) throws LexicalException, SyntacticException {
        ReturnNode returnNode = new ReturnNode(token,null);
        if(checkType(TokenType.SPECIAL_SYMBOL_S)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else if (checkType(TokenType.OPERATOR_ADD, TokenType.OPERATOR_SUB, TokenType.OPERATOR_NOT,
            TokenType.OPERATOR_INC, TokenType.OPERATOR_DEC, TokenType.KW_NIL, TokenType.BOOLEAN_CONSTANT,
                    TokenType.INT_CONSTANT, TokenType.STRING_CONSTANT, TokenType.SPECIAL_SYMBOL_PO,
                    TokenType.KW_SELF, TokenType.IDENTIFIER_CLASS, TokenType.KW_NEW, TokenType.IDENTIFIER_VAR_METHOD)) {
            returnNode.setExpression(expresion());
            matchTokenType(TokenType.SPECIAL_SYMBOL_S);
        } else {
            throw new SyntacticException(currentToken.getRowIndex(),
                    currentToken.getColumnIndex(),
                    "Se esperaba ';' '+', '-', '!', '++', '--', 'nil', 'true', 'false', entero, string, '(', 'self', identificador de clase, 'new' " +
                            "o identificador de variable o metodo, pero se encontro '" +
                            currentToken.getLexeme() + "'"
            );
        }
        return returnNode;
    }
    /*
        ⟨Bloque⟩ ::= { ⟨SentenciaK⟩ }
    */
    public BlockNode bloque() throws LexicalException, SyntacticException {
        LinkedList<SentenceNode> sentenceNodes = new LinkedList<>();
        matchTokenType(TokenType.SPECIAL_SYMBOL_LCB);
        sentenciaK(sentenceNodes);
        matchTokenType(TokenType.SPECIAL_SYMBOL_RCB);
        return new BlockNode(sentenceNodes, symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null,
                symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start", false);
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
            Method currentMethod = this.symbolTable.getCurrentMethod();
            Variable currentVariable = currentMethod.getVariables().get(token.getLexeme());
            Parameter currentParameter = currentMethod.getParameters().get(token.getLexeme());
            if (currentVariable != null) {
                simpleAccessNode = new SimpleVariableNode(token, currentVariable.getType(), null);
            } else {
                simpleAccessNode = new SimpleVariableNode(token, currentParameter.getType(), null);
            }

            SimpleVariableNode simpleVariableNode = (SimpleVariableNode) simpleAccessNode;
            simpleVariableNode.setSimpleChainedNode(encadenadoSimpleK());
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            simpleAccessNode = new SimpleArrayNode(token, expresion());
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
        } else if (checkType(TokenType.OPERATOR_ASSIGN)) {
            // Primeros(⟨Encadenado-SimpleK⟩) contiene λ, por lo tanto checkeo siguientes
            // Siguientes de accesoVarSimpleF, cierro
            /*
            Type type;
            Variable variable = this.getSymbolTable().getCurrentMethod().getVariables().get(token.getLexeme());

            if (variable == null){
                Attribute attribute = this.getSymbolTable().getClasses().get(this.getSymbolTable().getCurrentClass().getName()).getAttributes().get(token.getLexeme());
                if (attribute == null) {
                    Parameter parameter = this.getSymbolTable().getCurrentMethod().getParameters().get(token.getLexeme());
                    if (parameter == null){
                        throw new LocalVariableNotFoundException(token.getRowIndex(), token.getColumnIndex(), token.getLexeme(), symbolTable.getCurrentMethod().getName());
                    }else {
                        type = parameter.getType();
                    }
                } else {
                    type = attribute.getType();
                }

            } else {
                type = variable.getType();
            }
            */
            simpleAccessNode = new SimpleVariableNode(token, null, null);
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
        return new SimpleSelfNode(auxToken, encadenadoSimpleK());
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
                symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null,
                symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
    }

    /*
    ⟨Sentencia-Simple⟩ ::= (⟨Expresión⟩)
    */
    public SimpleSentenceNode sentenciaSimple() throws LexicalException, SyntacticException {
        matchTokenType(TokenType.SPECIAL_SYMBOL_PO);
        SimpleSentenceNode simpleSentenceNode = new SimpleSentenceNode(expresion());
        matchTokenType(TokenType.SPECIAL_SYMBOL_PC);
        return simpleSentenceNode;
    }

    /*
    ⟨Expresión⟩ ::= ⟨ExpOr⟩
    */
    public ExpressionNode expresion() throws LexicalException, SyntacticException {
        ExpressionNode expressionNode = expOr();
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


            chainedNode = new VariableNode(token, null, null, symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null,
                    symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
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
        Token auxToken = currentToken;
        matchTokenType(TokenType.KW_SELF);
        return new SelfNode(auxToken, accesoSelfF());
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
            chainedNode = new VariableNode(token, null, symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null, symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
            chainedNode.setChainedNode(encadenado());
        } else if (checkType(TokenType.SPECIAL_SYMBOL_LB)) {
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            ExpressionNode expressionNode = expresion();
            chainedNode = new ArrayNode(token, null, expressionNode, symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null, symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
            chainedNode.setChainedNode(accesoVarF());
        } else if (checkType(TokenType.OPERATOR_MUL, TokenType.OPERATOR_DIV, TokenType.OPERATOR_ADD,
                TokenType.OPERATOR_SUB, TokenType.OPERATOR_LT, TokenType.OPERATOR_GT,
                TokenType.OPERATOR_LTE, TokenType.OPERATOR_GTE, TokenType.OPERATOR_EQ,
                TokenType.OPERATOR_NEQ, TokenType.OPERATOR_AND, TokenType.OPERATOR_OR,
                TokenType.SPECIAL_SYMBOL_PC, TokenType.SPECIAL_SYMBOL_S, TokenType.SPECIAL_SYMBOL_RB,
                TokenType.SPECIAL_SYMBOL_CM)) {
            chainedNode = new VariableNode(token, null, symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null, symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
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
        Token idClass = currentToken;
        matchTokenType(TokenType.IDENTIFIER_CLASS);
        matchTokenType(TokenType.SPECIAL_SYMBOL_P);
        Token auxToken = currentToken;
        matchTokenType(TokenType.IDENTIFIER_VAR_METHOD);
        return new ChainedCallNode(idClass,llamadaMetodo(auxToken),null);
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
        Token auxToken = currentToken;
        if (checkType(TokenType.IDENTIFIER_CLASS)){
            matchTokenType(TokenType.IDENTIFIER_CLASS);
            Type type = new Type(auxToken.getTokenType(), auxToken.getLexeme());
            LinkedList<ExpressionNode> expressionNodes = argumentosActuales();
            ChainedNode chainedNode = llamadaConclassorF2();
            conclassorCallNode = new ClassConstructorNode(auxToken, type, expressionNodes, chainedNode);
        } else if (checkType(TokenType.INT_CLASS, TokenType.BOOL_CLASS, TokenType.STR_CLASS)) {
            Type type = new ArrayType(tipoPrimitivo());
            matchTokenType(TokenType.SPECIAL_SYMBOL_LB);
            ExpressionNode expressionNode = expresion();
            matchTokenType(TokenType.SPECIAL_SYMBOL_RB);
            ChainedNode chainedNode = llamadaConclassorF2();
            conclassorCallNode = new ArrayConstructorNode(auxToken, type, chainedNode, expressionNode);
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
            chainedNode = new VariableNode(token, null, symbolTable.getCurrentClass() != null ? symbolTable.getCurrentClass().getName() : null, symbolTable.getCurrentMethod() != null ? symbolTable.getCurrentMethod().getName() : "start");
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
