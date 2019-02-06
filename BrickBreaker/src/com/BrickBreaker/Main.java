package com.BrickBreaker;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        JFrame obj = new JFrame("Brick Breaker");
        Gameplay gameplay = new Gameplay();
        obj.setBounds(10, 10, 710, 600);
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gameplay);

    }
}
