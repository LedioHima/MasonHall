import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class WaiterOrders extends JFrame {
    private JTable ordersTable, orderDetailsTable;
    private DefaultTableModel ordersTableModel, orderDetailsTableModel;

    private String url = "jdbc:mysql://localhost:3306/MasonHall";
    private String user = "root";
    private String password = "";

    public WaiterOrders() {
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

        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int orderId = (int) ordersTableModel.getValueAt(selectedRow, 0);
                    loadOrderDetails(orderId);
                }
            }
        });

        setLayout(new BorderLayout());
        add(ordersScrollPane, BorderLayout.NORTH);
        add(orderDetailsScrollPane, BorderLayout.CENTER);

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
}