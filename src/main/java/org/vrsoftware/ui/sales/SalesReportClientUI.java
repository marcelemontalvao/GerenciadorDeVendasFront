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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.vrsoftware.ui.utils.BackToSalesMenu;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class SalesReportClientUI extends JFrame {
    private JTextField txtStartDate, txtEndDate;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JButton btnBuscar;

    public SalesReportClientUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Relatório de Vendas por Cliente", 800, 600);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new FlowLayout());
        JLabel lblStartDate = new JLabel("Data Inicial (YYYY-MM-DD):");
        txtStartDate = new JTextField(10);
        JLabel lblEndDate = new JLabel("Data Final (YYYY-MM-DD):");
        txtEndDate = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        BackToSalesMenu backToMenu = new BackToSalesMenu(this);

        formPanel.add(lblStartDate);
        formPanel.add(txtStartDate);
        formPanel.add(lblEndDate);
        formPanel.add(txtEndDate);
        formPanel.add(btnBuscar);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Limite de Compra", "Dia de Fechamento", "Total de Vendas"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(reportTable);

        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(backToMenu, BorderLayout.SOUTH);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchReport();
            }
        });

        showWindow(this);
    }

    private void searchReport() {
        String startDate = txtStartDate.getText().trim();
        String endDate = txtEndDate.getText().trim();

        try {
            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/vendas/agrupar/cliente?");
            urlBuilder.append("startDate=").append(startDate).append("&endDate=").append(endDate);

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
                JOptionPane.showMessageDialog(this, "Erro ao buscar relatório: " + responseCode, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(String jsonResponse) {
        tableModel.setRowCount(0);
        JSONObject reportData = new JSONObject(jsonResponse);

        Pattern pattern = Pattern.compile("id=(\\d+), nome=(\\w+), limiteCompra=([\\d.]+), diaFechamento=(\\d+)");

        for (String clientKey : reportData.keySet()) {
            Double totalSales = reportData.getDouble(clientKey);

            Matcher matcher = pattern.matcher(clientKey);

            if (matcher.find()) {
                String id = matcher.group(1);
                String nome = matcher.group(2);
                String limiteCompra = matcher.group(3);
                String diaFechamento = matcher.group(4);

                Vector<Object> row = new Vector<>();
                row.add(id);
                row.add(nome);
                row.add(limiteCompra);
                row.add(diaFechamento);
                row.add(totalSales);

                tableModel.addRow(row);
            } else {
                System.err.println("Não foi possível analisar a string do cliente: " + clientKey);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalesReportClientUI());
    }
}