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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodCartStatusAdapter;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FragmentCartHistory extends Fragment {
    private NavController navController;
    private View mView;
    private RecyclerView recyclerView;
    private List<BillFood> listBillFood;
    private FoodCartStatusAdapter foodCartStatusAdapter;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart_history, container, false);
        initUI();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
    }


    private void initUI() {
        recyclerView = mView.findViewById(R.id.rcv_foodcarthistory);
        progressDialog = new ProgressDialog(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        listBillFood = new ArrayList<>();
        getListBillFood();
        foodCartStatusAdapter = new FoodCartStatusAdapter(listBillFood, getActivity(), new FoodCartStatusAdapter.IClickItemFoodCartStatus() {
            @Override
            public void onClickItemFoodCartStatus(BillFood billFood) {
                Gson gson = new Gson();
                String json = gson.toJson(billFood);
                Bundle bundle = new Bundle();
                bundle.putString("billhistory", json);
                navController.navigate(R.id.action_fragmentCart_to_fragmentCartHistoryBill, bundle);

            }
        });
        recyclerView.setAdapter(foodCartStatusAdapter);

    }

    private void getListBillFood() {
        progressDialog.show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("customer.id", uid);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Xử lý lỗi nếu có
                    return;
                }
                if (value != null ) {
                    List<BillFood> newlist = new ArrayList<>();
                    for (DocumentSnapshot document : value.getDocuments()) {
                        BillFood billFood = document.toObject(BillFood.class);
                        if (billFood.getStatus().equals("Giao hàng thành công!") ||
                                billFood.getStatus().equals("Đơn đã hủy!")) {
                            newlist.add(billFood);
                        }
                    }
                    listBillFood.clear();
                    listBillFood.addAll(newlist);
                    foodCartStatusAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        });
//        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot querySnapshot) {
//                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
//                            if(documentSnapshot.exists()) {
//                                BillFood billFood = documentSnapshot.toObject(BillFood.class);
//                                if (billFood.getStatus().equals("Giao hàng thành công!") || billFood.getStatus().equals("Đơn đã hủy!")){
//                                    listBillFood.add(billFood);
//                                }
//
//                            }
//                        }
//                        foodCartStatusAdapter.notifyDataSetChanged();
//                        progressDialog.dismiss();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
//                        progressDialog.dismiss();
//                    }
//                });
    }
}
