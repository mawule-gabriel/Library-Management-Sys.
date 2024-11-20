package DAO;

import DatabaseConnection.DatabaseUtil;
import Entity.Enums.ReservationStatus;
import Entity.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public void addReservation(Reservation reservation)throws SQLException {
        String query = "INSERT INTO Reservations (patron_id, book_id, reservation_date, status, due_date) " + "VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, reservation.getPatronId());
            pstmt.setInt(2, reservation.getBookId());
            pstmt.setDate(3, Date.valueOf(reservation.getReservationDate()));
            pstmt.executeUpdate();
        }

    }

    public Reservation getReservationById(int reservationId)throws SQLException{
        String query = "SELECT * FROM Reservations WHERE reservation_id = ?";
        try(Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, reservationId);
            try(ResultSet rs = pstmt.executeQuery()){
                return new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("patron_id"),
                        rs.getInt("book_id"),
                        rs.getDate("reservation_date").toLocalDate(),
                        ReservationStatus.valueOf(rs.getString("status")),
                        rs.getDate("due_date").toLocalDate()
                );
            }
        }
    }

    public List<Reservation> getAllReservations()throws SQLException{
        String query = "SELECT * FROM Reservations";
        List<Reservation> reservations = new ArrayList<>();
        try(Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()){
            while(rs.next()){
                reservations.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("patron_id"),
                        rs.getInt("book_id"),
                        rs.getDate("reservation_date").toLocalDate(),
                        ReservationStatus.valueOf(rs.getString("status")),
                        rs.getDate("due_date").toLocalDate()
                ));
            }
        }
        return reservations;
    }

    public void updateReservationStatus(int reservationId, ReservationStatus status)throws SQLException{
        String query = "UPDATE Reservations SET status = ? WHERE reservation_id = ?";
        try(Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setString(1, status.name());
            pstmt.setInt(2, reservationId);
            pstmt.executeUpdate();
        }
    }

    public void deleteReservation(int reservationId) throws SQLException{
        String query = "DELETE FROM Reservations WHERE reservation_id = ?";
        try(Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }

    }


}
