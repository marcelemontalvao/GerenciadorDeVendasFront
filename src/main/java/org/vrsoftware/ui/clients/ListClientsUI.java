package org.vrsoftware.ui.clients;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.vrsoftware.ui.utils.BackToClientsMenu;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListClientsUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public ListClientsUI() {
        setTitle("Lista de Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Limite de Compra", "Dia Fechamento"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        BackToClientsMenu backToMenu = new BackToClientsMenu(this);
        mainPanel.add(backToMenu, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        fetchClients();
    }

    private void fetchClients() {
        try {
            URL url = new URL("http://localhost:8080/clientes");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                tableModel.addRow(new Object[]{
                        obj.getInt("id"),
                        obj.getString("nome"),
                        obj.getDouble("limiteCompra"),
                        obj.getInt("diaFechamento")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ListClientsUI();
    }
}
