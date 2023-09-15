package com.example.foodorderapp.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Location {

    private String id;
    private String city;
    private String district;
    private String ward;
    private String other;
    private @ServerTimestamp Date createdDate;


    public Location( String city, String district, String ward, String other) {
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.other = other;
    }

    public Location() {
    }

    public Location(String id, String city, String district, String ward, String other, Date createdDate) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.other = other;
        this.createdDate = createdDate;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
