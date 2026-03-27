package gui;

import assignment.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Staff Dashboard with Save functionality
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 */
public class StaffDashboard extends JFrame implements ActionListener{

    private CRS crs;
    private Staff staff;

    JLabel lbl_title, lbl_welcome;
    JButton btn_createTrip, btn_manageApps, btn_viewTrips, btn_saveData, btn_logout;
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tableModel;

    StaffDashboard(CRS crs, Staff staff){
        this.crs = crs;
        this.staff = staff;

        setLayout(null);
        setTitle("Staff Dashboard - " + staff.getName());
        setSize(900, 600);

        // Title
        lbl_title = new JLabel("<html><b>Staff Dashboard</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 24));
        lbl_title.setForeground(new Color(0, 102, 204));
        lbl_title.setBounds(50, 20, 400, 40);
        add(lbl_title);

        // Welcome message
        lbl_welcome = new JLabel("Welcome, " + staff.getName());
        lbl_welcome.setBounds(50, 70, 400, 30);
        lbl_welcome.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lbl_welcome);

        // Buttons
        btn_createTrip = new JButton("Create New Trip");
        btn_createTrip.setBounds(50, 120, 150, 35);
        btn_createTrip.setBackground(new Color(34, 139, 34));
        btn_createTrip.setForeground(Color.WHITE);
        btn_createTrip.addActionListener(this);
        add(btn_createTrip);

        btn_manageApps = new JButton("Manage Applications");
        btn_manageApps.setBounds(220, 120, 170, 35);
        btn_manageApps.addActionListener(this);
        add(btn_manageApps);

        btn_viewTrips = new JButton("View All Trips");
        btn_viewTrips.setBounds(410, 120, 150, 35);
        btn_viewTrips.addActionListener(this);
        add(btn_viewTrips);

        // Save Data Button - NEW!
        btn_saveData = new JButton("Save Data");
        btn_saveData.setBounds(580, 120, 100, 35);
        btn_saveData.setBackground(new Color(0, 102, 204));
        btn_saveData.setForeground(Color.WHITE);
        btn_saveData.addActionListener(this);
        add(btn_saveData);

        btn_logout = new JButton("Logout");
        btn_logout.setBounds(700, 120, 120, 35);
        btn_logout.setBackground(Color.RED);
        btn_logout.setForeground(Color.WHITE);
        btn_logout.addActionListener(this);
        add(btn_logout);

        // Table for trips
        String[] columns = {"Trip ID", "Date", "Location", "Crisis", "Volunteers", "Applications"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 180, 800, 350);
        add(scrollPane);

        // Load trips
        loadTrips();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_createTrip) {
            new CreateTripDialog(this, crs, staff);
            loadTrips(); // Refresh
        } else if (src == btn_manageApps) {
            List<Trip> trips = crs.getAllTrips();
            if (trips.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No trips available",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                new ManageApplicationsDialog(this, crs, trips);
                loadTrips(); // Refresh
            }
        } else if (src == btn_viewTrips) {
            loadTrips();
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

    private void loadTrips() {
        tableModel.setRowCount(0);

        List<Trip> trips = crs.getAllTrips();
        for (Trip trip : trips) {
            tableModel.addRow(new Object[]{
                    trip.getTripId(),
                    trip.getTripDate(),
                    trip.getLocation(),
                    trip.getCrisisType(),
                    trip.getAcceptedApplicationCount() + "/" + trip.getNumVolunteers(),
                    trip.getApplications().size()
            });
        }
    }

    /**
     * Save data to text files
     */
    private void handleSaveData() {
        if (TextDataManager.saveData(crs)) {
            JOptionPane.showMessageDialog(this,
                    "Data saved successfully!",
                    "Save Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save data.\nCheck console for errors.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}