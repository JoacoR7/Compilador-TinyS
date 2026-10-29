package uncuyo.compiladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

final class TestCaseRunner {
    private TestCaseRunner() {
    }

    static void run(String suite, String category, Path directory, TestCase testCase) throws IOException {
        List<Path> testFiles;
        try (Stream<Path> files = Files.list(directory)) {
            testFiles = files.filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".s"))
                    .sorted()
                    .toList();
        }
        assertFalse(testFiles.isEmpty(), "No se encontraron casos en " + directory);

        Map<Path, String> testsFailed = new LinkedHashMap<>();
        for (Path testFile : testFiles) {
            try {
                assertDoesNotThrow(() -> testCase.run(testFile), testFile.toString());
            } catch (AssertionError | Exception error) {
                StringWriter trace = new StringWriter();
                error.printStackTrace(new PrintWriter(trace));
                testsFailed.put(testFile, trace.toString());
            }
        }

        System.out.println("[" + suite + " " + category.toUpperCase(Locale.ROOT) + " TESTS]");
        System.out.println("Total tests: " + testFiles.size());
        System.out.println("Total tests passed: " + (testFiles.size() - testsFailed.size()));
        if (!testsFailed.isEmpty()) {
            StringBuilder report = new StringBuilder();
            report.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            report.append("TOTAL TEST FAILED: ").append(testsFailed.size()).append("\n");
            report.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");
            for (Map.Entry<Path, String> failedTest : testsFailed.entrySet()) {
                report.append("------------------------------------------------------------------\n");
                report.append("TEST FILE: ").append(failedTest.getKey()).append("\n");
                report.append(failedTest.getValue()).append("\n");
                report.append("------------------------------------------------------------------\n\n");
            }
            fail(report.toString());
        }
    }

    @FunctionalInterface
    interface TestCase {
        void run(Path source) throws Exception;
    }
}
