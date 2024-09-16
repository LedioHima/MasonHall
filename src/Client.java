import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Client {

    int imageIndex = 0;
    JLabel slideshowLabel;
    String[] imagePaths = {"src/image1.png", "src/image2.png", "src/image3.png"};

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        JFrame frame = new JFrame("WELCOME");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 800);
        frame.setLocation(300,0);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(222, 184, 135));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 400));

        slideshowLabel = new JLabel();
        slideshowLabel.setBounds(0, 0, 800, 400);
        slideshowLabel.setOpaque(true);

        JLabel nameLabel = new JLabel("MASON HALL", JLabel.CENTER);
        nameLabel.setFont(new Font("Brush Script MT", Font.BOLD, 30));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(100, 150, 600, 100);

        layeredPane.add(slideshowLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(nameLabel, JLayeredPane.PALETTE_LAYER);

        mainPanel.add(layeredPane);

        JPanel MenuPanel = new JPanel();
        MenuPanel.setLayout(null);
        MenuPanel.setBackground(new Color(222, 184, 135));

        ImageIcon fotomenu = new ImageIcon("src/fotomenu.jpg");
        JLabel photo1Label = new JLabel(fotomenu);
        photo1Label.setBounds(380, 0, 500, 400);
        MenuPanel.add(photo1Label);

        JLabel menuLabel = new JLabel("<html>Welcome to Mason Hall<br/>" +
                "At Mason Hall, we believe in creating a dining experience that delights all your senses.<br/>" +
                "Our carefully crafted menu features the freshest ingredients, skillfully prepared by our passionate chefs.<br/>" +
                "From the moment you step inside, we invite you to relax, savor every bite, and enjoy the warm ambiance we've lovingly curated just for you.<br/>" +
                "Whether you're here for a casual meal or a special occasion, Mason Hall promises a memorable culinary journey.</html>");
        menuLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setBounds(0, 50, 450, 200);
        MenuPanel.add(menuLabel);

        JButton openMenuButton = new JButton("Menu");
        openMenuButton.setBounds(90, 280, 200, 50);
        openMenuButton.setFont(new Font("Brush Script MT", Font.BOLD, 30));
        openMenuButton.setBackground(new Color(255, 255, 255));
        MenuPanel.add(openMenuButton);

        ImageIcon fotoreservimi = new ImageIcon("src/fotoreservimi.jpg");
        JLabel photo2Label = new JLabel(fotoreservimi);
        photo2Label.setBounds(10, 450, 500, 400);
        MenuPanel.add(photo2Label);

        JButton openReserveButton = new JButton("Reserve");
        openReserveButton.setBounds(550, 700, 200, 50);
        openReserveButton.setFont(new Font("Brush Script MT", Font.BOLD, 30));
        openReserveButton.setBackground(new Color(255, 255, 255));
        MenuPanel.add(openReserveButton);

        JLabel reserveLabel = new JLabel("<html>Experience the finest dining by reserving your table with us today.<br/>" +
                "Whether you're planning a special celebration or a quiet evening out, our attentive staff and exquisite menu options ensure an unforgettable experience.<br/>" +
                "Book your reservation here and let us take care of the rest.</html>");
        reserveLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        reserveLabel.setForeground(Color.WHITE);
        reserveLabel.setBounds(520, 500, 250, 200);
        reserveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        MenuPanel.add(reserveLabel);

        MenuPanel.setPreferredSize(new Dimension(800, 1100));

        mainPanel.add(MenuPanel);

        openMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new Menu().setVisible(true);
            }
        });

        openReserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame reservationFrame = new JFrame("Reservation");
                reservationFrame.setSize(400, 300);


                JPanel reservationPanel = new JPanel();
                reservationPanel.setBackground(new Color(241, 231, 202));
                reservationPanel.setLayout(new GridLayout(6, 2, 10, 10));

                reservationPanel.add(new JLabel("Name:"));
                JTextField nameField = new JTextField();
                reservationPanel.add(nameField);

                reservationPanel.add(new JLabel("Date:"));
                JComboBox<String> dateComboBox = new JComboBox<>();
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                for (int i = 0; i < 30; i++) {
                    dateComboBox.addItem(currentDate.plusDays(i).format(formatter));
                }
                reservationPanel.add(dateComboBox);

                reservationPanel.add(new JLabel("Time:"));
                JComboBox<String> timeComboBox = new JComboBox<>();
                for (int i = 12; i <= 22; i++) {
                    timeComboBox.addItem(String.format("%02d:00", i));
                    timeComboBox.addItem(String.format("%02d:30", i));
                }
                reservationPanel.add(timeComboBox);

                reservationPanel.add(new JLabel("Number of persons:"));
                String[] peopleOptions = {"1 person", "2 people", "3 people", "4 people", "5 people", "6 people", "7 people", "8+ people"};
                JComboBox<String> peopleComboBox = new JComboBox<>(peopleOptions);
                reservationPanel.add(peopleComboBox);

                JButton makeReservationButton = new JButton("Make Reservation");
                JButton cancelButton = new JButton("Cancel");

                makeReservationButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        String date = (String) dateComboBox.getSelectedItem();
                        String time = (String) timeComboBox.getSelectedItem();
                        int peopleNumber = peopleComboBox.getSelectedIndex() + 1;

                        saveReservationToDatabase(name, date, time, peopleNumber);

                        reservationFrame.dispose();
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        reservationFrame.dispose();
                    }
                });

                reservationPanel.add(makeReservationButton);
                reservationPanel.add(cancelButton);

                reservationFrame.add(reservationPanel);
                reservationFrame.setLocationRelativeTo(null);
                reservationFrame.setVisible(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu ourStoryMenu = new JMenu("Our Story");
        JMenu contactMenu = new JMenu("Contact");
        JMenu joinTeamMenu = new JMenu("Join Our Team");
        JMenuItem ourStoryItem = new JMenuItem("Read...");
        JMenuItem contactItem = new JMenuItem("Info");
        JMenuItem joinTeamItem = new JMenuItem("Apply");

        ourStoryItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OurStory().setVisible(true);
            }
        });

        contactItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Contact().setVisible(true);
            }
        });

        joinTeamItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JoinOurTeam().setVisible(true);
            }
        });

        ourStoryMenu.add(ourStoryItem);
        contactMenu.add(contactItem);
        joinTeamMenu.add(joinTeamItem);


        menuBar.add(ourStoryMenu);
        menuBar.add(contactMenu);
        menuBar.add(joinTeamMenu);

        frame.setJMenuBar(menuBar);

        startSlideshow();

        frame.setVisible(true);
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

    private void saveReservationToDatabase(String name, String date, String time, int peopleNumber) {
        String url = "jdbc:mysql://localhost:3306/MasonHall";
        String user = "root";
        String password = "";

        String sql = "INSERT INTO reservations (name, date, time, peopleNumber) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String timeWithSeconds = time + ":00";

            pstmt.setString(1, name);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            pstmt.setTime(3, java.sql.Time.valueOf(timeWithSeconds));
            pstmt.setInt(4, peopleNumber);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Reservation made successfully!");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error making reservation: " + ex.getMessage());
        }
    }
}
