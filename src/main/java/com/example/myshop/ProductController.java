package com.example.myshop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;

    @FXML private TextField nameInput;
    @FXML private TextField priceInput;
    @FXML private TextField stockInput;
    @FXML private TextField searchField;

    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Link table columns to Product class getters
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        loadProductsFromDatabase();

        setupSearchFilter();
    }

    private void loadProductsFromDatabase() {
        productList.clear();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productList.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
            productTable.setItems(productList);

        } catch (SQLException e) {
            showError("Database Error", "Could not load products: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddProduct() {
        String name = nameInput.getText();
        String priceText = priceInput.getText();
        String stockText = stockInput.getText();
        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            showError("Input Error", "Please fill all fields!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, stock);
                pstmt.executeUpdate();

                loadProductsFromDatabase();
                clearInputs();

            }
        } catch (NumberFormatException e) {
            showError("Input Error", "Price must be a number and Stock must be an integer.");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                showError("Duplicate Error", "A product with this name already exists.");
            } else {
                showError("Database Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Selection Error", "Please select a product to delete.");
            return;
        }

        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, selected.getId());
            pstmt.executeUpdate();

            loadProductsFromDatabase(); // Refresh table

        } catch (SQLException e) {
            showError("Database Error", "Could not delete product.");
        }
    }

    private void setupSearchFilter() {
        FilteredList<Product> filteredData = new FilteredList<>(productList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();

                return product.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        productTable.setItems(filteredData);
    }

    private void clearInputs() {
        nameInput.clear();
        priceInput.clear();
        stockInput.clear();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}