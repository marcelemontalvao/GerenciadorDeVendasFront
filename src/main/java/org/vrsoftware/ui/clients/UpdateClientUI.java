package org.vrsoftware.ui.clients;

import org.vrsoftware.ui.utils.BackToClientsMenu;
import org.vrsoftware.ui.utils.FormUtils;

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

public class UpdateClientUI extends JFrame {

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtBuyLimit;
    private JTextField txtClosureDay;
    private JButton btnUpdate;

    public UpdateClientUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this,"Atualizar Cliente", 800, 600);
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        FormUtils.addLabelToForm(formPanel,"ID:", gbc, 0, 0);
        FormUtils.addTextFieldToForm(formPanel, txtId, 20, gbc, 1);

        FormUtils.addLabelToForm(formPanel,"Nome:", gbc, 0, 1);
        FormUtils.addTextFieldToForm(formPanel, txtName, 20, gbc, 1);

        FormUtils.addLabelToForm(formPanel,"Limite Compra:", gbc, 0, 2);
        FormUtils.addTextFieldToForm(formPanel, txtBuyLimit, 20, gbc, 1);

        FormUtils.addLabelToForm(formPanel,"Dia Fechamento:", gbc, 0, 3);
        FormUtils.addTextFieldToForm(formPanel, txtClosureDay, 20, gbc, 1);

        btnUpdate = new JButton("Atualizar Cliente");
        gbc.gridwidth = 2;
        FormUtils.addButtonToForm(formPanel,btnUpdate,gbc,0,4,0);

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClient();
            }
        });

        BackToClientsMenu backToMenu = new BackToClientsMenu(this);
        gbc.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
        FormUtils.addButtonToForm(formPanel,backToMenu,gbc,1,5,1);

        mainPanel.add(formPanel, new GridBagConstraints());

        add(mainPanel);
        showWindow(this);
    }

    private void updateClient() {
        String idStr = txtId.getText().trim();
        String name = txtName.getText().trim();
        String buyLimitStr = txtBuyLimit.getText().trim();
        String closureDayStr = txtClosureDay.getText().trim();

        if (idStr.isEmpty() || name.isEmpty() || buyLimitStr.isEmpty() || closureDayStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            BigDecimal buyLimit = BigDecimal.valueOf(Double.parseDouble(buyLimitStr));
            int closureDay = Integer.parseInt(closureDayStr);

            String jsonBody = String.format("{\"nome\": \"%s\", \"limite_compra\": \"%s\", \"dia_fechamento\": %d}",
                    name, buyLimit, closureDay);

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
