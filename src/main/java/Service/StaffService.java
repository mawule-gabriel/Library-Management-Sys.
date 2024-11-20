package Service;

import DAO.StaffDAO;
import Entity.Staff;

import java.time.LocalDate;
import java.util.List;

public class StaffService {
    private final StaffDAO staffDAO;

    public StaffService() {
        this.staffDAO = new StaffDAO();
    }

    // Add a new staff member
    public void addStaff(Staff staff) {
        if (staff.getFirstName() == null || staff.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty.");
        }
        if (staff.getLastName() == null || staff.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty.");
        }
        if (staff.getRole() == null || staff.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty.");
        }
        if (staff.getEmail() == null || !staff.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email address.");
        }
        if (staff.getPhoneNumber() == null || staff.getPhoneNumber().length() < 10) {
            throw new IllegalArgumentException("Invalid phone number.");
        }
        if (staff.getHireDate() == null || staff.getHireDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Hire date cannot be in the future.");
        }

        staffDAO.addStaff(staff);
    }

    // Retrieve all staff members
    public List<Staff> getAllStaff() {
        return staffDAO.getAllStaff();
    }
}
