package com.example.foodorderapp.viewmodel;

import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.repository.FirebaseMyFoodFavorire;
import com.example.foodorderapp.repository.MyFoodFavorireRepo;

public class MyFoodFavoriteViewModel extends ViewModel {
    private MyFoodFavorireRepo myFoodFavorireRepo = new FirebaseMyFoodFavorire();
    public void addMyFoodFavorite(Food food){
        myFoodFavorireRepo.addMyFoodFavorite(food);
    }
    public void deleteMyFoodFavorite(Food food, String idFood){
        myFoodFavorireRepo.deleteMyFoodFavorite(food, idFood);
    }
    public void checkToHeartFood(String idFood, ImageView imageView){
        myFoodFavorireRepo.checkToHeartFood(idFood, imageView);
    }
}
