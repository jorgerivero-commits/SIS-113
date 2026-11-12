// Defensibility — GOOD
//
//   FAIL-FAST:        everything is validated BEFORE charging, with a clear error
//   SAFE DEFAULTS:    currency BOB; a terminal cannot refund unless told so
//   LEAST PRIVILEGE:  only a supervisor terminal is created with refunds on
//   NO LEAKS:         the log shows only the last 4 digits of the card
//
//   java GoodPayment.java

public class GoodPayment {

    public static void main(String[] args) {
        PaymentTerminal cashier = new PaymentTerminal();        // safe defaults
        cashier.pay("4111111111111111", 100);

        attempt("invalid card", () -> cashier.pay("1234", 50));
        attempt("negative amount", () -> cashier.pay("4111111111111111", -500));
        attempt("amount over the limit", () -> cashier.pay("4111111111111111", 50000));
        attempt("cashier refund", () -> cashier.refund(1000));

        PaymentTerminal supervisor = new PaymentTerminal("BOB", true);   // refunds granted on purpose
        supervisor.pay("5500000000000004", 80);
        attempt("refund more than collected", () -> supervisor.refund(1000));
        supervisor.refund(30);

        System.out.println();
        System.out.println("cashier collected:    Bs " + cashier.getCollected());
        System.out.println("supervisor collected: Bs " + supervisor.getCollected());
    }

    private static void attempt(String what, Runnable action) {
        try {
            action.run();
            System.out.println("  !! " + what + " was accepted");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("  rejected (" + what + "): " + e.getMessage());
        }
    }
}

class PaymentTerminal {

    private static final double MAX_AMOUNT = 10000;

    private final String currency;
    private final boolean canRefund;
    private double collected = 0;

    /** The safe default: local currency, no refunds. */
    PaymentTerminal() {
        this("BOB", false);
    }

    PaymentTerminal(String currency, boolean canRefund) {
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("currency must be a 3-letter code, got " + currency);
        }
        this.currency = currency;
        this.canRefund = canRefund;
    }

    void pay(String card, double amount) {
        validateCard(card);        // first, check everything...
        validateAmount(amount);
        collected += amount;       // ...then, and only then, charge
        System.out.println("  [log] charged " + amount + " " + currency + " to card " + mask(card));
    }

    void refund(double amount) {
        if (!canRefund) {
            throw new IllegalStateException("this terminal is not allowed to refund");
        }
        validateAmount(amount);
        if (amount > collected) {
            throw new IllegalArgumentException("cannot refund " + amount + ", only " + collected + " was collected");
        }
        collected -= amount;
        System.out.println("  [log] refunded " + amount + " " + currency);
    }

    double getCollected() {
        return collected;
    }

    private void validateCard(String card) {
        if (card == null || card.length() != 16) {
            throw new IllegalArgumentException("card must have 16 digits");
        }
        for (int i = 0; i < card.length(); i++) {
            if (!Character.isDigit(card.charAt(i))) {
                throw new IllegalArgumentException("card must have only digits");
            }
        }
    }

    private void validateAmount(double amount) {
        if (amount <= 0 || amount > MAX_AMOUNT) {
            throw new IllegalArgumentException("amount must be between 0 and " + MAX_AMOUNT + ", got " + amount);
        }
    }

    private String mask(String card) {
        return "**** **** **** " + card.substring(12);
    }
}
