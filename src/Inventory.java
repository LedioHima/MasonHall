import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Inventory extends JFrame {
    private JTextField productNameField, priceField, quantityField;
    private JTable productTable;
    private DefaultTableModel tableModel;

    private String url = "jdbc:mysql://localhost:3306/MasonHall";
    private String user = "root";
    private String password = "";

    public Inventory() {
        setTitle("Inventory Management");
        setSize(800, 600);

        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Product Name:");
        productNameField = new JTextField(15);
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField(10);
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField(5);

        JPanel inputPanel = new JPanel();
        inputPanel.add(nameLabel);
        inputPanel.add(productNameField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.setBackground(new Color(241, 231, 202));

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.setBackground(new Color(241, 231, 202));

        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Price", "Quantity"}, 0);
        productTable = new JTable(tableModel);
        loadProducts();

        JScrollPane tableScrollPane = new JScrollPane(productTable);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProduct();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadProducts() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Inventory")) {

            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                tableModel.addRow(new Object[]{id, name, price, quantity});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addProduct() {
        String name = productNameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Inventory (name, price, quantity) VALUES (?, ?, ?)")) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.executeUpdate();

            loadProducts();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = productNameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE Inventory SET name = ?, price = ?, quantity = ? WHERE product_id = ?")) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setInt(4, id);
            ps.executeUpdate();

            loadProducts();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Inventory WHERE product_id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            loadProducts();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Inventory();
    }
}
