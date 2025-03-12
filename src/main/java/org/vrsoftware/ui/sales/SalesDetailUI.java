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

public class SalesDetailUI extends JFrame {
    private JLabel lblId, lblClientId, lblDataVenda, lblTotal;
    private JTable itemsTable;
    private DefaultTableModel tableModel;
    private JTextField txtVendaId;
    private JButton btnBuscar;

    public SalesDetailUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Detalhes da Venda", 800, 600);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new FlowLayout());
        JLabel lblVendaId = new JLabel("ID da Venda:");
        txtVendaId = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        BackToSalesMenu backToMenu = new BackToSalesMenu(this);

        formPanel.add(lblVendaId);
        formPanel.add(txtVendaId);
        formPanel.add(btnBuscar);
        formPanel.add(backToMenu);

        JPanel headerPanel = new JPanel(new GridLayout(4, 2));
        lblId = new JLabel("ID: ");
        lblClientId = new JLabel("ID do Cliente: ");
        lblDataVenda = new JLabel("Data da Venda: ");
        lblTotal = new JLabel("Total: ");

        headerPanel.add(lblId);
        headerPanel.add(new JLabel());
        headerPanel.add(lblClientId);
        headerPanel.add(new JLabel());
        headerPanel.add(lblDataVenda);
        headerPanel.add(new JLabel());
        headerPanel.add(lblTotal);
        headerPanel.add(new JLabel());

        tableModel = new DefaultTableModel(new Object[]{"ID do Produto", "Quantidade", "Preço Unitário"}, 0);
        itemsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(itemsTable);

        add(formPanel, BorderLayout.NORTH);
        add(headerPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int vendaId = Integer.parseInt(txtVendaId.getText().trim());
                    buscarDetalhesDaVenda(vendaId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SalesDetailUI.this, "ID da Venda inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        showWindow(this);
    }

    private void buscarDetalhesDaVenda(int vendaId) {
        try {
            URL url = new URL("http://localhost:8080/vendas/" + vendaId);
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
                atualizarDetalhes(response.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao buscar detalhes da venda: " + responseCode, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar detalhes da venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarDetalhes(String jsonResponse) {
        try {
            JSONObject sale = new JSONObject(jsonResponse);

            lblId.setText("ID: " + sale.getInt("id"));
            lblClientId.setText("ID do Cliente: " + sale.getInt("clientId"));
            lblDataVenda.setText("Data da Venda: " + sale.getString("dataVenda"));
            lblTotal.setText("Total: " + sale.getDouble("total"));

            tableModel.setRowCount(0);
            JSONArray itensArray = sale.getJSONArray("itens");
            for (int i = 0; i < itensArray.length(); i++) {
                JSONObject item = itensArray.getJSONObject(i);
                Vector<Object> row = new Vector<>();
                row.add(item.getInt("produtoId"));
                row.add(item.getInt("quantidade"));
                row.add(item.getDouble("precoUnitario"));
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar detalhes da venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalesDetailUI());
    }
}