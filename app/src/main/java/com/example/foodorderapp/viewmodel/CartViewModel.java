package com.example.foodorderapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.repository.CardRepo;
import com.example.foodorderapp.repository.FirebaseCartRepo;
import com.google.firebase.firestore.DocumentSnapshot;

public class CartViewModel extends ViewModel {
    private CardRepo cardRepo = new FirebaseCartRepo();
    public void addFoodToCart(Cart cart){
        cardRepo.addFoodToCard(cart);
    }
    public boolean checkFoodToCard(String idFood){
        return cardRepo.checkFoodToCard(idFood);
    }
    public void updateFoodCart(int amount, String documentSnapshot){
        cardRepo.updateFoodCart(amount, documentSnapshot);
    }
    public void deleteFoodCart(Cart cart, String documentSnapshot){
        cardRepo.deleteFoodCart(cart, documentSnapshot);
    }
    public void updateFoodCartCheckBox(boolean check, String documentSnapshot){
        cardRepo.updateFoodCartCheckBox(check, documentSnapshot);
    }

}
