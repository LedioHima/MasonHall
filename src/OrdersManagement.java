import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class OrdersManagement extends JFrame {
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private JTextField customerNameField, customerAddressField, totalAmountField, orderDateField, customerPhoneField;

    private String url = "jdbc:mysql://localhost:3306/MasonHall";
    private String user = "root";
    private String password = "";

    public OrdersManagement() {
        setTitle("Orders Management");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        JLabel nameLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField(15);
        JLabel addressLabel = new JLabel("Customer Address:");
        customerAddressField = new JTextField(15);
        JLabel totalAmountLabel = new JLabel("Total Amount:");
        totalAmountField = new JTextField(10);
        JLabel orderDateLabel = new JLabel("Order Date (YYYY-MM-DD):");
        orderDateField = new JTextField(10);
        JLabel customerPhoneLabel = new JLabel("Customer Phone:");
        customerPhoneField = new JTextField(10);

        JPanel inputPanel = new JPanel();
        inputPanel.add(nameLabel);
        inputPanel.add(customerNameField);
        inputPanel.add(addressLabel);
        inputPanel.add(customerAddressField);
        inputPanel.add(totalAmountLabel);
        inputPanel.add(totalAmountField);
        inputPanel.add(orderDateLabel);
        inputPanel.add(orderDateField);
        inputPanel.add(customerPhoneLabel);
        inputPanel.add(customerPhoneField);
        inputPanel.setBackground(new Color(241, 231, 202));


        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        buttonPanel.setBackground(new Color(241, 231, 202));

        ordersTableModel = new DefaultTableModel(new String[]{"Order ID", "Customer Name", "Customer Address", "Total Amount", "Order Date", "Customer Phone"}, 0);
        ordersTable = new JTable(ordersTableModel);
        loadOrders();

        JScrollPane tableScrollPane = new JScrollPane(ordersTable);


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrder();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOrder();
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

    private void loadOrders() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Orders")) {

            ordersTableModel.setRowCount(0);

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String customerName = rs.getString("customer_name");
                String customerAddress = rs.getString("customer_address");
                double totalAmount = rs.getDouble("total_amount");
                Date orderDate = rs.getDate("order_date");
                String customerPhone = rs.getString("customer_phone");
                ordersTableModel.addRow(new Object[]{orderId, customerName, customerAddress, totalAmount, orderDate, customerPhone});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
            return;
        }

        int orderId = (int) ordersTableModel.getValueAt(selectedRow, 0);

        String customerName = customerNameField.getText();
        String customerAddress = customerAddressField.getText();
        double totalAmount;
        try {
            totalAmount = Double.parseDouble(totalAmountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid total amount.");
            return;
        }

        String orderDate = orderDateField.getText();
        java.sql.Date sqlOrderDate;
        try {
            sqlOrderDate = java.sql.Date.valueOf(orderDate);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in the format YYYY-MM-DD.");
            return;
        }

        String customerPhone = customerPhoneField.getText();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE Orders SET customer_name = ?, customer_address = ?, total_amount = ?, order_date = ?, customer_phone = ? WHERE order_id = ?")) {

            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setDouble(3, totalAmount);
            ps.setDate(4, sqlOrderDate);
            ps.setString(5, customerPhone);
            ps.setInt(6, orderId);
            ps.executeUpdate();

            loadOrders();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrder() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.");
            return;
        }

        int orderId = (int) ordersTableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Orders WHERE order_id = ?")) {

            ps.setInt(1, orderId);
            ps.executeUpdate();

            loadOrders();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new OrdersManagement();
    }
}
