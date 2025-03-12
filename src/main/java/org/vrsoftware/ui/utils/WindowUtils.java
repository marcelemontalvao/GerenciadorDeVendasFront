package org.vrsoftware.ui.utils;

import javax.swing.*;

public class WindowUtils {

    public static void configureWindow(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ou JFrame.DISPOSE_ON_CLOSE
        frame.setLocationRelativeTo(null);
    }

    public static void showWindow(JFrame frame) {
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] args) {

    }
}