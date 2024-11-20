package DAO;

import DatabaseConnection.DatabaseUtil;
import Entity.Patron;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatronDAO {

    public void addPatron(Patron patron) throws SQLException {
        String query = "INSERT INTO Patrons (first_name, last_name, email, phone_number, address, membership_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, patron.getFirstName());
            pstmt.setString(2, patron.getLastName());
            pstmt.setString(3, patron.getEmail());
            pstmt.setString(4, patron.getPhoneNumber());
            pstmt.setString(5, patron.getAddress());
            // Converting LocalDate to java.sql.Date before inserting into database
            pstmt.setDate(6, patron.getMembershipDate() != null ? Date.valueOf(patron.getMembershipDate()) : null);
            pstmt.executeUpdate();
        }
    }

    public Patron getPatronById(int patronId) throws SQLException {
        String query = "SELECT * FROM Patrons WHERE patron_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patronId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Patron(
                            rs.getInt("patron_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getString("address"),
                            rs.getDate("membership_date") != null ? rs.getDate("membership_date").toLocalDate() : null
                    );
                }
            }
        }
        return null;
    }

    public List<Patron> getAllPatrons() throws SQLException {
        String query = "SELECT * FROM Patrons";
        List<Patron> patrons = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                patrons.add(new Patron(
                        rs.getInt("patron_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getDate("membership_date") != null ? rs.getDate("membership_date").toLocalDate() : null
                ));
            }
        }
        return patrons;
    }

    public void updatePatronAddress(int patronId, String address) throws SQLException {
        String query = "UPDATE Patrons SET address = ? WHERE patron_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, address);
            pstmt.setInt(2, patronId);
            pstmt.executeUpdate();
        }
    }

    public void deletePatron(int patronId) throws SQLException {
        String query = "DELETE FROM Patrons WHERE patron_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patronId);
            pstmt.executeUpdate();
        }
    }

    public List<Patron> searchPatronsByName(String name) throws SQLException {
        String query = "SELECT * FROM Patrons WHERE first_name LIKE ? OR last_name LIKE ?";
        List<Patron> patrons = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            String searchTerm = "%" + name + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    patrons.add(new Patron(
                            rs.getInt("patron_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getString("address"),
                            rs.getDate("membership_date") != null ? rs.getDate("membership_date").toLocalDate() : null
                    ));
                }
            }
        }
        return patrons;
    }
}
