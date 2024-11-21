package DAO;

import DatabaseConnection.DatabaseUtil;
import Entity.Transaction;
import Entity.Enums.TransactionType;

import java.math.BigDecimal;
import java.sql.*;
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
                            transactionType = TransactionType.RESERVE; // Default value or log error
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
                        transactionType = TransactionType.RESERVE; // Default value or log error
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
}