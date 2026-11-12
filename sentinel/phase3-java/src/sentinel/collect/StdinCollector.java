package sentinel.collect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Reads the lines from a pipe: ./collector.sh | java ... sentinel.app.Main */
public class StdinCollector implements Collector {

    private final BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

    @Override
    public String nextLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("cannot read stdin: " + e.getMessage());
        }
    }

    @Override
    public String describe() {
        return "stdin";
    }

    /** Nothing to release: stdin belongs to whoever started the program. */
    @Override
    public void close() {
    }
}
