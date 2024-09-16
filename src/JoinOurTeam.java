import javax.swing.*;
import java.awt.*;

public class JoinOurTeam extends JFrame {

    public JoinOurTeam() {
        setTitle("Join Our Team");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(191, 177, 136));
        panel.setLayout(new BorderLayout(10, 10));


        JLabel joinLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>"
                + "<h2 style='font-family: Brush Script MT; color: #8B4513;'>Join Our Team</h2>"
                + "<p style='font-family: Apple Chancery; font-size: 16px; color: #FFFFFF;'>"
                + "Are you passionate about hospitality and food? We’re always looking for dedicated and enthusiastic individuals to join our team."
                + "</p>"
                + "<p style='font-family: Apple Chancery; font-size: 14px; color: #FFFFFF;'>"
                + "Current Openings:<br/>"
                + "<strong>Servers</strong><br/>"
                + "<strong>Cooks</strong><br/>"
                + "<strong>Hosts</strong>"
                + "</p>"
                + "<p style='font-family: Apple Chancery; font-size: 14px; color: #FFFFFF;'>"
                + "If you’re interested in becoming a part of the Mason Hall family, please send your resume and cover letter to <strong>jobs@masonhall.com</strong>."
                + "</p>"
                + "<p style='font-family: Apple Chancery; font-size: 16px; color: #FFFFFF;'>"
                + "We look forward to working with you!"
                + "</p>"
                + "</div></html>");

        joinLabel.setVerticalAlignment(SwingConstants.TOP);
        joinLabel.setHorizontalAlignment(SwingConstants.CENTER);
        joinLabel.setOpaque(true);
        joinLabel.setBackground(new Color(191, 177, 136));


        ImageIcon imageIcon = new ImageIcon("src/MasonHallLogo.png");
        JLabel imageLabel = new JLabel(imageIcon);


        panel.add(joinLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);


        add(panel);
    }
}
