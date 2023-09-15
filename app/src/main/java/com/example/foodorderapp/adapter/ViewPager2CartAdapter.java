package com.example.foodorderapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodorderapp.fragment.FragmentCartHistory;
import com.example.foodorderapp.fragment.FragmentCartMain;
import com.example.foodorderapp.fragment.FragmentCartStatus;

public class ViewPager2CartAdapter extends FragmentStateAdapter {
    public ViewPager2CartAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPager2CartAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentCartMain();
            case 1:
                return new FragmentCartStatus();
            case 2:
                return new FragmentCartHistory();
            default:
                return new FragmentCartMain();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
