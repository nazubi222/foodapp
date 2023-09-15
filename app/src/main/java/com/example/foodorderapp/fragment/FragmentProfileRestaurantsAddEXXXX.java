package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Location;
import com.example.foodorderapp.model.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentProfileRestaurantsAddEXXXX extends Fragment {
    private View mView;
    private EditText edtNameRestaurant, edtRestaurantCity, edtRestaurantDistrick
            , edtRestaurantWard, edtRestaurantOther;
    private Button btAddRestaurant;
    private NavController navController;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_addrestaurant, container, false);
        initUI();
        btAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAddRestaurant();
            }
        });


        return mView;
    }

    private void onClickAddRestaurant() {
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference restaurantsRef = database.getReference("restaurants");
        String name = edtNameRestaurant.getText().toString();
        String city = edtRestaurantCity.getText().toString();
        String districk = edtRestaurantDistrick.getText().toString();
        String ward = edtRestaurantWard.getText().toString();
        String other = edtRestaurantOther.getText().toString();

        String location = other + "/ " + ward + "/ " + districk + "/ " +city;
        Restaurant restaurant = new Restaurant(name, location);
        boolean check = checkValues(name, city, districk, ward, other);
        if(check){
            DatabaseReference newrestaurant = restaurantsRef.push();
            newrestaurant.setValue(restaurant)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            restaurant.setId(newrestaurant.getKey());
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            progressDialog.dismiss();
        }




    }

    private void initUI() {
        edtNameRestaurant = mView.findViewById(R.id.tvprofile_restaurantname);
        edtRestaurantCity = mView.findViewById(R.id.tvprofile_restaurantcity);
        edtRestaurantDistrick = mView.findViewById(R.id.tvprofile_restaurantdistrick);
        edtRestaurantWard = mView.findViewById(R.id.tvprofile_restaurantward);
        edtRestaurantOther = mView.findViewById(R.id.tvprofile_restaurantother);
        btAddRestaurant = mView.findViewById(R.id.btadd_restaurant);

        progressDialog = new ProgressDialog(getActivity());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgprofileaddrestaurant_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
    private boolean checkValues(String name, String city, String districk, String ward, String other) {
        if(name.isEmpty()){
            edtNameRestaurant.setError("Nhập thông tin");
            return false;
        }
        if(city.isEmpty()){
            edtRestaurantCity.setError("Nhập thông tin");
            return false;
        }
        if(districk.isEmpty()){
            edtRestaurantDistrick.setError("Nhập thông tin");
            return false;
        }
        if(ward.isEmpty()){
            edtRestaurantWard.setError("Nhập thông tin");
            return false;
        }
        if(other.isEmpty()){
            edtRestaurantOther.setError("Nhập thông tin");
            return false;
        }
        return true;
    }
}
