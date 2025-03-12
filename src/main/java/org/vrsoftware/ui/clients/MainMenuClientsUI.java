package org.vrsoftware.ui.clients;

import org.vrsoftware.ui.utils.BackToMenu;

import javax.swing.*;
import java.awt.*;

public class MainMenuClientsUI extends JFrame {
    public MainMenuClientsUI() {
        setTitle("Menu dos Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(400, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnClients = new JButton("Criar Cliente");
        JButton btnClients2 = new JButton("Listar Clientes");
        JButton btnClients3 = new JButton("Atualizar Cliente");
        JButton btnClients4 = new JButton("Deletar Cliente");

        btnClients.addActionListener(e -> openCreateClientUI());
        btnClients2.addActionListener(e -> openListClientsUI());
        btnClients3.addActionListener(e -> openUpdateClientUI());
        btnClients4.addActionListener(e -> openDeleteClientUI());

        btnClients.setMargin(new Insets(5, 15, 5, 15));
        btnClients2.setMargin(new Insets(5, 15, 5, 15));
        btnClients3.setMargin(new Insets(5, 15, 5, 15));
        btnClients4.setMargin(new Insets(5, 15, 5, 15));

        gbc.weightx = 1.0;
        gbc.gridy = 0;
        formPanel.add(btnClients, gbc);

        gbc.gridy = 1;
        formPanel.add(btnClients2, gbc);

        gbc.gridy = 2;
        formPanel.add(btnClients3, gbc);

        gbc.gridy = 3;
        formPanel.add(btnClients4, gbc);

        BackToMenu backToMenu = new BackToMenu(this);
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(backToMenu, gbc);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.weighty = 1.0;
        mainPanel.add(formPanel, mainGbc);

        add(mainPanel);
        setVisible(true);
        setResizable(false);
    }

    private void openCreateClientUI() {
        CreateClientUI createClientUI = new CreateClientUI();
        createClientUI.setVisible(true);
        dispose();
    }

    private void openListClientsUI() {
        ListClientsUI listClientsUI = new ListClientsUI();
        listClientsUI.setVisible(true);
        dispose();
    }

    private void openUpdateClientUI() {
        UpdateClientUI updateClientUI = new UpdateClientUI();
        updateClientUI.setVisible(true);
        dispose();
    }

    private void openDeleteClientUI() {
        DeleteClientUI deleteClientUI = new DeleteClientUI();
        deleteClientUI.setVisible(true);
        dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuClientsUI menu = new MainMenuClientsUI();
            menu.setVisible(true);
        });
    }

}
