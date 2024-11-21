package Service;

import DAO.TransactionDAO;
import Entity.Transaction;

import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    // Add a new transaction
    public void addTransaction(Transaction transaction) {
        validateTransaction(transaction);

        try {
            transactionDAO.addTransaction(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add transaction: " + e.getMessage(), e);
        }
    }



    // Get all transactions
    public List<Transaction> getAllTransactions() {
        try {
            return transactionDAO.getAllTransactions();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve all transactions: " + e.getMessage(), e);
        }
    }



    // Delete a transaction by ID
    public void deleteTransaction(int transactionId) {
        if (transactionId <= 0) {
            throw new IllegalArgumentException("Transaction ID must be greater than zero.");
        }

        try {
            transactionDAO.deleteTransaction(transactionId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete transaction: " + e.getMessage(), e);
        }
    }

    // Private validation method for transactions
    private void validateTransaction(Transaction transaction) {
        if (transaction.getPatronId() <= 0) {
            throw new IllegalArgumentException("Invalid patron ID.");
        }
        if (transaction.getBookId() <= 0) {
            throw new IllegalArgumentException("Invalid book ID.");
        }
        if (transaction.getBorrowDate() == null || transaction.getBorrowDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Borrow date cannot be null or in the future.");
        }
        if (transaction.getDueDate() == null || transaction.getDueDate().isBefore(transaction.getBorrowDate())) {
            throw new IllegalArgumentException("Due date cannot be null or before the borrow date.");
        }
        if (transaction.getTransactionType() == null) {
            throw new IllegalArgumentException("Transaction type cannot be null.");
        }
    }
}
