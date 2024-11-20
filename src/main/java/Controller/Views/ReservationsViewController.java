package Controller.Views;

import Controller.BookController;
import Controller.PatronController;
import Controller.ReservationController;
import Entity.Book;
import Entity.Enums.BookStatus;
import Entity.Patron;
import Entity.Reservation;
import Entity.Enums.ReservationStatus;
import Service.PatronService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class ReservationsViewController {
    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, Integer> patronColumn;
    @FXML private TableColumn<Reservation, Integer> bookColumn;
    @FXML private TableColumn<Reservation, LocalDate> dateColumn;
    @FXML private TableColumn<Reservation, ReservationStatus> statusColumn;
    @FXML private TableColumn<Reservation, LocalDate> dueDateColumn;
    @FXML private TableColumn<Reservation, Void> actionsColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<ReservationStatus> statusFilter;
    @FXML private DatePicker dateFilter;
    @FXML private Button addButton;

    @FXML private Label pendingCount;
    @FXML private Label fulfilledCount;
    @FXML private Label cancelledCount;

    private final ReservationController reservationController;
    private final ObservableList<Reservation> reservationsList;
    private FilteredList<Reservation> filteredReservations;
    private PatronController patronController;
    private BookController bookController = new BookController();
    private PatronService patronService = new PatronService();

    public ReservationsViewController() {
        this.reservationController = new ReservationController();
        this.reservationsList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTable();
        setupFilters();
        loadReservations();
        setupListeners();
        updateStatusCounts();
    }

    private void setupListeners() {
        // Add Button Listener
        addButton.setOnAction(event -> handleAddReservation());

        // Table Selection Listener
        reservationsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Enable/disable buttons based on selection
                        boolean hasSelection = true;
                        // You can add additional buttons here and set their disable property
                    }
                }
        );

        // Double Click Listener for Edit
        reservationsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && reservationsTable.getSelectionModel().getSelectedItem() != null) {
                handleEditReservation(reservationsTable.getSelectionModel().getSelectedItem());
            }
        });

        // Key Press Listener (e.g., for Delete key)
        reservationsTable.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("DELETE") &&
                    reservationsTable.getSelectionModel().getSelectedItem() != null) {
                handleDeleteReservation(reservationsTable.getSelectionModel().getSelectedItem());
            }
        });
    }



    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        patronColumn.setCellValueFactory(new PropertyValueFactory<>("patronName"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));



        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actions = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handleEditReservation(reservation);
                });

                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handleDeleteReservation(reservation);
                });

                // Add CSS classes for styling
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actions);
            }
        });
    }

    private void setupFilters() {
        statusFilter.setItems(FXCollections.observableArrayList(ReservationStatus.values()));

        filteredReservations = new FilteredList<>(reservationsList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                updateFilters());

        statusFilter.valueProperty().addListener((observable, oldValue, newValue) ->
                updateFilters());

        dateFilter.valueProperty().addListener((observable, oldValue, newValue) ->
                updateFilters());
    }


    private void loadReservations() {
        reservationsList.clear();
        reservationsList.addAll(reservationController.getAllReservations());  // Now includes patron name and book title
        reservationsTable.setItems(filteredReservations);
    }


    private void handleEditReservation(Reservation reservation) {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Edit Reservation");
        dialog.setHeaderText("Edit Reservation Details");

        // Create dialog content
        // This is a placeholder - you should create a proper form
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Show dialog and handle result
        dialog.showAndWait().ifPresent(result -> {
            try {
                // Update the reservation
                loadReservations(); // Reload the table
                updateStatusCounts(); // Update the counts
            } catch (Exception e) {
                showError("Error Updating Reservation", e.getMessage());
            }
        });
    }

    private void handleDeleteReservation(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setContentText("Are you sure you want to delete this reservation?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reservationController.deleteReservation(reservation.getReservationId());
                loadReservations();
                updateStatusCounts();
            }
        });
    }

    private void updateStatusCounts() {
        long pending = filteredReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.PENDING).count();
        long fulfilled = filteredReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.FULFILLED).count();
        long cancelled = filteredReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CANCELLED).count();

        pendingCount.setText(String.valueOf(pending));
        fulfilledCount.setText(String.valueOf(fulfilled));
        cancelledCount.setText(String.valueOf(cancelled));
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleBackToDashboard() {
        try {
            // Get the current stage (window)
            Stage stage = (Stage) searchField.getScene().getWindow();

            // Load the Dashboard view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/DashboardView.fxml"));
            Parent root = loader.load();

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError("Error", "Could not load Dashboard view.");
        }
    }

    private void updateFilters() {
        filteredReservations.setPredicate(reservation -> {
            // Search field filter (case-insensitive)
            String searchText = searchField.getText().toLowerCase();
            boolean matchesSearch = searchText.isEmpty() ||
                    String.valueOf(reservation.getReservationId()).contains(searchText) ||
                    String.valueOf(reservation.getPatronId()).contains(searchText) ||
                    String.valueOf(reservation.getBookId()).contains(searchText);

            // Status filter
            boolean matchesStatus = statusFilter.getValue() == null ||
                    reservation.getStatus() == statusFilter.getValue();

            // Date filter
            boolean matchesDate = dateFilter.getValue() == null ||
                    reservation.getReservationDate().equals(dateFilter.getValue());

            return matchesSearch && matchesStatus && matchesDate;
        });
    }

    @FXML
    public void handleAddReservation() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Reservation");
        dialog.setHeaderText("Enter Reservation Details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        VBox dialogContent = new VBox(10);

        // Initialize patron combo box
        ComboBox<Patron> patronComboBox = new ComboBox<>();
        try {
            ObservableList<Patron> patrons = FXCollections.observableArrayList(patronService.getAllPatrons());
            patronComboBox.setItems(patrons);
        } catch (SQLException e) {
            showError("Error Loading Patrons", "Failed to fetch patrons: " + e.getMessage());
            return;
        }

        patronComboBox.setConverter(new StringConverter<Patron>() {
            @Override
            public String toString(Patron patron) {
                return patron != null ? patron.getFirstName() + " " + patron.getLastName() : "";
            }

            @Override
            public Patron fromString(String string) {
                return null; // Not used
            }
        });

        // Initialize book combo box
        ComboBox<Book> bookComboBox = new ComboBox<>();
        bookComboBox.setEditable(false); // Make it non-editable
        bookComboBox.setConverter(new StringConverter<Book>() {
            @Override
            public String toString(Book book) {
                return book != null ? book.getTitle() : "";
            }

            @Override
            public Book fromString(String string) {
                return null; // Not used
            }
        });

        // Load books into book combo box
        try {
            ObservableList<Book> books = FXCollections.observableArrayList(bookController.getAllBooks());
            bookComboBox.setItems(books);
        } catch (Exception e) {
            showError("Error Loading Books", "Failed to fetch books: " + e.getMessage());
            return;
        }

        // Initialize due date picker
        DatePicker dueDatePicker = new DatePicker();

        // Add components to dialog content
        dialogContent.getChildren().addAll(
                new Label("Select Patron:"), patronComboBox,
                new Label("Select Book:"), bookComboBox,
                new Label("Select Due Date:"), dueDatePicker // Add the due date picker
        );

        dialog.getDialogPane().setContent(dialogContent);

        // Handle the button actions
        dialog.showAndWait().ifPresent(result -> {
            if (result == saveButtonType) {
                Patron selectedPatron = patronComboBox.getValue();
                Book selectedBook = bookComboBox.getValue();
                LocalDate reservationDate = LocalDate.now();
                LocalDate dueDate = dueDatePicker.getValue(); // Get the due date

                // Check for null values
                if (selectedPatron == null || selectedBook == null || dueDate == null) {
                    showError("Missing Information", "Please select a patron, a book, and a due date.");
                    return;
                }

                try {
                    // Convert LocalDate to java.sql.Date for reservationDate and dueDate
                    java.sql.Date sqlReservationDate = java.sql.Date.valueOf(reservationDate);
                    java.sql.Date sqlDueDate = java.sql.Date.valueOf(dueDate);

                    // Create a new Reservation object
                    Reservation newReservation = new Reservation(
                            0,
                            selectedPatron.getPatronId(),
                            selectedBook.getBookId(),
                            sqlReservationDate.toLocalDate(),  // Pass java.sql.Date
                            ReservationStatus.PENDING,  // Pass enum itself, not name()
                            sqlDueDate.toLocalDate()  // Pass java.sql.Date
                    );

                    reservationController.addReservation(newReservation);
                    bookController.updateBookStatus(selectedBook.getBookId(), BookStatus.RESERVED);
                    loadReservations();
                    updateStatusCounts();
                } catch (Exception e) {
                    showError("Error Adding Reservation", e.getMessage());
                }
            }
        });
    }


}