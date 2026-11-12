// Cohesion & SRP — GOOD
//
// Same behavior, five small classes. Each one has ONE reason to change:
//   UserValidator        the rules
//   UserRepository       the storage
//   EmailService         the email provider
//   UserReport           the report format
//   RegistrationService  the order of the steps, and nothing else
//
//   java GoodUserManager.java

import java.util.ArrayList;
import java.util.List;

public class GoodUserManager {

    public static void main(String[] args) {
        UserValidator validator = new UserValidator();
        UserRepository repository = new UserRepository();
        EmailService email = new EmailService();
        RegistrationService registration = new RegistrationService(validator, repository, email);

        registration.register("Ana Perez", "ana@ucb.edu.bo", 21);
        registration.register("Luis Rojas", "luis-at-ucb", 19);
        registration.register("Rosa Mamani", "rosa@ucb.edu.bo", 15);
        registration.register("Juan Quispe", "juan@ucb.edu.bo", 30);
        System.out.println();
        System.out.println(new UserReport().format(repository.findAll(), email.getSent()));

        // Each piece now works ALONE. Test the rules with no database and no email:
        System.out.println();
        System.out.println("validator alone, no database, no email:");
        try {
            validator.validate("Test", "not-an-email", 30);
        } catch (IllegalArgumentException e) {
            System.out.println("  rejected as expected: " + e.getMessage());
        }
    }
}

final class User {

    private final String name;
    private final String email;
    private final int age;

    User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    String getName() {
        return name;
    }

    String getEmail() {
        return email;
    }

    int getAge() {
        return age;
    }
}

class UserValidator {

    private static final int MIN_AGE = 18;

    void validate(String name, String email, int age) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("invalid email");
        }
        if (age < MIN_AGE) {
            throw new IllegalArgumentException("under " + MIN_AGE);
        }
    }
}

class UserRepository {

    private final List<User> users = new ArrayList<>();

    void save(User user) {
        users.add(user);
        System.out.println("  [database]   INSERT INTO users VALUES ('" + user.getName() + "', '"
                + user.getEmail() + "', " + user.getAge() + ")");
    }

    List<User> findAll() {
        return new ArrayList<>(users);
    }
}

class EmailService {

    private int sent = 0;

    void sendWelcome(User user) {
        System.out.println("  [smtp]       connecting to smtp.ucb.edu.bo:587... sent 'Welcome "
                + user.getName() + "' to " + user.getEmail());
        sent++;
    }

    int getSent() {
        return sent;
    }
}

class UserReport {

    String format(List<User> users, int emailsSent) {
        StringBuilder text = new StringBuilder("== Users report ==\n");
        for (User user : users) {
            text.append(user.getName()).append(" <").append(user.getEmail()).append(">\n");
        }
        text.append("total: ").append(users.size()).append(" | emails sent: ").append(emailsSent);
        return text.toString();
    }
}

class RegistrationService {

    private final UserValidator validator;
    private final UserRepository repository;
    private final EmailService email;

    RegistrationService(UserValidator validator, UserRepository repository, EmailService email) {
        this.validator = validator;
        this.repository = repository;
        this.email = email;
    }

    void register(String name, String emailAddress, int age) {
        try {
            validator.validate(name, emailAddress, age);
        } catch (IllegalArgumentException e) {
            System.out.println("  [validation] rejected " + name + ": " + e.getMessage());
            return;
        }
        User user = new User(name, emailAddress, age);
        repository.save(user);
        email.sendWelcome(user);
    }
}
