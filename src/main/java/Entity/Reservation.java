package Entity;

import Entity.Enums.ReservationStatus;

import java.time.LocalDate;

public class Reservation {

    private int reservationId;
    private int patronId;
    private int bookId;
    private LocalDate reservationDate;  // Change Date to LocalDate
    private ReservationStatus status;  // Use ReservationStatus enum
    private LocalDate dueDate;  // Change Date to LocalDate

    // Constructor with all parameters
    public Reservation(int reservationId, int patronId, int bookId, LocalDate reservationDate, ReservationStatus status, LocalDate dueDate) {
        this.reservationId = reservationId;
        this.patronId = patronId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", patronId=" + patronId +
                ", bookId=" + bookId +
                ", reservationDate=" + reservationDate +
                ", status=" + status +
                ", dueDate=" + dueDate +
                '}';
    }
}
