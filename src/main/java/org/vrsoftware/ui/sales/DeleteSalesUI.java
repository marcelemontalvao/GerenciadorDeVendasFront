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

public class DeleteSalesUI extends JFrame {
    private JTextField txtVendaId;
    private JButton btnDeletarVenda;

    public DeleteSalesUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Deletar Venda", 800, 600);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new FlowLayout());
        JLabel lblVendaId = new JLabel("ID da Venda:");
        txtVendaId = new JTextField(10);

        formPanel.add(lblVendaId);
        formPanel.add(txtVendaId);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnDeletarVenda = new JButton("Deletar Venda");
        BackToSalesMenu backToSalesMenu = new BackToSalesMenu(this);

        buttonPanel.add(btnDeletarVenda);
        buttonPanel.add(backToSalesMenu);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        btnDeletarVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarVenda();
            }
        });

        showWindow(this);
    }

    private void deletarVenda() {
        try {
            int vendaId = Integer.parseInt(txtVendaId.getText().trim());

            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/vendas/");
            urlBuilder.append(vendaId);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                JOptionPane.showMessageDialog(this, "Venda deletada com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao deletar venda: " + responseCode, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteSalesUI());
    }
}