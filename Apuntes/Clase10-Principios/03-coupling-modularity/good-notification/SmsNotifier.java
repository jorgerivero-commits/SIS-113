/** Added later. OrderProcessor was not touched to support it. */
public class SmsNotifier implements Notifier {

    @Override
    public void send(String customer, String message) {
        System.out.println("  [sms -> " + customer + "] " + message);
    }
}
