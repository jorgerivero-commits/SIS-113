package sentinel.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import sentinel.collect.Collector;
import sentinel.collect.ProcessCollector;
import sentinel.collect.ReplayCollector;
import sentinel.collect.StdinCollector;
import sentinel.render.ConsoleRenderer;
import sentinel.render.CsvRenderer;
import sentinel.render.PlainRenderer;
import sentinel.render.Renderer;
import sentinel.rules.AllOfRule;
import sentinel.rules.CpuRule;
import sentinel.rules.LoadRule;
import sentinel.rules.MemoryRule;
import sentinel.rules.Rule;
import sentinel.rules.SustainedRule;

/**
 * The ONLY place in Sentinel that names concrete classes.
 *
 * At the edge of the program, text from the command line must become objects,
 * and that takes an if or a switch. Here, and only here. From this point on,
 * the rest of the code works with Collector, Rule and Renderer, and never
 * asks which concrete class it got.
 *
 * Adding a new renderer = one new class + one line in this file.
 */
public final class Factory {

    private Factory() {
    }

    public static Renderer renderer(Config config) {
        return switch (config.getRender()) {
            case "plain" -> new PlainRenderer();
            case "csv" -> new CsvRenderer();
            default -> new ConsoleRenderer();
        };
    }

    public static List<Rule> rules(Config config) {
        List<Rule> rules = new ArrayList<>();
        if (config.getMemory() > 0) {
            rules.add(new MemoryRule(config.getMemory()));
        }
        if (config.getCpu() > 0) {
            rules.add(new CpuRule(config.getCpu()));
        }
        if (config.getLoad() > 0) {
            rules.add(new LoadRule(config.getLoad()));
        }
        if (rules.isEmpty()) {
            throw new IllegalArgumentException("at least one rule is required (--mem, --cpu or --load)");
        }

        if (config.isAll() && rules.size() > 1) {
            List<Rule> combined = new ArrayList<>();
            combined.add(new AllOfRule(rules));
            rules = combined;
        }

        if (config.getSustain() > 1) {
            List<Rule> sustained = new ArrayList<>();
            for (Rule rule : rules) {
                sustained.add(new SustainedRule(rule, config.getSustain()));
            }
            rules = sustained;
        }
        return rules;
    }

    /** Built last on purpose: a collector may start a process, so everything else is validated first. */
    public static Collector collector(Config config) {
        if (config.getReplayPath() != null) {
            return new ReplayCollector(Path.of(config.getReplayPath()));
        }
        if (config.getRunCommand() != null) {
            return new ProcessCollector(config.getRunCommand());
        }
        return new StdinCollector();
    }
}
