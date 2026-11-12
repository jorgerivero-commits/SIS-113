// Portability — BAD
//
// Works on exactly ONE machine: the author's Windows laptop, user "admin".
//   - a hard-coded Windows path
//   - a Windows-only command (cmd /c dir) to list files
//   - a hard-coded Windows line ending (\r\n)
// Run it in WSL, on macOS, or on a classmate's Windows: it fails.
//
//   java BadFileHandler.java

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class BadFileHandler {

    private static final String REPORTS_DIR = "C:/Users/admin/reports";

    public static void main(String[] args) {
        System.out.println("running on: " + System.getProperty("os.name"));
        save("report.txt", "line one" + "\r\n" + "line two" + "\r\n");
        listReports();
    }

    static void save(String name, String content) {
        try {
            FileWriter writer = new FileWriter(REPORTS_DIR + "/" + name);
            writer.write(content);
            writer.close();
            System.out.println("saved " + REPORTS_DIR + "/" + name);
        } catch (IOException e) {
            System.out.println("could not save: " + e.getMessage());
        }
    }

    static void listReports() {
        try {
            Process process = new ProcessBuilder("cmd", "/c", "dir", REPORTS_DIR).start();
            BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = output.readLine();
            while (line != null) {
                System.out.println(line);
                line = output.readLine();
            }
        } catch (IOException e) {
            System.out.println("could not list: " + e.getMessage());
        }
    }
}
