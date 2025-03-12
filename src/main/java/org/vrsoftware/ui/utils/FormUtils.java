package org.vrsoftware.ui.utils;

import javax.swing.*;
import java.awt.*;

public class FormUtils {


    public static void addLabelToForm(JPanel formPanel, String title, GridBagConstraints gbc, int gridX, int gridY) {
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        JLabel label =new JLabel(title);
        formPanel.add(label, gbc);
    }

    public static void addTextFieldToForm(JPanel formPanel, JTextField textField, Integer columns, GridBagConstraints gbc, int gridX) {
        textField = new JTextField(columns);
        gbc.gridx = gridX;
        formPanel.add(textField, gbc);
    }

    public static void addButtonToForm(JPanel formPanel, JComponent button, GridBagConstraints gbc, int gridX, int gridY, int weightX) {
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.insets = new Insets(10, 0, 0, 0);
        formPanel.add(button, gbc);
    }

}
