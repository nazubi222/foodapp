package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.foodorderapp.adapter.FoodFavoriteAdapter;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.viewmodel.MyFoodFavoriteViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavorite extends Fragment {
    private NavController navController;
    private View mView;
    private FoodFavoriteAdapter foodFavoriteAdapter;
    private List<Food> listFood;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private MyFoodFavoriteViewModel myFoodFavoriteViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorite, container, false);
        initUI();

        return mView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }

    private void initUI() {
        recyclerView = mView.findViewById(R.id.rcv_foodfavorite);
        progressDialog = new ProgressDialog(getActivity());
        myFoodFavoriteViewModel = new MyFoodFavoriteViewModel();

        listFood = new ArrayList<>();
        getListData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        foodFavoriteAdapter = new FoodFavoriteAdapter( listFood, getActivity() ,new FoodFavoriteAdapter.IcLickItemFoodFavoriteListener() {
            @Override
            public void onClickItemFoodFavorite(int position) {
                Food foodSelected = foodFavoriteAdapter.GetFoodByPosition(position);
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
                bundle.putBoolean("favorite", foodSelected.getFavorite());
                navController.navigate(R.id.action_fragmentFavorite_to_fragmentFoodView, bundle);

            }

            @Override
            public void deleteFoodFavorite(Food food, String idFood) {
                myFoodFavoriteViewModel.deleteMyFoodFavorite(food, idFood);
                Toast.makeText(getActivity(), "Đã xóa khỏi danh mục yêu thích!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void insertFoodFavorite(Food food, String idFood) {
                myFoodFavoriteViewModel.addMyFoodFavorite(food);
                Toast.makeText(getActivity(), "Đã thêm danh mục yêu thích!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(foodFavoriteAdapter);
    }

    private void getListData() {
        progressDialog.show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        DocumentReference cardRef = database.collection("customers").document(uid);
        CollectionReference collectionReference = cardRef.collection("myfoodfavorites");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document == null){
                            return;
                        }
                        String documentID = document.getId();
                        Food food = document.toObject(Food.class);
                        listFood.add(food);
                    }
                    progressDialog.dismiss();
                    foodFavoriteAdapter.notifyDataSetChanged();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Không có món yêu thích!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
