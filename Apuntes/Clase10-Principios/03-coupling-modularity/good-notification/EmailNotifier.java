public class EmailNotifier implements Notifier {

    @Override
    public void send(String customer, String message) {
        System.out.println("  [email -> " + customer + "@mail.com] " + message);
    }
}
