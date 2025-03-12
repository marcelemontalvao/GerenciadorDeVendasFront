package org.vrsoftware.ui.clients;

import org.vrsoftware.ui.utils.BackToClientsMenu;
import org.vrsoftware.ui.utils.FormUtils;

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

public class DeleteClientUI extends JFrame {

    private JTextField txtId;
    private JButton btnDelete;

    public DeleteClientUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Deletar Cliente",800, 600);
        JPanel mainPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        FormUtils.addLabelToForm(formPanel,"ID do Cliente:", gbc, 0, 0);
        FormUtils.addTextFieldToForm(formPanel, txtId, 20, gbc, 1);

        btnDelete = new JButton("Deletar Cliente");
        FormUtils.addButtonToForm(formPanel, btnDelete,gbc,0,1, 0);
        gbc.gridwidth = 2;

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteClient();
            }
        });

        BackToClientsMenu backToMenu = new BackToClientsMenu(this);
        gbc.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
        formPanel.add(backToMenu, gbc);
        FormUtils.addButtonToForm(formPanel, backToMenu, gbc, 0, 2, 1);

        mainPanel.add(formPanel, new GridBagConstraints());

        add(mainPanel);
        showWindow(this);
    }

    private void deleteClient() {
        String idStr = txtId.getText().trim();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o campo de ID.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            String response = sendDelete("http://localhost:8080/clientes/" + id);
            JOptionPane.showMessageDialog(this, "Cliente deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar cliente:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
