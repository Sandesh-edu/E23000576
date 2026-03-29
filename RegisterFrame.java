package gui;

import assignment.CRS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registration frame for creating new volunteer accounts in the CRS system.
 * Provides form interface for volunteers to register with username, password,
 * name, and phone number.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 * @version 1.0
 */
public class RegisterFrame extends JFrame implements ActionListener{

    private CRS crs;
    private JFrame parentFrame;

    JLabel lbl_title, lbl_username, lbl_password, lbl_confirm, lbl_name, lbl_phone;
    JTextField txt_username, txt_name, txt_phone;
    JPasswordField txt_password, txt_confirm;
    JButton btn_register, btn_cancel;

    /**
     * Constructor for RegisterFrame.
     * Initializes the registration form with input fields and validation.
     *
     * @param crs the CRS system instance
     * @param parent the parent JFrame (LoginPage)
     */
    RegisterFrame(CRS crs, JFrame parent){
        this.crs = crs;
        this.parentFrame = parent;

        setLayout(null);
        setTitle("Volunteer Registration");

        // Title
        lbl_title = new JLabel("<html><b><u>Create Volunteer Account</u></b></html>");
        lbl_title.setForeground(new Color(34, 139, 34));
        lbl_title.setFont(new Font("Arial", Font.BOLD, 18));
        lbl_title.setBounds(150, 20, 300, 40);
        add(lbl_title);

        // Username
        lbl_username = new JLabel("Username:");
        lbl_username.setBounds(80, 80, 100, 30);
        add(lbl_username);

        txt_username = new JTextField();
        txt_username.setBounds(200, 80, 200, 30);
        add(txt_username);

        // Password
        lbl_password = new JLabel("Password:");
        lbl_password.setBounds(80, 130, 100, 30);
        add(lbl_password);

        txt_password = new JPasswordField();
        txt_password.setBounds(200, 130, 200, 30);
        add(txt_password);

        // Confirm Password
        lbl_confirm = new JLabel("Confirm Password:");
        lbl_confirm.setBounds(80, 180, 120, 30);
        add(lbl_confirm);

        txt_confirm = new JPasswordField();
        txt_confirm.setBounds(200, 180, 200, 30);
        add(txt_confirm);

        // Name
        lbl_name = new JLabel("Full Name:");
        lbl_name.setBounds(80, 230, 100, 30);
        add(lbl_name);

        txt_name = new JTextField();
        txt_name.setBounds(200, 230, 200, 30);
        add(txt_name);

        // Phone
        lbl_phone = new JLabel("Phone:");
        lbl_phone.setBounds(80, 280, 100, 30);
        add(lbl_phone);

        txt_phone = new JTextField();
        txt_phone.setBounds(200, 280, 200, 30);
        add(txt_phone);

        // Register Button
        btn_register = new JButton("Register");
        btn_register.setBounds(200, 340, 100, 35);
        btn_register.setBackground(new Color(34, 139, 34));
        btn_register.setForeground(Color.WHITE);
        btn_register.addActionListener(this);
        add(btn_register);

        // Cancel Button
        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(310, 340, 100, 35);
        btn_cancel.addActionListener(this);
        add(btn_cancel);

        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Handles action events from buttons in the registration frame.
     * Processes Register and Cancel button clicks.
     *
     * @param e the action event triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_register) {
            handleRegister();
        } else if (src == btn_cancel) {
            this.dispose();
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
        }
    }

    /**
     * Handles the registration of a new volunteer.
     * Validates input fields including password matching and username uniqueness.
     * Registers the volunteer in the CRS system if validation succeeds.
     */
    private void handleRegister(){
        String username = txt_username.getText().trim();
        String password = new String(txt_password.getPassword());
        String confirmPwd = new String(txt_confirm.getPassword());
        String name = txt_name.getText().trim();
        String phone = txt_phone.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
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

        // Register volunteer
        crs.registerVolunteer(username, password, name, phone);

        JOptionPane.showMessageDialog(this,
                "Registration successful!\nYou can now login as Volunteer.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }

    }
}