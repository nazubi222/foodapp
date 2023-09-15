package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfileShipper extends Fragment {
    private NavController navController;
    private View mView;
    private RecyclerView recyclerView;
    private TextView tvHistoryShip, tvProcessingShip;
    private List<BillFood> listBill;
    private FoodCartStatusAdapter foodCartStatusAdapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_shipper, container, false);
        initUI();
        setOnClick();
        return mView;
    }

    private void setOnClick() {
        tvHistoryShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfileShipper_to_fragmentProfileShipperHistory);
            }
        });

        tvProcessingShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfileShipper_to_fragmentProfileShipperProcessing);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgshipper_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void initUI() {
        recyclerView = mView.findViewById(R.id.rcv_profileshipper);
        progressDialog = new ProgressDialog(getActivity());
        tvProcessingShip = mView.findViewById(R.id.tv_shipperprocessing);
        tvHistoryShip = mView.findViewById(R.id.tv_shipperhistory);

        listBill = new ArrayList<>();
        getListBill();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        foodCartStatusAdapter = new FoodCartStatusAdapter(listBill, getActivity(), new FoodCartStatusAdapter.IClickItemFoodCartStatus() {
            @Override
            public void onClickItemFoodCartStatus(BillFood billFood) {
                Gson gson = new Gson();
                String json = gson.toJson(billFood);
                Bundle bundle = new Bundle();
                bundle.putString("billshipper", json);
                navController.navigate(R.id.action_fragmentProfileShipper_to_fragmentProfileShipperBillFood, bundle);

            }
        });
        recyclerView.setAdapter(foodCartStatusAdapter);
    }

    private void getListBill() {
        progressDialog.show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference billRef = database.collection("billfoods");
        billRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BillFood billFood = document.toObject(BillFood.class);
                        if(billFood.getStatus().equals("Chờ xác nhận")){
                            listBill.add(billFood);
                        }
                    }
                    progressDialog.dismiss();
                    foodCartStatusAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
