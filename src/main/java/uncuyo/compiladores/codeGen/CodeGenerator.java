package uncuyo.compiladores.codeGen;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class CodeGenerator {
    private StringBuilder data;
    private StringBuilder text;
    private String baseData;
    private String baseText;
    private Map<String,String> stringLabels = new LinkedHashMap<>();
    private int strCount = 0;

    public CodeGenerator() {
        this.data = new StringBuilder();
        this.text = new StringBuilder();
        this.baseData = loadResource("codeGen/data.asm");
        this.baseText = loadResource("codeGen/text.asm");
    }

    private String loadResource(String path) {
        try {
            var stream = getClass().getClassLoader().getResourceAsStream(path);
            return new String(stream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error loading resource " + path, e);
        }
    }

    public void emitData(String line) {
        data.append("    ").append(line).append("\n");
    }

    public void emitText(String line) {
        text.append("    ").append(line).append("\n");
    }

    public String generate() {
        return baseData + "\n"
                + data + "\n"
                + baseText + "\n"
                + text;
    }

    public void writeOutput(String path) throws IOException {
        Files.writeString(Path.of(path), generate());
    }

    public String emitString(String lexeme){
        if (lexeme.equals("\"\"")){
            return "empty_str";
        }

        String label = stringLabels.get(lexeme);
        if (label == null){
            label = "str_" + strCount;
            strCount++;
            stringLabels.put(lexeme, label);
            emitData(label + ": .asciiz " + lexeme);
        }
        return label;
    }

}
