import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WaiterFrame extends JFrame {
    private JButton menuButton, orderButton,backButton;

    public WaiterFrame() {
        setTitle("Waiter");
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


        menuButton = new JButton("MENU");
        menuButton.setBounds(150, 50, 200, 50);
        menuButton.setBackground(new Color(255, 255, 255));

        orderButton = new JButton("ORDERS");
        orderButton.setBounds(450, 50, 200, 50);
        orderButton.setBackground(new Color(255, 255, 255));

        backButton = new JButton("Back");
        backButton.setBounds(330, 120, 130, 30);
        backButton.setBackground(new Color(255, 255, 255));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current KitchenStaff frame
                new StaffLogin(); // Open the StaffLogin frame
            }
        });


        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Menu();
            }
        });

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WaiterOrders();
            }
        });

        menuPanel.add(menuButton);
        menuPanel.add(orderButton);
        menuPanel.add(backButton);

        mainPanel.add(menuPanel);

        getContentPane().setBackground(new Color(241, 231, 202));
        add(mainPanel);

        setVisible(true);
    }
}