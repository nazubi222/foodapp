package com.example.foodorderapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodCartAdapter extends RecyclerView.Adapter<FoodCartAdapter.FoodCartViewHolder>{
    private List<Cart> listCart, listCartSell;
    private FoodCartAdapter.IClickItem iClickItem;
    private Context context;
    private Cart cart;

    public FoodCartAdapter(List<Cart> listCartSell, Context context) {
        this.listCartSell = listCartSell;
        this.context = context;
    }

    public interface IClickItem{
        void clickDeleteFoodCart(Cart cart, String documentSnapshot);
        void clickAddCheckBoxFoodToCart(Cart cart, boolean ischecked, String documentSnapshot);
        void updateAmountFood(Cart cart,int amount, String documentSnapshot);
    }
    public FoodCartAdapter(Context context, List<Cart> listCart, FoodCartAdapter.IClickItem iClickItem) {
        this.context = context;
        this.listCart = listCart;
        this.iClickItem = iClickItem;
    }

    @NonNull
    @Override
    public FoodCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_cart, parent, false);
        FoodCartAdapter.FoodCartViewHolder viewHolder = new FoodCartAdapter.FoodCartViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCartViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        if(cart == null){
            return;
        }
        final View mView = holder.linearLayout;
        final TextView tvFoodPriceSum =  holder.tvFoodPrice;
        final ImageView imageAddFood = holder.imgAddFood;
        final EditText edtFoodAmount = holder.tvFoodAmount;
        CheckBox checkBox = holder.cbAddToCart;

        holder.tvName.setText(cart.getFood().getName() + " - " + cart.getFood().getNameRestaurant());
        holder.tvFoodAmount.setText(String.valueOf(cart.getAmount()));
        int price = Integer.parseInt(cart.getFood().getPrice()) * cart.getAmount();
        holder.tvFoodPrice.setText(String.valueOf(price));


        //Xử lý thêm số lượng
        imageAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(edtFoodAmount.getText().toString()) + 1;
                edtFoodAmount.setText(String.valueOf(i));
                cart.setAmount(i);
                notifyDataSetChanged();
            }
        });

        //Xử lý khi edt thay đổi
        edtFoodAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        int value = Integer.parseInt(s.toString());
                        if (value != 0) {
                            tvFoodPriceSum.setText(Html.fromHtml
                                    ("<i>"+(String.valueOf
                                            (value * Integer.parseInt(cart.getFood().getPrice())) + " đ")+"</i>"));
                        }
                        iClickItem.updateAmountFood(cart, Integer.parseInt(String.valueOf(edtFoodAmount.getText())), cart.getId());

                    } catch (NumberFormatException e) {
//                        mView.invalidate();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    edtFoodAmount.setText("1");
                    edtFoodAmount.setSelection(1);

                }

            }
        });





        holder.tvDeleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xóa không ?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listCart.remove(cart);
                                iClickItem.clickDeleteFoodCart(cart, cart.getId());
                                notifyDataSetChanged();
                                Toast.makeText(context, "Xóathành công!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();

            }
        });




        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(iClickItem != null){
                    iClickItem.clickAddCheckBoxFoodToCart(cart, isChecked, cart.getId());
                    notifyDataSetChanged();
                }
            }
        });




        if(cart.getFood().getIdPhoto() == null || cart.getFood().getIdPhoto() == "") {
            return;
        }
        ImageView imageView = holder.imgFood;
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
                });
        setCart(cart);

    }

    @Override
    public int getItemCount() {
        if(listCart != null){
            return listCart.size();
        }
        return 0;
    }

    public class FoodCartViewHolder extends RecyclerView.ViewHolder{
        private CheckBox cbAddToCart;
        private ImageView imgFood, imgAddFood;
        private TextView tvName, tvFoodPrice, tvDeleteFood;
        private EditText tvFoodAmount;
        private LinearLayout linearLayout;

        public FoodCartViewHolder(@NonNull View itemView) {
            super(itemView);
            cbAddToCart = itemView.findViewById(R.id.cb_itemfoodcardadd);
            imgFood = itemView.findViewById(R.id.img_itemfoodcart);
            imgAddFood = itemView.findViewById(R.id.img_foodcartsub);
            tvName = itemView.findViewById(R.id.tv_itemfoodcartname);
            tvFoodPrice = itemView.findViewById(R.id.tv_itemfoodcartprice);
            tvFoodAmount = itemView.findViewById(R.id.tv_itemfoodcartcount);
            tvDeleteFood = itemView.findViewById(R.id.tvdelete_itemfoodcart);
            linearLayout = itemView.findViewById(R.id.item_foodcart);

        }
    }
    public void setDataList(List<Cart> newDataList) {
        this.listCart = newDataList;
    }
    private void setCart(Cart cart){
        this.cart = cart;
    }
    public int getCountCart(){
        return cart.getAmount();
    }
}
