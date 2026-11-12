// Encapsulation & Abstraction — GOOD
//
// ENCAPSULATION: the fields are private. The only way to change the balance is
// deposit() or withdraw(), and both enforce the rules.
// ABSTRACTION: the client says "withdraw 300" and does not know (or care) that
// a history is updated, or how the funds are checked.
//
//   java GoodBankAccount.java

import java.util.ArrayList;
import java.util.List;

public class GoodBankAccount {

    public static void main(String[] args) {
        BankAccount account = new BankAccount("Ana Perez");
        account.deposit(100);

        try {
            account.withdraw(300);
        } catch (IllegalArgumentException e) {
            System.out.println("rejected: " + e.getMessage());
        }
        try {
            account.deposit(-50);
        } catch (IllegalArgumentException e) {
            System.out.println("rejected: " + e.getMessage());
        }

        account.withdraw(30);

        // The history is handed out as a copy: changing it changes nothing.
        account.getHistory().add("deposit 1000000.0");

        System.out.println();
        System.out.println("owner:   " + account.getOwner());
        System.out.println("balance: " + account.getBalance());
        System.out.println("history: " + account.getHistory());
    }
}

class BankAccount {

    private final String owner;
    private double balance = 0;
    private final List<String> history = new ArrayList<>();

    BankAccount(String owner) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner is required");
        }
        this.owner = owner;
    }

    void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("deposit must be positive, got " + amount);
        }
        balance += amount;
        history.add("deposit " + amount);
    }

    void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("withdrawal must be positive, got " + amount);
        }
        if (amount > balance) {
            throw new IllegalArgumentException("insufficient funds: balance " + balance + ", asked " + amount);
        }
        balance -= amount;
        history.add("withdraw " + amount);
    }

    String getOwner() {
        return owner;
    }

    double getBalance() {
        return balance;
    }

    List<String> getHistory() {
        return new ArrayList<>(history);
    }
}
