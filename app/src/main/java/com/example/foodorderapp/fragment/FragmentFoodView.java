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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodAdapter;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FragmentFoodView extends Fragment {
    private View mView;
    private NavController navController;
    private ImageView imgFood, imgAddFood, imgFoodCartHeart;
    private TextView tvFoodName, tvFoodPrice, tvFoodInfo, tvFoodPriceSum, tvFoodCartEvaluate, tvFoodCountSelled;
    private View lnAddToCard;
    private EditText edtFoodAmount;
    private Button btAddFoodToCard;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private List<Food> listFood;
    private FoodAdapter foodAdapter;
    private MyFoodFavoriteViewModel myFoodFavoriteViewModel;
    private int amount =0;
    private static final String CUSTOMER_COLLECTION = "customers";
    private String documentSnapshot = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_foodview, container, false);
        initUI();
        getToHeartFood();
        getDataFood();
        eventClickAddFoodToCard();
        return mView;
    }

    private void getToHeartFood() {
        String idFood = getArguments().getString("idfood", null);
        myFoodFavoriteViewModel.checkToHeartFood(idFood, imgFoodCartHeart);


    }


    private void eventClickAddFoodToCard() {
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
        //button add food
        imgAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(edtFoodAmount.getText().toString()) + 1;
                edtFoodAmount.setText(String.valueOf(amount));

            }
        });
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
                            lnAddToCard.setVisibility(View.VISIBLE);
                            tvFoodPriceSum.setText(Html.fromHtml
                                    ("<b><i>"+(String.valueOf
                                            (value * Integer.parseInt(priceFood)) + " đ")+"</i></b>"));
                            mView.invalidate();
                        }else{
                            lnAddToCard.setVisibility(View.GONE);
                            mView.invalidate();
                        }
                    } catch (NumberFormatException e) {
                        lnAddToCard.setVisibility(View.GONE);
                        mView.invalidate();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    edtFoodAmount.setText("0");
                    edtFoodAmount.setSelection(1);
                }

            }
        });

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        CollectionReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid).collection("carts");

        btAddFoodToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food(idFood, idPhotoFood, nameFood, typeFood, infoFood
                        , priceFood, idrestaurant, locationRes, namerestaurant , amountt, evaluate);
                Cart cart = new Cart(food, Integer.parseInt(edtFoodAmount.getText().toString()));
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


        imgFoodCartHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable currentDrawable = imgFoodCartHeart.getDrawable();
                Drawable normalDrawable = getResources().getDrawable(R.drawable.heart);
                Drawable newDrawable = getResources().getDrawable(R.drawable.heart1);
                Food food = new Food(idFood, idPhotoFood, nameFood, typeFood, infoFood
                        , priceFood, idrestaurant, locationRes, namerestaurant, true );

                // So sánh hình ảnh hiện tại với hình ảnh mặc định
                if (currentDrawable.getConstantState().equals(normalDrawable.getConstantState())) {
                    imgFoodCartHeart.setImageDrawable(newDrawable);
                    Log.d("myfood", food.getName() + food.getId()+ food.getIdPhoto()+
                            food.getInfomationFood()+ food.getPrice()+ food.getFavorite());
                    checkFoodFavorite(food.getId(), food);
                } else {

                    imgFoodCartHeart.setImageDrawable(normalDrawable);
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

        CollectionReference foodFavoriteRef = database.collection(CUSTOMER_COLLECTION).document(uid).collection("myfoodfavorites");
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
                                        documentSnapshot = document.getId();
                                        Toast.makeText(getActivity(), "Đã có trong yêu thích!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        myFoodFavoriteViewModel.addMyFoodFavorite(food);
                                        Toast.makeText(getActivity(), "Đã thêm vào mục yêu thích!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                myFoodFavoriteViewModel.addMyFoodFavorite(food);
                            }
                        }
                    }
                });

    }




    private void getRecycleViewDataFood() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("foods").getRef();
        String idRestaurant = getArguments().getString("idrestaurant", null);
        String idFood = getArguments().getString("idfood", null);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    Food food = postSnapshot.getValue(Food.class);
