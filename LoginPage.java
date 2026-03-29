package gui;

import assignment.CRS;
import assignment.Staff;
import assignment.Volunteer;
import assignment.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * This is the Login page for CRS system.
 * It Provides user authentication of the interface for managers, staff, and volunteers.
 * It Includes role selection and registration link for new volunteers.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 */
public class LoginPage extends JFrame implements ActionListener, MouseListener {
    private static CRS crs;

    JLabel lbl_username, lbl_password, lbl_title, lbl_register, lbl_role;
    JTextField txt_username;
    JPasswordField txt_password;
    JButton btn_login, btn_cancel;
    String[] users = {"Manager", "Staff", "Volunteer"};
    JComboBox<String> cmb_role;

    /**
     * This Sets the CRS instance (called from Main)
     */
    public static void setCRS(CRS crsInstance) {
        crs = crsInstance;
    }

    /**
     * This is the Constructor for LoginPage.
     * It Initializes the login interface with username, password, and role selection.
     */
    LoginPage() {
        setLayout(null);
        setTitle("Crisis Relief Services - Login");

        lbl_title = new JLabel("<html><b><u>Crisis Relief Services System</u></b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 20));
        lbl_title.setBounds(80, 30, 400, 40);
        add(lbl_title);

        //Role Selection
        lbl_role = new JLabel("Login As:");
        lbl_role.setBounds(50, 80, 80, 30);
        add(lbl_role);

        cmb_role = new JComboBox<>(users);
        cmb_role.setBounds(140, 80, 200, 30);
        add(cmb_role);

        // Username
        lbl_username = new JLabel("Username:");
        lbl_username.setBounds(50, 130, 80, 30);
        add(lbl_username);

        txt_username = new JTextField();
        txt_username.setBounds(140, 130, 200, 30);
        add(txt_username);

        // Password
        lbl_password = new JLabel("Password:");
        lbl_password.setBounds(50, 180, 80, 30);
        add(lbl_password);

        txt_password = new JPasswordField();
        txt_password.setBounds(140, 180, 200, 30);
        add(txt_password);

        // Login Button
        btn_login = new JButton("Login");
        btn_login.setBounds(140, 230, 90, 35);
        btn_login.setBackground(new Color(34, 139, 34));
        btn_login.setForeground(Color.WHITE);
        add(btn_login);
        btn_login.addActionListener(this);

        // Cancel Button
        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(240, 230, 90, 35);
        add(btn_cancel);
        btn_cancel.addActionListener(this);

        // Register Link
        lbl_register = new JLabel("<html><b><u>New Volunteer? Register Here</u></b></html>");
        lbl_register.setBounds(120, 290, 250, 40);
        lbl_register.setForeground(Color.BLUE);
        lbl_register.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(lbl_register);
        lbl_register.addMouseListener(this);

        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * This Method Handles the action events from login and cancel buttons.
     * It Validates credentials and routes for appropriate dashboard.
     *
     * @param e the action event triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == btn_login) {
            handleLogin();
        } else if (src == btn_cancel) {
            System.exit(0);
        }
    }

    private void handleLogin() {
        String username = txt_username.getText().trim();
        String password = new String(txt_password.getPassword());
        String role = (String) cmb_role.getSelectedItem();

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter username and password",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check login based on role
        if (role.equals("Manager")) {
            if (crs.managerLogin(username, password)) {
                new ManagerDashboard(crs);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid manager credentials",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (role.equals("Staff") || role.equals("Volunteer")) {
            User user = crs.userLogin(username, password);

            if (user != null) {
                if (role.equals("Staff") && user instanceof Staff) {
                    new StaffDashboard(crs, (Staff) user);
                    this.dispose();
                } else if (role.equals("Volunteer") && user instanceof Volunteer) {
                    new VolunteerDashboard(crs, (Volunteer) user);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid credentials for " + role,
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method Handles mouse click events for registration link.
     * It Opens registration frame when clicked.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        new RegisterFrame(crs, this);
    }

    /**
     * Handles mouse pressed event. Not implemented.
     *
     * @param e the mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * Handles mouse released event. Not implemented.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * Handles mouse entered event. Not implemented.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Handles mouse exited event. Not implemented.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseExited(MouseEvent e) {}
}