package com.example.foodorderapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodCartPayAdapter extends  RecyclerView.Adapter<FoodCartPayAdapter.FoodCartPayViewHolder>{
    private List<Cart> listCart;
    private Context context;

    public FoodCartPayAdapter(List<Cart> listCart) {
        this.listCart = listCart;
        this.context = context;
    }


    @NonNull
    @Override
    public FoodCartPayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_cart_pay, parent, false);
        FoodCartPayAdapter.FoodCartPayViewHolder viewHolder = new FoodCartPayAdapter.FoodCartPayViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCartPayViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        if(cart == null){
            return;
        }

        holder.tvFoodCartPayName.setText(cart.getFood().getName() + "-" + cart.getFood().getNameRestaurant());
        holder.tvFoodCartPayLoccation.setText(cart.getFood().getLocationRestaurant());
        holder.tvFoodCartPayPrice.setText(cart.getFood().getPrice());
        holder.tvFoodCartPayAmount.setText("x"+String.valueOf(cart.getAmount()));
        holder.tvFoodCartPayShip.setText("18000đ");




        if(cart.getFood().getIdPhoto() == null || cart.getFood().getIdPhoto() == "") {
            return;
        }
        ImageView imageView = holder.imgFoodCartPay;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(cart.getFood().getIdPhoto());
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
                });;


    }

    @Override
    public int getItemCount() {
        if(listCart != null){
            return listCart.size();
        }
        return 0;
    }

    public class FoodCartPayViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFoodCartPay;
        private TextView tvFoodCartPayName, tvFoodCartPayLoccation,
                tvFoodCartPayPrice, tvFoodCartPayAmount, tvFoodCartPayShip;
        public FoodCartPayViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoodCartPay = itemView.findViewById(R.id.img_itemfoodcartpay);
            tvFoodCartPayName = itemView.findViewById(R.id.tv_itemfoodcartpayname);
            tvFoodCartPayLoccation = itemView.findViewById(R.id.tv_itemfoodcartpaylocation);
            tvFoodCartPayPrice = itemView.findViewById(R.id.tv_itemfoodcartpayprice);
            tvFoodCartPayAmount = itemView.findViewById(R.id.tv_itemfoodcartpayamount);
            tvFoodCartPayShip = itemView.findViewById(R.id.tv_itemfoodcartpayship);
        }
    }
}
