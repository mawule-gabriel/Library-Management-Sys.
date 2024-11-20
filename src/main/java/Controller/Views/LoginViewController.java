package Controller.Views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class LoginViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Sample credentials (can be replaced with a real database or authentication service)
    private final String correctUsername = "admin";
    private final String correctPassword = "password123";

    @FXML
    private void handleLogin(ActionEvent event) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        // Validate username and password
        if (enteredUsername.equals(correctUsername) && enteredPassword.equals(correctPassword)) {
            // Successful login - load the Dashboard view
            loadDashboardView(event);
        } else {
            // Invalid credentials, show an error message
            showError("Invalid login", "The username or password is incorrect.");
        }
    }

    private void loadDashboardView(ActionEvent event) {
        try {
            // Load the Dashboard view (DashboardView.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/DashboardView.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            // Set the new scene on the stage (Dashboard view)
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Error loading Dashboard", "An error occurred while loading the dashboard view.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
