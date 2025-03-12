package org.vrsoftware.ui.sales;

import org.vrsoftware.ui.utils.BackToSalesMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class UpdateQuantityProductSalesUI extends JFrame {

    private JTextField txtVendaId, txtItemId, txtNewQuantity;
    private JButton btnAtualizarQuantidade;

    public UpdateQuantityProductSalesUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Atualizar Quantidade do Item", 800, 600);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        JLabel lblVendaId = new JLabel("ID da Venda:");
        txtVendaId = new JTextField(10);
        JLabel lblItemId = new JLabel("ID do Item:");
        txtItemId = new JTextField(10);
        JLabel lblNewQuantity = new JLabel("Nova Quantidade:");
        txtNewQuantity = new JTextField(10);

        formPanel.add(lblVendaId);
        formPanel.add(txtVendaId);
        formPanel.add(lblItemId);
        formPanel.add(txtItemId);
        formPanel.add(lblNewQuantity);
        formPanel.add(txtNewQuantity);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAtualizarQuantidade = new JButton("Atualizar Quantidade");
        BackToSalesMenu backToSalesMenu = new BackToSalesMenu(this);

        buttonPanel.add(btnAtualizarQuantidade);
        buttonPanel.add(backToSalesMenu);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAtualizarQuantidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarQuantidade();
            }
        });

        showWindow(this);
    }

    private void atualizarQuantidade() {
        try {
            int vendaId = Integer.parseInt(txtVendaId.getText().trim());
            int itemId = Integer.parseInt(txtItemId.getText().trim());
            int newQuantity = Integer.parseInt(txtNewQuantity.getText().trim());

            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/vendas/");
            urlBuilder.append(vendaId).append("/itens/").append(itemId).append("?newQuantity=").append(newQuantity);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Quantidade do item atualizada com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar quantidade do item: " + responseCode, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar quantidade do item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateQuantityProductSalesUI());
    }
}
