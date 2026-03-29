package gui;

import assignment.CRS;

/**
 * This is the Main entry point for CRS GUI Application
 * It Loads data from files or starts with empty system
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 */
public class CRSGUI {

    public static void main(String[] args) {
        CRS crs;

        // Try to load existing data from text files
        if (TextDataManager.dataFilesExist()) {
            System.out.println("Loading saved data from files...");

            crs = TextDataManager.loadData();

            if (crs == null) {
                System.out.println("Failed to load. Starting with empty system.");
                crs = new CRS();
            }
        } else {
            System.out.println("No saved data found.");
            System.out.println("Starting with empty system.");

            crs = new CRS();
        }



        //This Initialize LoginPage with CRS
        LoginPage.setCRS(crs);
        new LoginPage();
    }
}