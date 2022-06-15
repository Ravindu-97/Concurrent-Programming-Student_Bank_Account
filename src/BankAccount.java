public class BankAccount implements CurrentAccount{

    private int balance = 0;
    private int accountNumber = 12345;
    private String accountHolder = "Ravindu Kaveesha";
    Statement bankStatement = new Statement(accountHolder, accountNumber);

    public synchronized int getBalance() {
        return balance;
    }

    public synchronized int getAccountNumber() {
        return accountNumber;
    }

    public synchronized String getAccountHolder() {
        return accountHolder;
    }

    public synchronized void deposit(Transaction t) {
        balance += t.getAmount();
        bankStatement.addTransaction(t.getCID(), t.getAmount(), balance);
        notifyAll();
    }

    public synchronized void withdrawal(Transaction t) {
        while(isOverdrawn(t) == true) {
            try {
                wait(); // add calling thread to 'wait set'
            } catch (InterruptedException e) { }
        }
        try {
            if (isOverdrawn(t) == false){
                balance -= t.getAmount();
                bankStatement.addTransaction(t.getCID(), t.getAmount(), balance);
                notifyAll();
            }
        } catch (Exception e) {
            System.out.println("Account balance is insufficient for the withdrawal!");
        }
    }

    public synchronized boolean isOverdrawn(Transaction t) {
        if (balance - t.getAmount() < 0){
            return true;
        }
        else {
            return false;
        }
    }

    public synchronized void printStatement() {
        bankStatement.print();
    }
}
