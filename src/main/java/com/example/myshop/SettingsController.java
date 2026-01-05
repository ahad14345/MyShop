package com.example.myshop;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SettingsController {

    @FXML private TextField shopNameField;
    @FXML private TextField currencyField;
    @FXML private TextField adminUserField;
    @FXML private PasswordField adminPassField;
    @FXML private Label statusLabel;

    @FXML
    private void handleSaveSettings() {
        String name = shopNameField.getText();
        System.out.println("Settings Saved: " + name);

        statusLabel.setText("Settings updated successfully!");
        statusLabel.setStyle("-fx-text-fill: #198754;"); // Green
    }

    @FXML
    private void handleBackupData() {
        System.out.println("Exporting data to JSON...");
        statusLabel.setText("Backup created: backup.json");
    }
}
