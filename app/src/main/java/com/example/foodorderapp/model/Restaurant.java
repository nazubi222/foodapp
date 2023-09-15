package com.example.foodorderapp.model;

public class Restaurant {
    private String id;
    private String name;
    private String location;

    public Restaurant(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
    public Restaurant( String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Restaurant() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
