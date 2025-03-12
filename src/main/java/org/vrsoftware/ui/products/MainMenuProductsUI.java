package org.vrsoftware.ui.products;

import org.vrsoftware.ui.utils.BackToMenu;

import javax.swing.*;
import java.awt.*;

public class MainMenuProductsUI extends JFrame {
    public MainMenuProductsUI() {
        setTitle("Menu dos Produtos");
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

        JButton btnProducts = new JButton("Criar Produto");
        JButton btnProducts2 = new JButton("Listar Produtos");
        JButton btnProducts3 = new JButton("Atualizar Produto");
        JButton btnProducts4 = new JButton("Deletar Produto");

        btnProducts.addActionListener(e -> openCreateProductUi());
        btnProducts2.addActionListener(e -> openListProductsUI());
        btnProducts3.addActionListener(e -> openUpdateProductUI());
        btnProducts4.addActionListener(e -> openDeleteProductUI());

        btnProducts.setMargin(new Insets(5, 15, 5, 15));
        btnProducts2.setMargin(new Insets(5, 15, 5, 15));
        btnProducts3.setMargin(new Insets(5, 15, 5, 15));
        btnProducts4.setMargin(new Insets(5, 15, 5, 15));

        gbc.weightx = 1.0;
        gbc.gridy = 0;
        formPanel.add(btnProducts, gbc);

        gbc.gridy = 1;
        formPanel.add(btnProducts2, gbc);

        gbc.gridy = 2;
        formPanel.add(btnProducts3, gbc);

        gbc.gridy = 3;
        formPanel.add(btnProducts4, gbc);

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

    private void openCreateProductUi() {
        CreateProductUI productUi = new CreateProductUI();
        productUi.setVisible(true);
        dispose();
    }

    private void openListProductsUI() {
        ListProductsUI products = new ListProductsUI();
        products.setVisible(true);
        dispose();
    }

    private void openUpdateProductUI() {
        UpdateProductUI product = new UpdateProductUI();
        product.setVisible(true);
        dispose();
    }

    private void openDeleteProductUI() {
        DeleteProductUI product = new DeleteProductUI();
        product.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuProductsUI menu = new MainMenuProductsUI();
            menu.setVisible(true);
        });
    }

}
