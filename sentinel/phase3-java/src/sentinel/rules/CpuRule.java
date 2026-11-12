package sentinel.rules;

import sentinel.model.Snapshot;

public class CpuRule extends PercentRule {

    public CpuRule(int threshold) {
        super("cpu", threshold);
    }

    @Override
    protected double valueOf(Snapshot reading) {
        return reading.getCpuPct();
    }
}
