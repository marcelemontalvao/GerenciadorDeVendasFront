package org.vrsoftware.ui.sales;

import org.json.JSONArray;
import org.json.JSONObject;
import org.vrsoftware.ui.utils.BackToSalesMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class ListSalesUI extends JFrame {
    private JTable salesTable;
    private DefaultTableModel tableModel;

    public ListSalesUI() {
        configureWindow(this, "Lista de Vendas", 800, 600);
        JPanel mainPanel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "ID Cliente", "Data da Venda", "Itens", "Total"}, 0);
        salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        BackToSalesMenu backToMenu = new BackToSalesMenu(this);
        mainPanel.add(backToMenu, BorderLayout.SOUTH);

        add(mainPanel);
        showWindow(this);
        fetchSales();
    }

    private void fetchSales() {
        try {
            URL url = new URL("http://localhost:8080/vendas");
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

            tableModel.setRowCount(0);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                JSONArray itensArray = obj.getJSONArray("itens");
                StringBuilder itensStr = new StringBuilder();
                for (int j = 0; j < itensArray.length(); j++) {
                    JSONObject itemObj = itensArray.getJSONObject(j);

                    int productId = itemObj.getInt("produtoId");
                    int quantity = itemObj.getInt("quantidade");
                    double unityPrice = itemObj.getDouble("precoUnitario");

                    itensStr.append(String.format("Prod:%d, Qtd:%d, Preço:%.2f",
                            productId,
                            quantity,
                            unityPrice));

                    if (j < itensArray.length() - 1) {
                        itensStr.append("; ");
                    }
                }

                tableModel.addRow(new Object[]{
                        obj.getInt("id"),
                        obj.getInt("clientId"),
                        obj.getString("dataVenda"),
                        itensStr.toString(),
                        obj.getDouble("total")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar vendas: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ListSalesUI();
    }
}
