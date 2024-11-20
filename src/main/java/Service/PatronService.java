package Service;

import DAO.PatronDAO;
import Entity.Patron;

import java.sql.SQLException;
import java.util.List;

public class PatronService {
    private final PatronDAO patronDAO;

    public PatronService() {
        this.patronDAO = new PatronDAO();
    }

    // Add a new patron
    public void addPatron(Patron patron) throws SQLException {
        if (patron.getFirstName() == null || patron.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("Patron first name cannot be null or empty.");
        }
        if (patron.getLastName() == null || patron.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Patron last name cannot be null or empty.");
        }
        if (patron.getEmail() == null || patron.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Patron email cannot be null or empty.");
        }
        patronDAO.addPatron(patron);
    }

    // Retrieve a patron by their ID
    public Patron getPatronById(int patronId) throws SQLException {
        Patron patron = patronDAO.getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("No patron found with ID: " + patronId);
        }
        return patron;
    }

    // Retrieve all patrons
    public List<Patron> getAllPatrons() throws SQLException {
        return patronDAO.getAllPatrons();
    }

    // Update a patron's address
    public void updatePatronAddress(int patronId, String address) throws SQLException {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        Patron patron = patronDAO.getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Cannot update address. No patron found with ID: " + patronId);
        }
        patronDAO.updatePatronAddress(patronId, address);
    }

    // Delete a patron by their ID
    public void deletePatron(int patronId) throws SQLException {
        Patron patron = patronDAO.getPatronById(patronId);
        if (patron == null) {
            throw new IllegalArgumentException("Cannot delete. No patron found with ID: " + patronId);
        }
        patronDAO.deletePatron(patronId);
    }

    public List<Patron> searchPatronsByName(String name) throws SQLException {
        return patronDAO.searchPatronsByName(name);
    }
}
