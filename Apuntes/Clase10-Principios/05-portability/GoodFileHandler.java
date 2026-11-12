// Portability — GOOD
//
// Works on Windows, WSL and macOS without changing a line:
//   - the folder comes from configuration (an environment variable), with a
//     safe default: the system's temporary folder, which exists everywhere
//   - Path builds paths with the right separator for each system
//   - Java lists the files itself: no shell command
//   - Files.write ends each line with the system's own line separator
//
//   java GoodFileHandler.java
//   SIS113_REPORTS=$HOME/reportes java GoodFileHandler.java     (choose the folder)

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GoodFileHandler {

    public static void main(String[] args) {
        System.out.println("running on: " + System.getProperty("os.name"));
        Path folder = reportsFolder();
        System.out.println("reports folder: " + folder);

        List<String> lines = new ArrayList<>();
        lines.add("line one");
        lines.add("line two");
        save(folder, "report.txt", lines);
        list(folder);
    }

    /** Configuration first, a portable default second. Never a hard-coded path. */
    static Path reportsFolder() {
        String configured = System.getenv("SIS113_REPORTS");
        if (configured != null && !configured.isBlank()) {
            return Path.of(configured);
        }
        return Path.of(System.getProperty("java.io.tmpdir"), "sis113-reports");
    }

    static void save(Path folder, String name, List<String> lines) {
        try {
            Files.createDirectories(folder);
            Path file = folder.resolve(name);
            Files.write(file, lines);
            System.out.println("saved " + file);
        } catch (IOException e) {
            System.out.println("could not save in " + folder + ": " + e.getMessage());
        }
    }

    static void list(Path folder) {
        File[] files = folder.toFile().listFiles();
        if (files == null) {
            System.out.println("cannot list " + folder);
            return;
        }
        for (File file : files) {
            System.out.println("  " + file.getName() + " (" + file.length() + " bytes)");
        }
    }
}
