package gui;

import assignment.*;
import java.io.*;
import java.time.LocalDate;

/**
 * This Manages the saving and loading CRS data using TEXT files (CSV format)
 * No serialization required - human-readable files
 *
 * @author Sandesh Pokharel
 * @student_id E2300576
 */
public class TextDataManager {

    private static final String USERS_FILE = "users.txt";
    private static final String TRIPS_FILE = "trips.txt";
    private static final String APPLICATIONS_FILE = "applications.txt";
    private static final String DOCUMENTS_FILE = "documents.txt";

    /**
     * This Save all the CRS data to text files
     * @param crs The CRS object to save
     * @return true if successful
     */
    public static boolean saveData(CRS crs) {
        try {
            saveUsers(crs);
            saveTrips(crs);
            saveApplications(crs);
            saveDocuments(crs);

            System.out.println("✓ Data saved successfully!");
            System.out.println("  Files: users.txt, trips.txt, applications.txt, documents.txt");
            return true;

        } catch (IOException e) {
            System.err.println("✗ Error saving data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This static Method Loads all the CRS data from text files
     * @return CRS object with loaded data, or null if failed
     */
    public static CRS loadData() {
        try {
            CRS crs = new CRS();

            if (!dataFilesExist()) {
                System.out.println("No saved data files found");
                return null;
            }

            loadUsers(crs);
            loadTrips(crs);
            loadApplications(crs);
            loadDocuments(crs);

            System.out.println("✓ Data loaded successfully!");
            System.out.println("  Users: " + crs.getAllUsers().size());
            System.out.println("  Trips: " + crs.getAllTrips().size());

            return crs;

        } catch (IOException e) {
            System.err.println("✗ Error loading data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if data files exist
     * @return true if at least the users file exists
     */
    public static boolean dataFilesExist() {
        return new File(USERS_FILE).exists();
    }


    /**
     * This Saves user to text file
     */
    private static void saveUsers(CRS crs) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE));

        for (User user : crs.getAllUsers()) {
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                writer.println("STAFF|" +
                        staff.getUsername() + "|" +
                        staff.getPassword() + "|" +
                        staff.getName() + "|" +
                        staff.getPhone() + "|" +
                        staff.getPosition() + "|" +
                        staff.getDateJoined());
            } else if (user instanceof Volunteer) {
                Volunteer vol = (Volunteer) user;
                writer.println("VOLUNTEER|" +
                        vol.getUsername() + "|" +
                        vol.getPassword() + "|" +
                        vol.getName() + "|" +
                        vol.getPhone());
            }
        }

        writer.close();
        System.out.println("  ✓ Saved " + crs.getAllUsers().size() + " users to " + USERS_FILE);
    }

    /**
     * This Saves trip to text file
     */
    private static void saveTrips(CRS crs) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(TRIPS_FILE));

        for (Trip trip : crs.getAllTrips()) {
            writer.println(trip.getTripId() + "|" +
                    trip.getDescription() + "|" +
                    trip.getTripDate() + "|" +
                    trip.getLocation() + "|" +
                    trip.getNumVolunteers() + "|" +
                    trip.getCrisisType() + "|" +
                    trip.getOrganizerStaff().getUsername());
        }

        writer.close();
        System.out.println("  ✓ Saved " + crs.getAllTrips().size() + " trips to " + TRIPS_FILE);
    }

