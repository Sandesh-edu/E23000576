package gui;

import assignment.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

/**
 * Dialog for volunteers to upload documents in the CRS system.
 * Allows selecting document type, expiry date, and file path.
 * Includes file browser functionality for selecting files.
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 * @version 1.0
 */
public class UploadDocumentDialog extends JDialog implements ActionListener {

    private Volunteer volunteer;

    JLabel lbl_title, lbl_docType, lbl_expiry, lbl_path, lbl_hint;
    JComboBox<Document.DOCType> cmb_docType;
    JTextField txt_expiry, txt_path;
    JCheckBox chk_noExpiry;
    JButton btn_browse, btn_upload, btn_cancel;

    /**
     * Constructor for UploadDocumentDialog.
     * Initializes the dialog with document upload controls.
     *
     * @param parent the parent JFrame
     * @param volunteer the volunteer uploading the document
     */
    UploadDocumentDialog(JFrame parent, Volunteer volunteer){

        super(parent, "Upload Document", true);
        this.volunteer = volunteer;

        setLayout(null);
        setSize(500, 450);

        // Title
        lbl_title = new JLabel("<html><b>Upload Document</b></html>");
        lbl_title.setFont(new Font("Arial", Font.BOLD, 16));
        lbl_title.setBounds(170, 20, 200, 30);
        add(lbl_title);

        // Document Type
        lbl_docType = new JLabel("Document Type:");
        lbl_docType.setBounds(50, 70, 130, 30);
        add(lbl_docType);

        cmb_docType = new JComboBox<>(Document.DOCType.values());
        cmb_docType.setBounds(180, 70, 250, 30);
        add(cmb_docType);

        // Expiry Date
        lbl_expiry = new JLabel("Expiry Date:");
        lbl_expiry.setBounds(50, 120, 130, 30);
        add(lbl_expiry);

        txt_expiry = new JTextField();
        txt_expiry.setBounds(180, 120, 250, 30);
        add(txt_expiry);

        // No expiry checkbox
        chk_noExpiry = new JCheckBox("No expiry date");
        chk_noExpiry.setBounds(180, 175, 150, 30);
        chk_noExpiry.addActionListener(this);
        add(chk_noExpiry);

        // Document Path
        lbl_path = new JLabel("Document Path:");
        lbl_path.setBounds(50, 220, 130, 30);
        add(lbl_path);

        txt_path = new JTextField();
        txt_path.setBounds(180, 220, 250, 30);
        add(txt_path);

        // Browse Button
        btn_browse = new JButton("Browse...");
        btn_browse.setBounds(180, 260, 100, 30);
        btn_browse.addActionListener(this);
        add(btn_browse);

        // Upload Button
        btn_upload = new JButton("Upload");
        btn_upload.setBounds(180, 320, 100, 35);
        btn_upload.setBackground(new Color(34, 139, 34));
        btn_upload.setForeground(Color.WHITE);
        btn_upload.addActionListener(this);
        add(btn_upload);

        // Cancel Button
        btn_cancel = new JButton("Cancel");
        btn_cancel.setBounds(290, 320, 100, 35);
        btn_cancel.addActionListener(this);
        add(btn_cancel);

        setLocationRelativeTo(parent);
        setVisible(true);

    }

    /**
     * Handles action events from dialog components.
     * Processes checkbox selection, file browsing, upload, and cancel actions.
     *
     * @param e the action event triggered by user interaction
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == chk_noExpiry) {
            txt_expiry.setEnabled(!chk_noExpiry.isSelected());
        } else if (src == btn_browse) {
            browseFile();
        } else if (src == btn_upload) {
            handleUpload();
        } else if (src == btn_cancel) {
            this.dispose();
        }
    }

    /**
     * Opens a file chooser dialog for selecting document file.
     * Updates the file path text field with selected file's absolute path.
     */
    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            txt_path.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Handles the upload of a document.
     * Validates document type, expiry date, and file path.
     * Creates a Document object and adds it to the volunteer's profile.
     */
    private void handleUpload() {
        Document.DOCType docType = (Document.DOCType) cmb_docType.getSelectedItem();
        String imagePath = txt_path.getText().trim();

        if (imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please provide document path",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate expiryDate = null;

        if (!chk_noExpiry.isSelected()) {
            String dateStr = txt_expiry.getText().trim();

            if (dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter expiry date or check 'No expiry date'",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                expiryDate = LocalDate.parse(dateStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format. Use YYYY-MM-DD",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Document doc = new Document(docType, expiryDate, imagePath, volunteer);
        volunteer.addDocument(doc);

        JOptionPane.showMessageDialog(this,
                "Document uploaded successfully!\nTotal documents: " + volunteer.getDocuments().size(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();
    }
}
