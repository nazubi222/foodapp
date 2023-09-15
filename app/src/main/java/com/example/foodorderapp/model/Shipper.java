package com.example.foodorderapp.model;

public class Shipper {


    private Customer customer;

    public Shipper(Customer customer) {
        this.customer = customer;
    }
    public Shipper() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
