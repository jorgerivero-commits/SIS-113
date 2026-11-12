/**
 * Depends on Notifier, the interface, not on any concrete sender.
 *
 * DEPENDENCY INJECTION: the notifier is handed in through the constructor.
 * OrderProcessor never says "new EmailNotifier()". Whoever creates the
 * processor decides which notifier it gets.
 */
public class OrderProcessor {

    private final Notifier notifier;

    public OrderProcessor(Notifier notifier) {
        if (notifier == null) {
            throw new IllegalArgumentException("notifier is required");
        }
        this.notifier = notifier;
    }

    public void process(String customer, double total) {
        if (total <= 0) {
            throw new IllegalArgumentException("total must be positive, got " + total);
        }
        System.out.println("processing order of " + customer + ": Bs " + total);
        notifier.send(customer, "Your order of Bs " + total + " is confirmed");
    }
}
