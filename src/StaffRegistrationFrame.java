import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class StaffRegistrationFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JTable staffTable;
    private DefaultTableModel tableModel;

    public StaffRegistrationFrame() {
        setTitle("Staff Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocation(450, 150);
        setLayout(null);
        getContentPane().setBackground(new Color(241, 231, 202));

        JLabel headerLabel = new JLabel("Staff Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBounds(350, 20, 200, 30);
        add(headerLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(190, 80, 100, 30);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(300, 80, 200, 30);
        add(usernameField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(550, 80, 150, 30);
        registerButton.setBackground(new Color(255, 255, 255));
        add(registerButton);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(190, 130, 100, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(300, 130, 200, 30);
        add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(190, 180, 100, 30);
        add(roleLabel);

        roleComboBox = new JComboBox<>(new String[]{"Manager", "Waiter", "Kitchen Staff"});
        roleComboBox.setBounds(300, 180, 200, 30);
        roleComboBox.setBackground(new Color(255, 255, 255));
        add(roleComboBox);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(550, 130, 150, 30);
        changePasswordButton.setBackground(new Color(255, 255, 255));
        add(changePasswordButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(550, 180, 150, 30);
        deleteButton.setBackground(new Color(255, 255, 255));
        add(deleteButton);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Password");
        tableModel.addColumn("Role");

        staffTable = new JTable(tableModel);
        staffTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBounds(50, 280, 800, 250);
        add(scrollPane);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerStaff();
                loadStaffData();
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
                loadStaffData();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStaff();
                loadStaffData();
            }
        });

        staffTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = staffTable.getSelectedRow();
                usernameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                roleComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        loadStaffData();
        setVisible(true);
    }

    private void registerStaff() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username or Password cannot be empty.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MasonHall", "root", "")) {
            String query = "INSERT INTO staff (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, role);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Staff registered successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void changePassword() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member from the table.");
            return;
        }

        String username = tableModel.getValueAt(selectedRow, 1).toString();
        String newPassword = new String(passwordField.getPassword());

        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "New Password cannot be empty.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MasonHall", "root", "")) {
            String query = "UPDATE staff SET password = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hashedPassword);
            stmt.setString(2, username);
            int updated = stmt.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Password updated successfully for " + username);
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteStaff() {
        int selectedRow = staffTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member from the table.");
            return;
        }

        String username = tableModel.getValueAt(selectedRow, 1).toString();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MasonHall", "root", "")) {
            String query = "DELETE FROM staff WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            int deleted = stmt.executeUpdate();

            if (deleted > 0) {
                JOptionPane.showMessageDialog(this, "Staff deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Username not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadStaffData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MasonHall", "root", "")) {
            String query = "SELECT id, username, password, role FROM staff";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role")});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading staff data: " + ex.getMessage());
        }
    }
}
