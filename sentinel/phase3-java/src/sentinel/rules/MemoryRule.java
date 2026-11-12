package sentinel.rules;

import sentinel.model.Snapshot;

public class MemoryRule extends PercentRule {

    public MemoryRule(int threshold) {
        super("memory", threshold);
    }

    @Override
    protected double valueOf(Snapshot reading) {
        return reading.getMemoryPercent();
    }
}
