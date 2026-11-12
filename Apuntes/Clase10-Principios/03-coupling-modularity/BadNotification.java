// Loose Coupling & Modularity — BAD
//
// OrderService creates its own MailSender with "new". It is welded to it:
//   - a customer wants SMS instead of email -> edit OrderService
//   - you want to test an order            -> every test sends a real email
//   - MailSender changes its constructor  -> OrderService breaks
//
//   java BadNotification.java

public class BadNotification {

    public static void main(String[] args) {
        OrderService processor = new OrderService();
        processor.process("ana", 150.0);
        processor.process("luis", 80.0);
        System.out.println();
        System.out.println("Luis asked for SMS. To support it, OrderService itself must change.");
    }
}

class MailSender {

    void send(String address, String message) {
        System.out.println("  [email -> " + address + "] " + message);
    }
}

class OrderService {

    private final MailSender sender = new MailSender();   // hard-wired dependency

    void process(String customer, double total) {
        System.out.println("processing order of " + customer + ": Bs " + total);
        sender.send(customer + "@mail.com", "Your order of Bs " + total + " is confirmed");
    }
}
