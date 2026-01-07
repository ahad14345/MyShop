package com.example.myshop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class BillingController {

    @FXML private TextField searchField;
    @FXML private TextField quantityField;
    @FXML private TextField customerNameField;
    @FXML private ListView<Product> searchResultsList;

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> colItem;
    @FXML private TableColumn<CartItem, Integer> colQty;
    @FXML private TableColumn<CartItem, Double> colPrice;
    @FXML private TableColumn<CartItem, Double> colTotal;

    @FXML private Label totalLabel;

    private ObservableList<CartItem> cartData = FXCollections.observableArrayList();
    private double grandTotal = 0.0;

    @FXML
    public void initialize() {
        // Link TableColumns to CartItem properties
        colItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        cartTable.setItems(cartData);

        // Listener for real-time search
        searchField.textProperty().addListener((obs, oldVal, newVal) -> searchProductFromDB(newVal));

        // Initial load of products
        searchProductFromDB("");
    }

    private void searchProductFromDB(String query) {
        searchResultsList.getItems().clear();
        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + query + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                searchResultsList.getItems().add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddToCart() {
        Product selected = searchResultsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                int qty = Integer.parseInt(quantityField.getText());

                if (qty <= 0) {
                    showAlert("Invalid Quantity", "Quantity must be greater than 0.");
                    return;
                }

                if (qty > selected.getStock()) {
                    showAlert("Insufficient Stock", "Only " + selected.getStock() + " items left in stock.");
                    return;
                }

                CartItem newItem = new CartItem(selected.getName(), qty, selected.getPrice());
                cartData.add(newItem);

                updateGrandTotal(newItem.getTotal());
                quantityField.setText("1");

            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number for quantity.");
            }
        } else {
            showAlert("No Selection", "Please select a product from the list first.");
        }
    }
    @FXML
    private void handleClearSearch() {
        searchField.clear();
        quantityField.setText("1");
        searchProductFromDB(""); // Refresh the full list
    }
    @FXML
    private void handleRemoveItem() {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            updateGrandTotal(-selectedItem.getTotal()); // Pass negative to subtract
            cartData.remove(selectedItem);
        } else {
            showAlert("No Selection", "Select an item in the cart table to remove.");
        }
    }

    private void updateGrandTotal(double amount) {
        grandTotal += amount;
        // Ensure total doesn't show -0.00 due to floating point precision
        if (Math.abs(grandTotal) < 0.001) grandTotal = 0.0;
        totalLabel.setText(String.format("$%.2f", grandTotal));
    }

    @FXML
    private void handleGenerateBill() {
        if (cartData.isEmpty()) {
            showAlert("Cart Empty", "Add items to the cart before completing the sale.");
            return;
        }

        String customer = (customerNameField != null && !customerNameField.getText().isEmpty())
                ? customerNameField.getText() : "Walk-in Customer";



        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            String billSql = "INSERT INTO bills (customer_name, total_amount) VALUES (?, ?)";
            PreparedStatement pstmtBill = conn.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS);
            pstmtBill.setString(1, customer);
            pstmtBill.setDouble(2, grandTotal);
            pstmtBill.executeUpdate();

            ResultSet generatedKeys = pstmtBill.getGeneratedKeys();
            int billId = generatedKeys.next() ? generatedKeys.getInt(1) : -1;

            String itemSql = "INSERT INTO bill_items (bill_id, product_name, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
            String stockSql = "UPDATE products SET stock = stock - ? WHERE name = ?";

            PreparedStatement pstmtItem = conn.prepareStatement(itemSql);
            PreparedStatement pstmtStock = conn.prepareStatement(stockSql);

            for (CartItem item : cartData) {
                pstmtItem.setInt(1, billId);
                pstmtItem.setString(2, item.getName());
                pstmtItem.setInt(3, item.getQuantity());
                pstmtItem.setDouble(4, item.getPrice());
                pstmtItem.setDouble(5, item.getTotal());
                pstmtItem.addBatch();

                pstmtStock.setInt(1, item.getQuantity());
                pstmtStock.setString(2, item.getName());
                pstmtStock.addBatch();
            }

            pstmtItem.executeBatch();
            pstmtStock.executeBatch();

            conn.commit(); // Finalize all changes
            showAlert("Success", "Bill #" + billId + " Generated Successfully!");

            clearBillingArea();
            searchProductFromDB(""); // Refresh list to show updated stock levels

        } catch (SQLException e) {
            showAlert("Database Error", "The transaction failed. Changes were not saved.");
            e.printStackTrace();
        }
    }

    private void clearBillingArea() {
        cartData.clear();
        grandTotal = 0.0;
        totalLabel.setText("$0.00");
        if(customerNameField != null) customerNameField.clear();
        if(searchField != null) searchField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}