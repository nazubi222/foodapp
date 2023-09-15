package com.example.foodorderapp.repository;

import android.util.Log;

import com.example.foodorderapp.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

public class FirebaseCartRepo implements CardRepo{
    private static final String CUSTOMER_COLLECTION = "customers";
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    private String uid = currentUser.getUid();

    @Override
    public void addFoodToCard(Cart cart) {

        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        Task<DocumentReference> colectionCard = cardRef.collection("carts").add(cart)
                .addOnSuccessListener((OnSuccessListener<DocumentReference>) documentReference -> {

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseUserRepository", "Error adding user", e);
                    }
                });

    }
    public boolean checkFoodToCard(String idFood){
        final boolean[] check = {false};
        CollectionReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid).collection("carts");
        cardRef.whereEqualTo("food.id", idFood)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if((document.get("food.id").toString()).equals(idFood)){
                                    check[0] = true;
                                }
                                Log.d("checkquery", (document.get("food.id").toString()));
                            }
                        } else {
                            Log.d("checkquery", "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {

                    }
                });
        return check[0];
    }

    @Override
    public void deleteFoodCart(Cart cart,String documentSnapshot) {
        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        Task<Void> documentReference = cardRef.collection("carts")
                .document(String.valueOf(documentSnapshot)).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("tad", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("tad", "Error deleting document", e);
                    }
                });
    }

    @Override
    public void updateFoodCart(int amount, String documentSnapshot) {
        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid)
                .collection("carts").document(String.valueOf(documentSnapshot));

        cardRef
                .update("amount", amount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    @Override
    public void updateFoodCartCheckBox(boolean check, String documentSnapshot) {
        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid)
                .collection("carts").document(String.valueOf(documentSnapshot));

        cardRef
                .update("checkBox", check)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

}
