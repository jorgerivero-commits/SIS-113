package sentinel.app;

import java.util.ArrayList;
import java.util.List;
import sentinel.collect.Collector;
import sentinel.model.Contract;
import sentinel.model.History;
import sentinel.model.Snapshot;
import sentinel.render.Renderer;
import sentinel.rules.Rule;

/**
 * The engine: read a line, turn it into a Snapshot, ask every rule, show.
 *
 * POLYMORPHISM: this class only knows Collector, Rule and Renderer. It works
 * the same with a pipe or a file, with one rule or ten, with colors or CSV.
 * Read the imports: not one concrete rule, collector or renderer.
 */
public class Sentinel {

    private final Collector collector;
    private final List<Rule> rules;
    private final Renderer renderer;
    private final int maxLines;
    private final History history = new History();
    private int read = 0;
    private int invalid = 0;

    /** maxLines = 0 means no limit. */
    public Sentinel(Collector collector, List<Rule> rules, Renderer renderer, int maxLines) {
        if (collector == null || renderer == null) {
            throw new IllegalArgumentException("collector and renderer are required");
        }
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("at least one rule is required");
        }
        if (maxLines < 0) {
            throw new IllegalArgumentException("max lines cannot be negative, got " + maxLines);
        }
        this.collector = collector;
        this.rules = new ArrayList<>(rules);
        this.renderer = renderer;
        this.maxLines = maxLines;
    }

    public void run() {
        renderer.start(collector.describe(), rules);
        try {
            while (maxLines == 0 || read < maxLines) {
                String line = collector.nextLine();
                if (line == null) {
                    break;
                }
                process(line);
            }
        } finally {
            collector.close();   // runs even if reading failed: a launched process must not be left running
        }
        renderer.summary(history, rules, read, invalid);
    }

    private void process(String line) {
        read++;
        Snapshot reading;
        try {
            reading = Contract.parse(line);
        } catch (IllegalArgumentException e) {
            invalid++;
            renderer.invalid(line, e.getMessage());
            return;
        }
        history.add(reading);

        // Every rule is asked, even after one alerted: each keeps its own count.
        List<Rule> fired = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.evaluate(reading)) {
                fired.add(rule);
            }
        }
        renderer.show(reading, fired);
    }

    public int getRead() {
        return read;
    }

    public int getInvalid() {
        return invalid;
    }

    public int getValid() {
        return history.getCount();
    }
}
