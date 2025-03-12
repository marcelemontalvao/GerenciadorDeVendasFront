package org.vrsoftware.ui.utils;

import org.vrsoftware.ui.products.MainMenuProductsUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackToProductsMenu extends JPanel {
    public BackToProductsMenu(JFrame currentFrame) {
        setLayout(new FlowLayout());

        JButton btnBack = new JButton("Voltar ao Menu");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuProductsUI menuUi = new MainMenuProductsUI();
                menuUi.setVisible(true);
                currentFrame.dispose();
            }
        });

        add(btnBack);
    }
}