    /**
     * This Save applications to the text file
     */
    private static void saveApplications(CRS crs) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(APPLICATIONS_FILE));
        int count = 0;

        for (Trip trip : crs.getAllTrips()) {
            for (Application app : trip.getApplications()) {
                writer.println(app.getApplicationID() + "|" +
                        app.getApplicationDate() + "|" +
                        app.getStatus() + "|" +
                        (app.getRemarks() != null ? app.getRemarks().replace("|", ",") : "NONE") + "|" +
                        app.getVolunteer().getUsername() + "|" +
                        app.getTrip().getTripId());
                count++;
            }
        }

        writer.close();
        System.out.println("  ✓ Saved " + count + " applications to " + APPLICATIONS_FILE);
    }

    /**
     * This Saves documents to text file
     */
    private static void saveDocuments(CRS crs) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(DOCUMENTS_FILE));
        int count = 0;

        for (User user : crs.getAllUsers()) {
            if (user instanceof Volunteer) {
                Volunteer vol = (Volunteer) user;
                for (Document doc : vol.getDocuments()) {
                    writer.println(doc.getDocumentType() + "|" +
                            (doc.getExpiryDate() != null ? doc.getExpiryDate() : "NONE") + "|" +
                            doc.getImagePath() + "|" +
                            vol.getUsername());
                    count++;
                }
            }
        }

        writer.close();
        System.out.println("  ✓ Saved " + count + " documents to " + DOCUMENTS_FILE);
    }


    /**
     * This Loads the users data from text file
     */
    private static void loadUsers(CRS crs) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE));
        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");

            if (parts[0].equals("STAFF")) {
                crs.recordNewStaff(
                        parts[1],  // username
                        parts[2],  // password
                        parts[3],  // name
                        parts[4],  // phone
                        parts[5],  // position
                        LocalDate.parse(parts[6])  // dateJoined
                );
                count++;
            } else if (parts[0].equals("VOLUNTEER")) {
                crs.registerVolunteer(
                        parts[1],  // username
                        parts[2],  // password
                        parts[3],  // name
                        parts[4]   // phone
                );
                count++;
            }
        }

        reader.close();
        System.out.println("  ✓ Loaded " + count + " users from " + USERS_FILE);
    }

    /**
     * This Loads the trips from text file
     */
    private static void loadTrips(CRS crs) throws IOException {
        File file = new File(TRIPS_FILE);
        if (!file.exists()) {
            System.out.println("  ⚠ No trips file found");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(TRIPS_FILE));
        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");

            // Find the staff organizer
            Staff organizer = (Staff) crs.findUserByUsername(parts[6]);

            if (organizer != null) {
                crs.createTrip(
                        parts[1],  // description
                        LocalDate.parse(parts[2]),  // date
                        parts[3],  // location
                        Integer.parseInt(parts[4]),  // numVolunteers
                        CRS.CrisisType.valueOf(parts[5]),  // crisisType
                        organizer
                );
                count++;
            }
        }

        reader.close();
        System.out.println("  ✓ Loaded " + count + " trips from " + TRIPS_FILE);
    }

    /**
     * This Load applications from text file
     */
    private static void loadApplications(CRS crs) throws IOException {
        File file = new File(APPLICATIONS_FILE);
        if (!file.exists()) {
            System.out.println("  ⚠ No applications file found");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(APPLICATIONS_FILE));
        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");

            // Find volunteer and trip
            Volunteer volunteer = (Volunteer) crs.findUserByUsername(parts[4]);
            Trip trip = findTripById(crs, Integer.parseInt(parts[5]));

            if (volunteer != null && trip != null) {
                // Create application
                Application app = new Application(
                        parts[0],  // applicationID
                        LocalDate.parse(parts[1]),  // applicationDate
                        volunteer,
                        trip
                );

                // Set status and remarks
                app.setStatus(Application.Status.valueOf(parts[2]));
                if (!parts[3].equals("NONE")) {
                    app.setRemarks(parts[3]);
                }

                // Add to trip and volunteer
                trip.addApplication(app);
                volunteer.addApplication(app);
                count++;
            }
        }

        reader.close();
        System.out.println("  ✓ Loaded " + count + " applications from " + APPLICATIONS_FILE);
    }

    /**
     * This Load documents from text file
     */
    private static void loadDocuments(CRS crs) throws IOException {
        File file = new File(DOCUMENTS_FILE);
        if (!file.exists()) {
            System.out.println("  ⚠ No documents file found");
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(DOCUMENTS_FILE));
        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");

            Volunteer volunteer = (Volunteer) crs.findUserByUsername(parts[3]);

            if (volunteer != null) {
                LocalDate expiry = parts[1].equals("NONE") ? null : LocalDate.parse(parts[1]);

                Document doc = new Document(
                        Document.DOCType.valueOf(parts[0]),
                        expiry,
                        parts[2],
                        volunteer
                );

                volunteer.addDocument(doc);
                count++;
            }
        }

        reader.close();
        System.out.println("  ✓ Loaded " + count + " documents from " + DOCUMENTS_FILE);
    }


    /**
     * Find trip by ID
     */
    private static Trip findTripById(CRS crs, int tripId) {
        for (Trip trip : crs.getAllTrips()) {
            if (trip.getTripId() == tripId) {
                return trip;
            }
        }
        return null;
    }
}