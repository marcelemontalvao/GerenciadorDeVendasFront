package org.vrsoftware.ui.products;

import org.vrsoftware.ui.utils.BackToClientsMenu;
import org.vrsoftware.ui.utils.BackToProductsMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class DeleteProductUI extends JFrame {

    private JTextField txtId;
    private JButton btnDeletar;

    public DeleteProductUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Deletar Produto", 800, 600);
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID do Produto:"), gbc);
        txtId = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);

        btnDeletar = new JButton("Deletar Produto");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(btnDeletar, gbc);

        btnDeletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        BackToProductsMenu backToMenu = new BackToProductsMenu(this);
        gbc.gridy++;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
        formPanel.add(backToMenu, gbc);

        mainPanel.add(formPanel, new GridBagConstraints());

        add(mainPanel);
        showWindow(this);
    }

    private void deleteProduct() {
        String idStr = txtId.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o campo de ID.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            String response = sendDelete("http://localhost:8080/produtos/" + id);
            JOptionPane.showMessageDialog(this, "Produto deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar produto:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String sendDelete(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        InputStream is = (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) ? con.getInputStream() : con.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
