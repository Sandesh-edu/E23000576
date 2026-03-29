package gui;

import assignment.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dashboard interface for volunteers in the CRS system.
 * Provides access to view available trips, apply for trips, manage applications,
 * update profile, upload documents, and save system data.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 * @version 1.0
 */
public class VolunteerDashboard extends JFrame implements ActionListener{

    private CRS crs;
    private Volunteer volunteer;

    JLabel lbl_title, lbl_welcome;
    JButton btn_viewTrips, btn_apply, btn_myApps, btn_updateProfile, btn_uploadDoc, btn_saveData, btn_logout;
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tableModel;
    boolean showingTrips = true;

    /**
     * Constructor for VolunteerDashboard.
     * Initializes the dashboard with navigation buttons and trip information table.
     *
     * @param crs the CRS system instance
     * @param volunteer the currently logged-in volunteer
     */
    VolunteerDashboard(CRS crs, Volunteer volunteer){
        this.crs = crs;
        this.volunteer = volunteer;

        setLayout(null);
        setTitle("Volunteer Dashboard - " + volunteer.getName());
        setSize(950, 650);

        // Title
        lbl_title = new JLabel("<html><b>Volunteer Dashboard</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 24));
        lbl_title.setForeground(new Color(220, 20, 60));
        lbl_title.setBounds(50, 20, 400, 40);
        add(lbl_title);

        // Welcome message
        lbl_welcome = new JLabel("Welcome, " + volunteer.getName());
        lbl_welcome.setBounds(50, 70, 400, 30);
        lbl_welcome.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lbl_welcome);

        // Buttons - Row 1
        btn_viewTrips = new JButton("View Available Trips");
        btn_viewTrips.setBounds(50, 120, 170, 35);
        btn_viewTrips.addActionListener(this);
        add(btn_viewTrips);

        btn_apply = new JButton("Apply for Trip");
        btn_apply.setBounds(240, 120, 150, 35);
        btn_apply.setBackground(new Color(34, 139, 34));
        btn_apply.setForeground(Color.WHITE);
        btn_apply.addActionListener(this);
        add(btn_apply);

        btn_myApps = new JButton("My Applications");
        btn_myApps.setBounds(410, 120, 150, 35);
        btn_myApps.addActionListener(this);
        add(btn_myApps);

        btn_updateProfile = new JButton("Update Profile");
        btn_updateProfile.setBounds(580, 120, 140, 35);
        btn_updateProfile.addActionListener(this);
        add(btn_updateProfile);

        // Buttons - Row 2
        btn_uploadDoc = new JButton("Upload Document");
        btn_uploadDoc.setBounds(50, 165, 170, 35);
        btn_uploadDoc.addActionListener(this);
        add(btn_uploadDoc);

        // Save Data Button - NEW!
        btn_saveData = new JButton("Save Data");
        btn_saveData.setBounds(240, 165, 150, 35);
        btn_saveData.setBackground(new Color(0, 102, 204));
        btn_saveData.setForeground(Color.WHITE);
        btn_saveData.addActionListener(this);
        add(btn_saveData);

        btn_logout = new JButton("Logout");
        btn_logout.setBounds(750, 120, 120, 35);
        btn_logout.setBackground(Color.RED);
        btn_logout.setForeground(Color.WHITE);
        btn_logout.addActionListener(this);
        add(btn_logout);

        // Table for trips
        String[] columns = {"Trip ID", "Date", "Location", "Crisis", "Spaces", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 220, 850, 350);
        add(scrollPane);

        // Load available trips
        loadAvailableTrips();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Handles action events from dashboard buttons and components.
     * Routes to appropriate action handlers based on user interaction.
     *
     * @param e the action event triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_viewTrips) {
            loadAvailableTrips();
        } else if (src == btn_apply) {
            handleApplyForTrip();
        } else if (src == btn_myApps) {
            loadMyApplications();
        } else if (src == btn_updateProfile) {
            new UpdateProfileDialog(this, volunteer);
        } else if (src == btn_uploadDoc) {
            new UploadDocumentDialog(this, volunteer);
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

    /**
     * Loads and displays all available relief trips.
     * Populates the table with trip information including spaces available.
     */
    private void loadAvailableTrips() {
        showingTrips = true;
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"Trip ID", "Date", "Location", "Crisis", "Spaces", "Description"});

        List<Trip> trips = crs.getAvailableTrips();
        for (Trip trip : trips) {
            int spaces = trip.getNumVolunteers() - trip.getAcceptedApplicationCount();
            tableModel.addRow(new Object[]{
                    trip.getTripId(),
                    trip.getTripDate(),
                    trip.getLocation(),
                    trip.getCrisisType(),
                    spaces + " available",
                    trip.getDescription()
            });
        }
    }

    /**
     * Loads and displays the volunteer's trip applications.
     * Shows application status, remarks, and trip details.
     */
    private void loadMyApplications() {
        showingTrips = false;
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"App ID", "Trip", "Location", "Date", "Status", "Remarks"});

        List<Application> applications = volunteer.getApplications();
        for (Application app : applications) {
            tableModel.addRow(new Object[]{
                    app.getApplicationID(),
                    app.getTrip().getDescription(),
                    app.getTrip().getLocation(),
                    app.getTrip().getTripDate(),
                    app.getStatus(),
                    app.getRemarks() != null ? app.getRemarks() : "N/A"
            });
        }
    }

    /**
     * Handles the volunteer's application to join a selected trip.
     * Validates trip selection and existing applications before submitting.
     */
    private void handleApplyForTrip() {
        if (!showingTrips) {
            JOptionPane.showMessageDialog(this,
                    "Please view available trips first",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            loadAvailableTrips();
            return;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a trip from the table",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int tripId = (int) table.getValueAt(selectedRow, 0);

        Trip selectedTrip = null;
        for (Trip trip : crs.getAllTrips()) {
            if (trip.getTripId() == tripId) {
                selectedTrip = trip;
                break;
            }
        }

        if (selectedTrip == null) {
            JOptionPane.showMessageDialog(this,
                    "Trip not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Application app = crs.applyForTrip(volunteer, selectedTrip);

        if (app != null) {
            JOptionPane.showMessageDialog(this,
                    "Application submitted successfully!\nApplication ID: " + app.getApplicationID(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadAvailableTrips();
        } else {
            if (!selectedTrip.hasSpaceAvailable()) {
                JOptionPane.showMessageDialog(this,
                        "Trip is full",
                        "Application Failed",
                        JOptionPane.ERROR_MESSAGE);
            } else if (selectedTrip.getTripDate().isBefore(java.time.LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                        "Trip date has already passed",
                        "Application Failed",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "You have already applied for this trip",
                        "Application Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
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
                    "Failed to save data.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}