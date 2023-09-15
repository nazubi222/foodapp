package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.viewmodel.CartViewModel;
import com.example.foodorderapp.viewmodel.MyFoodFavoriteViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;

public class FragmentFoodViewRestaurant extends Fragment {
    private View mView;
    private NavController navController;
    private ImageView imgFood,  imgFoodResHeart, imgAddFood;
    private Button btAddFoodToCard;
    private TextView tvFoodName, tvFoodPrice, tvFoodInfo, tvFoodresPriceSum, tvFoodresPrice, tvFoodEvaluate, tvFoodCountSelled;
    private EditText edtFoodresAmount;
    private LinearLayout linearLayoutAddcart;
    private MyFoodFavoriteViewModel myFoodFavoriteViewModel;
    private int amount =0;

    private ProgressDialog progressDialog;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_foodview_restaurant, container, false);
        initUI();
        getDataFood();
        setEventClick();
        return mView;
    }
    private void getToHeartFood() {
        String idFood = getArguments().getString("idfood", null);
        myFoodFavoriteViewModel.checkToHeartFood(idFood, imgFoodResHeart);


    }


    private void setEventClick() {
        String priceFood = getArguments().getString("pricefood", null);
        String idFood = getArguments().getString("idfood", null);
        String nameFood = getArguments().getString("mamefood", null);
        String idPhotoFood = getArguments().getString("idphotofood", null);
        String infoFood = getArguments().getString("infofood", null);
        String idrestaurant = getArguments().getString("idrestaurant", null);
        String namerestaurant = getArguments().getString("namerestaurant", null);
        String locationRes = getArguments().getString("locationrestaurant", null);
        String typeFood = getArguments().getString("typefood", null);
        int amountt = getArguments().getInt("amount", 0);
        float evaluate = getArguments().getFloat("evaluate", 0);

        imgAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(edtFoodresAmount.getText().toString()) + 1;
                edtFoodresAmount.setText(String.valueOf(amount));
            }
        });

        edtFoodresAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    try {
                        int value = Integer.parseInt(s.toString());
                        if (value != 0) {
                            linearLayoutAddcart.setVisibility(View.VISIBLE);
                            tvFoodresPriceSum.setText(Html.fromHtml
                                    ("<b><i>"+(String.valueOf
                                            (value * Integer.parseInt(priceFood)) + " đ")+"</i></b>"));
                            mView.invalidate();
                        }else{
                            linearLayoutAddcart.setVisibility(View.GONE);
                            mView.invalidate();
                        }
                    } catch (NumberFormatException e) {
                        linearLayoutAddcart.setVisibility(View.GONE);
                        mView.invalidate();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    edtFoodresAmount.setText("0");
                    edtFoodresAmount.setSelection(1);
                }

            }
        });

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        CollectionReference cardRef = database.collection("customers").document(uid).collection("carts");

        btAddFoodToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food(idFood, idPhotoFood, nameFood, typeFood, infoFood
                        , priceFood, idrestaurant, locationRes, namerestaurant, amountt, evaluate);
                Cart cart = new Cart(food, Integer.parseInt(edtFoodresAmount.getText().toString()));
                CartViewModel cartViewModel = new CartViewModel();

                progressDialog.show();
                cardRef.whereEqualTo("food.id", idFood)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (!querySnapshot.isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if ((document.get("food.id").toString()).equals(idFood)) {
                                                Toast.makeText(getActivity(), "Đã có trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            } else {
                                                cartViewModel.addFoodToCart(cart);
                                                Toast.makeText(getActivity(), "Thêm giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    } else {
                                        cartViewModel.addFoodToCart(cart);
                                        Toast.makeText(getActivity(), "Thêm giỏ hàng thành công!", Toast.LENGTH_SHORT);
                                        progressDialog.dismiss();
                                    }
                                }else {
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });

        imgFoodResHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable currentDrawable = imgFoodResHeart.getDrawable();
                Drawable normalDrawable = getResources().getDrawable(R.drawable.heart);
                Drawable newDrawable = getResources().getDrawable(R.drawable.heart1);
                Food food = new Food(idFood, idPhotoFood, nameFood, typeFood, infoFood
                        , priceFood, idrestaurant, locationRes, namerestaurant, true );
                MyFoodFavoriteViewModel myFoodFavoriteViewModel = new MyFoodFavoriteViewModel();

                // So sánh hình ảnh hiện tại với hình ảnh mặc định
                if (currentDrawable.getConstantState().equals(normalDrawable.getConstantState())) {
                    imgFoodResHeart.setImageDrawable(newDrawable);
                    Log.d("myfood", food.getName() + food.getId()+ food.getIdPhoto()+
                            food.getInfomationFood()+ food.getPrice()+ food.getFavorite());
                    checkFoodFavorite(idFood, food);
                    Toast.makeText(getActivity(), "Đã thêm vào mục yêu thích!", Toast.LENGTH_SHORT).show();
                } else {

                    imgFoodResHeart.setImageDrawable(normalDrawable);
                    myFoodFavoriteViewModel.deleteMyFoodFavorite(food, idFood);
                    Toast.makeText(getActivity(), "Đã xóa khỏi mục yêu thích!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void checkFoodFavorite(String idFood, Food food){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();

        CollectionReference foodFavoriteRef = database.collection("customers").document(uid).collection("myfoodfavorites");
        foodFavoriteRef.whereEqualTo("id", idFood)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if ((document.get("id").toString()).equals(idFood)) {
                                        Toast.makeText(getActivity(), "Đã có trong yêu thích!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        myFoodFavoriteViewModel.addMyFoodFavorite(food);
                                    }
                                }
                            } else {
                                myFoodFavoriteViewModel.addMyFoodFavorite(food);
                            }
                        }
                    }
                });

    }

    private void getDataFood() {
        String priceFood = getArguments().getString("pricefood", null);
        String idFood = getArguments().getString("idfood", null);
        String nameFood = getArguments().getString("mamefood", null);
        String idPhotoFood = getArguments().getString("idphotofood", null);
        String infoFood = getArguments().getString("infofood", null);
        String idrestaurant = getArguments().getString("idrestaurant", null);
        String namerestaurant = getArguments().getString("namerestaurant", null);
        String locationRes = getArguments().getString("locationrestaurant", null);
        String typeFood = getArguments().getString("typefood", null);
        int amountt = getArguments().getInt("amount", 0);
        float evaluate = getArguments().getFloat("evaluate", 0);

        int decimalPlaces = 1;

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(decimalPlaces);
        decimalFormat.setMinimumFractionDigits(decimalPlaces);
        String roundedNumber = decimalFormat.format(evaluate);
        tvFoodName.setText(nameFood);
        tvFoodPrice.setText(priceFood +" vnđ");
        tvFoodInfo.setText(infoFood);
        tvFoodEvaluate.setText(roundedNumber);
        tvFoodCountSelled.setText(String.valueOf(amountt));

        if(idPhotoFood == null || idPhotoFood == "") {
            return;
        }
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(idPhotoFood);
        gsReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        Glide.with(getActivity())
                                .load(downloadUrl)
                                .error(R.drawable.baseline_image_24)
                                .into(imgFood);
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });


    }

    private void initUI() {
        imgFood = mView.findViewById(R.id.imgres_food);
        tvFoodName = mView.findViewById(R.id.tvfoodres_name);
        tvFoodPrice = mView.findViewById(R.id.tvfoodres_price);
        tvFoodInfo = mView.findViewById(R.id.tvfoodres_info);
        tvFoodCountSelled = mView.findViewById(R.id.tvfoodres_countselled);
        tvFoodEvaluate = mView.findViewById(R.id.tv_foodresevaluate);
        imgFoodResHeart = mView.findViewById(R.id.img_foodresheart);
        imgAddFood = mView.findViewById(R.id.imgres_click);
        btAddFoodToCard = mView.findViewById(R.id.btaddfoodres_tocard);
        tvFoodresPriceSum = mView.findViewById(R.id.tvfoodresprice_sum);
        edtFoodresAmount = mView.findViewById(R.id.edtfoodres_amount);
        tvFoodresPrice = mView.findViewById(R.id.tvfoodres_price);
        linearLayoutAddcart = mView.findViewById(R.id.ln_addfoodrestocard);
        myFoodFavoriteViewModel = new MyFoodFavoriteViewModel();
        progressDialog = new ProgressDialog(getActivity());

        getToHeartFood();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.imgres_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
}
