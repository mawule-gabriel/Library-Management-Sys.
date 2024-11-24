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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize ComboBox
        statusComboBox.setItems(FXCollections.observableArrayList(BookStatus.values()));

        // Initialize TableView columns
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

    @FXML
    private void handleClearForm() {
        clearForm();
    }


    public void handleRefresh() {
        try {
            loadBooks();  // This method already fetches books from the database
        } catch (Exception e) {
            System.err.println("Error refreshing book list: " + e.getMessage());
        }
    }

    private void clearForm() {
        titleField.clear();
        authorField.clear();
        genreField.clear();
        yearSpinner.getValueFactory().setValue(2024);
        statusComboBox.getSelectionModel().clearSelection();
    }

    private void loadBooks() {
        try {
            bookList = bookController.getAllBooks();
            bookTable.setItems(bookList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load books: " + e.getMessage());
        }
    }

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

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

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
