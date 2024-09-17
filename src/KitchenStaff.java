import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class KitchenStaff extends JFrame {
    private JTable ordersTable, orderDetailsTable;
    private DefaultTableModel ordersTableModel, orderDetailsTableModel;
    private JButton updateStatusButton, backButton;

    private String url = "jdbc:mysql://localhost:3306/MasonHall";
    private String user = "root";
    private String password = "";

    public KitchenStaff() {
        setTitle("Orders");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(241, 231, 202));

        ordersTableModel = new DefaultTableModel(new String[]{
                "Order ID", "Customer Name", "Customer Address",
                "Total Amount", "Order Date", "Customer Phone",
                "Order Status"}, 0);
        ordersTable = new JTable(ordersTableModel);
        ordersTable.setBackground(new Color(244, 226, 174));
        ordersTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane ordersScrollPane = new JScrollPane(ordersTable);
        ordersScrollPane.getViewport().setBackground(new Color(241, 231, 202));

        orderDetailsTableModel = new DefaultTableModel(new String[]{
                "Detail ID", "Dish Name", "Quantity", "Price"}, 0);
        orderDetailsTable = new JTable(orderDetailsTableModel);
        orderDetailsTable.setBackground(new Color(244, 226, 174));
        orderDetailsTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane orderDetailsScrollPane = new JScrollPane(orderDetailsTable);
        orderDetailsScrollPane.getViewport().setBackground(new Color(241, 231, 202));

        loadOrders();

        updateStatusButton = new JButton("Update Order Status");
        updateStatusButton.setBackground(new Color(255, 255, 255));
        updateStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOrderStatus();
            }
        });

        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (int) ordersTableModel.getValueAt(selectedRow, 0);
                    loadOrderDetails(orderId);
                }
            }
        });

        backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 255, 255));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffLogin();
            }
        });

        setLayout(new BorderLayout());
        add(ordersScrollPane, BorderLayout.NORTH);
        add(orderDetailsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateStatusButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadOrders() {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Orders WHERE DATE(order_date) = CURDATE()")) {

            ordersTableModel.setRowCount(0);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String customerName = rs.getString("customer_name");
                String customerAddress = rs.getString("customer_address");
                double totalAmount = rs.getDouble("total_amount");
                Date orderDate = rs.getDate("order_date");
                String customerPhone = rs.getString("customer_phone");
                String orderStatus = rs.getString("order_status");
                ordersTableModel.addRow(new Object[]{orderId, customerName, customerAddress, totalAmount, orderDate, customerPhone, orderStatus});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadOrderDetails(int orderId) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM OrderDetails WHERE order_id = ?")) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            orderDetailsTableModel.setRowCount(0);

            while (rs.next()) {
                int detailId = rs.getInt("detail_id");
                String dishName = rs.getString("dish_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                orderDetailsTableModel.addRow(new Object[]{detailId, dishName, quantity, price});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderStatus() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.");
            return;
        }

        int orderId = (int) ordersTableModel.getValueAt(selectedRow, 0);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE Orders SET order_status = ? WHERE order_id = ?")) {

            String newStatus = JOptionPane.showInputDialog(this, "Enter new status:", "Update Status", JOptionPane.PLAIN_MESSAGE);
            if (newStatus != null && !newStatus.trim().isEmpty()) {
                ps.setString(1, newStatus.trim());
                ps.setInt(2, orderId);
                ps.executeUpdate();
                loadOrders(); 
                JOptionPane.showMessageDialog(this, "Order status updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
