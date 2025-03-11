package org.vrsoftware.ui;

import org.vrsoftware.ui.clients.MainMenuClientsUI;
import org.vrsoftware.ui.products.MainMenuProductsUI;

import javax.swing.*;
import java.awt.*;

public class MainMenuUI extends JFrame {
    public MainMenuUI() {
        setTitle("Menu Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(300, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnClients = new JButton("Gerenciar Clientes");
        btnClients.addActionListener(e -> openMainMenuClientsUi());
        btnClients.setMargin(new Insets(5, 15, 5, 15));
        gbc.weightx = 1.0;
        gbc.gridy = 0;
        formPanel.add(btnClients, gbc);

        JButton btnProducts = new JButton("Gerenciar Produtos");
        btnProducts.addActionListener(e -> openMainMenuProductsUi());
        btnProducts.setMargin(new Insets(5, 15, 5, 15));
        gbc.weightx = 1.0;
        gbc.gridy = 1;
        formPanel.add(btnProducts, gbc);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.weighty = 1.0;
        mainPanel.add(formPanel, mainGbc);

        add(mainPanel);
        setVisible(true);
        setResizable(false);
    }

    private void openMainMenuClientsUi() {
        MainMenuClientsUI clientsUI = new MainMenuClientsUI();
        clientsUI.setVisible(true);
        dispose();
    }

    private void openMainMenuProductsUi() {
        MainMenuProductsUI productsUI = new MainMenuProductsUI();
        productsUI.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuUI menu = new MainMenuUI();
            menu.setVisible(true);
        });
    }

}
