// Reusability & Extensibility — GOOD (Open/Closed Principle)
//
// OPEN for extension: a new format is a new class.
// CLOSED for modification: Report and the existing formatters never change.
// REUSABLE: the same CsvFormatter serves the sales and the refunds.
//
// This is the Strategy pattern: the "how" is an object you pass in.
// It is exactly how Sentinel Phase 3 chooses its Renderer.
//
//   java GoodReport.java

import java.util.ArrayList;
import java.util.List;

public class GoodReport {

    public static void main(String[] args) {
        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale("Popcorn", 20.0));
        sales.add(new Sale("Ticket", 50.0));

        Report report = new Report();
        System.out.println(report.render("Sales", sales, new TextFormatter()));
        System.out.println(report.render("Sales", sales, new CsvFormatter()));
        System.out.println(report.render("Sales", sales, new HtmlFormatter()));

        // Added later, as a NEW class. Nothing above was edited.
        System.out.println(report.render("Sales", sales, new JsonFormatter()));

        // Reuse: the same formatter for a different list.
        List<Sale> refunds = new ArrayList<>();
        refunds.add(new Sale("Ticket", 50.0));
        System.out.println(report.render("Refunds", refunds, new CsvFormatter()));
    }
}

final class Sale {

    private final String product;
    private final double amount;

    Sale(String product, double amount) {
        this.product = product;
        this.amount = amount;
    }

    String getProduct() {
        return product;
    }

    double getAmount() {
        return amount;
    }
}

interface ReportFormatter {

    String format(String title, List<Sale> sales);
}

class TextFormatter implements ReportFormatter {

    @Override
    public String format(String title, List<Sale> sales) {
        StringBuilder out = new StringBuilder("== " + title + " ==\n");
        for (Sale sale : sales) {
            out.append(sale.getProduct()).append(": Bs ").append(sale.getAmount()).append("\n");
        }
        return out.toString();
    }
}

class CsvFormatter implements ReportFormatter {

    @Override
    public String format(String title, List<Sale> sales) {
        StringBuilder out = new StringBuilder("product,amount\n");
        for (Sale sale : sales) {
            out.append(sale.getProduct()).append(",").append(sale.getAmount()).append("\n");
        }
        return out.toString();
    }
}

class HtmlFormatter implements ReportFormatter {

    @Override
    public String format(String title, List<Sale> sales) {
        StringBuilder out = new StringBuilder("<h1>" + title + "</h1>\n<table>\n");
        for (Sale sale : sales) {
            out.append("  <tr><td>").append(sale.getProduct()).append("</td><td>")
                    .append(sale.getAmount()).append("</td></tr>\n");
        }
        return out.append("</table>\n").toString();
    }
}

/** The format that BadReport could not support without editing its if/else. */
class JsonFormatter implements ReportFormatter {

    @Override
    public String format(String title, List<Sale> sales) {
        StringBuilder out = new StringBuilder("[\n");
        for (int i = 0; i < sales.size(); i++) {
            Sale sale = sales.get(i);
            out.append("  {\"product\": \"").append(sale.getProduct())
                    .append("\", \"amount\": ").append(sale.getAmount()).append("}");
            out.append(i < sales.size() - 1 ? ",\n" : "\n");
        }
        return out.append("]\n").toString();
    }
}

/** Never changes when a format is added. It only knows the interface. */
class Report {

    String render(String title, List<Sale> sales, ReportFormatter formatter) {
        if (sales.isEmpty()) {
            return "(no " + title.toLowerCase() + ")\n";
        }
        return formatter.format(title, sales);
    }
}
