package com.example.foodorderapp.viewmodel;

import android.app.ProgressDialog;

import androidx.lifecycle.ViewModel;

import com.example.foodorderapp.model.Customer;
import com.example.foodorderapp.repository.CustomerRepo;
import com.example.foodorderapp.repository.FirebaseCustomerRepo;

public class CustomerViewModel extends ViewModel {
    private CustomerRepo customerRepo = new FirebaseCustomerRepo();

    public void addCustomer(Customer customer) {
        customerRepo.addCustomer(customer);
    }
}
