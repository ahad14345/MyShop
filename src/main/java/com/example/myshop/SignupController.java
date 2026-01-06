package com.example.myshop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleSignup() {
        String name = fullNameField.getText();
        String email = emailField.getText();
        String user = usernameField.getText();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            showErrorMessage("Error: All fields are required!");
            return;
        }

        if (!pass.equals(confirm)) {
            showErrorMessage("Error: Passwords do not match!");
            return;
        }
        String sql = "INSERT INTO users (fullname, email, username, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, user);
            pstmt.setString(4, pass);

            pstmt.executeUpdate();
            showSuccessAndRedirect();

        } catch (SQLException e) {
            String error = e.getMessage().toLowerCase();

            if (error.contains("username")) {
                showErrorMessage("Error: This username is already taken!");
            } else if (error.contains("email")) {
                showErrorMessage("Error: This email is already registered!");
            } else {
                showErrorMessage("Database error: " + e.getMessage());
            }
        } catch (Exception e) {
            showErrorMessage("An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    private void showErrorMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccessAndRedirect() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Created");
        alert.setHeaderText(null);
        alert.setContentText("Registration successful! Press OK to go to Login.");
        alert.showAndWait();

        try {
            mainApplication.showScene("login.fxml", "MyShop - Login");
        } catch (IOException e) {
            showErrorMessage("Failed to load login screen.");
        }
    }

    @FXML
    private void handleGoToLogin() {
        try {
            mainApplication.showScene("login.fxml", "MyShop - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}