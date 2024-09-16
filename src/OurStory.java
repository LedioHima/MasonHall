import javax.swing.*;
import java.awt.*;

public class OurStory extends JFrame {

    public OurStory() {
        setTitle("Our Story");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setBackground(new Color(241, 231, 202));
        panel.setLayout(new BorderLayout(10, 10));


        JLabel storyLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>"
                + "<h2 style='font-family: Brush Script MT; color: #8B4513;'>Welcome to Mason Hall Bar & Restaurant</h2>"
                + "<p style='font-family: Apple Chancery; font-size: 16px; color: #FFFFFF;'>"
                + "Where every visit is a journey through taste and tradition."
                + "</p>"
                + "<h3 style='font-family: Brush Script MT; color: #8B4513;'>A Dream Realized</h3>"
                + "<p style='font-family: Apple Chancery; font-size: 14px; color: #FFFFFF;'>"
                + "Mason Hall began as a shared dream between childhood friends, James Mason and Richard Hall. "
                + "Inspired by their small-town roots, where meals were more than just food—they were moments "
                + "that brought people together—James pursued culinary mastery in Europe, while Richard explored global hospitality."
                + "</p>"
                + "<h3 style='font-family: Brush Script MT; color: #8B4513;'>A Place to Connect</h3>"
                + "<p style='font-family: Apple Chancery; font-size: 14px; color: #FFFFFF;'>"
                + "Reuniting years later, James and Richard turned their dream into reality. They created Mason Hall as a place "
                + "where people could gather, share exceptional meals, and feel the warmth of true hospitality. "
                + "Our menu celebrates the best local ingredients, crafted into dishes that blend tradition with modern flair."
                + "</p>"
                + "<h3 style='font-family: Brush Script MT; color: #8B4513;'>Join Us</h3>"
                + "<p style='font-family: Apple Chancery; font-size: 14px; color: #FFFFFF;'>"
                + "At Mason Hall, we believe dining is about more than just food—it’s about connection, community, "
                + "and creating memories. Whether you’re here for a special occasion or a casual meal, we’re honored to have you at our table."
                + "</p>"
                + "<p style='font-family: Apple Chancery; font-size: 16px; color: #FFFFFF; font-style: italic;'>"
                + "Welcome to Mason Hall, where every meal tells a story."
                + "</p>"
                + "</div></html>");

        storyLabel.setVerticalAlignment(SwingConstants.TOP);
        storyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        storyLabel.setOpaque(true);
        storyLabel.setBackground(new Color(191, 177, 136));

        panel.add(storyLabel, BorderLayout.CENTER);

        add(panel);
    }

}
