package org.vrsoftware.ui.sales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.vrsoftware.ui.utils.BackToSalesMenu;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class FilterSalesUI extends JFrame {
    private JTextField txtClientId, txtStartDate, txtEndDate, txtProdutoId;
    private JTable salesTable;
    private DefaultTableModel tableModel;
    private JButton btnFiltrar;

    public FilterSalesUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Filtrar Vendas", 800, 600);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(5, 5, 5, 5);
        gbcMain.gridy = 0;
        gbcMain.gridx = 0;
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weightx = 1;
        gbcMain.weighty = 1;

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS)); // Layout vertical

        JPanel clientIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel clientIdLabel = new JLabel("Client ID:");
        txtClientId = new JTextField(30);
        clientIdPanel.add(clientIdLabel);
        clientIdPanel.add(txtClientId);
        formPanel.add(clientIdPanel);

        JPanel productIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel productIdLabel = new JLabel("Produto ID:");
        txtProdutoId = new JTextField(30);
        productIdPanel.add(productIdLabel);
        productIdPanel.add(txtProdutoId);
        formPanel.add(productIdPanel);

        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel startDateLabel = new JLabel("Data Início (YYYY-MM-DD):");
        txtStartDate = new JTextField(30);
        startDatePanel.add(startDateLabel);
        startDatePanel.add(txtStartDate);
        formPanel.add(startDatePanel);

        JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel endDateLabel = new JLabel("Data Fim (YYYY-MM-DD):");
        txtEndDate = new JTextField(30);
        endDatePanel.add(endDateLabel);
        endDatePanel.add(txtEndDate);
        formPanel.add(endDatePanel);

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setAlignmentX(Component.CENTER_ALIGNMENT); // Centralizar o botão
        formPanel.add(btnFiltrar);
        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSales();
            }
        });

        gbcMain.gridx = 0;
        gbcMain.gridy = 5;
        gbcMain.gridwidth = 2;
        gbcMain.anchor = GridBagConstraints.CENTER;
        BackToSalesMenu backToMenu = new BackToSalesMenu(this);
        formPanel.add(backToMenu, gbcMain);

        formPanel.add(Box.createVerticalGlue());

        tableModel = new DefaultTableModel(new Object[]{"Cliente ID", "Itens", "Data Venda", "Total"}, 0);
        salesTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(salesTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 300)); // Ajuste este tamanho
        formPanel.add(tableScrollPane);

        mainPanel.add(formPanel, gbcMain);
        add(mainPanel);
        showWindow(this);
    }

    private void filterSales() {
        try {
            String clientId = txtClientId.getText().trim();
            String productId = txtProdutoId.getText().trim();
            String startDate = txtStartDate.getText().trim();
            String endDate = txtEndDate.getText().trim();

            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/vendas/filter?");
            if (!startDate.isEmpty()) urlBuilder.append("startDate=").append(startDate).append("&");
            if (!endDate.isEmpty()) urlBuilder.append("endDate=").append(endDate).append("&");
            if (!productId.isEmpty()) urlBuilder.append("produtoId=").append(productId).append("&");
            if (!clientId.isEmpty()) urlBuilder.append("clientId=").append(clientId);

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                updateTable(response.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao buscar vendas.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao filtrar vendas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(String jsonResponse) {
        tableModel.setRowCount(0);
        JSONArray salesArray = new JSONArray(jsonResponse);
        for (int i = 0; i < salesArray.length(); i++) {
            JSONObject sale = salesArray.getJSONObject(i);
            Vector<Object> row = new Vector<>();

            row.add(sale.getInt("clientId"));
            row.add(sale.getJSONArray("itens"));
            row.add(sale.getString("dataVenda"));
            row.add(sale.getDouble("total"));
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FilterSalesUI());
    }
}
