package sentinel.render;

/**
 * The console output without colors, for log files and terminals that do not
 * understand ANSI codes:
 *
 *   java -cp out sentinel.app.Main --replay=... --render=plain > report.txt
 *
 * A GOOD use of inheritance: a plain renderer IS a console renderer. It reuses
 * all of it and changes only two small, well-defined details.
 */
public class PlainRenderer extends ConsoleRenderer {

    @Override
    protected String okTag() {
        return "[  ok  ]";
    }

    @Override
    protected String alertTag() {
        return "[ALERT ]";
    }
}
