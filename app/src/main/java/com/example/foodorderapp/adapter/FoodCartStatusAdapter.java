package com.example.foodorderapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.BillFood;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodCartStatusAdapter extends RecyclerView.Adapter<FoodCartStatusAdapter.FoodCartStatusViewHolder>{
    private List<BillFood> listBillFood;
    private Context context;
    private FoodCartStatusAdapter.IClickItemFoodCartStatus iClickItemFoodCartStatus;

    public interface IClickItemFoodCartStatus{
        void onClickItemFoodCartStatus(BillFood billFood);

    }

    public FoodCartStatusAdapter(List<BillFood> listBillFood, Context context, FoodCartStatusAdapter.IClickItemFoodCartStatus iClickItemFoodCartStatus) {
        this.listBillFood = listBillFood;
        this.context = context;
        this.iClickItemFoodCartStatus = iClickItemFoodCartStatus;
    }

    @NonNull
    @Override
    public FoodCartStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_cart_status, parent, false);
        FoodCartStatusAdapter.FoodCartStatusViewHolder viewHolder = new FoodCartStatusAdapter.FoodCartStatusViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCartStatusViewHolder holder, int position) {
        BillFood billFood = listBillFood.get(position);
        if(billFood == null){
            return;
        }
        holder.tvFoodCartStatusName.setText(billFood.getCart().getFood().getName()
                + "-" + billFood.getCart().getFood().getNameRestaurant() );
        holder.tvFoodCartStatusPrice.setText(String.valueOf(billFood.getCart().getFood().getPrice()));
        holder.tvFoodCartStatusAmount.setText("x"+ String.valueOf(billFood.getCart().getAmount()));
        holder.tvFoodCartStatusSumPrice.setText(String.valueOf(billFood.getTotalMoney()));
        holder.tvFoodCartStatusShip.setText(billFood.getStatus());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFoodCartStatus.onClickItemFoodCartStatus(billFood);

            }
        });

        String idPhoto = billFood.getCart().getFood().getIdPhoto().toString();
        if(idPhoto == null || idPhoto == "") {
            return;
        }
        ImageView imageView = holder.imgFoodCartStatus;
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
        if(listBillFood != null){
            return listBillFood.size();
        }
        return 0;
    }

    public class FoodCartStatusViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private TextView tvFoodCartStatusName, tvFoodCartStatusPrice, tvFoodCartStatusAmount,
                tvFoodCartStatusShip, tvFoodCartStatusSumPrice;
        private ImageView imgFoodCartStatus;

        public FoodCartStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.item_foodcartstatus);
            tvFoodCartStatusName = itemView.findViewById(R.id.tv_itemfoodcartstatusname);
            tvFoodCartStatusPrice = itemView.findViewById(R.id.tv_itemfoodcartstatusprice);
            tvFoodCartStatusShip = itemView.findViewById(R.id.tv_itemfoodcartstatusship);
            tvFoodCartStatusSumPrice = itemView.findViewById(R.id.tv_itemfoodcartstatussum);
            tvFoodCartStatusAmount = itemView.findViewById(R.id.tv_itemfoodcartstatusamount);
            imgFoodCartStatus = itemView.findViewById(R.id.img_itemfoodcartstatus);

        }
    }
    public void setDataList(List<BillFood> newDataList) {
        this.listBillFood = newDataList;
    }
}
