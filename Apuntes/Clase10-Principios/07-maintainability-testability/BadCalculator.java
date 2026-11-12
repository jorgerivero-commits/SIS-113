// Maintainability & Testability — BAD
//
// One method does everything: prices, discount, tax, printing.
//   - the Tuesday discount reads TODAY's date inside the method:
//     how do you test it on a Thursday? You wait until Tuesday.
//   - it prints instead of returning: a test cannot read the result
//   - magic numbers (50, 20, 0.9, 5, 1.13) with no names
//
//   java BadCalculator.java

import java.time.DayOfWeek;
import java.time.LocalDate;

public class BadCalculator {

    public static void main(String[] args) {
        checkout(new String[] {"popcorn", "ticket", "ticket"});
        checkout(new String[] {"ticket", "soda"});
        System.out.println();
        System.out.println("Is the Tuesday discount right? Today is " + LocalDate.now().getDayOfWeek()
                + ". No way to know until a Tuesday comes.");
    }

    static void checkout(String[] items) {
        double subtotal = 0;
        for (String item : items) {
            if (item.equals("ticket")) {
                subtotal += 50;
            } else if (item.equals("popcorn")) {
                subtotal += 20;
            } else if (item.equals("soda")) {
                subtotal += 12;
            }
        }
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.TUESDAY) {
            if (subtotal > 100) {
                subtotal = subtotal * 0.9;
            } else {
                subtotal = subtotal - 5;
            }
        }
        double total = subtotal * 1.13;
        System.out.println("items: " + items.length + "  total: Bs " + Math.round(total * 100) / 100.0);
    }
}
