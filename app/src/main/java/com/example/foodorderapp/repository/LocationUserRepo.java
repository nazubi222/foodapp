package com.example.foodorderapp.repository;

import com.example.foodorderapp.model.Location;
import com.google.android.gms.tasks.OnSuccessListener;

public interface LocationUserRepo {
    void addLocation(Location location);
    void deleteLocation(Location location, String documentSnapshot);
    void updateLocation(String location, String documentSnapshot);
}
