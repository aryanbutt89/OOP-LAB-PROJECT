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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NurseController {

    @FXML
    private Button createAppointmentButton;

    @FXML
    private TableView<Appointment> appointmentTable;

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
    private TextField searchField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField diseaseField;

    @FXML
    private TextField timeField;

    @FXML
    private ComboBox<Doctor> doctorComboBox;

    @FXML
    private TextField genderField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField wardField;

    private ObservableList<Appointment> appointmentList;

    private Appointment selectedAppointment;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        diseaseColumn.setCellValueFactory(new PropertyValueFactory<>("disease"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        wardColumn.setCellValueFactory(new PropertyValueFactory<>("ward"));

        fetchAppointments();
        loadDoctors();

        appointmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedAppointment = newSelection;
                priceField.setText(selectedAppointment.getPrice());
                wardField.setText(selectedAppointment.getWard());
            }
        });

        doctorComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        doctorComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    private void fetchAppointments() {
        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAllAppointments();
            appointmentList = FXCollections.observableArrayList(appointments);
            appointmentTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load appointments.");
        }
    }

    private void loadDoctors() {
        try {
            List<Doctor> doctors = DatabaseHandler.getInstance().getAllDoctors();
            doctorComboBox.getItems().clear(); // Clear existing items if any
            doctorComboBox.getItems().addAll(doctors);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load doctors.");
        }
    }

    @FXML
    private void handleCreateAppointment() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String disease = diseaseField.getText();
        String time = timeField.getText();
        Doctor doctor = doctorComboBox.getSelectionModel().getSelectedItem();
        String gender = genderField.getText();
        String price = priceField.getText();

        createAppointmentButton.setVisible(false);

        if (name.isEmpty() || phone.isEmpty() || disease.isEmpty() || time.isEmpty() || doctor == null || gender.isEmpty() || price.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Validate and format time input
        if (!isValidTimeFormat(time)) {
            showAlert("Error", "Invalid time format. Please enter time in HH:MM AM/PM format.");
            return;
        }

        try {
            DatabaseHandler.getInstance().saveAppointmentToPatientProfile(name, phone, disease, time, doctor.getName(), gender, price);
            showAlert("Success", "Appointment created successfully.");
            fetchAppointments();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to create appointment.");
        }
    }

    @FXML
    private void handleCreateAppointmentButtonAction(ActionEvent event) {
        nameField.setVisible(true);
        phoneField.setVisible(true);
        diseaseField.setVisible(true);
        timeField.setVisible(true);
        doctorComboBox.setVisible(true);
        genderField.setVisible(true);
        priceField.setVisible(true);
        wardField.setVisible(true); // Hide the ward field

        createAppointmentButton.setVisible(true);
    }

    @FXML
    private void handleUpdateAppointment(ActionEvent event) {
        if (selectedAppointment == null) {
            showAlert("Error", "No appointment selected.");
            return;
        }

        String price = priceField.getText();
        String ward = wardField.getText();

        if (price.isEmpty() || ward.isEmpty()) {
            showAlert("Error", "Please enter both price and ward.");
            return;
        }

        try {
            selectedAppointment.setPrice(price);
            selectedAppointment.setWard(ward);
            DatabaseHandler.getInstance().updateAppointment(selectedAppointment);
            showAlert("Success", "Appointment updated successfully.");
            fetchAppointments();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update appointment.");
        }
    }

    @FXML
    private void handleSearchAppointments(ActionEvent event) {
        String searchName = searchField.getText();
        if (searchName.isEmpty()) {
            showAlert("Error", "Please enter a name to search.");
            return;
        }

        try {
            List<Appointment> appointments = DatabaseHandler.getInstance().getAppointmentsByName(searchName);
            appointmentList = FXCollections.observableArrayList(appointments);
            appointmentTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search appointments.");
        }
    }

    @FXML
    private void handleDeleteAppointment(ActionEvent event) {
        if (selectedAppointment == null) {
            showAlert("Error", "No appointment selected.");
            return;
        }

        try {
            DatabaseHandler.getInstance().deleteAppointment(selectedAppointment);
            appointmentTable.getItems().remove(selectedAppointment);
            showAlert("Success", "Appointment deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete appointment.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Stage stage = (Stage) appointmentTable.getScene().getWindow();
        stage.close();

        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load login view.");
        }
    }

    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        diseaseField.clear();
        timeField.clear();
        doctorComboBox.getSelectionModel().clearSelection();
        genderField.clear();
        priceField.clear();
        wardField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidTimeFormat(String time) {
        // Regular expression to match HH:MM AM/PM format
        String timeRegex = "^([1-9]|1[0-2]):([0-5][0-9])\\s(?:AM|PM)$";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }
}
