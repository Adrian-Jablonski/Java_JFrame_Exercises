package com.Banking_Application;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
                String accountNumber = JOptionPane.showInputDialog(null, "Enter account to transfer to: ", "Withdrawal", JOptionPane.PLAIN_MESSAGE);
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
    }

    public void setFields(Account currentAccount) {
        customerName.setText(currentAccount.getAccHolder());
        openDate.setText(currentAccount.getOpenDate());
        customerBalance.setText(String.valueOf(currentAccount.getBalance()));
    }

}
