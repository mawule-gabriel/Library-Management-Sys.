package Entity;

import Entity.Enums.ReservationStatus;
import DAO.PatronDAO;
import DAO.BookDAO;
import java.sql.SQLException;
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

    // This method fetches the Patron's full name using PatronDAO.
    public String getPatronName() throws SQLException {
        Patron patron = PatronDAO.getPatronById(this.patronId);  // Fetch Patron by patronId
        if (patron != null) {
            return patron.getFirstName() + " " + patron.getLastName();  // Combine first and last names
        } else {
            return "Unknown Patron";  // If no Patron is found, return a default name
        }
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

    // This method fetches the Book's title using a BookDAO or any suitable service.
    public String getBookTitle() throws SQLException {
        Book book = BookDAO.getBookById(this.bookId);  // Fetch Book by bookId
        if (book != null) {
            return book.getTitle();  // Return the book title
        } else {
            return "Unknown Book";  // Return a default value if the book is not found
        }
    }

}
