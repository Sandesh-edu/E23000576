package gui;

import assignment.CRS;

/**
 * Main entry point for CRS GUI Application
 * Loads data from files or starts with empty system
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 * @date March 2026
 */
public class CRSGUI {

    public static void main(String[] args) {
        CRS crs;

        // Try to load existing data from text files
        if (TextDataManager.dataFilesExist()) {
            System.out.println("=================================");
            System.out.println("Loading saved data from files...");
            System.out.println("=================================");

            crs = TextDataManager.loadData();

            if (crs == null) {
                System.out.println("Failed to load. Starting with empty system.");
                crs = new CRS();
            }
        } else {
            System.out.println("=================================");
            System.out.println("No saved data found.");
            System.out.println("Starting with empty system.");
            System.out.println("=================================");

            crs = new CRS();
        }



        // Initialize LoginPage with CRS
        LoginPage.setCRS(crs);
        new LoginPage();
    }
}