package org.vrsoftware.ui.utils;

import org.vrsoftware.ui.sales.MainMenuSalesUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackToSalesMenu extends JPanel {
    public BackToSalesMenu(JFrame currentFrame) {
        setLayout(new FlowLayout());

        JButton btnBack = new JButton("Voltar ao Menu");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuSalesUI menuUi = new MainMenuSalesUI();
                menuUi.setVisible(true);
                currentFrame.dispose();
            }
        });

        add(btnBack);
    }
}
