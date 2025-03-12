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

public class ListSalesUI extends JFrame {
    private JTable salesTable;
    private DefaultTableModel tableModel;

    public ListSalesUI() {
        setTitle("Lista de Vendas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID Cliente", "Data da Venda", "Itens", "Total"}, 0);
        salesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        BackToSalesMenu backToMenu = new BackToSalesMenu(this);
        mainPanel.add(backToMenu, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        fetchSales();
    }

//    private void fetchSales() {
//        try {
//            URL url = new URL("http://localhost:8080/vendas");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Accept", "application/json");
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {
//                response.append(line);
//            }
//            br.close();
//
//            JSONArray jsonArray = new JSONArray(response.toString());
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject obj = jsonArray.getJSONObject(i);
//                tableModel.addRow(new Object[]{
//                        obj.getInt("clientId"),
//                        obj.getString("dataVenda"),
//                        obj.getJSONArray("itens"),
//                        obj.getDouble("total")
//                });
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Erro ao buscar vendas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
//        }
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

        // Converte a resposta em JSONArray
        JSONArray jsonArray = new JSONArray(response.toString());

        // Limpa a tabela antes de adicionar novas linhas
        tableModel.setRowCount(0);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            // Monta uma string amigável para os itens
            JSONArray itensArray = obj.getJSONArray("itens");
            StringBuilder itensStr = new StringBuilder();
            for (int j = 0; j < itensArray.length(); j++) {
                JSONObject itemObj = itensArray.getJSONObject(j);

                int produtoId = itemObj.getInt("produtoId");
                int quantidade = itemObj.getInt("quantidade");
                double precoUnitario = itemObj.getDouble("precoUnitario");

                // Exemplo de formatação de cada item
                itensStr.append(String.format("Prod:%d, Qtd:%d, Preço:%.2f",
                        produtoId,
                        quantidade,
                        precoUnitario));

                if (j < itensArray.length() - 1) {
                    itensStr.append("; "); // Separador entre itens
                }
            }

            tableModel.addRow(new Object[]{
                    obj.getInt("clientId"),
                    obj.getString("dataVenda"),
                    itensStr.toString(), // String amigável em vez do JSONArray
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
