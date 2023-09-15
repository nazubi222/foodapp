package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.LocationUserAdapter;
import com.example.foodorderapp.model.Location;
import com.example.foodorderapp.viewmodel.LocationUserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfileLocation extends Fragment {
    private ProgressDialog progressDialog;
    private NavController navController;
    private TextView tvAddLocation;
    private ImageView imgBack;
    private RecyclerView recyclerView;
    private LocationUserAdapter locationUserAdapter;
    private List<Location> listLocation;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_location, container, false);
        initUI();
        getListLocationFirebase();

        locationUserAdapter = new LocationUserAdapter(getActivity(), listLocation, new LocationUserAdapter.IClickListener() {

            @Override
            public void onClickDeleteLocation(Location location, String documentSnapshot) {
//                onClickDeleteLocationL(location);
                LocationUserViewModel locationUserViewModel= new LocationUserViewModel();
                documentSnapshot = location.getId();


                locationUserViewModel.deleteLocation(location, documentSnapshot);

            }

            @Override
            public void onClickRadioBtLocation(Location location) {
                LocationUserViewModel locationUserViewModel= new LocationUserViewModel();
                String documentSnapshot = location.getId();
                String locations = location.getCity()+"/ "+location.getDistrict()+"/ "+location.getWard()+"/ "+location.getOther();
                Log.d("location12", locations + documentSnapshot);
                locationUserViewModel.updateLocation(locations, documentSnapshot);
                navController.navigateUp();

            }
        });
        recyclerView.setAdapter(locationUserAdapter);




        return mView;
    }

    private void getListLocationFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        progressDialog.show();
        CollectionReference documentReference = db.collection("customers").document(uid).collection("locations");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String documentID = document.getId();
                        Location location = document.toObject(Location.class);
                        location.setId(documentID);

                        listLocation.add(location);
                        Log.d("location123", "Name: " +location.getCity() +
                                location.getDistrict() + location.getWard() + location.getOther() + "/"+location.getId());
                        Log.d("location123", "Name: " +listLocation.size());
                    }
                    locationUserAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Không có địa chỉ!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        onClickTvAddLocation();

        view.findViewById(R.id.imglocation_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void onClickTvAddLocation() {
        tvAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfileLocation_to_fragmentProfileAddLocation);
            }
        });
    }

    private void initUI() {
        progressDialog = new ProgressDialog(getActivity());
        imgBack = mView.findViewById(R.id.imgaddlocation_back);
        tvAddLocation = mView.findViewById(R.id.tvadd_location);
        recyclerView = mView.findViewById(R.id.rcv_location);

        listLocation = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void onClickDeleteLocationL(Location location){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có chắc chắn muốn xóa địa chỉ này không ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LocationUserViewModel locationUserViewModel= new LocationUserViewModel();
                                final String documentSnapshot = location.getId();
                                Log.d("location12", "Name: " +location.getCity() +
                                        location.getDistrict() + location.getWard() + location.getOther() + "/"+location.getId());

                                locationUserViewModel.deleteLocation(location, documentSnapshot);
                                locationUserAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

    }
}

