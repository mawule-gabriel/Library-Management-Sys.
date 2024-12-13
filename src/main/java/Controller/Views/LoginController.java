package Controller.Views;

import Controller.StaffController;
import Entity.Staff;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private StaffController staffController = new StaffController();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check if fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both email and password.");
            errorLabel.setVisible(true);
            return;
        }

        // Validate email format (basic validation)
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errorLabel.setText("Please enter a valid email address.");
            errorLabel.setVisible(true);
            return;
        }

        // Authenticate user
        Staff staff = staffController.authenticateStaff(email, password);

        if (staff != null) {
            // Successful login, navigate to the dashboard
            navigateToDashboard(staff);
        } else {
            // Show error message
            errorLabel.setText("Invalid email or password.");
            errorLabel.setVisible(true);
        }
    }

    private void navigateToDashboard(Staff staff) {
        try {
            // Hide the error label in case of successful login
            errorLabel.setVisible(false);

            // Get the current stage (window)
            Stage stage = (Stage) emailField.getScene().getWindow();

            // Load the Dashboard view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/DashboardView.fxml"));
            Parent root = loader.load();

            // Pass staff object to the DashboardController
            DashboardViewController dashboardController = loader.getController();
            dashboardController.initializeDashboard(staff);

            // Create a new scene and set it on the stage
            Scene scene = new Scene(root);

            // Apply the login.css stylesheet to the scene
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/login.css").toExternalForm());

            stage.setScene(scene);  // Switch to the new scene
            stage.show();  // Display the new scene
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the Dashboard.", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content, String details) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setExpandableContent(new TextArea(details));  // Optionally add the error message in a collapsible TextArea for more details
        alert.showAndWait();
    }

    // Optionally: Trigger login with Enter key press
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleLogin();
        }
    }
}
