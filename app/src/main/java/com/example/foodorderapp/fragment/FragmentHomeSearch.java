package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class FragmentHomeSearch extends Fragment {
    private NavController navController;
    private View mView;
    private EditText edtSearch;
    private ImageView imgSearch;
    private RecyclerView recyclerView;
    private List<Food> listFood, newList;
    private FoodAdapter foodAdapter;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_food, container, false);
        initUI();
        getSearch();
        setImageSearch();
        return mView;
    }

    private void setImageSearch() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                imgSearch.performClick(); // Tương tự như việc bấm vào button
                return true;
            }
            return false;
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newList = new ArrayList<>();

                progressDialog.show();
                String keyFood = edtSearch.getText().toString();

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("foods");
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            Food food = childSnapshot.getValue(Food.class);
                            if (isMatch(food.getName(), keyFood)) {
                                newList.add(food);
                            }
                        }
                        foodAdapter.setDataList(newList);
                        foodAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xử lý lỗi truy vấn
                    }
                });

            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.img_homesearchback).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void getSearch() {
        progressDialog.show();
        String keyFood = getArguments().getString("keyFood", null);
        Toast.makeText(getActivity(), keyFood, Toast.LENGTH_SHORT).show();
        edtSearch.setText(keyFood);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("foods");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Food food = childSnapshot.getValue(Food.class);
                    if (isMatch(food.getName(), keyFood)) {
                        listFood.add(food);
                    }
                }
                foodAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi truy vấn
            }
        });


    }

    private void initUI() {
        recyclerView = mView.findViewById(R.id.rcv_searchfood);
        edtSearch = mView.findViewById(R.id.edt_searchfood);
        imgSearch = mView.findViewById(R.id.img_searchhomefood);
        progressDialog = new ProgressDialog(getActivity());

        listFood = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        foodAdapter = new FoodAdapter(getActivity(), listFood, new FoodAdapter.ICLickItemFoodListener() {
            @Override
            public void onClickItemFood(int position) {
                Food foodSelected = foodAdapter.GetFoodByPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("idfood", foodSelected.getId());
                bundle.putString("mamefood", foodSelected.getName());
                bundle.putString("idphotofood", foodSelected.getIdPhoto());
                bundle.putString("pricefood", foodSelected.getPrice());
                bundle.putString("infofood", foodSelected.getInfomationFood());
                bundle.putString("idrestaurant", foodSelected.getIdRestaurant());
                bundle.putString("locationrestaurant", foodSelected.getLocationRestaurant());
                bundle.putString("namerestaurant", foodSelected.getNameRestaurant());
                bundle.putString("typefood", foodSelected.getTypeFood());
                bundle.putInt("amount", foodSelected.getAmount());
                bundle.putFloat("evaluate", foodSelected.getEvaluate());

                navController.navigate(R.id.action_fragmentHomeSearch_to_fragmentFoodView, bundle);

            }
        });
        recyclerView.setAdapter(foodAdapter);


    }

    private boolean isMatch(String fullName, String searchTerm) {
        // Chuẩn hóa cả hai chuỗi
        String normalizedFullName = normalizeString(fullName);
        String normalizedSearchTerm = normalizeString(searchTerm);
        Log.d("checkfood", normalizedFullName + "/" +normalizedSearchTerm);

        // Kiểm tra sự xuất hiện của từ trong trường dữ liệu dài
        return normalizedFullName.contains(normalizedSearchTerm);
    }

    private String normalizeString(String input) {
        // Loại bỏ các ký tự không phải chữ cái và chuyển về chữ thường
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z]", "")
                .toLowerCase();
    }
}
