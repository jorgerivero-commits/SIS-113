package sentinel.rules;

import sentinel.model.Snapshot;

/**
 * Alerts only when another rule matches N readings in a row. A one-second
 * spike is noise; three seconds is a problem.
 *
 * COMPOSITION OVER INHERITANCE: this class does not extend MemoryRule or
 * CpuRule. It HAS a rule, any rule. Inheriting would have needed one class per
 * combination: SustainedMemoryRule, SustainedCpuRule, SustainedLoadRule...
 * Composing, one class works for all of them, including future ones.
 */
public class SustainedRule extends Rule {

    private final Rule inner;
    private final int requiredReadings;
    private int streak = 0;

    public SustainedRule(Rule inner, int requiredReadings) {
        super(inner == null ? "?" : inner.getName() + " sustained");
        if (inner == null) {
            throw new IllegalArgumentException("inner rule is required");
        }
        if (requiredReadings < 2) {
            throw new IllegalArgumentException("a sustained rule needs at least 2 readings, got " + requiredReadings);
        }
        this.inner = inner;
        this.requiredReadings = requiredReadings;
    }

    /** Asks the inner rule WITHOUT counting its hits: only this rule's alerts count. */
    @Override
    protected boolean matches(Snapshot reading) {
        if (inner.matches(reading)) {
            streak++;
        } else {
            streak = 0;
        }
        return streak >= requiredReadings;
    }

    @Override
    public String describe() {
        return inner.describe() + " for " + requiredReadings + " readings";
    }

    public int getStreak() {
        return streak;
    }
}
