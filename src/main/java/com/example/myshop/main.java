package com.example.myshop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class main {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
