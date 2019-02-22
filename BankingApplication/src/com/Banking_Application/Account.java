package com.Banking_Application;

public class Account {
    private String accNumber;
    private String accHolder;
    private String openDate;
    private double balance;
    private static int currentAccountNumber;

    public Account(String accNumber, String accHolder, String openDate, double balance) {
        this.accNumber = accNumber;
        this.accHolder = accHolder;
        this.openDate = openDate;
        this.balance = balance;
        currentAccountNumber = Integer.parseInt(accNumber);
    }

    public String getAccNumber() {
        return accNumber;
    }
    public String getAccHolder() {return accHolder; }
    public String getOpenDate() {return openDate;}
    public double getBalance() {return balance;}

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void transfer(Account a, double amount) {
        this.withdraw(amount);
        a.deposit(amount);
    }

    public String nextAccountNumber() {
        currentAccountNumber += 1;
        return currentAccountNumber + "";
    }

    @Override
    public String toString() {
        return this.accNumber;  // Shows only the account number in the dropdown list and prevents the full object from being seen in the dropdown
    }
}
