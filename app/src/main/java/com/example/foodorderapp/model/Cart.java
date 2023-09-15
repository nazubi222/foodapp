package com.example.foodorderapp.model;

public class Cart {
    private String id;

    private Food food;
    private int amount;
    private boolean checkBox;

    public Cart(String id, Food food, int amount) {
        this.id = id;
        this.food = food;
        this.amount = amount;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }



    public Cart(Food food, int amount) {
        this.food = food;
        this.amount = amount;
    }
    public Cart(Food food, int amount, boolean checkBox) {
        this.food = food;
        this.amount = amount;
        this.checkBox = checkBox;
    }
    public Cart(String id, Food food, int amount, boolean checkBox) {
        this.food = food;
        this.amount = amount;
        this.checkBox = checkBox;
        this.id = id;
    }

    public Cart() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }
}
