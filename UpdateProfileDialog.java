package gui;

import assignment.Volunteer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the Dialog for volunteers to update their profile information in the CRS system.
 * It Allows volunteer to update their name, phone number, and password.
 * Pre-populated with current volunteer information.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 */
public class UpdateProfileDialog extends JDialog implements ActionListener {

    private Volunteer volunteer;

    JLabel lbl_title, lbl_name, lbl_phone, lbl_password, lbl_info;
    JTextField txt_name, txt_phone;
    JPasswordField txt_password;
    JButton btn_update, btn_cancel;

    /**
     * This is the Constructor for UpdateProfileDialog.
     * It Initializes the dialog with volunteer's current profile information.
     *
     * @param parent the parent JFrame
     * @param volunteer the volunteer whose profile is being updated
     */
    UpdateProfileDialog(JFrame parent, Volunteer volunteer){
        super(parent, "Update Profile", true);
        this.volunteer = volunteer;

        setLayout(null);
        setSize(450, 400);

        // Title
        lbl_title = new JLabel("<html><b>Update Your Profile</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 16));
        lbl_title.setBounds(130, 20, 250, 30);
        add(lbl_title);

        // Info
        lbl_info = new JLabel("Leave blank to keep current value");
        lbl_info.setFont(new Font("Arial", Font.ITALIC, 11));
        lbl_info.setBounds(100, 60, 250, 20);
        add(lbl_info);

        // Name
        lbl_name = new JLabel("Name:");
        lbl_name.setBounds(50, 100, 100, 30);
        add(lbl_name);

        txt_name = new JTextField();
        txt_name.setText(volunteer.getName());
        txt_name.setBounds(150, 100, 250, 30);
        add(txt_name);

        // Phone
        lbl_phone = new JLabel("Phone:");
        lbl_phone.setBounds(50, 150, 100, 30);
        add(lbl_phone);

        txt_phone = new JTextField();
        txt_phone.setText(volunteer.getPhone());
        txt_phone.setBounds(150, 150, 250, 30);
        add(txt_phone);

        // Password
        lbl_password = new JLabel("New Password:");
        lbl_password.setBounds(50, 200, 100, 30);
        add(lbl_password);

        txt_password = new JPasswordField();
        txt_password.setBounds(150, 200, 250, 30);
        add(txt_password);

        // Update Button
        btn_update = new JButton("Update");
        btn_update.setBounds(150, 270, 100, 35);
        btn_update.setBackground(new Color(34, 139, 34));
        btn_update.setForeground(Color.WHITE);
        btn_update.addActionListener(this);
        add(btn_update);

        // Cancel Button
        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(260, 270, 100, 35);
        btn_cancel.addActionListener(this);
        add(btn_cancel);

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * It Handles the action events from buttons in the dialog.
     * This Processes Update and Cancel button clicks.
     *
     * @param e the action event triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_update) {
            handleUpdate();
        } else if (src == btn_cancel) {
            this.dispose();
        }
    }

    /**
     * It Handles the update of volunteer profile information.
     * This Updates only changed fields; blank fields retain current values.
     */
    private void handleUpdate() {
        String name = txt_name.getText().trim();
        String phone = txt_phone.getText().trim();
        String password = new String(txt_password.getPassword());

        volunteer.updateProfile(name, phone, password);

        JOptionPane.showMessageDialog(this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
    }
}
