package gui;

import assignment.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * This Dialog is for the managing trip applications in the CRS system.
 * It Allows staff to view, accept, and reject volunteer applications for trips.
 * This Displays the application details in a table format with filtering by trip.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 */
public class ManageApplicationsDialog extends JDialog implements ActionListener{

    private CRS crs;
    private List<Trip> trips;
    private Trip selectedTrip;

    JLabel lbl_title, lbl_tripSelect;
    JComboBox<String> cmb_trips;
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tableModel;
    JButton btn_accept, btn_reject, btn_close;

    /**
     * This is the Constructor for ManageApplicationsDialog.
     * It Initializes the dialog with trip selection and application management table.
     *
     * @param parent the parent JFrame
     * @param crs the CRS system instance
     * @param trips the list of trips to manage applications for
     */
    ManageApplicationsDialog(JFrame parent, CRS crs, List<Trip> trips){
        super(parent, "Manage Applications", true);
        this.crs = crs;
        this.trips = trips;

        setLayout(null);
        setSize(850, 550);

        // Title
        lbl_title = new JLabel("<html><b>Manage Trip Applications</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 16));
        lbl_title.setBounds(50, 20, 300, 30);
        add(lbl_title);

        // Trip Selection
        lbl_tripSelect = new JLabel("Select Trip:");
        lbl_tripSelect.setBounds(50, 60, 100, 30);
        add(lbl_tripSelect);

        String[] tripNames = new String[trips.size()];
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            tripNames[i] = trip.getTripId() + " - " + trip.getDescription() + " (" + trip.getLocation() + ")";
        }

        cmb_trips = new JComboBox<>(tripNames);
        cmb_trips.setBounds(150, 60, 500, 30);
        cmb_trips.addActionListener(this);
        add(cmb_trips);

        // Table
        String[] columns = {"App ID", "Volunteer", "Phone", "Date", "Status", "Documents", "Remarks"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 110, 750, 320);
        add(scrollPane);

        // Buttons
        btn_accept = new JButton("Accept");
        btn_accept.setBounds(250, 450, 100, 35);
        btn_accept.setBackground(new Color(34, 139, 34));
        btn_accept.setForeground(Color.WHITE);
        btn_accept.addActionListener(this);
        add(btn_accept);

        btn_reject = new JButton("Reject");
        btn_reject.setBounds(370, 450, 100, 35);
        btn_reject.setBackground(Color.RED);
        btn_reject.setForeground(Color.WHITE);
        btn_reject.addActionListener(this);
        add(btn_reject);

        btn_close = new JButton("Close");
        btn_close.setBounds(490, 450, 100, 35);
        btn_close.addActionListener(this);
        add(btn_close);

        // Load first trip
        if (trips.size() > 0) {
            loadApplications();
        }

        setLocationRelativeTo(parent);
        setVisible(true);
    }


    /**
     * This Handles action events from buttons and combo box.
     * And It Processes trip selection, accept, reject, and close actions.
     *
     * @param e the action event triggered by user interaction
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == cmb_trips) {
            loadApplications();
        } else if (src == btn_accept) {
            handleAccept();
        } else if (src == btn_reject) {
            handleReject();
        } else if (src == btn_close) {
            this.dispose();
        }
    }

    /**
     * This Loads and display the applications for the selected trip.
     * It adds the table with application details.
     */
    private void loadApplications() {
        int selectedIndex = cmb_trips.getSelectedIndex();
        if (selectedIndex == -1) return;

        selectedTrip = trips.get(selectedIndex);
        tableModel.setRowCount(0);

        List<Application> applications = selectedTrip.getApplications();
        for (Application app : applications) {
            Volunteer vol = app.getVolunteer();
            int docCount = vol.getDocuments().size();

            tableModel.addRow(new Object[]{
                    app.getApplicationID(),
                    vol.getName(),
                    vol.getPhone(),
                    app.getApplicationDate(),
                    app.getStatus(),
                    docCount + " docs",
                    app.getRemarks() != null ? app.getRemarks() : "N/A"
            });
        }
    }

    /**
     * This method Handles acceptance of a selected application.
     * This Updates the application status and allows it for entering remarks.
     */
    private void handleAccept() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an application",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appId = (String) table.getValueAt(selectedRow, 0);
        Application app = findApplication(appId);

        if (app == null) return;

        if (app.getStatus() != Application.Status.NEW) {
            JOptionPane.showMessageDialog(this,
                    "This application has already been processed",
                    "Already Processed",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String remarks = JOptionPane.showInputDialog(this,
                "Enter remarks (optional):",
                "Welcome to the team!");

        crs.updateApplicationStatus(app, Application.Status.ACCEPTED, remarks);

        JOptionPane.showMessageDialog(this,
                "Application accepted!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        loadApplications();
    }

    /**
     * This method Handles the rejection of a selected application.
     * It Updates the application status with rejection remarks.
     */
    private void handleReject() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an application",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appId = (String) table.getValueAt(selectedRow, 0);
        Application app = findApplication(appId);

        if (app == null) return;

        if (app.getStatus() != Application.Status.NEW) {
            JOptionPane.showMessageDialog(this,
                    "This application has already been processed",
                    "Already Processed",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String remarks = JOptionPane.showInputDialog(this,
                "Enter reason for rejection:",
                "");

        if (remarks == null || remarks.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please provide a reason for rejection",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        crs.updateApplicationStatus(app, Application.Status.REJECTED, remarks);

        JOptionPane.showMessageDialog(this,
                "Application rejected",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        loadApplications();
    }

    private Application findApplication(String appId) {
        for (Application app : selectedTrip.getApplications()) {
            if (app.getApplicationID().equals(appId)) {
                return app;
            }
        }
        return null;
    }
}
