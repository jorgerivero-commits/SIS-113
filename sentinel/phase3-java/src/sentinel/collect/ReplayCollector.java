package sentinel.collect;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Replays a saved sample file. No Linux, no live system: the same data every
 * time. This is how professionals test a program that normally reads live
 * data, and why the samples were saved back in Clase 4.
 */
public class ReplayCollector implements Collector {

    private final Path file;
    private final List<String> lines;
    private int position = 0;

    public ReplayCollector(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("sample file is required");
        }
        try {
            this.lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("cannot read sample file " + file);
        }
        this.file = file;
    }

    @Override
    public String nextLine() {
        if (position == lines.size()) {
            return null;
        }
        String line = lines.get(position);
        position++;
        return line;
    }

    @Override
    public String describe() {
        return "replay " + file.getFileName();
    }

    /** The whole file was read in the constructor: nothing left open. */
    @Override
    public void close() {
    }
}
