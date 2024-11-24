package Service;

import DAO.BookDAO;
import Entity.Book;
import Entity.Enums.BookStatus;
import Exception.BookException;

import java.time.LocalDate;
import java.sql.SQLException;
import java.util.*;


public class BookService {
    private final BookDAO bookDAO;
    private final LinkedList<Book> bookCache;
    private final Stack<Book> recentlyAddedBooks;
    private final Map<Integer, Book> quickAccessCache;
    private static final int CACHE_SIZE = 100;

    public BookService() {
        this.bookDAO = new BookDAO();
        this.bookCache = new LinkedList<>();
        this.recentlyAddedBooks = new Stack<>();
        this.quickAccessCache = new HashMap<>();
    }

    // Add a new book
    public void addBook(Book book) throws BookException {
        try {
            validateBook(book);
            bookDAO.addBook(book);

            // Update caches
            bookCache.add(book);
            recentlyAddedBooks.push(book);
            if (quickAccessCache.size() < CACHE_SIZE) {
                quickAccessCache.put(book.getBookId(), book);
            }
        } catch (SQLException e) {
            throw new BookException("Failed to add book: " + book.getTitle(), e);
        }
    }

    // Get a book by ID
    public Book getBookById(int bookId) throws BookException {
        // Check quick access cache first
        Book cachedBook = quickAccessCache.get(bookId);
        if (cachedBook != null) {
            return cachedBook;
        }

        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                throw new BookException("No book found with ID: " + bookId);
            }

            // Add to cache if not full
            if (quickAccessCache.size() < CACHE_SIZE) {
                quickAccessCache.put(bookId, book);
            }
            return book;
        } catch (SQLException e) {
            throw new BookException("Error retrieving book", e);
        }
    }

    // Get all books
    public List<Book> getAllBooks() throws BookException {
        try {
            List<Book> books = bookDAO.getAllBooks();
            // Update cache with fetched books
            bookCache.clear();
            bookCache.addAll(books);
            return books;
        } catch (SQLException e) {
            throw new BookException("Error retrieving books", e);
        }
    }

    // Update book status
    public void updateBookStatus(int bookId, BookStatus status) throws BookException {
        try {
            Book book = getBookById(bookId);  // Fetch the book by ID
            if (book == null) {
                throw new BookException("Cannot update status. Book not found with ID: " + bookId);
            }

            // Update book status in the database
            bookDAO.updateBookStatus(bookId, status);

            // Update the book status in the local object
            book.setStatus(String.valueOf(status));
        } catch (SQLException e) {
            throw new BookException("Error updating book status", e);
        }
    }

    // Delete a book
    public void deleteBook(int bookId) throws BookException {
        try {
            Book book = getBookById(bookId);
            if (book == null) {
                throw new BookException("Cannot delete. Book not found with ID: " + bookId);
            }

            bookDAO.deleteBook(bookId);

            // Update caches
            bookCache.remove(book);
            quickAccessCache.remove(bookId);
            // Note: We keep it in recentlyAddedBooks for history
        } catch (SQLException e) {
            throw new BookException("Error deleting book", e);
        }
    }



    // Private helper methods
    private void validateBook(Book book) throws BookException {
        List<String> errors = new ArrayList<>();

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            errors.add("Title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            errors.add("Author cannot be empty");
        }
        if (book.getPublicationYear() < 1000 || book.getPublicationYear() > LocalDate.now().getYear()) {
            errors.add("Invalid publication year");
        }
        if (book.getIsbn() != null && !isValidISBN(book.getIsbn())) {
            errors.add("Invalid ISBN format");
        }

        if (!errors.isEmpty()) {
            throw new BookException("Book validation failed: " + String.join(", ", errors));
        }
    }

    private boolean isValidISBN(String isbn) {
        return isbn.matches("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    }




}
