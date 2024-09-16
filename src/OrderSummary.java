import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Map;
import java.math.BigDecimal;

public class OrderSummary extends JFrame {

    public OrderSummary(Map<String, Integer> dishQuantities, double totalAmount) {
        setTitle("Order Summary");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(new Color(241, 231, 202));

        for (Map.Entry<String, Integer> entry : dishQuantities.entrySet()) {
            if (entry.getValue() > 0) {
                summaryPanel.add(new JLabel(entry.getKey() + ": " + entry.getValue()));
            }
        }

        summaryPanel.add(new JLabel("Total Amount: $" + String.format("%.2f", totalAmount)));

        JPanel userDetailsPanel = new JPanel();
        userDetailsPanel.setLayout(new GridLayout(4, 2)); // Change to 4 rows, 2 columns
        userDetailsPanel.setBackground(new Color(241, 231, 202));
        userDetailsPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        userDetailsPanel.add(nameField);
        userDetailsPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField();
        userDetailsPanel.add(addressField);
        userDetailsPanel.add(new JLabel("Phone Number:")); // Added phone number label
        JTextField phoneField = new JTextField();           // Added phone number text field
        userDetailsPanel.add(phoneField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton orderButton = new JButton("Order");
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(orderButton);
        buttonPanel.add(exitButton);

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText(); // Get the phone number
                if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(OrderSummary.this, "Please enter your name, address, and phone number.");
                } else {
                    saveOrderToDatabase(name, address, phone, dishQuantities, totalAmount);
                    JOptionPane.showMessageDialog(OrderSummary.this, "Order placed! Thank you, " + name + ".");
                    dispose();
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        mainPanel.add(summaryPanel, BorderLayout.CENTER);
        mainPanel.add(userDetailsPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void saveOrderToDatabase(String name, String address, String phone, Map<String, Integer> dishQuantities, double totalAmount) {
        String url = "jdbc:mysql://localhost:3306/MasonHall";
        String user = "root";
        String password = "";

        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement detailStmt = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            String insertOrderSQL = "INSERT INTO Orders (customer_name, customer_address, customer_phone, total_amount) VALUES (?, ?, ?, ?)";
            orderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setString(1, name);
            orderStmt.setString(2, address);
            orderStmt.setString(3, phone);
            orderStmt.setBigDecimal(4, BigDecimal.valueOf(totalAmount));
            orderStmt.executeUpdate();

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            int orderId = 0;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            String insertDetailSQL = "INSERT INTO OrderDetails (order_id, dish_name, quantity, price) VALUES (?, ?, ?, ?)";
            detailStmt = conn.prepareStatement(insertDetailSQL);

            for (Map.Entry<String, Integer> entry : dishQuantities.entrySet()) {
                String dishName = entry.getKey();
                int quantity = entry.getValue();
                double price = calculatePrice(dishName);

                detailStmt.setInt(1, orderId);
                detailStmt.setString(2, dishName);
                detailStmt.setInt(3, quantity);
                detailStmt.setBigDecimal(4, BigDecimal.valueOf(price));
                detailStmt.addBatch();
            }

            detailStmt.executeBatch();
            conn.commit();

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            ex.printStackTrace();
        } finally {
            try {
                if (orderStmt != null) orderStmt.close();
                if (detailStmt != null) detailStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private double calculatePrice(String dishName) {
        switch (dishName) {
            case "Bruschetta":
                return 7.99;
            case "Caprese Salad":
                return 8.99;
            case "Stuffed Mushrooms":
                return 9.99;
            case "Shrimp Cocktail":
                return 10.99;
            case "Calamari":
                return 11.99;
            case "Grilled Ribeye":
                return 24.99;
            case "Salmon Fillet":
                return 22.99;
            case "Chicken Alfredo":
                return 19.99;
            case "Vegetarian Lasagna":
                return 18.99;
            case "Lamb Chops":
                return 26.99;
            case "Tiramisu":
                return 7.99;
            case "Cheesecake":
                return 6.99;
            case "Chocolate Lava Cake":
                return 8.99;
            case "Crème Brûlée":
                return 7.99;
            case "Apple Pie":
                return 6.99;
            default:
                return 0.0;
        }
    }
}
