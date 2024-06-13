// DashboardController.java
package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private void handleDoctorLogin(ActionEvent event) {
        navigateTo("DoctorLoginController.fxml", event);
    }

    @FXML
    private void handleNurseLogin(ActionEvent event) {
        navigateTo("NurseLoginController.fxml", event);
    }

    @FXML
    private void handlePatientLogin(ActionEvent event) {
        navigateTo("patient.fxml", event);
    }

    @FXML
    private void handleAdminLogin(ActionEvent event) {
        navigateTo("admin.fxml", event);
    }

    @FXML
    private void handleMakeAppointment(ActionEvent event) {
        navigateTo("Appointment.fxml", event);
    }

    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxmlFile));
            javafx.scene.Parent root = loader.load();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Navigation Error", "Failed to load the page: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
