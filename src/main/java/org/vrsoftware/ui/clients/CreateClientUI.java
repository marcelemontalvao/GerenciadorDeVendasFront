package org.vrsoftware.ui.clients;

import org.vrsoftware.ui.utils.BackToClientsMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateClientUI extends JFrame {

    private JTextField txtNome;
    private JTextField txtLimiteCompra;
    private JTextField txtDiaFechamento;
    private JButton btnCriar;

    public CreateClientUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Criar Cliente");
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
        formPanel.add(new JLabel("Nome:"), gbc);
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Limite Compra:"), gbc);
        txtLimiteCompra = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtLimiteCompra, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Dia Fechamento:"), gbc);
        txtDiaFechamento = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(txtDiaFechamento, gbc);

        btnCriar = new JButton("Criar Cliente");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(btnCriar, gbc);

        btnCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createClient();
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

    private void createClient() {
        String nome = txtNome.getText().trim();
        String limiteCompraStr = txtLimiteCompra.getText().trim();
        String diaFechamentoStr = txtDiaFechamento.getText().trim();

        if (nome.isEmpty() || limiteCompraStr.isEmpty() || diaFechamentoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double limiteCompra = Double.parseDouble(limiteCompraStr);
            int diaFechamento = Integer.parseInt(diaFechamentoStr);

            String jsonBody = String.format("{\"nome\": \"%s\", \"limite_compra\": %s, \"dia_fechamento\": %s}",
                    nome,
                    limiteCompra,
                    diaFechamento);

            String response = sendPost("http://localhost:8080/clientes", jsonBody);
            JOptionPane.showMessageDialog(this, "Cliente criado com sucesso!\nResposta: " + response, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Limite de compra e dia de fechamento devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar cliente:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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