//                    Log.d("dbfood", String.valueOf(food) + food.getIdPhoto());
//                    if(food.getIdRestaurant().equals(idRestaurant)){
//                        listFood.add(food);
//                    }
////                    listFood.add(food);
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Thêm món ăn!"
//                        , Toast.LENGTH_SHORT).show();
//            }
//        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String idFood = snapshot.getKey();
                Food food = snapshot.getValue(Food.class);
                food.setId(idFood);
                    Log.d("dbfood", String.valueOf(food) + food.getIdPhoto());
                    if(food.getIdRestaurant().equals(idRestaurant)  ){
                        listFood.add(food);
                    }
                foodAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataFood() {
        String idFood = getArguments().getString("idfood", null);
        String nameFood = getArguments().getString("mamefood", null);
        String idPhotoFood = getArguments().getString("idphotofood", null);
        String priceFood = getArguments().getString("pricefood", null);
        String infoFood = getArguments().getString("infofood", null);
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
        tvFoodCountSelled.setText(String.valueOf(amountt));
        tvFoodCartEvaluate.setText(roundedNumber);

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
                        Log.d("getidphoto", downloadUrl + "-" + idPhotoFood);
//                        Picasso.get().load(downloadUrl).error(R.drawable.baseline_image_24).into(imgFood);
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
                        Log.d("getidphoto", "onFailure: "+ e);
                        progressDialog.dismiss();
                    }
                });


    }

    private void initUI() {
        imgFood = mView.findViewById(R.id.img_food);
        tvFoodName = mView.findViewById(R.id.tvfood_name);
        tvFoodPrice = mView.findViewById(R.id.tvfood_price);
        tvFoodInfo = mView.findViewById(R.id.tvfood_info);
        edtFoodAmount = mView.findViewById(R.id.edtfood_amount);
        btAddFoodToCard = mView.findViewById(R.id.btaddfood_tocard);
        lnAddToCard = mView.findViewById(R.id.ln_addtocard);
        tvFoodPriceSum = mView.findViewById(R.id.tvfoodprice_sum);
        progressDialog = new ProgressDialog(getActivity());
        recyclerView = mView.findViewById(R.id.rcv_listfoodview);
        imgAddFood = mView.findViewById(R.id.img_clickadd);
        imgFoodCartHeart = mView.findViewById(R.id.img_foodcartheart);
        tvFoodCountSelled = mView.findViewById(R.id.tvfood_countselled);
        tvFoodCartEvaluate = mView.findViewById(R.id.tv_foodcartevaluate);
        myFoodFavoriteViewModel = new MyFoodFavoriteViewModel();

        getRecycleViewDataFood();
        listFood = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        foodAdapter = new FoodAdapter(getActivity(),listFood, new FoodAdapter.ICLickItemFoodListener() {
            @Override
            public void onClickItemFood(int position) {
                Food foodSelected = foodAdapter.GetFoodByPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("idfood", foodSelected.getId());
                bundle.putString("mamefood", foodSelected.getName());
                bundle.putString("idphotofood", foodSelected.getIdPhoto());
                bundle.putString("pricefood", foodSelected.getPrice());
                bundle.putString("infofood", foodSelected.getInfomationFood());
                bundle.putString("idrestaurant", foodSelected.getIdRestaurant());
                bundle.putString("locationrestaurant", foodSelected.getLocationRestaurant());
                bundle.putString("namerestaurant", foodSelected.getName());
                bundle.putString("typefood", foodSelected.getTypeFood());
                bundle.putInt("amount", foodSelected.getAmount());
                bundle.putFloat("evaluate", foodSelected.getEvaluate());
                NavController navController = Navigation.findNavController(mView);
                navController.navigate(R.id.action_fragmentFoodView_to_fragmentFoodViewRestaurant, bundle);
                // Lấy thông tin về fragment hiện tại
//                NavDestination currentDestination = navController.getCurrentDestination();
//                if (currentDestination instanceof FragmentNavigator.Destination) {
//                    String currentFragmentTag = ((FragmentNavigator.Destination) currentDestination).getClassName();
//                }
//
//                // Lấy thông tin về action trước đó
//                NavBackStackEntry previousBackStackEntry = navController.getPreviousBackStackEntry();
//                if (previousBackStackEntry != null) {
//                    String previousDestinationName = previousBackStackEntry.getDestination().getDisplayName();
//                        navController.navigate(R.id.action_fragmentFoodView_to_fragmentFoodViewRestaurant, bundle);
//                    Log.d("abcde", previousDestinationName);
//                }
            }
        });
        recyclerView.setAdapter(foodAdapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.img_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

}
