package uncuyo.compiladores;

import java.io.IOException;

/** Ejecutador del chequeo semántico de sentencias y la exportación del AST. */
public class Etapa4 {
    public static void main(String[] args) throws IOException {
        SemanticStageRunner.run(args, SemanticStageRunner.Stage.SENTENCES);
    }
}
