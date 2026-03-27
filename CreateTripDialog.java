package gui;

import assignment.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class CreateTripDialog extends JDialog implements ActionListener{

    private CRS crs;
    private Staff staff;

    JLabel lbl_title, lbl_desc, lbl_location, lbl_date, lbl_volunteers, lbl_crisis;
    JTextField txt_desc, txt_location, txt_date, txt_volunteers;
    JComboBox<CRS.CrisisType> cmb_crisis;
    JButton btn_create, btn_cancel;

    CreateTripDialog(JFrame parent, CRS crs, Staff staff){
        super(parent, "Create New Trip", true);
        this.crs = crs;
        this.staff = staff;

        setLayout(null);
        setSize(500, 500);

        // Title
        lbl_title = new JLabel("<html><b>Create New Relief Trip</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 16));
        lbl_title.setBounds(150, 20, 250, 30);
        add(lbl_title);

        // Description
        lbl_desc = new JLabel("Description:");
        lbl_desc.setBounds(50, 70, 120, 30);
        add(lbl_desc);

        txt_desc = new JTextField();
        txt_desc.setBounds(180, 70, 250, 30);
        add(txt_desc);

        // Location
        lbl_location = new JLabel("Location:");
        lbl_location.setBounds(50, 120, 120, 30);
        add(lbl_location);

        txt_location = new JTextField();
        txt_location.setBounds(180, 120, 250, 30);
        add(txt_location);

        // Date
        lbl_date = new JLabel("Date (YYYY-MM-DD):");
        lbl_date.setBounds(50, 170, 130, 30);
        add(lbl_date);

        txt_date = new JTextField();
        txt_date.setBounds(180, 170, 250, 30);
        add(txt_date);

        // Required Volunteers
        lbl_volunteers = new JLabel("Volunteers Needed:");
        lbl_volunteers.setBounds(50, 230, 130, 30);
        add(lbl_volunteers);

        txt_volunteers = new JTextField();
        txt_volunteers.setBounds(180, 230, 250, 30);
        add(txt_volunteers);

        // Crisis Type
        lbl_crisis = new JLabel("Crisis Type:");
        lbl_crisis.setBounds(50, 280, 120, 30);
        add(lbl_crisis);

        cmb_crisis = new JComboBox<>(CRS.CrisisType.values());
        cmb_crisis.setBounds(180, 280, 250, 30);
        add(cmb_crisis);

        // Create Button
        btn_create = new JButton("Create Trip");
        btn_create.setBounds(180, 350, 120, 40);
        btn_create.setBackground(new Color(34, 139, 34));
        btn_create.setForeground(Color.WHITE);
        btn_create.addActionListener(this);
        add(btn_create);

        // Cancel Button
        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(310, 350, 120, 40);
        btn_cancel.addActionListener(this);
        add(btn_cancel);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_create) {
            handleCreateTrip();
        } else if (src == btn_cancel) {
            this.dispose();
        }
    }

    private void handleCreateTrip() {
        String description = txt_desc.getText().trim();
        String location = txt_location.getText().trim();
        String dateStr = txt_date.getText().trim();
        String volunteersStr = txt_volunteers.getText().trim();
        CRS.CrisisType crisisType = (CRS.CrisisType) cmb_crisis.getSelectedItem();

        // Validation
        if (description.isEmpty() || location.isEmpty() || dateStr.isEmpty() || volunteersStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use YYYY-MM-DD",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this,
                    "Trip date must be in the future",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int requiredVolunteers;
        try {
            requiredVolunteers = Integer.parseInt(volunteersStr);
            if (requiredVolunteers <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Required volunteers must be a positive number",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create trip
        crs.createTrip(description, date, location, requiredVolunteers, crisisType, staff);

        JOptionPane.showMessageDialog(this,
                "Trip created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
    }
}
