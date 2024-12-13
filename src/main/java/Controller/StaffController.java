package Controller;

import DAO.StaffDAO;
import Entity.Staff;
import Service.StaffService;

import java.util.List;

public class StaffController {

    private final StaffService staffService;
    private final StaffDAO staffDAO;

    public StaffController() {
        this.staffDAO = new StaffDAO();
        this.staffService = new StaffService();
    }

    // Add a new staff member
    public void addStaff(Staff staff) {
        try {
            staffService.addStaff(staff);
            System.out.println("Staff member added successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }

    // Get all staff members
    public void getAllStaff() {
        try {
            List<Staff> staffList = staffService.getAllStaff();
            if (!staffList.isEmpty()) {
                staffList.forEach(System.out::println);
            } else {
                System.out.println("No staff members found.");
            }
        } catch (Exception e) {
            System.err.println("Error retrieving staff members: " + e.getMessage());
        }
    }

    // Authenticate staff based on email and password
    public Staff authenticateStaff(String email, String password) {
        return staffDAO.getStaffByCredentials(email, password);
    }
}
