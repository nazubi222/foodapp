package com.example.foodorderapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.ViewPager2CartAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentCart extends Fragment {
    private NavController navController;
    private View mView;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPager2CartAdapter viewPager2CartAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart, container, false);
        initUI();

        return mView;
    }

    private void initUI() {
        viewPager2 = mView.findViewById(R.id.vpg_cart);
        tabLayout = mView.findViewById(R.id.tab_cart);
        viewPager2CartAdapter = new ViewPager2CartAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(viewPager2CartAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Đơn hàng");
                    break;
                case 1:
                    tab.setText("Trạng thái đơn hàng");
                    break;
                case 2:
                    tab.setText("Lịch sử đơn hàng");
                    break;
            }
        }).attach();


    }


    @Override
    public void onResume() {
        super.onResume();
        mView.invalidate();
    }
}
