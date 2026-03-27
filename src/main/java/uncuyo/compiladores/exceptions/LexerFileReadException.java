package uncuyo.compiladores.exceptions;

public class LexerFileReadException extends RuntimeException {

    private final String filePath;
    public LexerFileReadException(String filePath, String message, Throwable cause) {
        super(message, cause);
        this.filePath = filePath;
    }

    @Override
    public String getMessage() {
        return String.format("ERROR DE LECTURA EN ARCHIVO '%s': %s", filePath, super.getMessage());
    }
}
