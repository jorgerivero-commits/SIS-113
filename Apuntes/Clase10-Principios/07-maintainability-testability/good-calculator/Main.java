import java.time.LocalDate;
import java.util.List;

/**
 * The only place that asks for today's date and prints. The calculation
 * itself (Calculator) does neither.
 *
 *   javac -d out *.java
 *   java -cp out Main
 *   java -cp out CalculatorTest
 */
public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        List<String> order = List.of("popcorn", "ticket", "ticket");
        System.out.println("today is " + LocalDate.now().getDayOfWeek());
        System.out.println("total: Bs " + calculator.total(order, LocalDate.now().getDayOfWeek()));
    }
}
