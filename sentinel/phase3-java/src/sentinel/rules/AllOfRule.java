package sentinel.rules;

import java.util.ArrayList;
import java.util.List;
import sentinel.model.Snapshot;

/**
 * Alerts only when EVERY inner rule matches: "memory AND cpu".
 *
 * A rule made of rules. To the outside it is just one more Rule, so Sentinel
 * and the renderers treat it exactly like the others. That is polymorphism
 * plus composition.
 */
public class AllOfRule extends Rule {

    private final List<Rule> parts;

    public AllOfRule(List<Rule> parts) {
        super("all");
        if (parts == null || parts.size() < 2) {
            throw new IllegalArgumentException("an all-of rule needs at least 2 rules");
        }
        this.parts = new ArrayList<>(parts);
    }

    /**
     * Every part is asked, even after one said no. Some rules remember the
     * past (SustainedRule counts a streak); skipping them would corrupt their
     * memory. Same trap as "alert = alert || rule.evaluate(...)" in Clase 9.
     */
    @Override
    protected boolean matches(Snapshot reading) {
        boolean all = true;
        for (Rule part : parts) {
            if (!part.matches(reading)) {
                all = false;
            }
        }
        return all;
    }

    @Override
    public String describe() {
        StringBuilder text = new StringBuilder("(");
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) {
                text.append(" AND ");
            }
            text.append(parts.get(i).describe());
        }
        return text.append(")").toString();
    }
}
