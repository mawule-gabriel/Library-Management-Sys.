package Service;

import DAO.ReservationDAO;
import Entity.Enums.ReservationStatus;
import Entity.Reservation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationService {
    private final ReservationDAO reservationDAO;

    public ReservationService() {
        this.reservationDAO = new ReservationDAO();
    }

    // Add a new reservation
    public void addReservation(Reservation reservation) throws SQLException {
        if (reservation.getPatronId() <= 0) {
            throw new IllegalArgumentException("Invalid Patron ID.");
        }
        if (reservation.getBookId() <= 0) {
            throw new IllegalArgumentException("Invalid Book ID.");
        }
        if (reservation.getReservationDate() == null) {
            throw new IllegalArgumentException("Reservation date cannot be null.");
        }
        if (reservation.getDueDate() != null && reservation.getDueDate().isBefore(reservation.getReservationDate())) {
            throw new IllegalArgumentException("Due date cannot be before the reservation date.");
        }
        reservationDAO.addReservation(reservation);
    }



    // Retrieve all reservations
    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.getAllReservations();
    }



    // Delete a reservation
    public void deleteReservation(int reservationId) throws SQLException {
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null) {
            throw new IllegalArgumentException("Cannot delete. No reservation found with ID: " + reservationId);
        }
        reservationDAO.deleteReservation(reservationId);
    }
}
