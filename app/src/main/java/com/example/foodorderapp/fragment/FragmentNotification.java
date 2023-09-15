package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.NotificationAdapter;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Notification;
import com.example.foodorderapp.viewmodel.NotificationViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentNotification extends Fragment {
    private NavController navController;
    private View mView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> listNotification;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notification, container, false);
        initUI();
        return mView;
    }

    private void initUI() {
        recyclerView = mView.findViewById(R.id.rcv_notification);
        progressDialog = new ProgressDialog(getActivity());

        listNotification = new ArrayList<>();
        getListNotifi();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        notificationAdapter = new NotificationAdapter(listNotification, getActivity());
        recyclerView.setAdapter(notificationAdapter);


    }

    private void getListNotifi() {
        progressDialog.show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("customer.id", uid);
//        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot querySnapshot) {
//                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
//                            if (documentSnapshot.exists()) {
//                                BillFood billFood = documentSnapshot.toObject(BillFood.class);
//                                Log.d("billfood", billFood.getStatus());
//                                String name = billFood.getCart().getFood().getName().toString() + "-" +billFood.getCart().getFood().getNameRestaurant().toString();
//                                Notification notification = new Notification(name, billFood.getStatus(), billFood.getCreatedDate(), false);
//                                listNotification.add(notification);
//
//
//                            }
//                        }
//                        notificationAdapter.notifyDataSetChanged();
//                        progressDialog.dismiss();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
//                        progressDialog.dismiss();
//                    }
//                });
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }
                List<BillFood> newDataList = new ArrayList<>();
                if (value != null ) {
                    for (DocumentSnapshot document : value.getDocuments()) {
                        BillFood billFood = document.toObject(BillFood.class);
                        Log.d("billfood", billFood.getStatus());
                        String name = billFood.getCart().getFood().getName().toString() + "-" +billFood.getCart().getFood().getNameRestaurant().toString();
                        Notification notification = new Notification(name, billFood.getStatus(), billFood.getCreatedDate(), false);
                        listNotification.add(notification);

                    }
                    notificationAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        });
    }

}
