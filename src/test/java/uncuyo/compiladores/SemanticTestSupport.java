package uncuyo.compiladores;

import java.nio.file.Path;

import uncuyo.compiladores.lexer.Lexer;
import uncuyo.compiladores.semantic.SemanticAnalyzer;

final class SemanticTestSupport {
    private SemanticTestSupport() {
    }

    static void analyze(Path source, Analysis analysis) throws Exception {
        Lexer lexer = new Lexer(source.toString());
        try {
            analysis.run(new SemanticAnalyzer(lexer, lexer.getNextToken()));
        } finally {
            lexer.close();
        }
    }

    @FunctionalInterface
    interface Analysis {
        void run(SemanticAnalyzer analyzer) throws Exception;
    }
}
