package com.example.foodorderapp.model;

import java.util.List;

public class Customer {
    private String id;
    private String name;
    private String numberphone;
    private String email;
    private String gender;

    private String date;



    private String location;


    public Customer(String id, String name, String numberphone, String date, String email, String gender, String location) {
        this.id = id;
        this.name = name;
        this.numberphone = numberphone;
        this.email = email;
        this.gender = gender;
        this.date = date;
        this.location = location;
    }

    public Customer() {
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberphone() {
        return numberphone;
    }

    public void setNumberphone(String numberphone) {
        this.numberphone = numberphone;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
