package sentinel.rules;

import sentinel.model.Snapshot;

/**
 * The root of every Sentinel rule.
 *
 *                     Rule (abstract)
 *          ┌────────────┼──────────────┬───────────────┐
 *   PercentRule      LoadRule     SustainedRule     AllOfRule
 *   (abstract)                    (HAS a Rule)      (HAS rules)
 *    ┌────┴────┐
 * MemoryRule  CpuRule
 *
 * INHERITANCE: what every rule shares lives here, once: a name, a hit counter
 * and the way hits are counted.
 *
 * POLYMORPHISM: Sentinel and the renderers only ever see "Rule". They call
 * evaluate() and describe(), and each subclass answers in its own way.
 */
public abstract class Rule {

    private final String name;
    private int hits = 0;

    protected Rule(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("rule name is required");
        }
        this.name = name;
    }

    /**
     * final: no subclass can change HOW hits are counted, only WHAT matches.
     * Every rule, including the ones written next year, counts the same way.
     */
    public final boolean evaluate(Snapshot reading) {
        if (reading == null) {
            throw new IllegalArgumentException("reading is required");
        }
        boolean alert = matches(reading);
        if (alert) {
            hits++;
        }
        return alert;
    }

    /**
     * The one question each subclass answers. protected: callers use
     * evaluate(), which also counts. Classes in this package (SustainedRule,
     * AllOfRule) may call it directly to ask without counting.
     */
    protected abstract boolean matches(Snapshot reading);

    /** Human-readable, for headers and reports. Example: "memory >= 80%". */
    public abstract String describe();

    public String getName() {
        return name;
    }

    public int getHits() {
        return hits;
    }

    @Override
    public String toString() {
        return describe();
    }
}
