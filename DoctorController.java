package application;

import application.models.Appointment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DoctorController {
    
    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, String> nameColumn;

    @FXML
    private TableColumn<Appointment, String> phoneColumn;

    @FXML
    private TableColumn<Appointment, String> diseaseColumn;

    @FXML
    private TableColumn<Appointment, String> timeColumn;

    @FXML
    private TableColumn<Appointment, String> doctorColumn;
    
    @FXML
    private TableColumn<Appointment, String> genderColumn;

    @FXML
    private Button logoutButton;

    @FXML
    private Button deleteButton;

    @FXML
    private void initialize() {
        // Initialize table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        diseaseColumn.setCellValueFactory(new PropertyValueFactory<>("disease"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Fetch appointments data
        fetchAppointments();
    }

    @FXML
    private void handleLogout() {
        // Close the current window
        Stage stage = (Stage) appointmentsTable.getScene().getWindow();
        stage.close();
       
        // Load the dashboard view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();
            Stage dashboardStage = new Stage();
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard.");
        }
    }

    @FXML
    private void handleDeleteAppointment() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            try {
                DatabaseHandler.getInstance().deleteAppointment(selectedAppointment);
                appointmentsTable.getItems().remove(selectedAppointment);
                showAlert("Success", "Appointment deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete appointment.");
            }
        } else {
            showAlert("Error", "Please select an appointment to delete.");
        }
    }

    private void fetchAppointments() {
        try {
            // Retrieve all appointments data from the database
            List<Appointment> appointments = DatabaseHandler.getInstance().getAllAppointments();

            // Clear any existing data in the TableView
            appointmentsTable.getItems().clear();

            // Populate the TableView with appointment data
            appointmentsTable.getItems().addAll(appointments);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch appointments.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
