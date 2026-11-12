package sentinel.check;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import sentinel.app.Config;
import sentinel.app.Factory;
import sentinel.app.Sentinel;
import sentinel.collect.ReplayCollector;
import sentinel.model.History;
import sentinel.model.Snapshot;
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
 * Self-check for Phase 3. Run from sentinel/phase3-java with ./build.sh,
 * or by hand:
 *
 *   javac -d out $(find src -name "*.java")
 *   javac -d out-test -cp out $(find test -name "*.java")
 *   java -cp out:out-test sentinel.check.SentinelCheck
 */
public class SentinelCheck {

    private static final Path CLEAN = Path.of("../phase1-bash/samples/muestra-2026-09-17.txt");
    private static final Path BROKEN = Path.of("../phase1-bash/samples/muestra-rota.txt");

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        percentRules();
        loadRule();
        sustainedRule();
        allOfRule();
        engine();
        config();
        factory();

        System.out.println();
        System.out.println("Result: " + passed + "/" + (passed + failed) + " checks passed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ── Rules ───────────────────────────────────────────────────────────────

    private static void percentRules() {
        section("PercentRule: MemoryRule and CpuRule");
        MemoryRule memory = new MemoryRule(80);
        check("85% memory matches 80", memory.evaluate(reading(85, 10, 0.5)));
        check("70% memory does not match 80", !memory.evaluate(reading(70, 10, 0.5)));
        check("hits count only the alert", memory.getHits() == 1);
        check("describe() says what it watches", memory.describe().equals("memory >= 80%"));
        rejects("threshold 0 is rejected", () -> new MemoryRule(0));
        rejects("threshold 101 is rejected", () -> new CpuRule(101));
        check("cpu 61.9 matches 60", new CpuRule(60).evaluate(reading(10, 61.9, 0.5)));
        rejects("a null reading is rejected", () -> new MemoryRule(80).evaluate(null));
    }

    private static void loadRule() {
        section("LoadRule: not a percentage");
        LoadRule load = new LoadRule(2.0);
        check("load 2.31 matches 2.0", load.evaluate(reading(10, 10, 2.31)));
        check("load 1.98 does not match 2.0", !load.evaluate(reading(10, 10, 1.98)));
        check("a threshold over 100 is fine for load", new LoadRule(150).describe().equals("load1 >= 150.0"));
        rejects("threshold 0 is rejected", () -> new LoadRule(0));
    }

    private static void sustainedRule() {
        section("SustainedRule: composition, any rule inside");
        SustainedRule rule = new SustainedRule(new MemoryRule(80), 3);
        check("85% -> streak 1, no alert", !rule.evaluate(reading(85, 10, 0.5)) && rule.getStreak() == 1);
        check("90% -> streak 2, no alert", !rule.evaluate(reading(90, 10, 0.5)) && rule.getStreak() == 2);
        check("88% -> streak 3, ALERT", rule.evaluate(reading(88, 10, 0.5)));
        check("40% -> streak back to 0", !rule.evaluate(reading(40, 10, 0.5)) && rule.getStreak() == 0);
        check("only the sustained alert is counted", rule.getHits() == 1);
        check("the name says it is sustained", rule.getName().equals("memory sustained"));
        check("works around a LoadRule too", new SustainedRule(new LoadRule(2.0), 2).describe()
                .equals("load1 >= 2.0 for 2 readings"));
        rejects("fewer than 2 readings is rejected", () -> new SustainedRule(new MemoryRule(80), 1));
        rejects("a null inner rule is rejected", () -> new SustainedRule(null, 3));
    }

    private static void allOfRule() {
        section("AllOfRule: a rule made of rules");
        AllOfRule both = new AllOfRule(List.of(new MemoryRule(80), new CpuRule(60)));
        check("memory 85 AND cpu 70 -> alert", both.evaluate(reading(85, 70, 0.5)));
        check("memory 85 but cpu 10 -> no alert", !both.evaluate(reading(85, 10, 0.5)));
        check("describe() joins the parts", both.describe().equals("(memory >= 80% AND cpu >= 60%)"));
        rejects("one part is not enough", () -> new AllOfRule(List.of(new MemoryRule(80))));

        // The memory part fails on the first reading. If AllOfRule stopped
        // asking there, the sustained cpu part would miss a streak step.
        AllOfRule withMemory = new AllOfRule(List.of(new MemoryRule(80), new SustainedRule(new CpuRule(60), 2)));
        withMemory.evaluate(reading(50, 70, 0.5));
        check("every part is asked, even after one fails", withMemory.evaluate(reading(85, 70, 0.5)));
    }

    // ── Engine, with a test double ──────────────────────────────────────────

    private static void engine() {
        section("Sentinel engine, replaying the samples");
        RecordingRenderer recorder = new RecordingRenderer();
        List<Rule> rules = new ArrayList<>();
        rules.add(new MemoryRule(80));
        Sentinel sentinel = new Sentinel(new ReplayCollector(CLEAN), rules, recorder, 0);
        sentinel.run();
        check("clean sample: 20 lines read, 20 valid", sentinel.getRead() == 20 && sentinel.getValid() == 20);
        check("clean sample: 3 alerts at memory 80", recorder.alerts == 3);
        check("the renderer was started and summarized once", recorder.started == 1 && recorder.summaries == 1);

        RecordingRenderer broken = new RecordingRenderer();
        Sentinel onBroken = new Sentinel(new ReplayCollector(BROKEN), rules, broken, 0);
        onBroken.run();
        check("broken sample: 7 read, 4 rejected, 3 shown",
                onBroken.getRead() == 7 && onBroken.getInvalid() == 4 && broken.shown == 3);

        Sentinel limited = new Sentinel(new ReplayCollector(CLEAN), rules, new RecordingRenderer(), 5);
        limited.run();
        check("max lines stops early", limited.getRead() == 5);

        rejects("a missing sample file is rejected", () -> new ReplayCollector(Path.of("nope.txt")));
        rejects("an empty rule list is rejected",
                () -> new Sentinel(new ReplayCollector(CLEAN), new ArrayList<>(), new RecordingRenderer(), 0));
    }

    // ── Config and Factory ──────────────────────────────────────────────────

    private static void config() {
        section("Config: the command line");
        Config defaults = Config.parse(new String[] {});
        check("defaults: memory 80, cpu off, color", defaults.getMemory() == 80 && defaults.getCpu() == 0
                && defaults.getRender().equals("color"));
        Config custom = Config.parse(new String[] {"--cpu=60", "--load=2.5", "--render=csv", "--lines=10"});
        check("options are read", custom.getCpu() == 60 && custom.getLoad() == 2.5
                && custom.getRender().equals("csv") && custom.getLines() == 10);
        rejects("an unknown option is rejected", () -> Config.parse(new String[] {"--colour=red"}));
        rejects("a bad number is rejected", () -> Config.parse(new String[] {"--mem=eighty"}));
        rejects("an unknown renderer is rejected", () -> Config.parse(new String[] {"--render=html"}));
        rejects("--replay and --run together are rejected",
                () -> Config.parse(new String[] {"--replay=a.txt", "--run=./collector.sh"}));
        rejects("a bare word is rejected", () -> Config.parse(new String[] {"80"}));
    }

    private static void factory() {
        section("Factory: text becomes objects, in one place");
        List<Rule> three = Factory.rules(Config.parse(new String[] {"--cpu=60", "--load=2.0"}));
        check("memory, cpu and load: 3 rules", three.size() == 3);
        List<Rule> sustained = Factory.rules(Config.parse(new String[] {"--cpu=60", "--sustain=3"}));
        check("--sustain wraps every rule", sustained.size() == 2
                && sustained.get(0).getName().equals("memory sustained")
                && sustained.get(1).getName().equals("cpu sustained"));
        List<Rule> all = Factory.rules(Config.parse(new String[] {"--cpu=60", "--all"}));
        check("--all combines them into one rule", all.size() == 1 && all.get(0).getName().equals("all"));
        rejects("turning every rule off is rejected", () -> Factory.rules(Config.parse(new String[] {"--mem=0"})));
        check("--render=csv builds a CsvRenderer",
                Factory.renderer(Config.parse(new String[] {"--render=csv"})) instanceof CsvRenderer);
        check("--render=plain builds a PlainRenderer",
                Factory.renderer(Config.parse(new String[] {"--render=plain"})) instanceof PlainRenderer);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    /** A valid Snapshot with exactly these memory %, cpu % and load. */
    private static Snapshot reading(int memoryPercent, double cpu, double load) {
        return new Snapshot(1758121200, cpu, memoryPercent * 1000L, 100000L, load, 300, "test");
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("== " + title);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  PASS  " + name);
        } else {
            failed++;
            System.out.println("  FAIL  " + name);
        }
    }

    private static void rejects(String name, Runnable action) {
        try {
            action.run();
            failed++;
            System.out.println("  FAIL  " + name + " (no exception was thrown)");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("  PASS  " + name + " -> " + e.getMessage());
        }
    }

    /**
     * A TEST DOUBLE: a Renderer that prints nothing and only counts what it
     * receives. Sentinel cannot tell the difference, because it only knows
     * the interface. That is one of the biggest payoffs of an interface.
     */
    private static class RecordingRenderer implements Renderer {

        private int started = 0;
        private int shown = 0;
        private int alerts = 0;
        private int summaries = 0;

        @Override
        public void start(String source, List<Rule> rules) {
            started++;
        }

        @Override
        public void show(Snapshot reading, List<Rule> fired) {
            shown++;
            if (!fired.isEmpty()) {
                alerts++;
            }
        }

        @Override
        public void invalid(String line, String reason) {
        }

        @Override
        public void summary(History history, List<Rule> rules, int read, int invalid) {
            summaries++;
        }
    }
}
