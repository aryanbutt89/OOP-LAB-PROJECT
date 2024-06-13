package application;

import application.models.Appointment;
import application.models.Doctor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminController {

    @FXML
    private VBox managePatientsPane;

    @FXML
    private VBox manageStaffPane;

    @FXML
    private HBox nameInputPane;

    @FXML
    private TextField nameTextField;

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableView<Doctor> doctorsTable;

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
    private TableColumn<Appointment, String> priceColumn;
    @FXML
    private TableColumn<Appointment, String> wardColumn;

    @FXML
    private TableColumn<Doctor, String> doctorNameColumn;

    @FXML
    private ComboBox<String> wardComboBox;

    private final DatabaseHandler databaseHandler = DatabaseHandler.getInstance();

    @FXML
    public void initialize() {
        // Hide the managePatientsPane, manageStaffPane, and nameInputPane initially
        managePatientsPane.setVisible(false);
        manageStaffPane.setVisible(false);
        nameInputPane.setVisible(false);

        // Initialize the columns for the appointments table
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        diseaseColumn.setCellValueFactory(cellData -> cellData.getValue().diseaseProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        doctorColumn.setCellValueFactory(cellData -> cellData.getValue().doctorProperty());
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        wardColumn.setCellValueFactory(cellData -> cellData.getValue().wardProperty());

        // Initialize the columns for the doctors table
        doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Populate the wardComboBox with example ward names
        wardComboBox.setItems(FXCollections.observableArrayList("Ward A", "Ward B", "Ward C"));
    }

    @FXML
    private void handleManagePatients() {
        // Display the managePatientsPane when "Manage Patients" button is clicked
        managePatientsPane.setVisible(true);
        manageStaffPane.setVisible(false);
        nameInputPane.setVisible(false);
        loadAppointments();
    }

    @FXML
    private void loadAppointments() {
        try {
            List<Appointment> appointments = databaseHandler.getAllAppointments();
            ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
            appointmentsTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePatient() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            try {
                databaseHandler.deleteAppointment(selectedAppointment);
                loadAppointments(); // Refresh the table
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle case where no appointment is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleManageStaff() {
        // Display the manageStaffPane when "Manage Staff" button is clicked
    	
        manageStaffPane.setVisible(true);
        managePatientsPane.setVisible(false);
        nameInputPane.setVisible(false);
        loadDoctors();
    }

    @FXML
    private void loadDoctors() {
        try {
            List<Doctor> doctors = databaseHandler.getAllDoctors();
            ObservableList<Doctor> doctorList = FXCollections.observableArrayList(doctors);
            doctorsTable.setItems(doctorList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteDoctor() {
        Doctor selectedDoctor = doctorsTable.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            try {
                databaseHandler.deleteDoctor(selectedDoctor);
                loadDoctors(); // Refresh the table
                showAlert("Success", "Doctor deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete doctor.");
            }
        } else {
            // Handle case where no doctor is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a doctor to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleViewReports(ActionEvent event) {
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
   
        // Show the nameInputPane when "View Reports" button is clicked
        managePatientsPane.setVisible(false);
        manageStaffPane.setVisible(false);
        nameInputPane.setVisible(true);
        
    }

    @FXML
    private void handleLogout(){// Close the current window
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
    private void handleAssignWardForPatients() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        String selectedWard = wardComboBox.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null && selectedWard != null) {
            selectedAppointment.setWard(selectedWard);
            try {
                databaseHandler.updateAppointment(selectedAppointment);
                loadAppointments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Handle case where no appointment or ward is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment and a ward.");
            alert.showAndWait();
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
