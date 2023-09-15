package com.example.foodorderapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> listFood;
    private Context context;
//    private FoodAdapter.IFoodListener iFoodListener;
//
//    public interface IFoodListener(){
//        void ShowListFood();
//    }
    private FoodAdapter.ICLickItemFoodListener icLickItemFoodListener;
    public interface ICLickItemFoodListener{
        void onClickItemFood(int position);
    }
    public FoodAdapter(Context context,List<Food> listFood, FoodAdapter.ICLickItemFoodListener icLickItemFoodListener) {
        this.listFood = listFood;
        this.icLickItemFoodListener = icLickItemFoodListener;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        FoodAdapter.FoodViewHolder viewHolder = new FoodAdapter.FoodViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = listFood.get(position);
        if(food == null){
            return;
        }
        holder.tvName.setText(food.getName() + " - " + food.getNameRestaurant());
        holder.tvLocation.setText(food.getLocationRestaurant());
        holder.tvPrice.setText(food.getPrice() + " vnđ");
        String idPhoto = food.getIdPhoto();
        Log.d("testuri", idPhoto);
        if(idPhoto == null || idPhoto == "") {
            return;
        }
        ImageView imageView = holder.imgFoodItem;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(idPhoto);
        gsReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        Picasso.get().load(downloadUrl).into(imageView);
                        Glide.with(context).load(downloadUrl).into(imageView);
                        Log.d("tag", "onSuccess: "+downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        holder.lnItemFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icLickItemFoodListener.onClickItemFood(position);

            }
        });
    }


    @Override
    public int getItemCount() {
        if(listFood != null){
            return listFood.size();
        }
        return 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFoodItem;
        private TextView tvPrice, tvName, tvLocation;
        private LinearLayout lnItemFood;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoodItem = itemView.findViewById(R.id.img_itemfood);
            tvName = itemView.findViewById(R.id.tv_itemfoodname);
            tvLocation = itemView.findViewById(R.id.tv_itemfoodloction);
            tvPrice = itemView.findViewById(R.id.tv_itemfoodprice);
            lnItemFood = itemView.findViewById(R.id.item_food);
        }
    }
    public Food GetFoodByPosition(int position) {
        List<Food> listFood = this.GetListFood();
        return listFood.get(position);
    }

    private List<Food> GetListFood() {
        return this.listFood;
    }

    public void setDataList(List<Food> newDataList) {
        this.listFood = newDataList;
    }
}
