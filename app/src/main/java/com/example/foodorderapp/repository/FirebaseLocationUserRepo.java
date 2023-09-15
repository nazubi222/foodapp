package com.example.foodorderapp.repository;

import android.util.Log;

import com.example.foodorderapp.model.Location;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class FirebaseLocationUserRepo implements LocationUserRepo{
    private static final String CUSTOMER_COLLECTION = "customers";
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    private String uid = currentUser.getUid();

    @Override
    public void addLocation(Location location) {


        DocumentReference locationRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        Task<DocumentReference> collectionReference = locationRef.collection("locations").add(location)
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
    public void deleteLocation(Location location, String documentSnapshot) {

        DocumentReference locationRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        Task<Void> collectionReference = locationRef.collection("locations").document(String.valueOf(documentSnapshot)).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("delete", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("delete", "Error deleting document", e);
                    }
                });
    }

    @Override
    public void updateLocation(String location, String documentSnapshot) {
        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        cardRef
                .update("location", location)
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

