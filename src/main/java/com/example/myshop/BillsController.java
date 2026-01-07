package com.example.myshop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class BillsController {
    @FXML private TableView<Bill> billsTable;
    @FXML private TableColumn<Bill, Integer> colBillId;
    @FXML private TableColumn<Bill, String> colDate;
    @FXML private TableColumn<Bill, String> colCustomer;
    @FXML private TableColumn<Bill, Double> colTotal;

    @FXML private TableView<CartItem> detailsTable;
    @FXML private TableColumn<CartItem, String> colItemName;
    @FXML private TableColumn<CartItem, Integer> colItemQty;
    @FXML private TableColumn<CartItem, Double> colItemPrice;

    @FXML private Label itemCountLabel;

    private ObservableList<Bill> masterList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colBillId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadBillsFromDatabase();
        billsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadBillDetails(newSelection.getId());
            }
        });
    }

    private void loadBillsFromDatabase() {
        masterList.clear();
        String sql = "SELECT * FROM bills ORDER BY sale_date DESC";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                masterList.add(new Bill(
                        rs.getInt("bill_id"),
                        rs.getString("sale_date"),
                        rs.getString("customer_name"),
                        rs.getDouble("total_amount")
                ));
            }
            billsTable.setItems(masterList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBillDetails(int billId) {
        ObservableList<CartItem> itemDetails = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bill_items WHERE bill_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                itemDetails.add(new CartItem(
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                ));
            }
            detailsTable.setItems(itemDetails);
            itemCountLabel.setText(String.valueOf(itemDetails.size()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadBillsFromDatabase();
        detailsTable.getItems().clear();
        itemCountLabel.setText("0");
    }
}