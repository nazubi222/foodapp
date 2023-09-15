package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHomeSnacks extends Fragment {
    private View mView;
    private NavController navController;
    private List<Food> listFood;
    private FoodAdapter foodAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home_snacks, container, false);
        recyclerView = mView.findViewById(R.id.rcv_foodsnacks);
        progressDialog = new ProgressDialog(getActivity());

        listFood = new ArrayList<>();
        getListFoodSnacks();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        foodAdapter = new FoodAdapter(getActivity(), listFood, new FoodAdapter.ICLickItemFoodListener() {
            @Override
            public void onClickItemFood(int position) {
                Food foodSelected = foodAdapter.GetFoodByPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("idfood", foodSelected.getId());
                bundle.putString("mamefood", foodSelected.getName());
                bundle.putString("idphotofood", foodSelected.getIdPhoto());
                bundle.putString("pricefood", foodSelected.getPrice());
                bundle.putString("infofood", foodSelected.getInfomationFood());
                bundle.putString("idrestaurant", foodSelected.getIdRestaurant());
                bundle.putString("locationrestaurant", foodSelected.getLocationRestaurant());
                bundle.putString("namerestaurant", foodSelected.getNameRestaurant());
                bundle.putString("typefood", foodSelected.getTypeFood());
                bundle.putInt("amount", foodSelected.getAmount());
                bundle.putFloat("evaluate", foodSelected.getEvaluate());
                navController.navigate(R.id.action_fragmentHomeSnacks_to_fragmentFoodView, bundle);
            }
        });
        recyclerView.setAdapter(foodAdapter);
        return mView;
    }

    private void getListFoodSnacks() {
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("foods").getRef();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    Log.d("typefood", food.getTypeFood());

                    String foodType =food.getTypeFood();
                    if(foodType == null){
                        return;
                    }

                    if(foodType.equals("Đồ ăn vặt".trim())){
                        food.setId(dataSnapshot.getKey());
                        listFood.add(food);
                        Log.d("typefood", food.getTypeFood());
                    }
                }
                progressDialog.dismiss();
                foodAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi!"
                        , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imghomesnacks_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
}
