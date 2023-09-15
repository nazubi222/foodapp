package com.example.foodorderapp.repository;

import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Customer;

public interface BillFoodRepo {
    void addBillFoodFireStore (BillFood billFood);
    void addBillFoodRealtime (BillFood billFood);
    void updateBillFood(String status, String idCart);
    void updateBillFoodShipper(Customer customer, String idCart);
    void updateDateBillFood(String field, String idCart);
    void updateAmountFood(int amount, String idFood);
    void updateAvaluateFood(float avaluatefood, String idFood);
    void updateAvaluated( String idCart, float evaluateCus);

}
