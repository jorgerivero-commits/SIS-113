package sentinel.render;

import java.util.List;
import sentinel.model.History;
import sentinel.model.Snapshot;
import sentinel.rules.Rule;

/**
 * Comma-separated values, ready for a spreadsheet:
 *
 *   java -cp out sentinel.app.Main --replay=... --render=csv > readings.csv
 *
 * The data goes to stdout and the diagnostics go to stderr, the Unix habit
 * from Phase 1: redirecting stdout to a file keeps the CSV clean.
 *
 * It implements Renderer directly. A CSV is NOT a console report, so
 * inheriting from ConsoleRenderer would be the Drill 5 popcorn mistake.
 */
public class CsvRenderer implements Renderer {

    @Override
    public void start(String source, List<Rule> rules) {
        System.out.println("time,mem_pct,cpu_pct,load1,procs,top_proc,alert,rules");
    }

    @Override
    public void show(Snapshot reading, List<Rule> fired) {
        StringBuilder names = new StringBuilder();
        for (Rule rule : fired) {
            if (names.length() > 0) {
                names.append(";");
            }
            names.append(rule.getName());
        }
        System.out.println(String.join(",",
                reading.getTime(),
                String.valueOf(reading.getMemoryPercent()),
                String.valueOf(reading.getCpuPct()),
                String.valueOf(reading.getLoad1()),
                String.valueOf(reading.getProcs()),
                escape(reading.getTopProc()),
                String.valueOf(!fired.isEmpty()),
                escape(names.toString())));
    }

    @Override
    public void invalid(String line, String reason) {
        System.err.println("skipped invalid line: " + reason);
    }

    @Override
    public void summary(History history, List<Rule> rules, int read, int invalid) {
        System.err.println("read " + read + ", valid " + history.getCount() + ", invalid " + invalid);
        for (Rule rule : rules) {
            System.err.println("alerts " + rule.describe() + ": " + rule.getHits());
        }
    }

    /** A value with a comma or a quote must be quoted, or it breaks the columns. */
    private String escape(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
