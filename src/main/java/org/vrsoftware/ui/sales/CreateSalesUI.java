package org.vrsoftware.ui.sales;

import org.vrsoftware.ui.utils.BackToSalesMenu;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;

import static org.vrsoftware.ui.utils.WindowUtils.configureWindow;
import static org.vrsoftware.ui.utils.WindowUtils.showWindow;

public class CreateSalesUI extends JFrame {

    private JTextField txtClientId;
    private JTextField txtDataVenda;
    private DefaultTableModel tableModel;
    private JTable itensTable;
    private List<Map<String, Object>> itens;
    private JButton btnCriarVenda;
    private JButton btnAddItem;

    public CreateSalesUI() {
        initComponents();
    }

    private void initComponents() {
        configureWindow(this, "Criar Venda", 800, 600);
        setLayout(new GridBagLayout());

        itens = new ArrayList<>();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(10, 10, 10, 10);
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.anchor = GridBagConstraints.CENTER;

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Client ID:"), gbc);
        gbc.gridx = 1;
        txtClientId = new JTextField(20);
        formPanel.add(txtClientId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Data Venda (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtDataVenda = new JTextField(20);
        formPanel.add(txtDataVenda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Itens:"), gbc);

        tableModel = new DefaultTableModel(new Object[]{"Produto ID", "Quantidade", "Preço Unitário"}, 0);
        itensTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(itensTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 150));
        gbc.gridy = 3;
        formPanel.add(tableScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        btnAddItem = new JButton("Adicionar Item");
        formPanel.add(btnAddItem, gbc);
        btnAddItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        gbc.gridx = 1;
        btnCriarVenda = new JButton("Criar Venda");
        formPanel.add(btnCriarVenda, gbc);
        btnCriarVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSale();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        BackToSalesMenu backToMenu = new BackToSalesMenu(this);
        formPanel.add(backToMenu, gbc);

        mainPanel.add(formPanel, gbcMain);
        add(mainPanel);
        showWindow(this);
    }

    private void addItem() {
        JTextField txtProdutoId = new JTextField(5);
        JTextField txtQuantidade = new JTextField(5);
        JTextField txtPrecoUnitario = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Produto ID:"));
        panel.add(txtProdutoId);
        panel.add(new JLabel("Quantidade:"));
        panel.add(txtQuantidade);
        panel.add(new JLabel("Preço Unitário:"));
        panel.add(txtPrecoUnitario);

        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int produtoId = Integer.parseInt(txtProdutoId.getText().trim());
                int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
                BigDecimal precoUnitario = BigDecimal.valueOf(Double.parseDouble(txtPrecoUnitario.getText().trim()));

                Map<String, Object> item = new HashMap<>();
                item.put("produtoId", produtoId);
                item.put("quantidade", quantidade);
                item.put("precoUnitario", precoUnitario);
                itens.add(item);

                tableModel.addRow(new Object[]{produtoId, quantidade, precoUnitario});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valores inválidos para o item.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createSale() {
        String clientIdStr = txtClientId.getText().trim();
        String dataVenda = txtDataVenda.getText().trim();

        if (clientIdStr.isEmpty() || dataVenda.isEmpty() || itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos e adicione pelo menos um item.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int clientId = Integer.parseInt(clientIdStr);

            StringBuilder itensJson = new StringBuilder();
            itensJson.append("[");
            for (int i = 0; i < itens.size(); i++) {
                Map<String, Object> item = itens.get(i);
                itensJson.append(String.format("{\"produtoId\": %s, \"quantidade\": %s, \"precoUnitario\": %s}",
                        item.get("produtoId"),
                        item.get("quantidade"),
                        item.get("precoUnitario")));
                if (i < itens.size() - 1) {
                    itensJson.append(", ");
                }
            }
            itensJson.append("]");

            String jsonBody = String.format("{\"clientId\": %s, \"dataVenda\": \"%s\", \"itens\": %s}",
                    clientId,
                    dataVenda,
                    itensJson.toString());

            String response = sendPost("http://localhost:8080/vendas", jsonBody);
            JOptionPane.showMessageDialog(this, "Venda criada com sucesso!\nResposta: " + response, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Client ID deve ser numérico.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao criar venda:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String sendPost(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
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
            return response.toString();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreateSalesUI::new);
    }
}
