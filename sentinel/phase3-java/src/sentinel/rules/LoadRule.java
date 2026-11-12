package sentinel.rules;

import sentinel.model.Snapshot;

/**
 * Load average is not a percentage: 2.0 is a valid threshold, 150 is too.
 * That is why this rule extends Rule directly instead of PercentRule.
 * (This is the answer to the LoadRule question of the Clase 8 homework.)
 */
public class LoadRule extends Rule {

    private final double threshold;

    public LoadRule(double threshold) {
        super("load1");
        if (threshold <= 0) {
            throw new IllegalArgumentException("load threshold must be positive, got " + threshold);
        }
        this.threshold = threshold;
    }

    @Override
    protected boolean matches(Snapshot reading) {
        return reading.getLoad1() >= threshold;
    }

    @Override
    public String describe() {
        return getName() + " >= " + threshold;
    }

    public double getThreshold() {
        return threshold;
    }
}
