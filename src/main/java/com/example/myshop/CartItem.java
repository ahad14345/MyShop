package com.example.myshop;

public class CartItem {
    private String name;
    private int quantity;
    private double price;
    private double total;

    public CartItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price; // Calculate initial total
    }

    // Getters - Required by TableView PropertyValueFactory
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return total; }

    // Setters - Useful if you allow editing quantity inside the table
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.updateTotal();
    }

    public void setPrice(double price) {
        this.price = price;
        this.updateTotal();
    }

    private void updateTotal() {
        this.total = this.quantity * this.price;
    }
}