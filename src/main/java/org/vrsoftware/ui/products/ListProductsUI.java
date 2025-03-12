package org.vrsoftware.ui.products;

import org.json.JSONArray;
import org.json.JSONObject;
import org.vrsoftware.ui.utils.BackToProductsMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class ListProductsUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ListProductsUI() {
        configureWindow(this, "Lista de Produtos", 800, 600);
        JPanel mainPanel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Descrição", "Preço(R$)"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        BackToProductsMenu backToMenu = new BackToProductsMenu(this);
        mainPanel.add(backToMenu, BorderLayout.SOUTH);

        add(mainPanel);
        showWindow(this);
        fetchClients();
    }

    private void fetchClients() {
        try {
            URL url = new URL("http://localhost:8080/produtos");
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
                        obj.getString("descricao"),
                        obj.getBigDecimal("preco")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ListProductsUI();
    }
}
