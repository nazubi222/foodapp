package com.example.foodorderapp.model;

public class Food {
    private String id;
    private String idPhoto;
    private String name;
    private String typeFood;
    private String infomationFood;
    private String price;
    private String idRestaurant;
    private String locationRestaurant;
    private String nameRestaurant;
    private boolean favorite;
    private int amount;
    private float evaluate;

    public Food() {
    }



    public Food(String id, String idPhoto, String name, String typeFood
            , String infomationFood, String price, String idRestaurant, String locationRestaurant, String nameRestaurant) {
        this.id = id;
        this.idPhoto = idPhoto;
        this.name = name;
        this.typeFood = typeFood;
        this.infomationFood = infomationFood;
        this.price = price;
        this.idRestaurant = idRestaurant;
        this.locationRestaurant = locationRestaurant;
        this.nameRestaurant = nameRestaurant;
    }

    public Food(String id, String idPhoto, String name, String typeFood
            , String infomationFood, String price, String idRestaurant, String locationRestaurant, String nameRestaurant, int amount, float evaluate) {
        this.id = id;
        this.idPhoto = idPhoto;
        this.name = name;
        this.typeFood = typeFood;
        this.infomationFood = infomationFood;
        this.price = price;
        this.idRestaurant = idRestaurant;
        this.locationRestaurant = locationRestaurant;
        this.nameRestaurant = nameRestaurant;
        this.amount = amount;
        this.evaluate = evaluate;
    }

    public Food(String id, String idPhoto, String name, String typeFood, String infomationFood
            , String price, String idRestaurant, String locationRestaurant, String nameRestaurant, boolean favorite) {
        this.id = id;
        this.idPhoto = idPhoto;
        this.name = name;
        this.typeFood = typeFood;
        this.infomationFood = infomationFood;
        this.price = price;
        this.idRestaurant = idRestaurant;
        this.locationRestaurant = locationRestaurant;
        this.nameRestaurant = nameRestaurant;
        this.favorite = favorite;
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

    public String getTypeFood() {
        return typeFood;
    }

    public void setTypeFood(String typeFood) {
        this.typeFood = typeFood;
    }

    public String getInfomationFood() {
        return infomationFood;
    }

    public void setInfomationFood(String infomationFood) {
        this.infomationFood = infomationFood;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }



    public String getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(String idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public void setLocationRestaurant(String locationRestaurant) {
        this.locationRestaurant = locationRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getLocationRestaurant() {
        return locationRestaurant;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean getFavorite() {
        return favorite;
    }


    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(float evaluate) {
        this.evaluate = evaluate;
    }
}
