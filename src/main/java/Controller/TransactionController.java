package Controller;

import Entity.Enums.BookStatus;
import Entity.Enums.TransactionType;
import Entity.Transaction;
import Service.BookService;
import Service.TransactionService;

import java.math.BigDecimal;
import java.sql.Date; // Import java.sql.Date for the correct type
import java.time.LocalDate;
import java.util.List;

public class TransactionController {

    private final TransactionService transactionService;
    private final BookService bookService;

    public TransactionController() {
        this.transactionService = new TransactionService();
        this.bookService = new BookService();
    }

    // Add a new transaction
    public void addTransaction(Transaction transaction) {
        try {
            transactionService.addTransaction(transaction);
            System.out.println("Transaction added successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;  // Rethrow to handle in view
        } catch (RuntimeException e) {
            System.err.println("Error adding transaction: " + e.getMessage());
            throw e;  // Rethrow to handle in view
        }
    }



    // Get all transactions
    public List<Transaction> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            }
            return transactions;
        } catch (RuntimeException e) {
            System.err.println("Error retrieving transactions: " + e.getMessage());
            throw e;  // Rethrow to handle in view
        }
    }



    // Delete a transaction by ID
    public void deleteTransaction(int transactionId) {
        try {
            transactionService.deleteTransaction(transactionId);
            System.out.println("Transaction deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;  // Rethrow to handle in view
        } catch (RuntimeException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            throw e;  // Rethrow to handle in view
        }
    }

    public void updateTransaction(Transaction transaction) {

    }
    // Process a book borrowing
    public void borrowBook(int patronId, int bookId) {
        try {
            transactionService.borrowBook(patronId, bookId);
            System.out.println("Book borrowed successfully.");
        } catch (Exception e) {
            System.err.println("Error borrowing book: " + e.getMessage());
            throw e;
        }
    }

    // Process a book return
    public void returnBook(int transactionId) {
        try {
            transactionService.returnBook(transactionId);
            System.out.println("Book returned successfully.");
        } catch (Exception e) {
            System.err.println("Error returning book: " + e.getMessage());
            throw e;
        }
    }
}