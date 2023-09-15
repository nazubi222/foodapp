package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.example.foodorderapp.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfileRestaurantsMenuEXXXX extends Fragment {
    private View mView;
    private NavController navController;
    private TextView tvAddFood;
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> listFood;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_menufood, container, false);
        tvAddFood = mView.findViewById(R.id.tvadd_menufood);
        recyclerView = mView.findViewById(R.id.rcv_menufood);
        progressDialog = new ProgressDialog(getActivity());

        getListFoodMenu();
        listFood = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        foodAdapter = new FoodAdapter(getActivity(), listFood, new FoodAdapter.ICLickItemFoodListener() {
            @Override
            public void onClickItemFood(int position) {

            }
        });
        recyclerView.setAdapter(foodAdapter);


        onClickAddFoodRestaurant();
        return mView;
    }

    private void getListFoodMenu() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("foods").getRef();
        progressDialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            String id = getArguments().getString("itemId", null);
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    Log.d("dbfood", String.valueOf(food) + food.getIdPhoto());
                    if(food.getIdRestaurant().equals(id)){
                        listFood.add(food);
                    }
//                    listFood.add(food);

                }
                foodAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Thêm món ăn!"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickAddFoodRestaurant() {
        tvAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemId = getArguments().getString("itemId", null);
                String itemName = getArguments().getString("itemName", null);
                String itemLocation = getArguments().getString("itemLocation", null);
                Bundle bundle = new Bundle();
                bundle.putString("idRestaurant", itemId);
                bundle.putString("nameRestaurant", itemName);
                bundle.putString("locationRestaurant", itemLocation);
                Log.d("testdb", "onCreateView: " + itemId + itemName + itemLocation);
                navController.navigate(R.id.action_fragmentProfileRestaurantsMenuEXXXX2_to_fragmentProfileRestaurantsAddFoodEXXXX, bundle);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgmenufood_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
}
