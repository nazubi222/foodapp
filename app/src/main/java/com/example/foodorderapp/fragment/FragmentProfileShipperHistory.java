package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodCartStatusAdapter;
import com.example.foodorderapp.model.BillFood;
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

public class FragmentProfileShipperHistory extends Fragment {
    private RecyclerView recyclerView;
    private List<BillFood> listBill;
    private ProgressDialog progressDialog;
    private NavController navController;
    private FoodCartStatusAdapter foodCartStatusAdapter;
    private View mView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_shipper_history, container, false);
        initUI();

        return mView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgshipperbillhistory_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void initUI() {
        recyclerView = mView.findViewById(R.id.rcv_shipperhistory);
        progressDialog = new ProgressDialog(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        listBill = new ArrayList<>();
        getListBill();
        foodCartStatusAdapter = new FoodCartStatusAdapter(listBill, getActivity(), new FoodCartStatusAdapter.IClickItemFoodCartStatus() {
            @Override
            public void onClickItemFoodCartStatus(BillFood billFood) {
            }
        });
        recyclerView.setAdapter(foodCartStatusAdapter);
    }
    private void getListBill() {
        progressDialog.show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("shipper.customer.id", uid);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            if(documentSnapshot.exists()) {
                                BillFood billFood = documentSnapshot.toObject(BillFood.class);
                                if (billFood.getStatus().equals("Giao hàng thành công!")){
                                    listBill.add(billFood);
                                }

                            }
                        }
                        foodCartStatusAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }
}
