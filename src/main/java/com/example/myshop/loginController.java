package com.example.myshop;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    protected void handlelogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.equals("Nabil") && pass.equals("HelloNabil")) {
            try {
                System.out.println("Login successful. Opening Dashboard...");
                mainApplication.showScene("dashboard.fxml", "MyShop - Admin Dashboard");

            } catch (IOException e) {
                errorLabel.setText("Error: Could not load Dashboard file.");
                e.printStackTrace();
            }
        } else if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please enter both username and password.");
            errorLabel.setStyle("-fx-text-fill: orange;");
        } else {
            errorLabel.setText("Invalid username or password!");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }
    @FXML
    private void handleGoToSignup() {
        try {
            System.out.println("Switching to Sign Up page...");
            mainApplication.showScene("signUp.fxml", "MyShop - Create Account");
        } catch (IOException e) {
            System.err.println("Failed to load signUp.fxml");
            e.printStackTrace();
        }
    }
}
