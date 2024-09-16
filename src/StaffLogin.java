import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class StaffLogin extends JFrame {
    JFrame frame;
    int imageIndex = 0;
    JLabel slideshowLabel;
    String[] imagePaths = {"src/image1.png", "src/image2.png", "src/image3.png"};

    JTextField usernameField;
    JPasswordField passwordField;
    JButton clockInButton, clockOutButton;

    public static void main(String[] args) {
        new StaffLogin();
    }

    public StaffLogin() {
       frame = new JFrame("LOGIN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocation(350, 0);
        frame.setVisible(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(222, 184, 135));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 400));

        slideshowLabel = new JLabel();
        slideshowLabel.setBounds(0, 0, 800, 400);
        slideshowLabel.setOpaque(true);

        JLabel nameLabel = new JLabel("MASON HALL STAFF", JLabel.CENTER);
        nameLabel.setFont(new Font("Brush Script MT", Font.BOLD, 30));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(100, 150, 600, 100);

        layeredPane.add(slideshowLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(nameLabel, JLayeredPane.PALETTE_LAYER);

        mainPanel.add(layeredPane);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(new Color(222, 184, 135));
        menuPanel.setPreferredSize(new Dimension(800, 300));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(80, 20, 100, 30);
        usernameField = new JTextField();
        usernameField.setBounds(200, 20, 200, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 60, 100, 30);
        passwordField = new JPasswordField();
        passwordField.setBounds(200, 60, 200, 30);

        clockInButton = new JButton("TIME IN");
        clockInButton.setBounds(420, 20, 120, 30);
        clockInButton.setBackground(new Color(251, 251, 251));

        clockOutButton = new JButton("TIME OUT");
        clockOutButton.setBounds(420, 60, 120, 30);
        clockOutButton.setBackground(new Color(255, 253, 253));

        menuPanel.add(usernameLabel);
        menuPanel.add(usernameField);
        menuPanel.add(passwordLabel);
        menuPanel.add(passwordField);
        menuPanel.add(clockInButton);
        menuPanel.add(clockOutButton);

        JButton managerButton = new JButton("MANAGER");
        managerButton.setBounds(80, 150, 200, 50);
        managerButton.setBackground(new Color(255, 255, 255));
        JButton waiterButton = new JButton("WAITER");
        waiterButton.setBounds(300, 150, 200, 50);
        waiterButton.setBackground(new Color(255, 255, 255));
        JButton kitchenStaffButton = new JButton("KITCHEN STAFF");
        kitchenStaffButton.setBounds(520, 150, 200, 50);
        kitchenStaffButton.setBackground(new Color(255, 255, 255));

        menuPanel.add(managerButton);
        menuPanel.add(waiterButton);
        menuPanel.add(kitchenStaffButton);

        mainPanel.add(menuPanel);
        frame.add(mainPanel);

        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser("manager");
            }
        });

        waiterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser("waiter");
            }
        });

        kitchenStaffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser("Kitchen Staff");
            }
        });

        clockInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTimestamp("in");
            }
        });

        clockOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTimestamp("out");
            }
        });

        startSlideshow();
    }

    private void startSlideshow() {
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon imageIcon = new ImageIcon(imagePaths[imageIndex]);
                Image image = imageIcon.getImage().getScaledInstance(slideshowLabel.getWidth(), slideshowLabel.getHeight(), Image.SCALE_SMOOTH);
                slideshowLabel.setIcon(new ImageIcon(image));
                imageIndex = (imageIndex + 1) % imagePaths.length;
            }
        });
        timer.start();
    }

    private void authenticateUser(String role) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MasonHall", "root", "");
            String query = "SELECT password FROM staff WHERE username = ? AND role = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, role);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");

                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    switch (role) {
                        case "manager":
                            frame.dispose();
                            new ManagerFrame();
                            break;
                        case "waiter":
                            frame.dispose();
                            new WaiterFrame();
                            break;
                        case "Kitchen Staff":
                            frame.dispose();
                            new KitchenStaff();
                            break;
                    }
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveTimestamp(String type) {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username before clocking in/out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MasonHall", "root", "");

            String checkUserQuery = "SELECT * FROM staff WHERE username = ?";
            PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery);
            checkUserStatement.setString(1, username);
            ResultSet rs = checkUserStatement.executeQuery();


            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                connection.close();
                return;
            }


            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String query = "INSERT INTO attendance (username, clock_" + type + ") VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE clock_" + type + " = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.setTimestamp(3, timestamp);


            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, type + " time saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving time " + type, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}