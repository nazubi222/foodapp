package com.example.foodorderapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.foodorderapp.R;

public class FragmentProfileRegulatoryPolicy extends Fragment {
    private View mView;
    private NavController navController;
    private ImageView imgBack;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_regulatorypolicy, container, false);
        initUI();
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.imgregulaytory_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void initUI() {
        imgBack = mView.findViewById(R.id.imgregulaytory_back);
    }
}
