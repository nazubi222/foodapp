package com.example.foodorderapp.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class BillFood {
    private String id;
    private Cart cart;
    private Customer customer;
    private Shipper shipper;
    private String status;
    private int totalMoney;
    private @ServerTimestamp Date createdDate;
    private Date endDate;
    private Date dateGetBill;
    private Date dateReceiverBill;
    private boolean evaluated;



    public BillFood() {
    }

    public BillFood(String id, Cart cart, Customer customer, Shipper shipper,
                    String status, int totalMoney) {
        this.id = id;
        this.cart = cart;
        this.customer = customer;
        this.shipper = shipper;
        this.status = status;
        this.totalMoney = totalMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDateGetBill() {
        return dateGetBill;
    }

    public void setDateGetBill(Date dateGetBill) {
        this.dateGetBill = dateGetBill;
    }

    public Date getDateReceiverBill() {
        return dateReceiverBill;
    }

    public void setDateReceiverBill(Date dateReceiverBill) {
        this.dateReceiverBill = dateReceiverBill;
    }

    public boolean getEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }
}
