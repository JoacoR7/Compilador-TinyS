package uncuyo.compiladores.codeGen;

import uncuyo.compiladores.semantic.symbolTable.Method;
import uncuyo.compiladores.semantic.symbolTable.Type;
import uncuyo.compiladores.semantic.symbolTable.Variable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class CodeGenerator {

    private StringBuilder data;
    private StringBuilder text;
    private StringBuilder main;
    private StringBuilder current;

    private String baseData;
    private String baseText;

    private Map<String, String> stringLabels = new LinkedHashMap<>();
    private int strCount = 0;

    public CodeGenerator() {
        this.data = new StringBuilder();
        this.text = new StringBuilder();
        this.main = new StringBuilder();

        // Por defecto, el código se genera fuera de main
        this.current = this.text;

        this.baseData = loadResource("codeGen/data.asm");
        this.baseText = loadResource("codeGen/text.asm");
    }

    /**
     * Carga un archivo desde resources.
     */
    private String loadResource(String path) {
        try {
            var stream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(path);

            if (stream == null) {
                throw new RuntimeException(
                        "No se encontró el recurso: " + path
                );
            }

            return new String(stream.readAllBytes());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al cargar " + path,
                    e
            );
        }
    }

    /**
     * Agrega una indentación adicional a todo un bloque.
     *
     * Por ejemplo:
     *
     * vtable_str:
     *     .word length_str
     *
     * pasa a:
     *
     *     vtable_str:
     *         .word length_str
     */
    private String indentBlock(String block, int spaces) {
        String indent = " ".repeat(spaces);
        StringBuilder result = new StringBuilder();

        for (String line : block.split("\\R", -1)) {

            if (line.isBlank()) {
                result.append("\n");
            } else {
                result.append(indent)
                        .append(line)
                        .append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Emite una directiva/dato dentro de .data.
     *
     * Tiene 8 espacios porque:
     *
     * .data
     *     label:
     *         contenido
     */
    public void emitData(String line) {
        data.append("        ")
                .append(line)
                .append("\n");
    }

    /**
     * Emite una etiqueta dentro de .data.
     */
    public void emitDataLabel(String label) {
        data.append("    ")
                .append(label)
                .append(":\n");
    }

    /**
     * Emite una instrucción dentro del bloque de código actual.
     *
     * current puede ser:
     * - main
     * - text
     */
    public void emitText(String line) {
        current.append("        ")
                .append(line)
                .append("\n");
    }

    /**
     * Emite una etiqueta de código.
     *
     * Ejemplo:
     *
     *     Persona_getEdad:
     */
    public void emitLabel(String label) {
        current.append("    ")
                .append(label)
                .append(":\n");
    }

    /**
     * Decide si las próximas instrucciones se generan
     * dentro de main o dentro del text general.
     */
    public void setInMain(boolean inMain) {
        this.current = inMain ? main : text;
    }

    /**
     * Genera/agrega un literal String en .data.
     *
     * Si el String ya fue generado anteriormente,
     * reutiliza su label.
     */
    public String emitString(String lexeme) {

        // El string vacío ya existe en data.asm
        if (lexeme.equals("\"\"")) {
            return "empty_str";
        }

        String label = stringLabels.get(lexeme);

        if (label == null) {

            label = "str_" + strCount;
            strCount++;

            stringLabels.put(lexeme, label);

            emitDataLabel(label);
            emitData(".asciiz " + lexeme);
        }

        return label;
    }

    /**
     * Construye el archivo ASM final.
     */
    public String generate() {

        return ".data\n"
                + indentBlock(baseData, 4)
                + data
                + "\n"

                + ".text\n"

                + "    .globl main\n"
                + "\n"

                + "    main:\n"
                + main
                + "\n"

                + indentBlock(baseText, 4)
                + text;


    }

    /**
     * Escribe el ASM generado a un archivo.
     */
    public void writeOutput(String path) throws IOException {
        Files.writeString(
                Path.of(path),
                generate()
        );
    }

    public void generateMethodPrologue(Method method) {

        int variableSpace = method.getVariables().size() * 4;

        emitText("# Prologue de " + method.getName());

        emitText("move $fp, $sp");
        emitText("sw $ra, 0($fp)");

        int frameSpace = 4 + variableSpace;

        emitText("addiu $sp, $sp, -" + frameSpace);

        initializeLocalVariables(method);

        emitText("# Fin Prologue de " + method.getName());
    }

    /*
        Según el tipo:
            Int  -> referencia a CIR Int(0)
            Bool -> referencia a CIR Bool(false)
            Str  -> referencia a CIR Str("")
            Clase -> nil
            Array -> nil
     */
    public void initializeLocalVariables(Method method) {
        emitText("# Inicializacion variables locales de " + method.getName());
        for (Variable variable : method.getVariables().values()) {
            emitText("# Inicializacion de " + variable.getName());
            switch (variable.getType().getTypeName()) {
                case "Int":
                case "Bool":
                    emitText("li $a0, 8");
                    emitText("li $v0, 9");
                    emitText("syscall");
                    emitText("sw $zero, 0($v0)");
                    emitText("sw $zero, 4($v0)");
                    break;
                case "Str":
                    emitText("li $a0, 8");
                    emitText("li $v0, 9");
                    emitText("syscall");
                    emitText("la $t0, empty_str");
                    emitText("sw $t0, 4($v0)");
                    emitText("la $t0, vtable_str");
                    emitText("sw $t0, 0($v0)");
                    break;
            }

            emitText("addiu $a0, $fp, " + getVariableOffset(variable));
            emitText("sw $v0, 0($a0)");
        }
    }

    public void generateMethodEpilogue(Method method) {
        if (method.getName().equals("start")) {
            emitText("li $v0 10");
            emitText("syscall");
        } else {
            int parameterSpace = method.getParameters().size() * 4;
            int variableSpace = method.getVariables().size() * 4;

            int frameSpace = 16 + parameterSpace + variableSpace;

            emitText("# Epilogue de " + method.getName());
            emitText("addiu $sp, $sp, " + frameSpace);
            emitText("jr $ra");
        }

    }

    public int getVariableOffset(Variable variable) {
        return -((variable.getPosition() + 1) * 4);
    }

    public void push(String register) {
        emitText("sw " + register + " 0($sp)");
        emitText("addiu $sp $sp -4");
    }

    public void pop() {
        emitText("lw $t0 4($sp)");
        emitText("addiu $sp $sp 4");
    }


}