package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.foodorderapp.adapter.RestaurantAdapter;
import com.example.foodorderapp.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfileRestaurantsEXXXX extends Fragment {
    private ProgressDialog progressDialog;
    private List<Restaurant> listRestaurant;
    private RecyclerView recyclerView;
    private RestaurantAdapter restaurantAdapter;
    private View mView;
    private NavController navController;
    private TextView tvAddRestaurant;
    private String idRestaurant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_restaurants, container, false);
        tvAddRestaurant = mView.findViewById(R.id.tvadd_restaurant);
        recyclerView = mView.findViewById(R.id.rcv_restaurant);
        progressDialog = new ProgressDialog(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        getListRestaurants();
        listRestaurant = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(listRestaurant, new RestaurantAdapter.IClickListenerRes() {
            @Override
            public void onClickRestaurant(int position) {
                Restaurant restaurantSelected = restaurantAdapter.GetRestaurantByPosition(position);
                Log.d("restaurant", "onClickRestaurant: "+ restaurantSelected.getId() + restaurantSelected.getName());
//                FragmentProfileRestaurantsAddFoodEXXXX fragment = new FragmentProfileRestaurantsAddFoodEXXXX();
                Bundle bundle = new Bundle();
                bundle.putString("itemId", restaurantSelected.getId());
                bundle.putString("itemName", restaurantSelected.getName());
                bundle.putString("itemLocation", restaurantSelected.getLocation());
//                fragment.setArguments(bundle);
//                Log.d("bundle", "onClickRestaurant: " + fragment);
//                getActivity().startActivity(intent);
                navController.navigate(R.id.action_fragmentProfileRestaurantsEXXXX_to_fragmentProfileRestaurantsMenuEXXXX2, bundle);


            }
        });
        recyclerView.setAdapter(restaurantAdapter);

        onClickAddRestaurant();
        return mView;
    }

    private void getListRestaurants() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants").getRef();
        progressDialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Restaurant restaurant = postSnapshot.getValue(Restaurant.class);
                    restaurant.setId(postSnapshot.getKey());
                    listRestaurant.add(restaurant);
                }
                restaurantAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Thêm nhà hàng!"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onClickAddRestaurant() {
        tvAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfileRestaurantsEXXXX_to_fragmentProfileRestaurantsAddEXXXX);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgrestaurant_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
}
