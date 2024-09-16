import javax.swing.*;
import java.awt.*;

public class Contact extends JFrame {

    public Contact() {
        setTitle("Contact");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setBackground(new Color(191, 177, 136));
        panel.setLayout(new BorderLayout(10, 10));


        JLabel contactLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>"
                + "<h2 style='font-family: Brush Script MT; color: #8B4513;'>Contact Us</h2>"
                + "<p style='font-family: Apple Chancery; font-size: 16px; color: #FFFFFF;'>"
                + "We would love to hear from you! Feel free to reach out to us through any of the following methods:"
                + "</p>"
                + "<p style='font-family: Apple Chancery; font-size: 14px; color: #FFFFFF;'>"
                + "<strong>Email:</strong> contact@masonhall.com<br/>"
                + "<strong>Phone:</strong> (+355) 69 366 9999<br/>"
                + "<strong>Address:</strong> Rruga Pjetër Bogdani, Bllok, Tirana, Albania"
                + "</p>"
                + "<p style='font-family: Apple Chancery; font-size: 16px; color: #FFFFFF;'>"
                + "Our team is here to assist you and make your experience with Mason Hall unforgettable."
                + "</p>"
                + "</div></html>");

        contactLabel.setVerticalAlignment(SwingConstants.TOP);
        contactLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contactLabel.setOpaque(true);
        contactLabel.setBackground(new Color(191, 177, 136));

        ImageIcon imageIcon = new ImageIcon("src/MasonHallBar.png");
        JLabel imageLabel = new JLabel(imageIcon);

        panel.add(contactLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);

        add(panel);
    }
}
