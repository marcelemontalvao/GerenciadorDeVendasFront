package org.vrsoftware.ui.sales;

import org.vrsoftware.ui.utils.BackToMenu;

import javax.swing.*;
import java.awt.*;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class MainMenuSalesUI extends JFrame {
    public MainMenuSalesUI() {
        configureWindow(this, "Menu das Vendas", 800, 600);
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
        JButton btnSales4 = new JButton("Atualizar a Quantidade de Um Item");
        JButton btnSales5 = new JButton("Deletar Venda");
        JButton btnSales6 = new JButton("Filtrar Vendas");
        JButton btnSales7 = new JButton("Visualizar Vendas Por Clientes");
        JButton btnSales8 = new JButton("Visualizar Vendas Por Produtos");
        JButton btnSales9 = new JButton("Listar Venda Por Id");
        JButton btnSales10 = new JButton("Excluir Produtos da Venda");

        btnSales.addActionListener(e -> openCreateSalesUi());
        btnSales2.addActionListener(e -> openListSalesUI());
        btnSales3.addActionListener(e -> openUpdateSalesUI());
        btnSales4.addActionListener(e -> openUpdateQuantityProductUI());
        btnSales5.addActionListener(e -> openDeleteSalesUI());
        btnSales6.addActionListener(e -> openFilterSalesUI());
        btnSales7.addActionListener(e -> openReportSalesClientUI());
        btnSales8.addActionListener(e -> openReportSalesProductUI());
        btnSales9.addActionListener(e -> openSalesDetailUI());
        btnSales10.addActionListener(e -> openDeleteSalesItemUI());

        btnSales.setMargin(new Insets(5, 15, 5, 15));
        btnSales2.setMargin(new Insets(5, 15, 5, 15));
        btnSales3.setMargin(new Insets(5, 15, 5, 15));
        btnSales4.setMargin(new Insets(5, 15, 5, 15));
        btnSales5.setMargin(new Insets(5, 15, 5, 15));
        btnSales6.setMargin(new Insets(5, 15, 5, 15));
        btnSales7.setMargin(new Insets(5, 15, 5, 15));
        btnSales8.setMargin(new Insets(5, 15, 5, 15));
        btnSales9.setMargin(new Insets(5, 15, 5, 15));
        btnSales10.setMargin(new Insets(5, 15, 5, 15));

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

        gbc.gridy = 7;
        formPanel.add(btnSales8, gbc);

        gbc.gridy = 8;
        formPanel.add(btnSales9, gbc);

        gbc.gridy = 9;
        formPanel.add(btnSales10, gbc);

        BackToMenu backToMenu = new BackToMenu(this);
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(backToMenu, gbc);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.weighty = 1.0;
        mainPanel.add(formPanel, mainGbc);

        add(mainPanel);
        showWindow(this);
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

    private void openUpdateQuantityProductUI() {
        UpdateQuantityProductSalesUI sale = new UpdateQuantityProductSalesUI();
        sale.setVisible(true);
        dispose();
    }

    private void openDeleteSalesUI() {
        DeleteSalesUI sales = new DeleteSalesUI();
        sales.setVisible(true);
        dispose();
    }

    private void openDeleteSalesItemUI() {
        DeleteSalesItemUI item = new DeleteSalesItemUI();
        item.setVisible(true);
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

    private void openSalesDetailUI() {
        SalesDetailUI sales = new SalesDetailUI();
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
