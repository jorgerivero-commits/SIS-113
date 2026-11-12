// Simplicity: KISS, DRY, YAGNI — BAD
//
// The task: capitalize names and cities ("  ana PEREZ " -> "Ana Perez").
// What was built:
//   KISS violated:  an interface, an abstract class, a factory and a pipeline
//                   to capitalize a word
//   DRY violated:   formatName() and formatCity() are the same code, twice
//   YAGNI violated: leetSpeak(), reverseWords() and an "enabled" flag that
//                   nobody asked for and nobody uses
//
//   java BadStringUtils.java

import java.util.ArrayList;
import java.util.List;

public class BadStringUtils {

    public static void main(String[] args) {
        TransformationPipeline pipeline = new TransformationPipeline();
        pipeline.add(TransformationFactory.create("trim"));
        pipeline.add(TransformationFactory.create("capitalize-words"));
        System.out.println("[" + pipeline.run("  ana PEREZ ") + "]");

        StringUtils utils = new StringUtils();
        System.out.println("[" + utils.formatName("  luis rojas") + "]");
        System.out.println("[" + utils.formatCity("la paz  ") + "]");
    }
}

interface TextTransformation {
    String apply(String input);
}

abstract class AbstractTextTransformation implements TextTransformation {

    protected boolean enabled = true;   // YAGNI: never set to false anywhere

    @Override
    public String apply(String input) {
        if (!enabled) {
            return input;
        }
        return doApply(input);
    }

    protected abstract String doApply(String input);
}

class TrimTransformation extends AbstractTextTransformation {
    @Override
    protected String doApply(String input) {
        return input.trim();
    }
}

class CapitalizeWordsTransformation extends AbstractTextTransformation {
    @Override
    protected String doApply(String input) {
        StringBuilder out = new StringBuilder();
        for (String word : input.split(" ")) {
            if (word.isEmpty()) {
                continue;
            }
            if (out.length() > 0) {
                out.append(" ");
            }
            out.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
        }
        return out.toString();
    }
}

class LeetTransformation extends AbstractTextTransformation {   // YAGNI
    @Override
    protected String doApply(String input) {
        return input.replace("a", "4").replace("e", "3").replace("o", "0");
    }
}

class TransformationFactory {
    static TextTransformation create(String type) {
        if (type.equals("trim")) {
            return new TrimTransformation();
        } else if (type.equals("capitalize-words")) {
            return new CapitalizeWordsTransformation();
        } else if (type.equals("leet")) {
            return new LeetTransformation();
        }
        throw new IllegalArgumentException("unknown transformation: " + type);
    }
}

class TransformationPipeline {

    private final List<TextTransformation> steps = new ArrayList<>();

    void add(TextTransformation step) {
        steps.add(step);
    }

    String run(String input) {
        String result = input;
        for (TextTransformation step : steps) {
            result = step.apply(result);
        }
        return result;
    }
}

class StringUtils {

    // DRY: these two methods are the same code with different names.
    String formatName(String name) {
        StringBuilder out = new StringBuilder();
        for (String word : name.trim().split(" ")) {
            if (word.isEmpty()) {
                continue;
            }
            if (out.length() > 0) {
                out.append(" ");
            }
            out.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
        }
        return out.toString();
    }

    String formatCity(String city) {
        StringBuilder out = new StringBuilder();
        for (String word : city.trim().split(" ")) {
            if (word.isEmpty()) {
                continue;
            }
            if (out.length() > 0) {
                out.append(" ");
            }
            out.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
        }
        return out.toString();
    }

    String reverseWords(String text) {   // YAGNI: nobody calls this
        String[] words = text.trim().split(" ");
        StringBuilder out = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            out.append(words[i]).append(i > 0 ? " " : "");
        }
        return out.toString();
    }
}
