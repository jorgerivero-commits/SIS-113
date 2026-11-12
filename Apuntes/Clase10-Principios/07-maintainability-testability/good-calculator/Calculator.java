import java.time.DayOfWeek;
import java.util.List;

/**
 * Small PURE methods: same input, same output, nothing printed, no hidden
 * "today". The day is a parameter, so a test can pass any day it wants.
 */
public class Calculator {

    private static final double TICKET = 50;
    private static final double POPCORN = 20;
    private static final double SODA = 12;
    private static final double BIG_ORDER = 100;
    private static final double BIG_ORDER_DISCOUNT_PERCENT = 10;
    private static final double SMALL_ORDER_DISCOUNT = 5;
    private static final double TAX_PERCENT = 13;   // IVA in Bolivia

    public double priceOf(String item) {
        return switch (item) {
            case "ticket" -> TICKET;
            case "popcorn" -> POPCORN;
            case "soda" -> SODA;
            default -> throw new IllegalArgumentException("unknown item: " + item);
        };
    }

    public double subtotal(List<String> items) {
        double subtotal = 0;
        for (String item : items) {
            subtotal += priceOf(item);
        }
        return subtotal;
    }

    /** Tuesday is discount day: 10 % on big orders, 5 Bs off on small ones. */
    public double discount(double subtotal, DayOfWeek day) {
        if (day != DayOfWeek.TUESDAY) {
            return 0;
        }
        if (subtotal > BIG_ORDER) {
            return subtotal * BIG_ORDER_DISCOUNT_PERCENT / 100;
        }
        return Math.min(SMALL_ORDER_DISCOUNT, subtotal);
    }

    public double tax(double amount) {
        return amount * TAX_PERCENT / 100;
    }

    public double total(List<String> items, DayOfWeek day) {
        double subtotal = subtotal(items);
        double afterDiscount = subtotal - discount(subtotal, day);
        return round(afterDiscount + tax(afterDiscount));
    }

    private double round(double amount) {
        return Math.round(amount * 100) / 100.0;
    }
}
