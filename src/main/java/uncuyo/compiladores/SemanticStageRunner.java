package uncuyo.compiladores;

import uncuyo.compiladores.exceptions.lexical.LexicalException;
import uncuyo.compiladores.exceptions.semantic.SemanticException;
import uncuyo.compiladores.exceptions.syntactic.SyntacticException;
import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Comparte la entrada, separación de fases y exportación de las etapas semánticas.
 * Ejecuta primero el chequeo de declaraciones; las fases posteriores abren
 * un nuevo lector para construir el AST una vez validada la TS.
 */
final class SemanticStageRunner {
    enum Stage {
        DECLARATIONS("etapa3.jar"), SENTENCES("etapa4.jar"), COMPILER("tinyS.jar");

        private final String jarName;

        Stage(String jarName) {
            this.jarName = jarName;
        }
    }

    private SemanticStageRunner() {
    }

    /**
     * Ejecuta la etapa solicitada y escribe los resultados al completar el análisis.
     *
     * <p>En modo DECLARATIONS sólo genera la TS. Los errores léxicos, sintácticos
     * y semánticos se presentan con el encabezado de su fase y detienen el flujo
     * antes de escribir nuevas salidas. Los archivos de ejecuciones anteriores
     * no se eliminan. La escritura de varios resultados no es transaccional.</p>
     *
     * @param args argumentos de línea de comandos; se requiere una única ruta
     * @param stage última etapa que se ejecutará
     * @throws IOException si falla una operación de entrada o salida
     */
    static void run(String[] args, Stage stage) throws IOException {
        if (args.length != 1) {
            System.out.println("USO: java -jar " + stage.jarName + " <ARCHIVO_FUENTE>.s");
            return;
        }

        String phase = "SEMANTICO - DECLARACIONES";
        try {
            SemanticAnalyzer analyzer;
            Lexer lexer = new Lexer(args[0]);
            try {
                analyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
                analyzer.analyzeDeclarations();
            } finally {
                lexer.close();
            }

            if (stage != Stage.DECLARATIONS) {
                phase = "SEMANTICO - SENTENCIAS";
                lexer = new Lexer(args[0]);
                try {
                    analyzer = new SemanticAnalyzer(lexer, lexer.getNextToken());
                    analyzer.analyzeSentences();
                } finally {
                    lexer.close();
                }
            }

            // Se completan el análisis y las representaciones antes de escribir salidas.
            String tableJson = analyzer.getSymbolTable().toJson("");
            String astJson = stage != Stage.DECLARATIONS ? analyzer.getAst().toJson("") : null;
            String assembly = null;
            if (stage == Stage.COMPILER) {
                phase = "GENERACION DE CODIGO";
                analyzer.getAst().generate();
                assembly = analyzer.getAst().getCodeGenerator().generate();
            }

            String base = args[0].endsWith(".s") ? args[0].substring(0, args[0].length() - 2) : args[0];
            Files.writeString(Path.of(base + ".ts.json"), tableJson);
            if (astJson != null) {
                Files.writeString(Path.of(base + ".ast.json"), astJson);
            }
            if (assembly != null) {
                Files.writeString(Path.of(base + ".asm"), assembly);
            }
            System.out.println("CORRECTO: " + phase);
        } catch (LexicalException error) {
            reportError("LEXICO", error);
        } catch (SyntacticException error) {
            reportError("SINTACTICO", error);
        } catch (SemanticException error) {
            reportError(phase, error);
        }
    }

    /**
     * Presenta el mensaje con el encabezado de fase, omitiendo el encabezado de la excepción.
     *
     * @param phase nombre de la fase que detectó el error
     * @param error excepción cuyo mensaje se presentará
     */
    private static void reportError(String phase, Exception error) {
        String message = error.getMessage();
        int headerEnd = message.indexOf('\n');
        System.out.println("ERROR: " + phase);
        System.out.println(headerEnd >= 0 ? message.substring(headerEnd + 1) : message);
    }
}
