package org.vrsoftware.ui.clients;

import org.vrsoftware.ui.utils.BackToClientsMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateClientUI extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtLimiteCompra;
    private JTextField txtDiaFechamento;
    private JButton btnAtualizar;

    public UpdateClientUI() {
        initComponents();
    }

    private void initComponents() {

        setTitle("Atualizar Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        formPanel.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Limite Compra:"), gbc);
        txtLimiteCompra = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtLimiteCompra, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Dia Fechamento:"), gbc);
        txtDiaFechamento = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtDiaFechamento, gbc);

        btnAtualizar = new JButton("Atualizar Cliente");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(btnAtualizar, gbc);

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClient();
            }
        });

        BackToClientsMenu backToMenu = new BackToClientsMenu(this);
        gbc.gridy++;
        gbc.weightx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
        formPanel.add(backToMenu, gbc);

        mainPanel.add(formPanel, new GridBagConstraints());

        add(mainPanel);
        setVisible(true);
        setResizable(false);
    }

    private void updateClient() {
        String idStr = txtId.getText().trim();
        String nome = txtNome.getText().trim();
        String limiteCompraStr = txtLimiteCompra.getText().trim();
        String diaFechamentoStr = txtDiaFechamento.getText().trim();

        if (idStr.isEmpty() || nome.isEmpty() || limiteCompraStr.isEmpty() || diaFechamentoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            BigDecimal limiteCompra = BigDecimal.valueOf(Double.parseDouble(limiteCompraStr));
            int diaFechamento = Integer.parseInt(diaFechamentoStr);

            String jsonBody = String.format("{\"nome\": \"%s\", \"limite_compra\": \"%s\", \"dia_fechamento\": %d}",
                    nome, limiteCompra, diaFechamento);

            sendPut("http://localhost:8080/clientes/" + id, jsonBody);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, limite de compra e dia de fechamento devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente (código " + responseCode + "):\n" + response, "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!\nResposta: " + response, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            return response.toString();
        }
    }
}
