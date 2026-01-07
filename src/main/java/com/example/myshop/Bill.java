package com.example.myshop;

public class Bill {
    private int id;
    private String date;
    private String customer;
    private double total;

    public Bill(int id, String date, String customer, double total) {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.total = total;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public String getCustomer() { return customer; }
    public double getTotal() { return total; }
}