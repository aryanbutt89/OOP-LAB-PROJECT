package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppointmentController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField diseaseField;

    @FXML
    private TextField timeField;

    @FXML
    private ComboBox<String> doctorComboBox;

    @FXML
    private TextField genderField;

    @FXML
    private ComboBox<String> dayComboBox;

    @FXML
    private void initialize() {
        doctorComboBox.setItems(FXCollections.observableArrayList(
                "Dr. Umar", "Dr. Rashid", "Dr. Abdullah"));
    }

    @FXML
    private void handleMakeAppointment() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String disease = diseaseField.getText();
        String time = timeField.getText();
        String doctor = doctorComboBox.getValue();
        String gender = genderField.getText();

        if (name.isEmpty() || phone.isEmpty() || disease.isEmpty() || time.isEmpty() || doctor == null || doctor.isEmpty() || gender.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Validate and format time input
        if (!isValidTimeFormat(time)) {
            showAlert("Error", "Invalid time format. Please enter time in HH:MM AM/PM format.");
            return;
        }

        try {
            DatabaseHandler.getInstance().saveAppointmentToPatientProfile(name, phone, disease, time, doctor, gender);
            showAlert("Success", "Appointment made successfully.");
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            if (e.getMessage().contains("Appointment already exists")) {
                showAlert("Error", "Appointment already exists for this doctor at the specified time.");
            } else {
                showAlert("Error", "Failed to make appointment. Please try again.");
            }
            e.printStackTrace();
        }
    }

    private boolean isValidTimeFormat(String time) {
        // Regular expression to match HH:MM AM/PM format
        String timeRegex = "^([1-9]|1[0-2]):([0-5][0-9])\\s(?:AM|PM)$";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
