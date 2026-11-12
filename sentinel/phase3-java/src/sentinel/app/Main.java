package sentinel.app;

import java.util.List;
import sentinel.collect.Collector;
import sentinel.render.Renderer;
import sentinel.rules.Rule;

/**
 * Sentinel — Phase 3 (Java): inheritance and polymorphism.
 *
 * Main only wires the pieces: options -> objects -> engine. Compare with the
 * Phase 2 Main, which also read the stream and decided the alerts.
 *
 *   ./build.sh                                   compile and run the checks
 *   java -cp out sentinel.app.Main --help        every option
 */
public class Main {

    public static void main(String[] args) {
        try {
            Config config = Config.parse(args);
            if (config.isHelp()) {
                System.out.println(Config.usage());
                return;
            }
            List<Rule> rules = Factory.rules(config);
            Renderer renderer = Factory.renderer(config);
            Collector collector = Factory.collector(config);
            new Sentinel(collector, rules, renderer, config.getLines()).run();
        } catch (IllegalArgumentException e) {
            System.err.println("error: " + e.getMessage());
            System.err.println();
            System.err.println(Config.usage());
            System.exit(2);
        } catch (IllegalStateException e) {
            System.err.println("error: " + e.getMessage());
            System.exit(1);
        }
    }
}
