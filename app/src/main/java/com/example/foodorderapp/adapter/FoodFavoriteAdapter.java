package com.example.foodorderapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.viewmodel.MyFoodFavoriteViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodFavoriteAdapter extends RecyclerView.Adapter<FoodFavoriteAdapter.FoodFavoriteViewHolder>{
    private List<Food> listFood;
    private Context context;
    private FoodFavoriteAdapter.IcLickItemFoodFavoriteListener icLickItemFoodFavoriteListener;

    public interface IcLickItemFoodFavoriteListener{
        void onClickItemFoodFavorite(int positio);
        void deleteFoodFavorite(Food food, String idFood);
        void insertFoodFavorite(Food food, String idFood);
    }

    public FoodFavoriteAdapter(List<Food> listFood, Context context
            , FoodFavoriteAdapter.IcLickItemFoodFavoriteListener icLickItemFoodFavoriteListener) {
        this.listFood = listFood;
        this.context = context;
        this.icLickItemFoodFavoriteListener = icLickItemFoodFavoriteListener;
    }

    @NonNull
    @Override
    public FoodFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_favorite, parent, false);
        FoodFavoriteAdapter.FoodFavoriteViewHolder viewHolder = new FoodFavoriteAdapter.FoodFavoriteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodFavoriteViewHolder holder, int position) {
        Food food = listFood.get(position);
        if(food == null){
            return;
        }
        holder.tvName.setText(food.getName() + " - " + food.getNameRestaurant());
        holder.tvLocation.setText(food.getLocationRestaurant());
        holder.tvPrice.setText(food.getPrice() + " vnđ");

        int amount = position;
        holder.lnItemFoodFvorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icLickItemFoodFavoriteListener.onClickItemFoodFavorite(amount);

            }
        });

        final ImageView imgFoodItemHeart =  holder.imgFoodItemHeart;
        holder.imgFoodItemHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imgFoodItemHeart.setImageResource(R.drawable.heart);
//                notifyDataSetChanged();
//                icLickItemFoodFavoriteListener.deleteFoodFavorite(food, food.getId());

                Drawable currentDrawable = imgFoodItemHeart.getDrawable();
                Drawable normalDrawable = context.getResources().getDrawable(R.drawable.heart);
                Drawable newDrawable = context.getResources().getDrawable(R.drawable.heart1);
                // So sánh hình ảnh hiện tại với hình ảnh mặc định
                if (currentDrawable.getConstantState().equals(newDrawable.getConstantState())) {
                    imgFoodItemHeart.setImageResource(R.drawable.heart);
                    notifyDataSetChanged();
                    icLickItemFoodFavoriteListener.deleteFoodFavorite(food, food.getId());
                } else {
                    imgFoodItemHeart.setImageDrawable(newDrawable);
                    icLickItemFoodFavoriteListener.insertFoodFavorite(food, food.getId());
                    notifyDataSetChanged();

                }


            }
        });

        String idPhoto = food.getIdPhoto().toString().trim();
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
                        Picasso.get().load(downloadUrl).error(R.drawable.baseline_image_24).into(imageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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

    public class FoodFavoriteViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgFoodItem, imgFoodItemHeart;
        private TextView tvPrice, tvName, tvLocation;
        private LinearLayout lnItemFoodFvorite;

        public FoodFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoodItem = itemView.findViewById(R.id.img_itemfoodfavorite);
            tvName = itemView.findViewById(R.id.tv_itemfoodfavoritename);
            tvLocation = itemView.findViewById(R.id.tv_itemfoodfavoriteloction);
            tvPrice = itemView.findViewById(R.id.tv_itemfoodfavoriteprice);
            lnItemFoodFvorite = itemView.findViewById(R.id.item_foodfavorite);
            imgFoodItemHeart = itemView.findViewById(R.id.img_itemfoodfavoriteheart);
        }
    }
    public Food GetFoodByPosition(int position) {
        List<Food> listFood = this.GetListFood();
        return listFood.get(position);
    }

    private List<Food> GetListFood() {
        return this.listFood;
    }
}
