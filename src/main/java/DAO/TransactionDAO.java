package DAO;

import DatabaseConnection.DatabaseUtil;
import Entity.Transaction;
import Entity.Enums.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Add a new transaction to the database
    public void addTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO Transactions (patron_id, book_id, borrow_date, return_date, due_date, fine, transaction_type) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transaction.getPatronId());
            pstmt.setInt(2, transaction.getBookId());
            pstmt.setDate(3, Date.valueOf(transaction.getBorrowDate()));
            pstmt.setDate(4, transaction.getReturnDate() != null ? Date.valueOf(transaction.getReturnDate()) : null);
            pstmt.setDate(5, Date.valueOf(transaction.getDueDate()));

            // Directly set BigDecimal fine
            pstmt.setBigDecimal(6, transaction.getFine());

            pstmt.setString(7, transaction.getTransactionType().name());
            pstmt.executeUpdate();
        }
    }

    // Retrieve a transaction by its ID
    public Transaction getTransactionById(int transactionId) throws SQLException {
        String query = "SELECT * FROM Transactions WHERE transaction_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve transaction_type and handle potential case insensitivity
                    String transactionTypeString = rs.getString("transaction_type");
                    TransactionType transactionType = null;

                    // Check if the string is not null and handle the case insensitivity
                    if (transactionTypeString != null) {
                        try {
                            transactionType = TransactionType.valueOf(transactionTypeString.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            // Handle unexpected values (e.g., log an error or set to a default value)
                            transactionType = TransactionType.RETURN; // Default value or log error
                        }
                    }

                    return new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getInt("patron_id"),
                            rs.getInt("book_id"),
                            rs.getDate("borrow_date").toLocalDate(),
                            rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                            rs.getDate("due_date").toLocalDate(),
                            rs.getBigDecimal("fine"),
                            transactionType // Set the parsed transaction type
                    );
                }
            }
        }
        return null;
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        String query = "SELECT * FROM Transactions";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Retrieve transaction_type and handle potential case insensitivity
                String transactionTypeString = rs.getString("transaction_type");
                TransactionType transactionType = null;

                // Check if the string is not null and handle the case insensitivity
                if (transactionTypeString != null) {
                    try {
                        transactionType = TransactionType.valueOf(transactionTypeString.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        // Handle unexpected values (e.g., log an error or set to a default value)
                        transactionType = TransactionType.RETURN; // Default value or log error
                    }
                }

                transactions.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("patron_id"),
                        rs.getInt("book_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                        rs.getDate("due_date").toLocalDate(),
                        rs.getBigDecimal("fine"),
                        transactionType // Set the parsed transaction type
                ));
            }
        }
        return transactions;
    }


    // Update the fine for a specific transaction
    public void updateTransactionFine(int transactionId, BigDecimal fine) throws SQLException {
        String query = "UPDATE Transactions SET fine = ? WHERE transaction_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBigDecimal(1, fine);
            pstmt.setInt(2, transactionId);
            pstmt.executeUpdate();
        }
    }

    // Optional method to update fine using double
    public void updateTransactionFine(int transactionId, double fine) throws SQLException {
        updateTransactionFine(transactionId, BigDecimal.valueOf(fine));
    }

    // Delete a transaction by its ID
    public void deleteTransaction(int transactionId) throws SQLException {
        String query = "DELETE FROM Transactions WHERE transaction_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, transactionId);
            pstmt.executeUpdate();
        }
    }

    public boolean borrowBook(int patronId, int bookId) throws SQLException {
        // First, check if the book is available for borrowing
        String checkAvailabilityQuery = "SELECT status FROM Books WHERE book_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkAvailabilityQuery)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getString("status").equalsIgnoreCase("available")) {
                    // The book is available, create a borrow transaction

                    // Get the current date and calculate the due date (e.g., 2 weeks from now)
                    LocalDate borrowDate = LocalDate.now();
                    LocalDate dueDate = borrowDate.plusWeeks(2);

                    // Start a transaction (disable auto-commit)
                    conn.setAutoCommit(false);
                    try {
                        // Insert the transaction record in the Transactions table
                        String insertTransactionQuery = "INSERT INTO Transactions (patron_id, book_id, borrow_date, due_date, transaction_type) " +
                                "VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmtInsert = conn.prepareStatement(insertTransactionQuery, Statement.RETURN_GENERATED_KEYS)) {
                            pstmtInsert.setInt(1, patronId);
                            pstmtInsert.setInt(2, bookId);
                            pstmtInsert.setDate(3, Date.valueOf(borrowDate));
                            pstmtInsert.setDate(4, Date.valueOf(dueDate));
                            pstmtInsert.setString(5, "BORROW"); // Transaction type is BORROW
                            int affectedRows = pstmtInsert.executeUpdate();

                            if (affectedRows > 0) {
                                // Get the generated transaction ID
                                try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
                                    if (generatedKeys.next()) {
                                        int transactionId = generatedKeys.getInt(1);

                                        // Now update the book status to "BORROWED"
                                        String updateBookStatusQuery = "UPDATE Books SET status = ? WHERE book_id = ?";
                                        try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateBookStatusQuery)) {
                                            pstmtUpdate.setString(1, "BORROWED"); // Mark the book as borrowed
                                            pstmtUpdate.setInt(2, bookId);
                                            pstmtUpdate.executeUpdate();
                                        }

                                        // Commit the transaction if both updates succeed
                                        conn.commit();

                                        // Optionally, you can return the transactionId for further processing
                                        System.out.println("Transaction created successfully with ID: " + transactionId);
                                        return true; // Successfully borrowed the book
                                    }
                                }
                            }
                        }
                    } catch (SQLException e) {
                        // Rollback if any exception occurs
                        conn.rollback();
                        throw new SQLException("Failed to borrow the book, rolling back transaction.", e);
                    } finally {
                        // Set auto-commit back to true
                        conn.setAutoCommit(true);
                    }
                }
            }
        }
        return false; // Book not available for borrowing
    }



}