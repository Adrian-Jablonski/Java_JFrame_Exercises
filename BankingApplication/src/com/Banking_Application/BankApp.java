package com.Banking_Application;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BankApp extends javax.swing.JFrame{
    private JPanel panel1;
    private JTextField customerName;
    private JTextField openDate;
    private JComboBox <Account> accountDropdown;
    private JButton exitButton;
    private JButton transferButton;
    private JButton withdrawButton;
    private JButton depositButton;
    private JTextField customerBalance;
    private JButton newAccount;

    ArrayList<Account> accounts = Utility.readFile("accounts.txt");
    Account currentAccount;

    private void initiateAccounts() {
        for (Account a: accounts) {
            accountDropdown.addItem(a);
        }
        currentAccount = accounts.get(0);
        setFields(accounts.get(0));
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public BankApp() {
        initiateAccounts();
        accountDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentAccount = (Account) accountDropdown.getSelectedItem();
                setFields(currentAccount);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountString = JOptionPane.showInputDialog(null, "Enter an amount to deposit: ", "Deposit", JOptionPane.PLAIN_MESSAGE);
                double amount = Double.parseDouble(amountString);
                currentAccount.deposit(amount);
                customerBalance.setText(String.valueOf(currentAccount.getBalance()));
                Utility.writeFile(accounts, "accounts.txt");
            }
        });
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountString = JOptionPane.showInputDialog(null, "Enter an amount to withdraw: ", "Withdrawal", JOptionPane.PLAIN_MESSAGE);
                double amount = Double.parseDouble(amountString);
                currentAccount.withdraw(amount);
                customerBalance.setText(String.valueOf(currentAccount.getBalance()));
                Utility.writeFile(accounts, "accounts.txt");
            }
        });
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String accountNumber = JOptionPane.showInputDialog(null, "Enter account to transfer to: ", "Transfer", JOptionPane.PLAIN_MESSAGE);
                String amountString = JOptionPane.showInputDialog(null, "Enter the amount to transfer to account " + accountNumber + ": ", "Withdrawal", JOptionPane.PLAIN_MESSAGE);
                double amount = Double.parseDouble(amountString);
                Account transferAccount = null;
                for (Account a: accounts) {
                    if (a.getAccNumber().equals(accountNumber)) {
                        transferAccount = a;
                        break;
                    }
                }

                if (transferAccount != null) {
                    currentAccount.transfer(transferAccount, amount);
                    customerBalance.setText(String.valueOf(currentAccount.getBalance()));
                    Utility.writeFile(accounts, "accounts.txt");
                }
            }
        });

        newAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerName = JOptionPane.showInputDialog(null, "Enter customer first and last name: ", "New Account", JOptionPane.PLAIN_MESSAGE);
                String today = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
                Account acc = new Account(currentAccount.nextAccountNumber(), customerName, today, 0);
                accounts.add(acc);
                accountDropdown.addItem(acc);
                accountDropdown.setSelectedItem(acc);      // Selects new account in dropdown
                Utility.writeFile(accounts, "accounts.txt");
            }
        });

        //TODO: Don't allow user to withdraw more money than in account
        //TODO: Make sure deposit and withdraw amounts are numbers
        //TODO: Validate that account to transfer to is valid
        //TODO: Handle cancel buttons
        //TODO: Delete an account
    }

    public void setFields(Account currentAccount) {
        customerName.setText(currentAccount.getAccHolder());
        openDate.setText(currentAccount.getOpenDate());
        customerBalance.setText(String.valueOf(currentAccount.getBalance()));
    }

}
