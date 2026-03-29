package gui;

import assignment.CRS;
import assignment.User;
import assignment.Staff;
import assignment.Trip;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * This is the Manager Dashboard for CRS system.
 * It Provides the access for staff management, user viewing, trip management, and allows data persistence.
 * This Allows the managers to add staff, view users and trips, and save/load system data.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 */
public class ManagerDashboard extends JFrame implements ActionListener {
    private CRS crs;
    JLabel lbl_title, lbl_welcome;
    JButton btn_addStaff, btn_viewUsers, btn_viewTrips, btn_logout, btn_saveData;
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tableModel;

    /**
     * This is the Constructor for ManagerDashboard.
     * It Initializes the dashboard with navigation buttons and user information table.
     *
     * @param crs the CRS system instance
     */
    ManagerDashboard(CRS crs){
        this.crs = crs;
        setLayout(null);
        setTitle("Manager Dashboard");
        setSize(800, 600);

        // Title
        lbl_title = new JLabel("<html><b>Manager Dashboard</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 24));
        lbl_title.setForeground(new Color(34, 139, 34));
        lbl_title.setBounds(50, 20, 400, 40);
        add(lbl_title);

        // Welcome message
        lbl_welcome = new JLabel("Welcome, Manager");
        lbl_welcome.setBounds(50, 70, 300, 30);
        lbl_welcome.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lbl_welcome);

        // Buttons Panel
        btn_addStaff = new JButton("Add New Staff");
        btn_addStaff.setBounds(50, 120, 150, 35);
        btn_addStaff.addActionListener(this);
        add(btn_addStaff);

        btn_viewUsers = new JButton("View All Users");
        btn_viewUsers.setBounds(220, 120, 150, 35);
        btn_viewUsers.addActionListener(this);
        add(btn_viewUsers);

        btn_viewTrips = new JButton("View All Trips");
        btn_viewTrips.setBounds(390, 120, 150, 35);
        btn_viewTrips.addActionListener(this);
        add(btn_viewTrips);

        btn_logout = new JButton("Logout");
        btn_logout.setBounds(560, 120, 150, 35);
        btn_logout.setBackground(Color.RED);
        btn_logout.setForeground(Color.WHITE);
        btn_logout.addActionListener(this);
        add(btn_logout);

        // Save Data Button - NEW!
        btn_saveData = new JButton("Save Data");
        btn_saveData.setBounds(50, 540, 120, 35);
        btn_saveData.setBackground(new Color(0, 102, 204));
        btn_saveData.setForeground(Color.WHITE);
        btn_saveData.addActionListener(this);
        add(btn_saveData);

        // Table for displaying data
        String[] columns = {"Username", "Name", "Phone", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 180, 700, 350);
        add(scrollPane);

        // Initially show all users
        showAllUsers();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * This method Handles action events from the dashboard buttons.
     * It adds the Routes to appropriate functions based on manager's selection.
     *
     * @param e the action event triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_addStaff) {
            new AddStaffDialog(this, crs);
            showAllUsers(); // Refresh
        } else if (src == btn_viewUsers) {
            showAllUsers();
        } else if (src == btn_viewTrips) {
            showAllTrips();
        } else if (src == btn_saveData) {
            handleSaveData();
        } else if (src == btn_logout) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                new LoginPage();
                this.dispose();
            }
        }
    }

    private void showAllUsers() {
        // Clear table
        tableModel.setRowCount(0);

        // Update columns
        tableModel.setColumnIdentifiers(new String[]{"Username", "Name", "Phone", "Type"});

        // Load users
        List<User> users = crs.getAllUsers();
        for (User user : users) {
            String type = (user instanceof Staff) ? "Staff" : "Volunteer";
            tableModel.addRow(new Object[]{
                    user.getUsername(),
                    user.getName(),
                    user.getPhone(),
                    type
            });
        }
    }

    private void showAllTrips() {
        // Clear table
        tableModel.setRowCount(0);

        // Update columns
        tableModel.setColumnIdentifiers(new String[]{"Trip ID", "Date", "Location", "Crisis", "Volunteers"});

        // Load trips
        List<Trip> trips = crs.getAllTrips();
        for (Trip trip : trips) {
            tableModel.addRow(new Object[]{
                    trip.getTripId(),
                    trip.getTripDate(),
                    trip.getLocation(),
                    trip.getCrisisType(),
                    trip.getAcceptedApplicationCount() + "/" + trip.getNumVolunteers()
            });
        }
    }

    /**
     * Save data to text files
     */
    private void handleSaveData() {
        if (TextDataManager.saveData(crs)) {
            JOptionPane.showMessageDialog(this,
                    "Data saved successfully!\n\n" +
                            "Files created:\n" +
                            "- users.txt\n" +
                            "- trips.txt\n" +
                            "- applications.txt\n" +
                            "- documents.txt\n\n" +
                            "Users: " + crs.getAllUsers().size() + "\n" +
                            "Trips: " + crs.getAllTrips().size(),
                    "Save Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save data.\n\nCheck console for error details.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}