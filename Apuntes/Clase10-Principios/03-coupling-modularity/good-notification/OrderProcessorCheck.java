/**
 * Tests OrderProcessor WITHOUT sending anything. FakeNotifier only remembers
 * what it was asked to send. This is only possible because OrderProcessor
 * depends on an interface and receives it from outside.
 *
 *   java -cp out OrderProcessorCheck
 */
public class OrderProcessorCheck {

    public static void main(String[] args) {
        FakeNotifier fake = new FakeNotifier();
        OrderProcessor processor = new OrderProcessor(fake);

        processor.process("ana", 150.0);

        check("one message was sent", fake.count == 1);
        check("it went to the right customer", "ana".equals(fake.lastCustomer));
        check("it mentions the total", fake.lastMessage.contains("150.0"));

        try {
            processor.process("luis", -10);
            check("a negative total is rejected", false);
        } catch (IllegalArgumentException e) {
            check("a negative total is rejected", true);
        }
        check("nothing was sent for the rejected order", fake.count == 1);
    }

    private static void check(String name, boolean condition) {
        System.out.println((condition ? "  PASS  " : "  FAIL  ") + name);
    }

    /** A test double: implements the interface, sends nothing, remembers everything. */
    private static class FakeNotifier implements Notifier {

        private int count = 0;
        private String lastCustomer;
        private String lastMessage;

        @Override
        public void send(String customer, String message) {
            count++;
            lastCustomer = customer;
            lastMessage = message;
        }
    }
}
