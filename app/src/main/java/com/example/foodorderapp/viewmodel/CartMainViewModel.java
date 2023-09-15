package com.example.foodorderapp.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartMainViewModel extends ViewModel {
    private MutableLiveData<List<Cart>> listMutableLiveData;

    private List<Cart> listCart;

    public CartMainViewModel() {
        listMutableLiveData = new MutableLiveData<>();
        listCart = new ArrayList<>();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        DocumentReference cardRef = database.collection("customers").document(uid);
        CollectionReference collectionReference = cardRef.collection("carts");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document == null){
                            return;
                        }
                        String documentID = document.getId();
                        Food food = document.get("food", Food.class);
                        int amount = document.get("amount", Integer.class);
                        Cart cart  = new Cart(food, amount);
                        cart.setId(documentID);
                        Log.d("getFoodCart", food.getName()
                                + food.getPrice() + food.getNameRestaurant() + documentID +amount);

                        listCart.add(cart);
                    }

                }
            }
        });
        listMutableLiveData.setValue(listCart);
    }

    public MutableLiveData<List<Cart>> getListMutableLiveData() {
        return listMutableLiveData;
    }
}
