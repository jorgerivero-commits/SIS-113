package sentinel.render;

import java.util.List;
import sentinel.model.History;
import sentinel.model.Snapshot;
import sentinel.rules.Rule;

/**
 * How Sentinel's results are shown.
 *
 *              Renderer (interface)
 *            ┌─────────┴─────────┐
 *     ConsoleRenderer        CsvRenderer
 *            │
 *      PlainRenderer   (inherits everything, changes only the colors)
 *
 * Sentinel calls these four methods and never knows whether the output is a
 * colored terminal, a plain log file or a CSV for a spreadsheet.
 */
public interface Renderer {

    void start(String source, List<Rule> rules);

    /** One valid reading. 'fired' holds the rules that alerted; empty if none did. */
    void show(Snapshot reading, List<Rule> fired);

    void invalid(String line, String reason);

    void summary(History history, List<Rule> rules, int read, int invalid);
}
