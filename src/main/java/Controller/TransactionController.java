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

    public void borrowBook(int bookId, int patronId) {
        try {
            // Convert LocalDate to java.sql.Date
            Date borrowDate = Date.valueOf(LocalDate.now());
            Date dueDate = Date.valueOf(LocalDate.now().plusDays(14));

            // Set the fine as BigDecimal
            BigDecimal fine = BigDecimal.ZERO;

            // Create the transaction
            Transaction transaction = new Transaction(
                    0, patronId, bookId, borrowDate.toLocalDate(), null,
                    dueDate.toLocalDate(), fine, TransactionType.BORROW // Use TransactionType enum directly
            );

            // Add the transaction
            transactionService.addTransaction(transaction);

            // Update the book status
            bookService.updateBookStatus(bookId, BookStatus.BORROWED);
            System.out.println("Book borrowed successfully.");
        } catch (Exception e) {
            System.err.println("Error borrowing book: " + e.getMessage());
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

    // Update the fine for a transaction
    public void updateTransactionFine(int transactionId, BigDecimal fine) {
        try {
            transactionService.updateTransactionFine(transactionId, fine);
            System.out.println("Transaction fine updated successfully.");
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            throw e;  // Rethrow to handle in view
        } catch (RuntimeException e) {
            System.err.println("Error updating transaction fine: " + e.getMessage());
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
}