package Controller;

import Entity.Patron;
import Service.PatronService;

import java.sql.SQLException;
import java.util.List;

public class PatronController {

    private final PatronService patronService;

    public PatronController() {
        this.patronService = new PatronService();
    }

    // Add a new patron
    public void addPatron(Patron patron) {
        try {
            patronService.addPatron(patron);
            System.out.println("Patron added successfully: " + patron);
        } catch (SQLException e) {
            System.err.println("Error adding patron: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }

    // Get patron by ID
    public Patron getPatronById(int patronId) {
        try {
            Patron patron = patronService.getPatronById(patronId);
            if (patron != null) {
                System.out.println("Patron found: " + patron);
                return patron;
            } else {
                System.out.println("No patron found with ID: " + patronId);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patron: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
        }
        return null; // Return null if not found
    }

    // Get all patrons
    public List<Patron> getAllPatrons() {
        try {
            List<Patron> patrons = patronService.getAllPatrons();
            if (!patrons.isEmpty()) {
                return patrons; // Return the list of patrons
            } else {
                System.out.println("No patrons found.");
                return List.of(); // Return an empty list if none found
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patrons: " + e.getMessage());
            return List.of(); // Return an empty list in case of an error
        }
    }

    // Update a patron's address
    public void updatePatronAddress(int patronId, String address) {
        try {
            patronService.updatePatronAddress(patronId, address);
            System.out.println("Patron's address updated successfully for ID: " + patronId);
        } catch (SQLException e) {
            System.err.println("Error updating patron address: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }

    // Delete a patron
    public void deletePatron(int patronId) {
        try {
            patronService.deletePatron(patronId);
            System.out.println("Patron deleted successfully with ID: " + patronId);
        } catch (SQLException e) {
            System.err.println("Error deleting patron: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }
}