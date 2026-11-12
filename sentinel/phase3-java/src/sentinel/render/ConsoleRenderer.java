package sentinel.render;

import java.util.List;
import sentinel.model.History;
import sentinel.model.Snapshot;
import sentinel.rules.Rule;

/** Human-readable output for a terminal, with colors. The default renderer. */
public class ConsoleRenderer implements Renderer {

    private static final String RED = "\033[31m";
    private static final String GREEN = "\033[32m";
    private static final String RESET = "\033[0m";

    @Override
    public void start(String source, List<Rule> rules) {
        System.out.println("Sentinel · source: " + source);
        System.out.println("rules: " + describeAll(rules));
    }

    @Override
    public void show(Snapshot reading, List<Rule> fired) {
        String tag = fired.isEmpty() ? okTag() : alertTag();
        String line = String.format("%s %s  mem %3d%%  cpu %5.1f%%  load %.2f  procs %d  top %s",
                tag,
                reading.getTime(),
                reading.getMemoryPercent(),
                reading.getCpuPct(),
                reading.getLoad1(),
                reading.getProcs(),
                reading.getTopProc());
        if (!fired.isEmpty()) {
            line += "  <- " + namesOf(fired);
        }
        System.out.println(line);
    }

    @Override
    public void invalid(String line, String reason) {
        System.out.println("invalid line (" + reason + "): " + line);
    }

    @Override
    public void summary(History history, List<Rule> rules, int read, int invalid) {
        System.out.println("── Summary ─────────────────────────────────────");
        System.out.println("lines read: " + read + "  valid: " + history.getCount() + "  invalid: " + invalid);
        if (history.getCount() > 0) {
            Snapshot peak = history.getMemoryPeak();
            System.out.println("memory average: " + history.getAverageMemory()
                    + "%  max: " + peak.getMemoryPercent() + "% at " + peak.getTime());
            System.out.printf("max load: %.2f%n", history.getMaxLoad());
            String process = history.getMostFrequentProcess();
            System.out.println("most frequent process: " + process + " (" + history.countOf(process) + " times)");
        }
        for (Rule rule : rules) {
            System.out.println("alerts " + rule.describe() + ": " + rule.getHits());
        }
    }

    /** protected: the only part a subclass is expected to change. */
    protected String okTag() {
        return GREEN + "[  ok  ]" + RESET;
    }

    protected String alertTag() {
        return RED + "[ALERT ]" + RESET;
    }

    private String describeAll(List<Rule> rules) {
        StringBuilder text = new StringBuilder();
        for (Rule rule : rules) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(rule.describe());
        }
        return text.toString();
    }

    private String namesOf(List<Rule> rules) {
        StringBuilder text = new StringBuilder();
        for (Rule rule : rules) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(rule.getName());
        }
        return text.toString();
    }
}
