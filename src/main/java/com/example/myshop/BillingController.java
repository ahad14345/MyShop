package com.example.myshop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class BillingController {

    @FXML private TextField searchField;
    @FXML private TextField quantityField;
    @FXML private ListView<String> searchResultsList;

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> colItem;
    @FXML private TableColumn<CartItem, Integer> colQty;
    @FXML private TableColumn<CartItem, Double> colPrice;
    @FXML private TableColumn<CartItem, Double> colTotal;

    @FXML private Label totalLabel;

    private ObservableList<CartItem> cartData = FXCollections.observableArrayList();
    private double grandTotal = 0.0;
    private final String[] mockProducts = {"Milk - $2.50", "Bread - $1.80", "Eggs (12pk) - $3.99", "Butter - $4.50"};

    @FXML
    public void initialize() {
        searchResultsList.getItems().addAll(mockProducts);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterList(newVal, mockProducts));

        colItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        cartTable.setItems(cartData);
    }

    private void filterList(String query, String[] allProducts) {
        searchResultsList.getItems().clear();
        for (String p : allProducts) {
            if (p.toLowerCase().contains(query.toLowerCase())) {
                searchResultsList.getItems().add(p);
            }
        }
    }

    @FXML
    private void handleAddToCart() {
        String selected = searchResultsList.getSelectionModel().getSelectedItem();
        if (selected != null && !quantityField.getText().isEmpty()) {
            try {
                String name = selected.split(" - ")[0];
                double price = Double.parseDouble(selected.split("\\$")[1]);
                int qty = Integer.parseInt(quantityField.getText());

                CartItem newItem = new CartItem(name, qty, price);
                cartData.add(newItem);

                grandTotal += newItem.getTotal();
                totalLabel.setText(String.format("$%.2f", grandTotal));
                quantityField.setText("1");
            } catch (Exception e) {
                System.out.println("Error adding to cart: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleGenerateBill() {
        if(!cartData.isEmpty()) {
            System.out.println("Sale Completed: " + totalLabel.getText());
            cartData.clear();
            grandTotal = 0.0;
            totalLabel.setText("$0.00");
        }
    }
}