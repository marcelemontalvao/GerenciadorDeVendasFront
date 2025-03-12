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

public class DeleteSalesItemUI extends JFrame {
    private JTextField txtVendaId, txtItemId;
    private JButton btnDeletarItem;

    public DeleteSalesItemUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Deletar Item da Venda", 800, 600);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        JLabel lblVendaId = new JLabel("ID da Venda:");
        txtVendaId = new JTextField(10);
        JLabel lblItemId = new JLabel("ID do Item:");
        txtItemId = new JTextField(10);

        formPanel.add(lblVendaId);
        formPanel.add(txtVendaId);
        formPanel.add(lblItemId);
        formPanel.add(txtItemId);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnDeletarItem = new JButton("Deletar Item");
        BackToSalesMenu backToSalesMenu = new BackToSalesMenu(this);

        buttonPanel.add(btnDeletarItem);
        buttonPanel.add(backToSalesMenu);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        btnDeletarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarItem();
            }
        });

        showWindow(this);
    }

    private void deletarItem() {
        try {
            int vendaId = Integer.parseInt(txtVendaId.getText().trim());
            int itemId = Integer.parseInt(txtItemId.getText().trim());

            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/vendas/");
            urlBuilder.append(vendaId).append("/itens/").append(itemId);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                JOptionPane.showMessageDialog(this, "Item deletado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao deletar item: " + responseCode, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar item: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteSalesItemUI());
    }
}
