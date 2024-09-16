import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AttendanceFrame extends JFrame {
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    public AttendanceFrame() {
        setTitle("Attendance");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Clock In", "Clock Out"}, 0);
        attendanceTable = new JTable(tableModel);
        attendanceTable.setBackground(new Color(244, 226, 174));
        attendanceTable.setGridColor(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(attendanceTable);

        loadAttendanceData();

        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadAttendanceData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/MasonHall", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM attendance")) {

            tableModel.setRowCount(0); // Clear the table

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");

                // Use Timestamp to include both date and time
                Timestamp clockIn = rs.getTimestamp("clock_in");
                Timestamp clockOut = rs.getTimestamp("clock_out");

                // Convert the Timestamp to a readable format (optional)
                String clockInStr = (clockIn != null) ? clockIn.toString() : "Null";
                String clockOutStr = (clockOut != null) ? clockOut.toString() : "Null";

                tableModel.addRow(new Object[]{id, username, clockInStr, clockOutStr});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
