package Controller.Views;

import Controller.BookController;
import Controller.TransactionController;
import Entity.Book;
import Entity.Enums.BookStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for managing book-related operations in the view.
 * Handles adding, deleting, searching, and displaying books.
 */
public class BookViewController implements Initializable {
    private final TransactionController transactionController = new TransactionController();

    private final BookController bookController = new BookController();
    private ObservableList<Book> bookList;

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField searchField;
    @FXML private Spinner<Integer> yearSpinner;
    @FXML private ComboBox<BookStatus> statusComboBox;
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> genreColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, BookStatus> statusColumn;

    /**
     * Initializes the view components and populates the table with book data.
     * Sets up columns in the table, populates the status combo box, and adds a listener for the search field.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        statusComboBox.setItems(FXCollections.observableArrayList(BookStatus.values()));


        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));

        // Load initial data
        loadBooks();
    }

    /**
     * Handles the addition of a new book based on user input from the form.
     * Validates the input, adds the book to the database, and reloads the table.
     */
    @FXML
    private void handleAddBook() {
        try {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            int year = yearSpinner.getValue();
            BookStatus status = statusComboBox.getValue();

            if (status == null) {
                throw new Exception("Please select a book status");
            }

            bookController.addBook(title, author, genre, year, status);
            clearForm();
            loadBooks();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add book: " + e.getMessage());
        }
    }

    /**
     * Deletes the selected book from the table.
     */
    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Delete");
            confirmDialog.setHeaderText(null);
            confirmDialog.setContentText("Are you sure you want to delete the selected book?");

            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        bookController.deleteBook(selectedBook.getBookId());
                        loadBooks();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Book deleted successfully!");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete book: " + e.getMessage());
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a book to delete.");
        }
    }

    /**
     * Clears all input fields in the book form.
     */
    @FXML
    private void handleClearForm() {
        clearForm();
    }


    /**
     * Refreshes the book list by reloading the data from the database.
     */
    public void handleRefresh() {
        try {
            loadBooks();  // This method already fetches books from the database
        } catch (Exception e) {
            System.err.println("Error refreshing book list: " + e.getMessage());
        }
    }

    /**
     * Clears the form fields for entering book information, resetting them to their default values.
     */
    private void clearForm() {
        titleField.clear();
        authorField.clear();
        genreField.clear();
        yearSpinner.getValueFactory().setValue(2024);
        statusComboBox.getSelectionModel().clearSelection();
    }

    /**
     * Loads the list of all books from the database and populates the table.
     * If an error occurs, an error alert is displayed.
     */
    private void loadBooks() {
        try {
            bookList = bookController.getAllBooks();
            bookTable.setItems(bookList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load books: " + e.getMessage());
        }
    }

    /**
     * Filters the book list based on the search text entered by the user.
     * The search is case-insensitive and checks if the text appears in the title, author, or genre.
     * @param searchText The text entered by the user to filter the book list.
     */
    private void filterBooks(String searchText) {
        if (bookList != null) {
            ObservableList<Book> filteredList = bookList.filtered(book ->
                    book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                            book.getAuthor().toLowerCase().contains(searchText.toLowerCase()) ||
                            book.getGenre().toLowerCase().contains(searchText.toLowerCase())
            );
            bookTable.setItems(filteredList);
        }
    }

    /**
     * Displays a custom alert with a given type, title, and content.
     * This method is used for showing success, error, and informational messages to the user.
     * @param type The type of the alert (e.g., INFORMATION, ERROR).
     * @param title The title of the alert.
     * @param content The content text of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Navigates the user back to the dashboard view.
     * This method loads the dashboard FXML and switches the scene to display it.
     */
    @FXML
    public void handleBackToDashboard() {
        try {
            // Get the current stage
            Stage stage = (Stage) titleField.getScene().getWindow();

            // Load the Dashboard view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/DashboardView.fxml"));
            Parent root = loader.load();

            // Create new scene and set it on the stage
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/dashboard.css").toExternalForm());

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error navigating to Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
