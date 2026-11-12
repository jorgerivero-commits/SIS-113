// Simplicity: KISS, DRY, YAGNI — GOOD
//
// Same three results. Two methods:
//   KISS:  a plain method for a plain job
//   DRY:   names and cities use the SAME method
//   YAGNI: no leet speak, no reverse, no pipeline. When someone needs them,
//          they get written then, for the real need.
//
//   java GoodStringUtils.java

public class GoodStringUtils {

    public static void main(String[] args) {
        System.out.println("[" + Text.capitalizeWords("  ana PEREZ ") + "]");
        System.out.println("[" + Text.capitalizeWords("  luis rojas") + "]");
        System.out.println("[" + Text.capitalizeWords("la paz  ") + "]");
    }
}

final class Text {

    private Text() {
    }

    static String capitalizeWords(String text) {
        StringBuilder out = new StringBuilder();
        for (String word : text.trim().split(" ")) {
            if (word.isEmpty()) {
                continue;
            }
            if (out.length() > 0) {
                out.append(" ");
            }
            out.append(capitalize(word));
        }
        return out.toString();
    }

    static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
}
