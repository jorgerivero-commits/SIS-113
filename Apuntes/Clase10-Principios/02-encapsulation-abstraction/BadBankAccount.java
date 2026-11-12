// Encapsulation & Abstraction — BAD
//
// Every field is public. Every client must know the internal rules:
// "update the balance AND the history, and check the funds first".
// One client that forgets is enough to corrupt the account.
//
//   java BadBankAccount.java

import java.util.ArrayList;
import java.util.List;

public class BadBankAccount {

    public static void main(String[] args) {
        OpenAccount account = new OpenAccount();
        account.owner = "Ana Perez";

        // Client 1 does it right: balance AND history.
        account.balance = account.balance + 100;
        account.history.add("deposit 100.0");

        // Client 2 forgets to check the funds, and forgets the history.
        account.balance = account.balance - 300;

        // Client 3 "fixes" the history by hand.
        account.history.add("deposit 1000000.0");

        System.out.println("owner:   " + account.owner);
        System.out.println("balance: " + account.balance);
        System.out.println("history: " + account.history);
        System.out.println();
        System.out.println("The balance is negative and the history is a lie.");
        System.out.println("Nothing stopped it: the data had no owner.");
    }
}

class OpenAccount {
    public String owner;
    public double balance;
    public List<String> history = new ArrayList<>();
}
