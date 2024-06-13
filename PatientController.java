package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import application.models.Appointment;
import java.sql.SQLException;
import java.util.List;

public class PatientController {

    @FXML
    private TextField nameTextField;

    @FXML
    private void handleViewProfile(ActionEvent event) {
        String patientName = nameTextField.getText().trim();
        if (!patientName.isEmpty()) {
            try {
                List<Appointment> appointments = DatabaseHandler.getInstance().getAppointmentsByName(patientName);
                if (!appointments.isEmpty()) {
                    StringBuilder details = new StringBuilder();
                    for (Appointment appointment : appointments) {
                        details.append("Name: ").append(appointment.getName()).append("\n");
                        details.append("Gender: ").append(appointment.getGender()).append("\n");
                        details.append("Doctor: ").append(appointment.getDoctor()).append("\n");
                        details.append("Disease: ").append(appointment.getDisease()).append("\n");
                        details.append("Appointment Time: ").append(appointment.getTime()).append("\n");
                        details.append("Price: ").append(appointment.getPrice()).append("\n\n");
                    }
                    showAlert("Appointment Details", details.toString());
                } else {
                    showAlert("No Appointments", "No appointments found for " + patientName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to retrieve appointments.");
            }
        } else {
            showAlert("Error", "Please enter your name.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        
        // Lambda expression to handle user response
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Close the current window
                Stage stage = (Stage) nameTextField.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
