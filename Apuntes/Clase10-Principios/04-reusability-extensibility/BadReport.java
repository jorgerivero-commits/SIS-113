// Reusability & Extensibility — BAD
//
// One method, one if/else per format. To add JSON you must OPEN and EDIT
// generate(), the same method that already works for the other three.
// Every new format risks breaking the old ones.
// And nothing can be reused: the CSV logic only exists inside this if.
//
//   java BadReport.java

import java.util.ArrayList;
import java.util.List;

public class BadReport {

    public static void main(String[] args) {
        List<String> products = new ArrayList<>();
        List<Double> amounts = new ArrayList<>();
        products.add("Popcorn");
        amounts.add(20.0);
        products.add("Ticket");
        amounts.add(50.0);

        ReportGenerator generator = new ReportGenerator();
        System.out.println(generator.generate(products, amounts, "text"));
        System.out.println(generator.generate(products, amounts, "csv"));
        System.out.println(generator.generate(products, amounts, "html"));
        System.out.println(generator.generate(products, amounts, "json"));
    }
}

class ReportGenerator {

    String generate(List<String> products, List<Double> amounts, String format) {
        StringBuilder out = new StringBuilder();
        if (format.equals("text")) {
            out.append("== Sales ==\n");
            for (int i = 0; i < products.size(); i++) {
                out.append(products.get(i)).append(": Bs ").append(amounts.get(i)).append("\n");
            }
        } else if (format.equals("csv")) {
            out.append("product,amount\n");
            for (int i = 0; i < products.size(); i++) {
                out.append(products.get(i)).append(",").append(amounts.get(i)).append("\n");
            }
        } else if (format.equals("html")) {
            out.append("<table>\n");
            for (int i = 0; i < products.size(); i++) {
                out.append("  <tr><td>").append(products.get(i)).append("</td><td>")
                        .append(amounts.get(i)).append("</td></tr>\n");
            }
            out.append("</table>\n");
        } else {
            out.append("unsupported format: ").append(format)
                    .append(" (to add it, edit ReportGenerator.generate)\n");
        }
        return out.toString();
    }
}
