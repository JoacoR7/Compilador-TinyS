package uncuyo.compiladores;

import java.io.IOException;

/**
 * Ejecutador de la etapa de chequeo de declaraciones de TinyS.
 * Analiza un archivo fuente y exporta su tabla de símbolos como
 * {@code <nombre>.ts.json} si el análisis termina correctamente.
 */
public class Etapa3 {
    /**
     * Ejecuta exclusivamente el reconocimiento y chequeo de declaraciones.
     * El ejecutador compartido informa el uso si no recibe exactamente una ruta.
     *
     * @param args ruta del archivo fuente TinyS
     * @throws IOException si falla la apertura, cierre o escritura de archivos
     */
    public static void main(String[] args) throws IOException {
        SemanticStageRunner.run(args, SemanticStageRunner.Stage.DECLARATIONS);
    }
}
