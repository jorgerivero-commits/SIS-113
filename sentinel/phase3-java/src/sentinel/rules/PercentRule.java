package sentinel.rules;

import sentinel.model.Snapshot;

/**
 * A rule over a percentage: the threshold must be between 1 and 100.
 *
 * In Clase 8 this guard lived in Rule itself, and LoadRule could not fit:
 * load is not a percentage. Moving the guard one level down is the fix.
 * The lesson: a base class decides for ALL its children, so it should only
 * hold what is true for all of them.
 */
public abstract class PercentRule extends Rule {

    private final int threshold;

    protected PercentRule(String name, int threshold) {
        super(name);
        if (threshold < 1 || threshold > 100) {
            throw new IllegalArgumentException(name + " threshold must be between 1 and 100, got " + threshold);
        }
        this.threshold = threshold;
    }

    /** Which percentage this rule watches. The only thing each child writes. */
    protected abstract double valueOf(Snapshot reading);

    /** final: every percentage rule compares the same way. */
    @Override
    protected final boolean matches(Snapshot reading) {
        return valueOf(reading) >= threshold;
    }

    @Override
    public String describe() {
        return getName() + " >= " + threshold + "%";
    }

    public int getThreshold() {
        return threshold;
    }
}
