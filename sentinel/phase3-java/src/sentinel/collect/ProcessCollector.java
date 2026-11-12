package sentinel.collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Java is the boss: it launches the collector as a child process and reads
 * its stdout, line by line. Bash today, the C binary tomorrow: Sentinel does
 * not change, because both respect the contract.
 *
 * Needs Linux or WSL, and a collector with execute permission.
 */
public class ProcessCollector implements Collector {

    private final String command;
    private final Process process;
    private final BufferedReader output;

    public ProcessCollector(String command) {
        if (command == null || command.isBlank()) {
            throw new IllegalArgumentException("collector command is required");
        }
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);   // the collector's errors go to our stderr
        try {
            this.process = builder.start();
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot start " + command + " (does it exist and is it executable?)");
        }
        this.command = command;
        this.output = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public String nextLine() {
        try {
            return output.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("cannot read from " + command + ": " + e.getMessage());
        }
    }

    @Override
    public String describe() {
        return "process " + command;
    }

    /** The collector is a "while true" loop: if we do not stop it, nobody will. */
    @Override
    public void close() {
        process.destroy();
    }
}
