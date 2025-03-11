package org.vrsoftware.ui.utils;

import org.vrsoftware.ui.MainMenuUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackToMenu extends JPanel {
    public BackToMenu(JFrame currentFrame) {
        setLayout(new FlowLayout());

        JButton btnVoltar = new JButton("Voltar ao Menu Principal");
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuUI menuUi = new MainMenuUI();
                menuUi.setVisible(true);

                currentFrame.dispose();
            }
        });

        add(btnVoltar);
    }
}
