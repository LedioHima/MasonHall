import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerFrame extends JFrame {
    public static void main(String[] args) {
        new ManagerFrame();
    }

    public ManagerFrame() {
        setTitle("Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocation(350, 50);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(222, 184, 135));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 400));

        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(0, 0, 800, 400);
        imageLabel.setOpaque(true);
        ImageIcon imageIcon = new ImageIcon("src/MasonHallLogo.png");
        Image image = imageIcon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));

        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);
        mainPanel.add(layeredPane);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(null);
        menuPanel.setBackground(new Color(222, 184, 135));
        menuPanel.setPreferredSize(new Dimension(800, 200));

        JButton inventoryButton = new JButton("INVENTORY");
        inventoryButton.setBounds(80, 50, 200, 50);
        inventoryButton.setBackground(new Color(255, 255, 255));

        JButton ordersButton = new JButton("ORDERS");
        ordersButton.setBounds(300, 50, 200, 50);
        ordersButton.setBackground(new Color(255, 255, 255));

        JButton staffRegButton = new JButton("STAFF REGISTRATION");
        staffRegButton.setBounds(520, 50, 200, 50);
        staffRegButton.setBackground(new Color(255, 255, 255));

        JButton backButton = new JButton("BACK");
        backButton.setBounds(185, 120, 200, 50);
        backButton.setBackground(new Color(255, 255, 255));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StaffLogin();
            }
        });

        JButton timeButton = new JButton("TIME IN/OUT");
        timeButton.setBounds(410, 120, 200, 50);
        timeButton.setBackground(new Color(255, 255, 255));
        timeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AttendanceFrame();
            }
        });

        menuPanel.add(inventoryButton);
        menuPanel.add(ordersButton);
        menuPanel.add(staffRegButton);
        menuPanel.add(backButton);
        menuPanel.add(timeButton);
        mainPanel.add(menuPanel);

        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Inventory();
            }
        });

        ordersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OrdersManagement();
            }
        });

        staffRegButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StaffRegistrationFrame();
            }
        });

        add(mainPanel);
        setVisible(true);
    }
}
