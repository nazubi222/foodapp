package com.example.foodorderapp.activity;




import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.foodorderapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_buttom);

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();


        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.fragmentHome) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }else if (destination.getId() == R.id.fragmentCart) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }else if (destination.getId() == R.id.fragmentFavorite) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }else if (destination.getId() == R.id.fragmentNotification) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }else if (destination.getId() == R.id.fragmentProfile) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }else{
                bottomNavigationView.setVisibility(View.GONE);
            }
        });




    }





//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        int currentDestination = navController.getCurrentDestination().getId();
//
//        if(item.getItemId() == R.id.fragmentHome) {
//            if (currentDestination != R.id.fragmentHome) {
//                navController.popBackStack(R.id.fragmentHome, false);
//            }
//            return true;
//        }else if(item.getItemId() == R.id.fragmentCart) {
//            if (currentDestination != R.id.fragmentCart) {
//                navController.popBackStack(R.id.fragmentCart, false);
//            }
//            return true;
//        }else if(item.getItemId() == R.id.fragmentFavorite) {
//            if (currentDestination != R.id.fragmentFavorite) {
//                navController.popBackStack(R.id.fragmentFavorite, false);
//            }
//            return true;
//        }else if(item.getItemId() == R.id.fragmentNotification) {
//            if (currentDestination != R.id.fragmentNotification) {
//                navController.popBackStack(R.id.fragmentNotification, false);
//            }
//            return true;
//        }else if(item.getItemId() == R.id.fragmentProfile) {
//            if (currentDestination != R.id.fragmentProfile) {
//                navController.popBackStack(R.id.fragmentProfile, false);
//            }
//            return true;
//        }
//
//        return false;
//
//    }


}
