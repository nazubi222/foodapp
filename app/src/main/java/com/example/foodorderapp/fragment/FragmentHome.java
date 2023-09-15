package com.example.foodorderapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.adapter.PhotoBannerAdapter;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.PhotoBanner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class FragmentHome extends Fragment {
    private LinearLayout btHomeRice, btHomeDrinks, btHomeSnacks, btHomeNoodles;
    private View mView;
    private NavController navController;
    private ViewPager2 viewPager2PhotoBanner;
    private CircleIndicator3 circleIndicator3;
    private List<PhotoBanner> listPhotoBanner;
    private Handler handler = new Handler();
    private List<Food> listFood;
    private FoodAdapter foodAdapter;
    private RecyclerView recyclerViewListFood;
    private EditText edtHomeSearchFood;
    private ImageView imgSearchHome;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(viewPager2PhotoBanner.getCurrentItem() == listPhotoBanner.size() - 1){
                viewPager2PhotoBanner.setCurrentItem(0);

            }else{
                viewPager2PhotoBanner.setCurrentItem(viewPager2PhotoBanner.getCurrentItem()+1);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();


        onClickBtHomeRice();
        onClickBtHomeDrinks();
        onClickBtHomeSnacks();
        onClickBtHomeNoodles();
        eventSearchyFood();


        return mView;
    }

    private void eventSearchyFood() {
        edtHomeSearchFood.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                imgSearchHome.performClick(); // Tương tự như việc bấm vào button
                return true;
            }
            return false;
        });

        imgSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyFood = edtHomeSearchFood.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("keyFood", keyFood);
                navController.navigate(R.id.action_fragmentHome_to_fragmentHomeSearch, bundle);
            }
        });

    }


    private void onClickBtHomeNoodles() {
        btHomeNoodles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentHome_to_fragmentHomeNoodles);
            }
        });
    }

    private void onClickBtHomeSnacks() {
        btHomeSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentHome_to_fragmentHomeSnacks);
            }
        });
    }

    private void onClickBtHomeDrinks() {
        btHomeDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentHome_to_fragmentHomeDrinks);
            }
        });
    }

    private void onClickBtHomeRice() {
        btHomeRice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentHome_to_fragmentHomeRice);
            }
        });

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }

    private void initUI() {
        btHomeRice = mView.findViewById(R.id.bthome_rice);
        btHomeDrinks = mView.findViewById(R.id.bthome_drinks);
        btHomeSnacks = mView.findViewById(R.id.bthome_snacks);
        btHomeNoodles = mView.findViewById(R.id.bthome_noodles);
        edtHomeSearchFood = mView.findViewById(R.id.edt_homesearchfood);
        imgSearchHome = mView.findViewById(R.id.img_searchhome);
        viewPager2PhotoBanner = mView.findViewById(R.id.viewpager2_photobanner);
        circleIndicator3 = mView.findViewById(R.id.homeindicator_3);

        listPhotoBanner = new ArrayList<>();
        listPhotoBanner.add(new PhotoBanner(R.drawable.banner3));
        listPhotoBanner.add(new PhotoBanner(R.drawable.banner1));
        listPhotoBanner.add(new PhotoBanner(R.drawable.banner2));
        listPhotoBanner.add(new PhotoBanner(R.drawable.banner4));
        listPhotoBanner.add(new PhotoBanner(R.drawable.banner5));

        Log.e("listphoto", String.valueOf(listPhotoBanner.size()));
        PhotoBannerAdapter photoBannerAdapter = new PhotoBannerAdapter(listPhotoBanner);
        viewPager2PhotoBanner.setAdapter(photoBannerAdapter);
        circleIndicator3.setViewPager(viewPager2PhotoBanner);

        viewPager2PhotoBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(mRunnable);
                handler.postDelayed(mRunnable, 5000);

            }
        });

        getListFoodHome();
        listFood = new ArrayList<>();
        recyclerViewListFood = mView.findViewById(R.id.rcv_listfood);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListFood.setLayoutManager(linearLayoutManager);
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
                bundle.putString("namerestaurant", foodSelected.getNameRestaurant());
                bundle.putString("typefood", foodSelected.getTypeFood());
                bundle.putInt("amount", foodSelected.getAmount());
                bundle.putFloat("evaluate", foodSelected.getEvaluate());

                navController.navigate(R.id.action_fragmentHome_to_fragmentFoodView, bundle);

            }
        });


        recyclerViewListFood.setAdapter(foodAdapter);



    }

    private void getListFoodHome() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("foods").getRef();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    food.setId(postSnapshot.getKey());
                    listFood.add(food);

                }
                foodAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi!"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(mRunnable, 5000);
    }
}
