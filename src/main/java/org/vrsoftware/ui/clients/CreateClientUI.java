package org.vrsoftware.ui.clients;

import org.vrsoftware.ui.utils.BackToClientsMenu;
import org.vrsoftware.ui.utils.FormUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class CreateClientUI extends JFrame {

    private JTextField txtName = new JTextField(); ;
    private JTextField txtBuyLimit = new JTextField(); ;
    private JTextField txtClosureDay = new JTextField(); ;
    private JButton btnCreate;

    public CreateClientUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Criar Cliente", 800, 600);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;


        FormUtils.addLabelToForm(formPanel,"Nome", gbc,0,0);
        FormUtils.addTextFieldToForm(formPanel, txtName, 20,gbc, 1);

        FormUtils.addLabelToForm(formPanel,"Limite Compra", gbc,0,1);
        FormUtils.addTextFieldToForm(formPanel, txtBuyLimit, 20,gbc, 1);

        FormUtils.addLabelToForm(formPanel,"Dia Fechamento", gbc,0,2);
        FormUtils.addTextFieldToForm(formPanel, txtClosureDay, 20,gbc, 1);

        btnCreate = new JButton("Criar Cliente");
        gbc.gridwidth = 2;
        FormUtils.addButtonToForm(formPanel, btnCreate, gbc, 0, 3, 0);

        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createClient();
            }
        });

        BackToClientsMenu backToMenu = new BackToClientsMenu(this);
        gbc.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
        FormUtils.addButtonToForm(formPanel, backToMenu, gbc, 0, 4, 1);

        mainPanel.add(formPanel, new GridBagConstraints());

        add(mainPanel);
        showWindow(this);
    }

    private void createClient() {
        String nome = txtName.getText().trim();
        String limiteCompraStr = txtBuyLimit.getText().trim();
        String diaFechamentoStr = txtClosureDay.getText().trim();

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