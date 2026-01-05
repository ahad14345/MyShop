package com.example.myshop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

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
            statusLabel.setText("Error: All fields are required!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (!pass.equals(confirm)) {
            statusLabel.setText("Error: Passwords do not match!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        System.out.println("Registering user: " + user);
        statusLabel.setText("Account created successfully!");
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void handleGoToLogin() {
        System.out.println("Button Clicked: Attempting to switch to Login scene...");
        try {

            mainApplication.showScene("login.fxml", "My Shop - Admin Login");

            System.out.println("Scene switch successful!");
        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Could not load login.fxml. Check the filename spelling!");
            e.printStackTrace();
        }
    }
}