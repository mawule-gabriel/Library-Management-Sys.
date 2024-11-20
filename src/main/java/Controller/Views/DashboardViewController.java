package Controller.Views;

import Service.BookService;
import Service.PatronService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.example.librarymanagementsys.HelloApplication;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;



public class DashboardViewController implements Initializable {

    @FXML public Button bookView;
    @FXML private Label totalBooksLabel;
    @FXML private Label activePatronsLabel;
    @FXML private Label activeTransactionsLabel;
    @FXML private Label pendingReservationsLabel;

    @FXML private ImageView mandelaImageView;
    @FXML private ImageView nkrumahImageView;
    @FXML private ImageView maathaiImageView;
    @FXML private HBox dashBox;
    private BookService bookService;

    @FXML private BarChart<String, Number> circulationChart;  // Added BarChart for book circulation
    @FXML private PieChart categoryChart; // Added PieChart for categories

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.bookService = new BookService();
        loadDashboardStats();
        verifyImages();
        populateCharts(); // Call method to populate charts with data
    }

    private void verifyImages() {
        try {
            if (mandelaImageView != null && mandelaImageView.getImage() == null) {
                Image mandelaImage = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsys/images/mandela.jpg"));
                mandelaImageView.setImage(mandelaImage);
            }
            if (nkrumahImageView != null && nkrumahImageView.getImage() == null) {
                Image nkrumahImage = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsys/images/nkrumah.jpg"));
                nkrumahImageView.setImage(nkrumahImage);
            }
            if (maathaiImageView != null && maathaiImageView.getImage() == null) {
                Image maathaiImage = new Image(getClass().getResourceAsStream("/org/example/librarymanagementsys/images/maathai.jpg"));
                maathaiImageView.setImage(maathaiImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    private void loadDashboardStats() {
        try {
            // For now, using dummy data
            int totalBooks = bookService.getAllBooks().size();
            totalBooksLabel.setText(String.valueOf(totalBooks));

            activePatronsLabel.setText(String.valueOf(34));

            activeTransactionsLabel.setText(String.valueOf(89));
            pendingReservationsLabel.setText(String.valueOf(22));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateCharts() {
        // BarChart: Book Circulation Data
        XYChart.Series<String, Number> circulationSeries = new XYChart.Series<>();
        circulationSeries.setName("Book Circulation");
        circulationSeries.getData().add(new XYChart.Data<>("January", 120));
        circulationSeries.getData().add(new XYChart.Data<>("February", 150));
        circulationSeries.getData().add(new XYChart.Data<>("March", 180));
        circulationSeries.getData().add(new XYChart.Data<>("April", 200));
        circulationChart.getData().add(circulationSeries);

        // PieChart: Categories Data
        PieChart.Data fictionData = new PieChart.Data("Fiction", 40);
        PieChart.Data nonFictionData = new PieChart.Data("Non-fiction", 30);
        PieChart.Data scienceData = new PieChart.Data("Science", 20);
        PieChart.Data historyData = new PieChart.Data("History", 10);
        categoryChart.getData().addAll(fictionData, nonFictionData, scienceData, historyData);
    }

    @FXML
    private void handleDashboardView(ActionEvent event) {
        try {
            Stage stage = (Stage) dashBox.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/DashboardView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/dashboard.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Error loading Dashboard view", e);
        }
    }

    @FXML
    private void handleBookView(ActionEvent event) {
        try {
            Stage stage = (Stage) dashBox.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/BookView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Error loading Book view", e);
            e.printStackTrace(); // Add this to see more detailed error information
        }
    }

    @FXML
    private void handlePatronView(ActionEvent event) {
        try {
            Stage stage = (Stage) dashBox.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/Patron.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/patron-styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Error loading Patron view", e);
        }
    }

    @FXML
    private void handleStaffView(ActionEvent event) {
        try {
            Stage stage = (Stage) dashBox.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/staff-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/staff-view.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Error loading Staff view", e);
        }
    }

    @FXML
    private void handleTransactionView(ActionEvent event) {
        try {
            Stage stage = (Stage) dashBox.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/TransactionView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/transaction-styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Error loading Transaction view", e);
        }
    }

    @FXML
    private void handleReservationView(ActionEvent event) {
        try {
            Stage stage = (Stage) dashBox.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsys/ReservationsView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/org/example/librarymanagementsys/reservations.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Error loading Reservation view", e);
        }
    }

    private void showError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}
