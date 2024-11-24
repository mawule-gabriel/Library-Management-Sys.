package Service;

import DAO.TransactionDAO;
import Entity.Enums.BookStatus;
import Entity.Enums.TransactionType;
import Entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final BookService bookService;


    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.bookService = new BookService();
    }

    // Add a new transaction
    public void addTransaction(Transaction transaction) {
        validateTransaction(transaction);
        try {
            transactionDAO.addTransaction(transaction);

            // Update book status based on transaction type
            if (transaction.getTransactionType() == TransactionType.BORROW) {
                bookService.updateBookStatus(transaction.getBookId(), BookStatus.BORROWED);
            } else if (transaction.getTransactionType() == TransactionType.RETURN) {
                bookService.updateBookStatus(transaction.getBookId(), BookStatus.AVAILABLE);
            }
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

    public void borrowBook(int patronId, int bookId) {
        try {
            // Create new borrow transaction
            Transaction transaction = new Transaction(
                    0, // ID will be generated
                    patronId,
                    bookId,
                    LocalDate.now(),
                    null,
                    LocalDate.now().plusWeeks(2), // 2 weeks due date
                    BigDecimal.ZERO,
                    TransactionType.BORROW
            );

            // Validate and add transaction
            validateTransaction(transaction);
            transactionDAO.addTransaction(transaction);

            // Update book status to BORROWED
            bookService.updateBookStatus(bookId, BookStatus.BORROWED);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process borrow transaction: " + e.getMessage(), e);
        }
    }

    // Process a book return
    public void returnBook(int transactionId) {
        try {
            // Get the original transaction
            Transaction transaction = transactionDAO.getTransactionById(transactionId);
            if (transaction == null) {
                throw new RuntimeException("Transaction not found");
            }

            // Update transaction details
            transaction.setReturnDate(LocalDate.now());
            transaction.setTransactionType(TransactionType.RETURN);

            // Calculate fine if returned late
            if (LocalDate.now().isAfter(transaction.getDueDate())) {
                long daysLate = transaction.getDueDate().until(LocalDate.now()).getDays();
                transaction.setFine(BigDecimal.valueOf(daysLate)); // $1 per day
            }

            // Update transaction in database
            transactionDAO.addTransaction(transaction); // This will update the existing transaction

            // Update book status to AVAILABLE
            bookService.updateBookStatus(transaction.getBookId(), BookStatus.AVAILABLE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process return transaction: " + e.getMessage(), e);
        }
    }



}
