package org.vrsoftware.ui.products;

import org.vrsoftware.ui.utils.BackToClientsMenu;
import org.vrsoftware.ui.utils.BackToProductsMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class UpdateProductUI extends JFrame {

    private JTextField txtId;
    private JTextField txtDescricao;
    private JTextField bigDecimalPreco;
    private JButton btnAtualizar;

    public UpdateProductUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Atualizar Produto", 800, 600);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        txtId = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Descrição:"), gbc);
        txtDescricao = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtDescricao, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Preço:"), gbc);
        bigDecimalPreco = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(bigDecimalPreco, gbc);

        btnAtualizar = new JButton("Atualizar Produto");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(btnAtualizar, gbc);

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
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

    private void updateProduct() {
        String idStr = txtId.getText().trim();
        String description = txtDescricao.getText().trim();
        String price = bigDecimalPreco.getText().trim();

        if (idStr.isEmpty() || description.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            BigDecimal priceBd = BigDecimal.valueOf(Double.parseDouble(price));

            String jsonBody = String.format("{\"descricao\": \"%s\", \"preco\": \"%s\"}",
                    description, priceBd);

            sendPut("http://localhost:8080/produtos/" + id, jsonBody);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID e preço devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar produto:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String sendPut(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        InputStream is = (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) ? con.getInputStream() : con.getErrorStream();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar produto (código " + responseCode + "):\n" + response, "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!\nResposta: " + response, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            return response.toString();
        }
    }
}
