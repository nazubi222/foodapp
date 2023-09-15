package com.example.foodorderapp.repository;

import android.widget.ImageView;

import com.example.foodorderapp.model.Food;

public interface MyFoodFavorireRepo {
    void addMyFoodFavorite(Food food);
    void deleteMyFoodFavorite(Food food, String idFood);
    void checkToHeartFood(String idFood, ImageView imageView);
}
