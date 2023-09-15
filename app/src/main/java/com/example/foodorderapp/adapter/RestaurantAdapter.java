package com.example.foodorderapp.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.fragment.FragmentProfileRestaurantsEXXXX;
import com.example.foodorderapp.model.Location;
import com.example.foodorderapp.model.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>{
    private List<Restaurant> listRestaurant;
    private RestaurantAdapter.IClickListenerRes mIClickListener;

    public interface IClickListenerRes {
        void onClickRestaurant(int position);
    }

    public RestaurantAdapter(List<Restaurant> listRestaurant, RestaurantAdapter.IClickListenerRes iClickListenerRes) {
        this.listRestaurant = listRestaurant;
        this.mIClickListener = iClickListenerRes;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        RestaurantAdapter.RestaurantViewHolder viewHolder = new RestaurantAdapter.RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Restaurant restaurant = listRestaurant.get(position);
        if(restaurant == null){
            return;
        }
        holder.tvRestaurantName.setText(restaurant.getName().toString());
        holder.tvRestaurantMain.setText(restaurant.getLocation().toString());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIClickListener.onClickRestaurant(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(listRestaurant != null){
            return listRestaurant.size();
        }
        return 0;
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRestaurantName, tvRestaurantOther, tvRestaurantMain;
        private  LinearLayout linearLayout;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantName = itemView.findViewById(R.id.tv_restaurantname);
            tvRestaurantMain = itemView.findViewById(R.id.tv_restaurantmain);
            linearLayout = itemView.findViewById(R.id.item_retaurant);
        }
    }

    public Restaurant GetRestaurantByPosition(int position) {
        List<Restaurant> listRestaurant = this.GetListRestaurant();
        return listRestaurant.get(position);
    }

    private List<Restaurant> GetListRestaurant() {
        return this.listRestaurant;
    }
}
