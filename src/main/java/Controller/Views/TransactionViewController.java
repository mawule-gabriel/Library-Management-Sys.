package Controller.Views;

import Entity.Transaction;
import Entity.Enums.TransactionType;
import Controller.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransactionViewController implements Initializable {
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, Integer> patronColumn;
    @FXML private TableColumn<Transaction, Integer> bookColumn;
    @FXML private TableColumn<Transaction, LocalDate> borrowDateColumn;
    @FXML private TableColumn<Transaction, LocalDate> dueDateColumn;
    @FXML private TableColumn<Transaction, LocalDate> returnDateColumn;
    @FXML private TableColumn<Transaction, BigDecimal> fineColumn;
    @FXML private TableColumn<Transaction, TransactionType> typeColumn;
    @FXML private TableColumn<Transaction, Void> actionsColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<TransactionType> filterType;
    @FXML private DatePicker dateFilter;
    @FXML private Button searchButton;
    @FXML private Button addButton;

    @FXML private TextField patronIdField;
    @FXML private TextField bookIdField;
    @FXML private DatePicker borrowDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<TransactionType> transactionTypeCombo;
    @FXML private TextField fineField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final TransactionController transactionController;
    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private Transaction currentTransaction; // For editing existing transactions

    public TransactionViewController() {
        this.transactionController = new TransactionController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        setupComboBoxes();
        setupListeners();
        setupValidation();
        loadTransactions();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        patronColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        fineColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));

        setupActionColumn();
        transactionTable.setItems(transactionList);
    }

    private void setupActionColumn() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonsContainer = new HBox(5, editButton, deleteButton);

            {
                editButton.getStyleClass().add("edit-button");
                deleteButton.getStyleClass().add("delete-button");

                editButton.setOnAction(event -> {
                    Transaction transaction = getTableRow().getItem();
                    if (transaction != null) {
                        showEditForm(transaction);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Transaction transaction = getTableRow().getItem();
                    if (transaction != null) {
                        handleDelete(transaction);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsContainer);
            }
        });
    }

    private void setupComboBoxes() {
        filterType.getItems().addAll(TransactionType.values());
        transactionTypeCombo.getItems().addAll(TransactionType.values());
    }

    private void setupListeners() {
        addButton.setOnAction(event -> showAddForm());
        searchButton.setOnAction(event -> handleSearch());
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> clearForm());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
        filterType.valueProperty().addListener((observable, oldValue, newValue) -> handleSearch());
        dateFilter.valueProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    private void setupValidation() {
        patronIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                patronIdField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        bookIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bookIdField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        fineField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                fineField.setText(oldValue);
            }
        });
    }

    private void loadTransactions() {
        transactionList.clear();
        try {
            transactionList.addAll(transactionController.getAllTransactions());
        } catch (Exception e) {
            showError("Error loading transactions", e.getMessage());
        }
    }

    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        TransactionType selectedType = filterType.getValue();
        LocalDate selectedDate = dateFilter.getValue();

        transactionList.clear();
        transactionList.addAll(transactionController.getAllTransactions().stream()
                .filter(t -> matchesSearchCriteria(t, searchText, selectedType, selectedDate))
                .toList());
    }

    private boolean matchesSearchCriteria(Transaction t, String searchText,
                                          TransactionType type, LocalDate date) {
        boolean matchesText = searchText.isEmpty() ||
                String.valueOf(t.getTransactionId()).contains(searchText) ||
                String.valueOf(t.getPatronId()).contains(searchText) ||
                String.valueOf(t.getBookId()).contains(searchText);

        boolean matchesType = type == null || t.getTransactionType() == type;
        boolean matchesDate = date == null ||
                (t.getBorrowDate() != null && t.getBorrowDate().equals(date));

        return matchesText && matchesType && matchesDate;
    }

    private void showAddForm() {
        currentTransaction = null;
        clearForm();
        borrowDatePicker.setValue(LocalDate.now());
        dueDatePicker.setValue(LocalDate.now().plusDays(14)); // Default loan period
        fineField.setText("0.00");
    }

    private void showEditForm(Transaction transaction) {
        currentTransaction = transaction;
        patronIdField.setText(String.valueOf(transaction.getPatronId()));
        bookIdField.setText(String.valueOf(transaction.getBookId()));
        borrowDatePicker.setValue(transaction.getBorrowDate());
        dueDatePicker.setValue(transaction.getDueDate());
        transactionTypeCombo.setValue(transaction.getTransactionType());
        fineField.setText(transaction.getFine().toString());
    }

    private void handleSave() {
        if (!validateForm()) {
            return;
        }

        try {
            Transaction transaction = createTransactionFromForm();
            if (currentTransaction != null) {
                transaction.setTransactionId(currentTransaction.getTransactionId());
                transactionController.updateTransaction(transaction);
            } else {
                transactionController.addTransaction(transaction);
            }
            loadTransactions();
            clearForm();
            showSuccess("Transaction saved successfully");
        } catch (Exception e) {
            showError("Error saving transaction", e.getMessage());
        }
    }

    private void handleDelete(Transaction transaction) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Transaction");
        alert.setContentText("Are you sure you want to delete this transaction?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                transactionController.deleteTransaction(transaction.getTransactionId());
                loadTransactions();
                showSuccess("Transaction deleted successfully");
            } catch (Exception e) {
                showError("Error deleting transaction", e.getMessage());
            }
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (patronIdField.getText().isEmpty()) {
            errors.append("Patron ID is required\n");
        }
        if (bookIdField.getText().isEmpty()) {
            errors.append("Book ID is required\n");
        }
        if (borrowDatePicker.getValue() == null) {
            errors.append("Borrow Date is required\n");
        }
        if (dueDatePicker.getValue() == null) {
            errors.append("Due Date is required\n");
        }
        if (transactionTypeCombo.getValue() == null) {
            errors.append("Transaction Type is required\n");
        }
        if (fineField.getText().isEmpty()) {
            errors.append("Fine amount is required\n");
        }

        if (errors.length() > 0) {
            showError("Validation Error", errors.toString());
            return false;
        }
        return true;
    }

    private Transaction createTransactionFromForm() {
        return new Transaction(
                0, // For new transactions, ID will be generated
                Integer.parseInt(patronIdField.getText()),
                Integer.parseInt(bookIdField.getText()),
                borrowDatePicker.getValue(),
                null, // Return date will be set later
                dueDatePicker.getValue(),
                new BigDecimal(fineField.getText()),
                transactionTypeCombo.getValue()
        );
    }

    private void clearForm() {
        currentTransaction = null;
        patronIdField.clear();
        bookIdField.clear();
        borrowDatePicker.setValue(null);
        dueDatePicker.setValue(null);
        transactionTypeCombo.setValue(null);
        fineField.setText("0.00");
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBackToDashboard() {
        try {
            Stage stage = (Stage) searchField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/DashboardView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError("Error", "Could not load Dashboard view.");
        }
    }
}