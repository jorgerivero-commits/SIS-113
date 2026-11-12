package sentinel.app;

/**
 * The command-line options, parsed and validated once.
 *
 * Every option has the form --name=value. A typo or a bad number is rejected
 * here, before anything starts, with a message that says which option failed.
 */
public final class Config {

    private String replayPath;
    private String runCommand;
    private String render = "color";
    private int memory = 80;
    private int cpu = 0;
    private double load = 0;
    private int sustain = 1;
    private boolean all = false;
    private int lines = 0;
    private boolean help = false;

    private Config() {
    }

    public static Config parse(String[] args) {
        Config config = new Config();
        for (String arg : args) {
            if (arg.equals("--help")) {
                config.help = true;
                continue;
            }
            if (arg.equals("--all")) {
                config.all = true;
                continue;
            }
            if (!arg.startsWith("--") || !arg.contains("=")) {
                throw new IllegalArgumentException("unknown argument: " + arg);
            }
            String key = arg.substring(2, arg.indexOf('='));
            String value = arg.substring(arg.indexOf('=') + 1);
            switch (key) {
                case "replay" -> config.replayPath = required(key, value);
                case "run" -> config.runCommand = required(key, value);
                case "render" -> config.render = renderName(value);
                case "mem" -> config.memory = toInt(key, value);
                case "cpu" -> config.cpu = toInt(key, value);
                case "load" -> config.load = toDouble(key, value);
                case "sustain" -> config.sustain = toInt(key, value);
                case "lines" -> config.lines = toInt(key, value);
                default -> throw new IllegalArgumentException("unknown option: --" + key);
            }
        }
        if (config.replayPath != null && config.runCommand != null) {
            throw new IllegalArgumentException("use --replay or --run, not both");
        }
        if (config.sustain < 1) {
            throw new IllegalArgumentException("--sustain must be 1 or more, got " + config.sustain);
        }
        if (config.lines < 0) {
            throw new IllegalArgumentException("--lines must be 0 (no limit) or more, got " + config.lines);
        }
        return config;
    }

    public static String usage() {
        return String.join("\n",
                "usage: java -cp out sentinel.app.Main [options]",
                "",
                "  source (default: read lines from stdin)",
                "    --replay=PATH     replay a saved sample file",
                "    --run=PATH        launch a collector and read its output (Linux/WSL)",
                "",
                "  rules (0 turns a rule off)",
                "    --mem=N           memory >= N %          (default 80)",
                "    --cpu=N           cpu >= N %             (default off)",
                "    --load=X          load1 >= X             (default off)",
                "    --sustain=N       alert only after N readings in a row",
                "    --all             alert only when ALL rules match",
                "",
                "  output",
                "    --render=NAME     color | plain | csv    (default color)",
                "    --lines=N         stop after N lines     (default 0: no limit)",
                "    --help            show this message");
    }

    private static String required(String key, String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("--" + key + " needs a value");
        }
        return value;
    }

    private static String renderName(String value) {
        if (!value.equals("color") && !value.equals("plain") && !value.equals("csv")) {
            throw new IllegalArgumentException("--render must be color, plain or csv, got " + value);
        }
        return value;
    }

    private static int toInt(String key, String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("--" + key + " must be a whole number, got " + value);
        }
    }

    private static double toDouble(String key, String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("--" + key + " must be a number, got " + value);
        }
    }

    public String getReplayPath() {
        return replayPath;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public String getRender() {
        return render;
    }

    public int getMemory() {
        return memory;
    }

    public int getCpu() {
        return cpu;
    }

    public double getLoad() {
        return load;
    }

    public int getSustain() {
        return sustain;
    }

    public boolean isAll() {
        return all;
    }

    public int getLines() {
        return lines;
    }

    public boolean isHelp() {
        return help;
    }
}
