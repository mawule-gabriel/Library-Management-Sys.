package Controller;

import Service.BookService;
import Entity.Book;
import Entity.Enums.BookStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class BookController {
    private final BookService bookService;

    public BookController() {
        this.bookService = new BookService();
    }

    // Add a new book
    public void addBook(String title, String author, String genre, int yearPublished, BookStatus status) throws Exception {
        if (title == null || title.trim().isEmpty()) {
            throw new Exception("Title cannot be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new Exception("Author cannot be empty");
        }

        Book book = new Book(0, title, author, yearPublished, genre, status, generateISBN());
        bookService.addBook(book);
    }

    // Get a book by its ID
    public Book getBookById(int bookId) throws Exception {
        try {
            Book book = bookService.getBookById(bookId);
            if (book == null) {
                throw new Exception("Book not found with ID: " + bookId);
            }
            return book;
        } catch (SQLException e) {
            throw new Exception("Error retrieving book: " + e.getMessage());
        }
    }

    // Retrieve all books
    public ObservableList<Book> getAllBooks() throws Exception {
        List<Book> books = bookService.getAllBooks();
        return FXCollections.observableArrayList(books);
    }

    // Update the status of a book
    public void updateBookStatus(int bookId, BookStatus status) throws Exception {
        bookService.updateBookStatus(bookId, status);
    }

    // Delete a book by its ID
    public void deleteBook(int bookId) throws Exception {
        bookService.deleteBook(bookId);
    }

    // Helper method to generate ISBN (simplified version)
    private String generateISBN() {
        // Generate a random 13-digit ISBN (simplified)
        return "978" + String.format("%010d", (long) (Math.random() * 10000000000L));
    }


}