package org.vrsoftware.ui.products;

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

public class CreateProductUI extends JFrame {

    private JTextField txtDescricao;
    private JTextField bigDecimalPreco;
    private JButton btnCriar;

    public CreateProductUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Criar Produto", 800, 600);
        setLayout(new GridBagLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        txtDescricao = new JTextField(30);
        gbc.gridx = 1;
        formPanel.add(txtDescricao, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Preço:"), gbc);
        bigDecimalPreco = new JTextField(30);
        gbc.gridx = 1;
        formPanel.add(bigDecimalPreco, gbc);

        btnCriar = new JButton("Criar Produto");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btnCriar, gbc);

        btnCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createProduct();
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

    private void createProduct() {
        String description = txtDescricao.getText().trim();
        String price = bigDecimalPreco.getText().trim();

        if (description.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            BigDecimal priceDouble = BigDecimal.valueOf(Double.parseDouble(price));

            String jsonBody = String.format("{\"descricao\": \"%s\", \"preco\": %s}",
                    description,
                    priceDouble);

            String response = sendPost("http://localhost:8080/produtos", jsonBody);
            JOptionPane.showMessageDialog(this, "Produto criado com sucesso!\nResposta: " + response, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço deve ser numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar produto:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String sendPost(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
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
            return response.toString();
        }
    }
}