/**
 * The only place that chooses concrete classes: the wiring.
 *
 *   javac -d out *.java
 *   java -cp out Main
 *   java -cp out OrderProcessorCheck
 */
public class Main {

    public static void main(String[] args) {
        OrderProcessor byEmail = new OrderProcessor(new EmailNotifier());
        OrderProcessor bySms = new OrderProcessor(new SmsNotifier());

        byEmail.process("ana", 150.0);
        bySms.process("luis", 80.0);

        System.out.println();
        System.out.println("Same OrderProcessor class, two different ways to notify.");
    }
}
