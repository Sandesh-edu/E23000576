package gui;

import assignment.CRS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

/**
 * Dialog for adding new staff members to the CRS system.
 * Provides a form interface for managers to enter staff details including
 * username, password, name, phone, and position.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 * @version 1.0
 */
public class AddStaffDialog extends JDialog implements ActionListener{

    private CRS crs;
    JLabel lbl_title, lbl_username, lbl_password, lbl_name, lbl_phone, lbl_position;
    JTextField txt_username, txt_name, txt_phone, txt_position;
    JPasswordField txt_password;
    JButton btn_add, btn_cancel;

    /**
     * Constructor for AddStaffDialog.
     * Initializes the dialog window with input fields for staff details.
     *
     * @param parent the parent JFrame
     * @param crs the CRS system instance
     */
    AddStaffDialog(JFrame parent, CRS crs) {
        super(parent, "Add New Staff", true);
        this.crs = crs;

        setLayout(null);
        setSize(450, 450);

        // Title
        lbl_title = new JLabel("<html><b>Add New Staff Member</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 16));
        lbl_title.setBounds(120, 20, 250, 30);
        add(lbl_title);

        // Username
        lbl_username = new JLabel("Username:");
        lbl_username.setBounds(50, 70, 100, 30);
        add(lbl_username);

        txt_username = new JTextField();
        txt_username.setBounds(150, 70, 200, 30);
        add(txt_username);

        // Password
        lbl_password = new JLabel("Password:");
        lbl_password.setBounds(50, 120, 100, 30);
        add(lbl_password);

        txt_password = new JPasswordField();
        txt_password.setBounds(150, 120, 200, 30);
        add(txt_password);

        // Name
        lbl_name = new JLabel("Full Name:");
        lbl_name.setBounds(50, 170, 100, 30);
        add(lbl_name);

        txt_name = new JTextField();
        txt_name.setBounds(150, 170, 200, 30);
        add(txt_name);

        // Phone
        lbl_phone = new JLabel("Phone:");
        lbl_phone.setBounds(50, 220, 100, 30);
        add(lbl_phone);

        txt_phone = new JTextField();
        txt_phone.setBounds(150, 220, 200, 30);
        add(txt_phone);

        // Position
        lbl_position = new JLabel("Position:");
        lbl_position.setBounds(50, 270, 100, 30);
        add(lbl_position);

        txt_position = new JTextField();
        txt_position.setBounds(150, 270, 200, 30);
        add(txt_position);

        // Add Button
        btn_add = new JButton("Add Staff");
        btn_add.setBounds(150, 330, 100, 35);
        btn_add.setBackground(new Color(34, 139, 34));
        btn_add.setForeground(Color.WHITE);
        btn_add.addActionListener(this);
        add(btn_add);

        // Cancel Button
        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(260, 330, 100, 35);
        btn_cancel.addActionListener(this);
        add(btn_cancel);

        setLocationRelativeTo(parent);
        setVisible(true);

    }



    /**
     * Handles action events from buttons in the dialog.
     * Processes Add and Cancel button clicks.
     *
     * @param e the action event triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_add) {
            handleAddStaff();
        } else if (src == btn_cancel) {
            this.dispose();
        }
    }

    /**
     * Handles the addition of a new staff member.
     * Validates input fields and adds the staff to the CRS system.
     * Shows appropriate error or success messages.
     */
    private void handleAddStaff(){

        String username = txt_username.getText().trim();
        String password = new String(txt_password.getPassword());
        String name = txt_name.getText().trim();
        String phone = txt_phone.getText().trim();
        String position = txt_position.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() ||
                phone.isEmpty() || position.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (crs.findUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Add staff
        crs.recordNewStaff(username, password, name, phone, position, LocalDate.now());

        JOptionPane.showMessageDialog(this,
                "Staff member added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
    }
}
