package com.example.myshop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class mainApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        try {
            DatabaseManager.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Database Initialization Failed!");
            e.printStackTrace();
        }

        try {
            showScene("login.fxml", "My Shop - Admin Login");
        } catch (IOException e) {
            System.err.println("Critical Error: Could not load login.fxml. Check your resources folder.");
            e.printStackTrace();
        }
    }
    public static void showScene(String fxml, String title) throws IOException {
        URL resource = mainApplication.class.getResource(fxml);

        if (resource == null) {
            throw new IOException("FXML file not found: " + fxml);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        Parent root = fxmlLoader.load();

        if (primaryStage.getScene() == null) {
            Scene scene = new Scene(root, 900, 600);
            primaryStage.setScene(scene);
        } else {
            primaryStage.getScene().setRoot(root);
        }

        primaryStage.setTitle(title);
        primaryStage.centerOnScreen();
        primaryStage.show();

        System.out.println("Scene switched to: " + fxml);
    }

    public static void main(String[] args) {
        launch(args);
    }
}