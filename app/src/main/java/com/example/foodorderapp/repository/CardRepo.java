package com.example.foodorderapp.repository;

import com.example.foodorderapp.model.Cart;
import com.google.firebase.firestore.DocumentSnapshot;

public interface CardRepo {
    void addFoodToCard(Cart cart);
    boolean checkFoodToCard(String idFood);

    void deleteFoodCart(Cart cart, String documentSnapshot);

    void updateFoodCart(int amount, String documentSnapshot);
    void updateFoodCartCheckBox(boolean check, String documentSnapshot);


}
