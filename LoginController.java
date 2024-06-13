package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String username = validateLogin(email, password);
        if (username != null) {
            showAlert("Login Successful", "Welcome " + username);
            loadDashboard(); // Call the method to load the dashboard
        } else {
            showAlert("Login Failed", "Invalid email or password");
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (!email.isEmpty() && !password.isEmpty()) {
            String username = getUsernameByEmail(email);
            if (username != null && deleteUser(email, password)) {
                showAlert("Deletion Successful",  username + " has been deleted from the Data.");
            } else {
                showAlert("Deletion Failed", "Could not delete user. User might not exist.");
            }
        } else {
            showAlert("Deletion Failed", "Please enter both email and password.");
        }
    }

    @FXML
    void handleDeleteAllUsers(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Authentication");
        dialog.setHeaderText("Enter Admin Credentials");
        dialog.setContentText("Email:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String enteredEmail = result.get();
            if (enteredEmail.equals("admin@example.com")) {
                PasswordField passwordInput = new PasswordField();
                dialog.getDialogPane().setContent(passwordInput);
                dialog.setHeaderText("Enter Password:");
                dialog.setContentText("Password:");
                Optional<String> passwordResult = dialog.showAndWait();
                if (passwordResult.isPresent()) {
                    String enteredPassword = passwordResult.get();
                    if (enteredPassword.equals("adminPassword")) {
                        if (deleteAllUsers()) {
                            showAlert("Deletion Successful", "All users have been deleted.");
                        } else {
                            showAlert("Deletion Failed", "Failed to delete all users.");
                        }
                    } else {
                        showAlert("Authentication Failed", "Invalid password.");
                    }
                }
            } else {
                showAlert("Authentication Failed", "Invalid email.");
            }
        }
    }

    private String validateLogin(String email, String password) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE email = ? AND password = ?")) {
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getUsernameByEmail(String email) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE email = ?")) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean deleteUser(String email, String password) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE email = ? AND password = ?")) {
            statement.setString(1, email);
            statement.setString(2, password);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteAllUsers() {
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate("DELETE FROM users");
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadDashboard() {
        try {
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("dashboard.fxml"));

            Stage currentStage = (Stage) emailField.getScene().getWindow();

            Scene scene = new Scene(dashboardRoot);
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
