package org.vrsoftware;

import org.vrsoftware.ui.MainMenuUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainMenuUI().setVisible(true);
        });
    }
}