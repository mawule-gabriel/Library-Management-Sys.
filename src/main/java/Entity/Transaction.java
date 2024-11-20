package Entity;

import Entity.Enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private int patronId;
    private int bookId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private BigDecimal fine; // Changed from double to BigDecimal
    private TransactionType transactionType;

    // Constructor with all parameters
    public Transaction(int transactionId, int patronId, int bookId, LocalDate borrowDate, LocalDate returnDate, LocalDate dueDate, BigDecimal fine, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.patronId = patronId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.fine = fine;
        this.transactionType = transactionType;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getFine() {
        return fine; // Return type changed to BigDecimal
    }

    public void setFine(BigDecimal fine) { // Method updated to accept BigDecimal
        this.fine = fine;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", patronId=" + patronId +
                ", bookId=" + bookId +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", dueDate=" + dueDate +
                ", fine=" + fine +
                ", transactionType=" + transactionType +
                '}';
    }
}