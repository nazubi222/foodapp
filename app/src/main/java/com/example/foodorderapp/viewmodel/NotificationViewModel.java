package com.example.foodorderapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Notification;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewModel extends ViewModel {
    private List<Notification> listNotification;

    public NotificationViewModel(){
        initData();
    }
    private void initData() {
        listNotification = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("customer.id", uid);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            if (documentSnapshot.exists()) {
                                BillFood billFood = documentSnapshot.toObject(BillFood.class);
                                Log.d("billfood", billFood.getStatus());
                                String name = billFood.getCart().getFood().getName() + "-" +billFood.getCart().getFood().getNameRestaurant();
                                Notification notification = new Notification(name, billFood.getStatus(), billFood.getEndDate(), false);
                                listNotification.add(notification);


                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                    }
                });
    }

    public List<Notification> getListNotification(){
        return listNotification;
    }
}
