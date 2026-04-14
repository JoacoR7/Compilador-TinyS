package uncuyo.compiladores;

import org.junit.jupiter.api.util.PreemptiveTimeoutUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private File folder;

    public FileUtils(String path) {
        this.folder = new File(path);
    }

    public List<String> traverseFiles() {
        List<String> fileNames = new ArrayList<>();
        String path = folder.getPath();
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                fileNames.add(path + "\\" + file.getName());
            }
        }
        return fileNames;
    }



}
