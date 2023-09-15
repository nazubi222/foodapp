package com.example.foodorderapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Location;
import com.example.foodorderapp.repository.FirebaseLocationUserRepo;
import com.example.foodorderapp.viewmodel.CustomerViewModel;
import com.example.foodorderapp.viewmodel.LocationUserViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentProfileAddLocation  extends Fragment {
    private NavController navController;
    private ImageView imgBack;
    private EditText edtAddCity, edtAddWard, edtAddDistrick, edtAddOther;
    private Button btSaveLocation;
    private  View mView;



    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = auth.getCurrentUser();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_addlocation, container, false);
        initUI();
        btSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickSaveLocation();

            }
        });
        return mView;
    }

    private void onCLickSaveLocation() {

        String city = edtAddCity.getText().toString();
        String districk = edtAddDistrick.getText().toString();
        String ward = edtAddWard.getText().toString();
        String other = edtAddOther.getText().toString();
        String id;


        boolean check = checkValues(city, districk, ward,other);

        if(check) {
            Location location = new Location(city, districk, ward, other);
            LocationUserViewModel locationUserViewModel = new ViewModelProvider(this).get(LocationUserViewModel.class);
            locationUserViewModel.addLocation(location);
            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        }

    }

    private boolean checkValues(String city, String districk, String ward, String other){
        if(city.isEmpty()){
            edtAddCity.setError("Nhập thông tin!");
            return false;
        }
        if(districk.isEmpty()){
            edtAddDistrick.setError("Nhập thông tin!");
            return false;
        }
        if(ward.isEmpty()){
            edtAddWard.setError("Nhập thông tin!");
            return false;
        }
        if(other.isEmpty()){
            edtAddOther.setError("Nhập thông tin!");
            return false;
        }
        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.imgaddlocation_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void initUI() {
        imgBack = mView.findViewById(R.id.imgaddlocation_back);
        edtAddCity = mView.findViewById(R.id.edtaddlocation_city);
        edtAddDistrick = mView.findViewById(R.id.edtaddlocation_district);
        edtAddWard = mView.findViewById(R.id.edtaddlocation_ward);
        edtAddOther = mView.findViewById(R.id.edtaddlocation_other);
        btSaveLocation = mView.findViewById(R.id.btsaveupdate_loction);
    }


}
