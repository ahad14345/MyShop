package com.example.myshop;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import java.io.IOException;

public class DashboardController {

    @FXML private BorderPane mainPane;

    // Inject all sidebar buttons to change their styles
    @FXML private Button btnDashboard, btnProducts, btnBilling, btnBills, btnSettings;

    private final String IDLE_STYLE = "-fx-background-color: transparent; -fx-text-fill: #333;";
    private final String ACTIVE_STYLE = "-fx-background-color: #0D6EFD; -fx-text-fill: white; -fx-font-weight: bold;";

    @FXML
    public void initialize() {
        loadView("homeView.fxml");
        setActiveButton(btnDashboard);
    }

    private void loadView(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button activeBtn) {
        btnDashboard.setStyle(IDLE_STYLE);
        btnProducts.setStyle(IDLE_STYLE);
        btnBilling.setStyle(IDLE_STYLE);
        btnBills.setStyle(IDLE_STYLE);
        btnSettings.setStyle(IDLE_STYLE);
        activeBtn.setStyle(ACTIVE_STYLE);
    }
    @FXML
    private void showDashboard(ActionEvent event) {
        loadView("homeView.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void showProducts(ActionEvent event) {
        loadView("products.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void showBilling(ActionEvent event) {
        loadView("billing.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void showBills(ActionEvent event) {
        loadView("bills.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void showSettings(ActionEvent event) {
        loadView("settings.fxml");
        setActiveButton((Button) event.getSource());
    }

    @FXML
    private void handleLogout() throws IOException {
        mainApplication.showScene("login.fxml", "Login");
    }
}