package com.example.foodorderapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Customer;
import com.example.foodorderapp.repository.BillFoodRepo;
import com.example.foodorderapp.repository.FirebaseBillFoodRepo;

public class BillFoodViewModel extends ViewModel {
    private BillFoodRepo billFoodRepo= new FirebaseBillFoodRepo();
    public void addBillFoodRealtime(BillFood billFood){
        billFoodRepo.addBillFoodRealtime(billFood);
    }
    public void addBillFoodFireStore(BillFood billFood){
        billFoodRepo.addBillFoodFireStore(billFood);
    }

    public void updateBillFood(String status, String idCart){
        billFoodRepo.updateBillFood(status, idCart);
    }

    public void updateBillFoodShipper(Customer customer, String idCart){
        billFoodRepo.updateBillFoodShipper(customer, idCart);
    }

    public void updateDateBillFood(String field, String idCart){
        billFoodRepo.updateDateBillFood(field, idCart);
    }
    public void updateAmountFood(int amount, String idFood){
        billFoodRepo.updateAmountFood(amount, idFood);
    }
    public void updateAvaluateFood(float avaluatefood, String idFood){
        billFoodRepo.updateAvaluateFood(avaluatefood, idFood);
    }
    public void updateAvaluated( String idCart,float evaluateCus){
        billFoodRepo.updateAvaluated(idCart,evaluateCus);
    }
}
