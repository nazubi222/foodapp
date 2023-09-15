package com.example.foodorderapp.repository;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

public class FirebaseMyFoodFavorire implements MyFoodFavorireRepo {
    private static final String CUSTOMER_COLLECTION = "customers";
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    private String uid = currentUser.getUid();
    @Override
    public void addMyFoodFavorite(Food food) {
        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        Task<DocumentReference> colectionFood = cardRef.collection("myfoodfavorites").add(food)
                .addOnSuccessListener((OnSuccessListener<DocumentReference>) documentReference -> {

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseUserRepository", "Error adding user", e);
                    }
                });

    }

    @Override
    public void deleteMyFoodFavorite(Food food, String idfood) {
        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        CollectionReference collectionReference = cardRef.collection("myfoodfavorites");
        Query query = collectionReference.whereEqualTo("id", idfood);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void checkToHeartFood(String idFood, ImageView imageView) {

        CollectionReference foodFavoriteRef = database.collection(CUSTOMER_COLLECTION).document(uid).collection("myfoodfavorites");
        foodFavoriteRef.whereEqualTo("id", idFood)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                imageView.setImageResource(R.drawable.heart1);
                            }
                        }
                    }
                });
    }
}
