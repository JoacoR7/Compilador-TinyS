package uncuyo.compiladores.semantic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/** Codifica estructuras JSON con la biblioteca estándar, conservando su orden. */
public final class JsonWriter {
    private JsonWriter() {
    }

    public static Map<String, Object> object(Object... fields) {
        Map<String, Object> object = new LinkedHashMap<>();
        for (int i = 0; i < fields.length; i += 2) {
            object.put((String) fields[i], fields[i + 1]);
        }
        return object;
    }

    public static String write(Object value, String indent) {
        return indent + encode(value, indent);
    }

    private static String encode(Object value, String indent) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String text) {
            return quote(text);
        }
        if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        }
        String childIndent = indent + "\t";
        StringBuilder output = new StringBuilder();
        if (value instanceof Map<?, ?> object) {
            output.append("{");
            Iterator<? extends Map.Entry<?, ?>> entries = object.entrySet().iterator();
            while (entries.hasNext()) {
                var entry = entries.next();
                output.append("\n").append(childIndent).append(quote((String) entry.getKey()))
                        .append(": ").append(encode(entry.getValue(), childIndent));
                if (entries.hasNext()) {
                    output.append(",");
                }
            }
            if (!object.isEmpty()) {
                output.append("\n").append(indent);
            }
            return output.append("}").toString();
        }
        if (value instanceof Iterable<?> array) {
            output.append("[");
            Iterator<?> elements = array.iterator();
            boolean nonEmpty = elements.hasNext();
            while (elements.hasNext()) {
                output.append("\n").append(childIndent).append(encode(elements.next(), childIndent));
                if (elements.hasNext()) {
                    output.append(",");
                }
            }
            if (nonEmpty) {
                output.append("\n").append(indent);
            }
            return output.append("]").toString();
        }
        throw new IllegalArgumentException("Tipo no soportado por JSON: " + value.getClass());
    }

    private static String quote(String value) {
        StringBuilder output = new StringBuilder("\"");
        for (char character : value.toCharArray()) {
            switch (character) {
                case '"' -> output.append("\\\"");
                case '\\' -> output.append("\\\\");
                case '\b' -> output.append("\\b");
                case '\f' -> output.append("\\f");
                case '\n' -> output.append("\\n");
                case '\r' -> output.append("\\r");
                case '\t' -> output.append("\\t");
                default -> {
                    if (character < 32) {
                        output.append(String.format("\\u%04x", (int) character));
                    } else {
                        output.append(character);
                    }
                }
            }
        }
        return output.append('"').toString();
    }
}
