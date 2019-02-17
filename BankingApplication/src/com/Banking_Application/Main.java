package com.Banking_Application;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Bank App");
        frame.setContentPane(new BankApp().getPanel1());
        frame.setBounds(10, 10, 700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
