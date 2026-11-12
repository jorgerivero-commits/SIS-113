// Defensibility — BAD
//
//   - checks the card AFTER charging it, and hides the failure
//   - accepts a negative amount: a "payment" that takes money out
//   - unsafe defaults: every terminal can refund, the currency starts as null
//   - logs the full card number
// At the end the register shows a negative total, and no error was ever printed.
//
//   java BadPayment.java

public class BadPayment {

    public static void main(String[] args) {
        Register register = new Register();
        register.pay("4111111111111111", 100);
        register.pay("1234", 50);                  // invalid card: charged anyway, error swallowed
        register.pay("4111111111111111", -500);    // negative "payment"
        register.refund(1000);                     // a cashier refunds more than was ever paid

        System.out.println();
        System.out.println("collected: Bs " + register.collected);
        System.out.println("No error was reported. Nobody knows anything went wrong.");
    }
}

class Register {

    boolean canRefund = true;   // every terminal, by default
    String currency;            // null until someone remembers to set it
    double collected = 0;

    void pay(String card, double amount) {
        try {
            System.out.println("  [log] charging card " + card + " amount " + amount + " " + currency);
            collected += amount;
            if (card.length() != 16) {
                throw new RuntimeException("bad card");   // detected too late...
            }
        } catch (Exception e) {
            // ...and ignored
        }
    }

    void refund(double amount) {
        if (canRefund) {
            collected -= amount;
        }
    }
}
