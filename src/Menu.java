import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Menu extends JFrame {

    private Map<String, Integer> dishQuantities = new HashMap<>();
    private JPanel dishesPanel;

    public Menu() {
        setTitle("Menu");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(241, 231, 202));

        // Title label
        JLabel menuTitle = new JLabel("<html>Welcome to Mason Hall<br/>" +
                "Enjoy our carefully curated menu with a variety of options to suit every taste.</html>");
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuTitle.setForeground(Color.BLACK);
        menuTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(menuTitle, BorderLayout.NORTH);

        // Dishes panel
        dishesPanel = new JPanel();
        dishesPanel.setLayout(new GridLayout(0, 1, 10, 10));
        dishesPanel.setBackground(new Color(241, 231, 202));
        JScrollPane scrollPane = new JScrollPane(dishesPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Adding section headers and dishes
        addSectionHeader("Appetizers");
        addDish("Bruschetta", "src/bruschetta.png", "Grilled bread topped with diced tomatoes, garlic, and basil.", 7.99);
        addDish("Caprese Salad", "src/caprese.png", "Fresh mozzarella, tomatoes, and basil drizzled with balsamic glaze.", 8.99);
        addDish("Stuffed Mushrooms", "src/stuffed_mushrooms.png", "Mushrooms stuffed with cheese and herbs, baked to perfection.", 9.99);
        addDish("Shrimp Cocktail", "src/shrimp_cocktail.jpg", "Chilled shrimp served with cocktail sauce and lemon.", 10.99);
        addDish("Calamari", "src/calamari.jpg", "Lightly breaded and fried calamari served with marinara sauce.", 11.99);

        addSectionHeader("Main Dishes");
        addDish("Grilled Ribeye", "src/ribeye.png", "Juicy ribeye steak grilled to your liking, served with garlic butter.", 24.99);
        addDish("Salmon Fillet", "src/salmon.png", "Pan-seared salmon with a lemon butter sauce and seasonal vegetables.", 22.99);
        addDish("Chicken Alfredo", "src/alfredo.png", "Creamy Alfredo sauce over fettuccine pasta and grilled chicken.", 19.99);
        addDish("Vegetarian Lasagna", "src/lasagna.jpg", "Layers of pasta, vegetables, and cheese, baked to perfection.", 18.99);
        addDish("Lamb Chops", "src/lamb_chops.jpg", "Tender lamb chops grilled with rosemary and served with mashed potatoes.", 26.99);

        addSectionHeader("Desserts");
        addDish("Tiramisu", "src/tiramisu.jpg", "Classic Italian dessert with layers of coffee-soaked ladyfingers and mascarpone cheese.", 7.99);
        addDish("Cheesecake", "src/cheesecake.png", "Rich and creamy cheesecake with a graham cracker crust.", 6.99);
        addDish("Chocolate Lava Cake", "src/lava_cake.png", "Warm chocolate cake with a molten center, served with vanilla ice cream.", 8.99);
        addDish("Crème Brûlée", "src/creme_brulee.png", "Creamy custard topped with a caramelized sugar crust.", 7.99);
        addDish("Apple Pie", "src/apple_pie.png", "Traditional apple pie with a flaky crust and cinnamon-spiced apples.", 6.99);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton completeButton = new JButton("Complete Reservation");
        completeButton.setBackground(new Color(255, 255, 255));
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(255, 255, 255));
        buttonsPanel.add(completeButton);
        buttonsPanel.add(exitButton);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Button actions
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double totalAmount = 0.0;
                Map<String, Integer> selectedDishes = new HashMap<>();

                Component[] components = dishesPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JPanel) {
                        JPanel dishPanel = (JPanel) component;
                        String dishName = ((JLabel) ((JPanel) dishPanel.getComponent(1)).getComponent(0)).getText();
                        String priceText = ((JLabel) ((JPanel) dishPanel.getComponent(1)).getComponent(2)).getText();
                        double price = Double.parseDouble(priceText.replace("$", ""));
                        JSpinner quantitySpinner = (JSpinner) dishPanel.getComponent(2);
                        int quantity = (Integer) quantitySpinner.getValue();

                        if (quantity > 0) {
                            selectedDishes.put(dishName, quantity);
                            totalAmount += price * quantity;
                        }
                    }
                }

                // Open the OrderSummary frame
                OrderSummary orderSummary = new OrderSummary(selectedDishes, totalAmount);
                orderSummary.setVisible(true);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        add(mainPanel);
    }

    private void addSectionHeader(String title) {
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(new Color(101, 67, 33)); // Brown color
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dishesPanel.add(headerLabel);
    }

    private void addDish(String name, String imagePath, String description, double price) {
        JPanel dishPanel = new JPanel();
        dishPanel.setLayout(new BorderLayout());
        dishPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Dish image
        ImageIcon dishImageIcon = new ImageIcon(imagePath);
        Image dishImage = dishImageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(dishImage));
        dishPanel.add(imageLabel, BorderLayout.WEST);

        // Dish details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(3, 1));
        detailsPanel.setBackground(new Color(241, 231, 202));
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel descriptionLabel = new JLabel("<html>" + description + "</html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsPanel.add(nameLabel);
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(priceLabel);
        dishPanel.add(detailsPanel, BorderLayout.CENTER);

        // Quantity spinner
        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 10, 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        dishPanel.add(quantitySpinner, BorderLayout.EAST);

        dishesPanel.add(dishPanel);
        dishesPanel.revalidate();
        dishesPanel.repaint();
    }
}