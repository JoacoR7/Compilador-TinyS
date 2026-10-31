package uncuyo.compiladores;

import java.io.IOException;

/** Compilador completo: tabla de símbolos, AST y código MIPS. */
public class TinyS {
    public static void main(String[] args) throws IOException {
        SemanticStageRunner.run(args, SemanticStageRunner.Stage.COMPILER);
    }
}
