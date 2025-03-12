package org.vrsoftware.ui.utils;

import org.vrsoftware.ui.clients.MainMenuClientsUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackToClientsMenu extends JPanel {
    public BackToClientsMenu(JFrame currentFrame) {
        setLayout(new FlowLayout());

        JButton btnBack = new JButton("Voltar ao Menu");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuClientsUI menuUi = new MainMenuClientsUI();
                menuUi.setVisible(true);
                currentFrame.dispose();
            }
        });

        add(btnBack);
    }
}
