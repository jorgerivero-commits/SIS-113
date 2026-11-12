import java.time.DayOfWeek;
import java.util.List;

/**
 * Tests every rule on ANY day of the week, today. Possible only because the
 * day is a parameter and every method returns its result.
 *
 *   java -cp out CalculatorTest
 */
public class CalculatorTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        List<String> big = List.of("popcorn", "ticket", "ticket");   // 120
        List<String> small = List.of("ticket", "soda");              // 62

        check("prices", calculator.priceOf("ticket") == 50 && calculator.priceOf("popcorn") == 20);
        check("subtotal of the big order is 120", calculator.subtotal(big) == 120);
        check("no discount on a Thursday", calculator.discount(120, DayOfWeek.THURSDAY) == 0);
        check("10 % off a big order on Tuesday", calculator.discount(120, DayOfWeek.TUESDAY) == 12);
        check("5 Bs off a small order on Tuesday", calculator.discount(62, DayOfWeek.TUESDAY) == 5);
        check("the discount never exceeds the subtotal", calculator.discount(3, DayOfWeek.TUESDAY) == 3);
        check("13 % tax", calculator.tax(100) == 13);
        check("big order on Thursday: 120 + 13 % = 135.6", calculator.total(big, DayOfWeek.THURSDAY) == 135.6);
        check("big order on Tuesday: 108 + 13 % = 122.04", calculator.total(big, DayOfWeek.TUESDAY) == 122.04);
        check("small order on Tuesday: 57 + 13 % = 64.41", calculator.total(small, DayOfWeek.TUESDAY) == 64.41);
        try {
            calculator.priceOf("nachos");
            check("an unknown item is rejected", false);
        } catch (IllegalArgumentException e) {
            check("an unknown item is rejected", true);
        }

        System.out.println();
        System.out.println("Result: " + passed + "/" + (passed + failed) + " checks passed");
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  PASS  " + name);
        } else {
            failed++;
            System.out.println("  FAIL  " + name);
        }
    }
}
