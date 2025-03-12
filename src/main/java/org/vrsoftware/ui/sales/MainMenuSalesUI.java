package org.vrsoftware.ui.sales;

import org.vrsoftware.ui.utils.BackToMenu;

import javax.swing.*;
import java.awt.*;

public class MainMenuSalesUI extends JFrame {
    public MainMenuSalesUI() {
        setTitle("Menu das Vendas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(300, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnSales = new JButton("Criar Venda");
        JButton btnSales2 = new JButton("Listar Vendas");
        JButton btnSales3 = new JButton("Atualizar Venda");
        JButton btnSales4 = new JButton("Deletar Venda");
        JButton btnSales5 = new JButton("Filtrar Vendas");
        JButton btnSales6 = new JButton("Visualizar Vendas Por Clientes");
        JButton btnSales7 = new JButton("Visualizar Vendas Por Produtos");

        btnSales.addActionListener(e -> openCreateSalesUi());
        btnSales2.addActionListener(e -> openListSalesUI());
        btnSales3.addActionListener(e -> openUpdateSalesUI());
        btnSales4.addActionListener(e -> openDeleteSalesUI());
        btnSales5.addActionListener(e -> openFilterSalesUI());
        btnSales6.addActionListener(e -> openReportSalesClientUI());
        btnSales7.addActionListener(e -> openReportSalesProductUI());

        btnSales.setMargin(new Insets(5, 15, 5, 15));
        btnSales2.setMargin(new Insets(5, 15, 5, 15));
        btnSales3.setMargin(new Insets(5, 15, 5, 15));
        btnSales4.setMargin(new Insets(5, 15, 5, 15));
        btnSales5.setMargin(new Insets(5, 15, 5, 15));
        btnSales6.setMargin(new Insets(5, 15, 5, 15));
        btnSales7.setMargin(new Insets(5, 15, 5, 15));

        gbc.weightx = 1.0;
        gbc.gridy = 0;
        formPanel.add(btnSales, gbc);

        gbc.gridy = 1;
        formPanel.add(btnSales2, gbc);

        gbc.gridy = 2;
        formPanel.add(btnSales3, gbc);

        gbc.gridy = 3;
        formPanel.add(btnSales4, gbc);

        gbc.gridy = 4;
        formPanel.add(btnSales5, gbc);

        gbc.gridy = 5;
        formPanel.add(btnSales6, gbc);

        gbc.gridy = 6;
        formPanel.add(btnSales7, gbc);

        BackToMenu backToMenu = new BackToMenu(this);
        gbc.gridy = 7;
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

    private void openCreateSalesUi() {
        CreateSalesUI salesUi = new CreateSalesUI();
        salesUi.setVisible(true);
        dispose();
    }

    private void openListSalesUI() {
        ListSalesUI saless = new ListSalesUI();
        saless.setVisible(true);
        dispose();
    }

    private void openUpdateSalesUI() {
        UpdateSalesUI sales = new UpdateSalesUI();
        sales.setVisible(true);
        dispose();
    }

    private void openDeleteSalesUI() {
        DeleteSalesUI sales = new DeleteSalesUI();
        sales.setVisible(true);
        dispose();
    }

    private void openFilterSalesUI() {
        FilterSalesUI sales = new FilterSalesUI();
        sales.setVisible(true);
        dispose();
    }

    private void openReportSalesClientUI() {
        SalesReportClientUI sales = new SalesReportClientUI();
        sales.setVisible(true);
        dispose();
    }

    private void openReportSalesProductUI() {
        SalesReportProductUI sales = new SalesReportProductUI();
        sales.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuSalesUI menu = new MainMenuSalesUI();
            menu.setVisible(true);
        });
    }
}
