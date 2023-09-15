package com.example.foodorderapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.foodorderapp.model.Location;
import com.example.foodorderapp.repository.FirebaseLocationUserRepo;
import com.example.foodorderapp.repository.LocationUserRepo;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationUserViewModel extends ViewModel {
    private LocationUserRepo locationUserRepo = new FirebaseLocationUserRepo();
    public void addLocation(Location location) {
        locationUserRepo.addLocation(location);
    }
    public void deleteLocation(Location location, String documentSnapshot){
        locationUserRepo.deleteLocation(location, documentSnapshot);
    }
    public void updateLocation(String location, String documentSnapshot){
        locationUserRepo.updateLocation(location, documentSnapshot);
    }

}
