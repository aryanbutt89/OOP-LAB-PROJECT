package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NurseLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check if email and password match
        if (email.equals("nurse123@gmail.com") && password.equals("nurse123")) {
            // Load Nurse dashboard
            loadNurseDashboard();
        } else {
            showAlert("Error", "Invalid email or password.");
        }
    }

    private void loadNurseDashboard() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("nurse.fxml"));
            javafx.scene.Parent root = loader.load();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to load Nurse dashboard.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
