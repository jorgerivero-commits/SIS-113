/**
 * The abstraction OrderProcessor depends on. It says WHAT can be done,
 * not HOW: email, SMS, WhatsApp, or a fake one for tests.
 */
public interface Notifier {

    void send(String customer, String message);
}
