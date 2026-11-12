// Cohesion & SRP — BAD
//
// One class, FOUR reasons to change:
//   1. the validation rules change   (minimum age is now 16)
//   2. the database changes          (MySQL -> PostgreSQL)
//   3. the email provider changes    (SMTP -> an API)
//   4. the report format changes     (text -> PDF)
// Every one of those changes touches UserManager, and can break the other three.
//
//   java BadUserManager.java

import java.util.ArrayList;
import java.util.List;

public class BadUserManager {

    public static void main(String[] args) {
        UserManager manager = new UserManager();
        manager.register("Ana Perez", "ana@ucb.edu.bo", 21);
        manager.register("Luis Rojas", "luis-at-ucb", 19);
        manager.register("Rosa Mamani", "rosa@ucb.edu.bo", 15);
        manager.register("Juan Quispe", "juan@ucb.edu.bo", 30);
        System.out.println();
        System.out.println(manager.report());
        System.out.println();
        System.out.println("Want to test ONLY the email validation? You can't:");
        System.out.println("register() also writes to the database and sends an email every time.");
    }
}

class UserManager {

    private final List<String> rows = new ArrayList<>();
    private int emailsSent = 0;

    void register(String name, String email, int age) {
        // 1) validation
        if (email == null || !email.contains("@") || !email.contains(".")) {
            System.out.println("  [validation] rejected " + name + ": invalid email");
            return;
        }
        if (age < 18) {
            System.out.println("  [validation] rejected " + name + ": under 18");
            return;
        }

        // 2) storage, pretending to be SQL
        rows.add(name + "," + email + "," + age);
        System.out.println("  [database]   INSERT INTO users VALUES ('" + name + "', '" + email + "', " + age + ")");

        // 3) email
        System.out.println("  [smtp]       connecting to smtp.ucb.edu.bo:587... sent 'Welcome " + name + "' to " + email);
        emailsSent++;
    }

    String report() {
        // 4) reporting, which also has to know how rows are stored
        StringBuilder text = new StringBuilder("== Users report ==\n");
        for (String row : rows) {
            String[] parts = row.split(",");
            text.append(parts[0]).append(" <").append(parts[1]).append(">\n");
        }
        text.append("total: ").append(rows.size()).append(" | emails sent: ").append(emailsSent);
        return text.toString();
    }
}
