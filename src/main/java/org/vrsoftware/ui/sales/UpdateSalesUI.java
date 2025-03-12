package org.vrsoftware.ui.sales;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.vrsoftware.ui.utils.BackToSalesMenu;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class UpdateSalesUI extends JFrame {
    private JTextField txtVendaId, txtClientId, txtDataVenda;
    private JTable itemsTable;
    private DefaultTableModel tableModel;
    private JButton btnAtualizar, btnAdicionarItem, btnRemoverItem;

    public UpdateSalesUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Atualizar Venda", 800, 600);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        JLabel lblVendaId = new JLabel("ID da Venda:");
        txtVendaId = new JTextField(10);
        JLabel lblClientId = new JLabel("ID do Cliente:");
        txtClientId = new JTextField(10);
        JLabel lblDataVenda = new JLabel("Data da Venda (YYYY-MM-DD):");
        txtDataVenda = new JTextField(10);

        formPanel.add(lblVendaId);
        formPanel.add(txtVendaId);
        formPanel.add(lblClientId);
        formPanel.add(txtClientId);
        formPanel.add(lblDataVenda);
        formPanel.add(txtDataVenda);

        tableModel = new DefaultTableModel(new Object[]{"ID do Item", "ID do Produto", "Quantidade", "Preço Unitário"}, 0);
        itemsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(itemsTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAtualizar = new JButton("Atualizar");
        btnAdicionarItem = new JButton("Adicionar Item");
        btnRemoverItem = new JButton("Remover Item");
        BackToSalesMenu backToSalesMenu = new BackToSalesMenu(this);

        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnAdicionarItem);
        buttonPanel.add(btnRemoverItem);
        buttonPanel.add(backToSalesMenu);

        add(formPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarVenda();
            }
        });

        btnAdicionarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarItem();
            }
        });

        btnRemoverItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerItem();
            }
        });

        showWindow(this);
    }

    private void atualizarVenda() {
        try {
            int vendaId = Integer.parseInt(txtVendaId.getText().trim());
            int clientId = Integer.parseInt(txtClientId.getText().trim());
            String dataVenda = txtDataVenda.getText().trim();

            JSONObject payload = new JSONObject();
            payload.put("clientId", clientId);
            payload.put("dataVenda", dataVenda);

            JSONArray itensArray = new JSONArray();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                JSONObject item = new JSONObject();
                item.put("id", tableModel.getValueAt(i, 0));
                item.put("produtoId", tableModel.getValueAt(i, 1));
                item.put("quantidade", tableModel.getValueAt(i, 2));
                item.put("precoUnitario", tableModel.getValueAt(i, 3));
                itensArray.put(item);
            }
            payload.put("itens", itensArray);

            URL url = new URL("http://localhost:8080/vendas/" + vendaId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = payload.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "Venda atualizada com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar venda: " + responseCode, "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarItem() {
        tableModel.addRow(new Object[]{null, null, null, null});
    }

    private void removerItem() {
        int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateSalesUI());
    }
}