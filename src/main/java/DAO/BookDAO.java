package DAO;

import DatabaseConnection.DatabaseUtil;
import Entity.Book;
import Entity.Enums.BookStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // Add a new book to the database
    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO Books (title, author, publication_year, genre, status, isbn) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getPublicationYear());
            pstmt.setString(4, book.getGenre());
            pstmt.setString(5, book.getStatus().toString().toLowerCase());  // Convert to lowercase
            pstmt.setString(6, book.getIsbn());
            pstmt.executeUpdate();
        }
    }

    // Retrieve a book by its ID
    public Book getBookById(int bookId) throws SQLException {
        String query = "SELECT * FROM Books WHERE book_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("publication_year"),
                            rs.getString("genre"),
                            BookStatus.valueOf(rs.getString("status").toUpperCase()),  // Convert to uppercase
                            rs.getString("isbn")
                    );
                }
            }
        }
        return null;
    }

    // Retrieve all books
    public List<Book> getAllBooks() throws SQLException {
        String query = "SELECT * FROM Books";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publication_year"),
                        rs.getString("genre"),
                        BookStatus.valueOf(rs.getString("status").toUpperCase()),  // Convert to uppercase
                        rs.getString("isbn")
                ));
            }
        }
        return books;
    }

     public void updateBookStatus(int bookId, BookStatus status) throws SQLException {
        String query = "UPDATE Books SET status = ? WHERE book_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status.name().toLowerCase());  // Convert to lowercase
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }
    }

    // Delete a book by its ID remains the same
    public void deleteBook(int bookId) throws SQLException {
        String query = "DELETE FROM Books WHERE book_id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        String query = "SELECT * FROM Books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ?";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword.toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("publication_year"),
                            rs.getString("genre"),
                            BookStatus.valueOf(rs.getString("status").toUpperCase()),
                            rs.getString("isbn")
                    ));
                }
            }
        }
        return books;
    }
}