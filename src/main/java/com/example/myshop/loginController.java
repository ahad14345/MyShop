package com.example.myshop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;
    public static String loggedInUserName;

    @FXML
    protected void handlelogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            errorLabel.setStyle("-fx-text-fill: orange;");
            return;
        }

        String sql = "SELECT fullname FROM users WHERE username = ? AND password = ?";



        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                loggedInUserName = rs.getString("fullname");

                System.out.println("Login successful. Welcome, " + loggedInUserName);
                mainApplication.showScene("dashboard.fxml", "MyShop - Admin Dashboard");
            } else {
                errorLabel.setText("Invalid username or password!");
                errorLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (SQLException e) {
            errorLabel.setText("Database error. Check if 'myshop.db' exists.");
            e.printStackTrace();
        } catch (Exception e) {
            errorLabel.setText("An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGoToSignup() {
        try {
            mainApplication.showScene("signUp.fxml", "MyShop - Create Account");
        } catch (IOException e) {
            System.err.println("Could not load signUp.fxml");
            e.printStackTrace();
        }
    }
}