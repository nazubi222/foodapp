package com.example.foodorderapp.fragment;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodCartPayAdapter;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Customer;
import com.example.foodorderapp.model.Shipper;
import com.example.foodorderapp.viewmodel.BillFoodViewModel;
import com.example.foodorderapp.viewmodel.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FragmentCartMainPay extends Fragment {
    private NavController navController;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private TextView tvFoodCartPayNameUser, tvFoodCartPayLocation, tvPhuongThucThanhToan, tvFoodCartPayBuy,
            tvFoodCartPayShipSum, tvFoodCartPayFoodSum, tvFoodCartPaySum, tvFoodCartPaySumPrice;
    private View mView;
    private FoodCartPayAdapter foodCartPayAdapter;
    private List<Cart> listCart;
    private BillFoodViewModel billFoodViewModel;
    private CartViewModel cartViewModel;
    private Customer customer;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart_main_pay, container, false);
        initUI();
        getTvInfoCustomer();
        onClickTvFoodCartPayBuy();


        return mView;
    }

    private void onClickTvFoodCartPayBuy() {
        tvFoodCartPayBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Bạn có chắc chắn thanh toán không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i = 0; i<listCart.size(); i++){
                                    Shipper shipper = new Shipper();
                                    int totalMonney = listCart.get(i).getAmount() * Integer.parseInt(listCart.get(i).getFood().getPrice()) + 18000;
                                    BillFood billFood = new BillFood("", listCart.get(i), customer, shipper, "Chờ xác nhận", totalMonney);
                                    billFoodViewModel.addBillFoodRealtime(billFood);
                                    cartViewModel.deleteFoodCart(listCart.get(i), listCart.get(i).getId());
                                }
                                navController.navigate(R.id.action_fragmentCartMainPay_to_fragmentCart);
                                mView.invalidate();

                                Toast.makeText(getActivity(), "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();

            }
        });
    }

    private void getTvInfoCustomer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();

        DocumentReference docRef = db.collection("customers").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        customer = document.toObject(Customer.class);
                        String name =customer.getName();
                        String phone = customer.getNumberphone().toString();
                        String location = customer.getLocation();
                        tvFoodCartPayNameUser.setText(name + "-" + phone);
                        tvFoodCartPayLocation.setText(location);
                    }
                }
            }
        });
    }

    private void initUI() {
        imageView = mView.findViewById(R.id.imgcardmainpay_back);
        recyclerView = mView.findViewById(R.id.rcv_foodcartpay);
        tvFoodCartPayNameUser = mView.findViewById(R.id.tv_foodcartpayname);
        tvFoodCartPayLocation = mView.findViewById(R.id.tv_foodcartpaylocation);
        tvPhuongThucThanhToan = mView.findViewById(R.id.tv_phuongthucthanhtoan);
        tvFoodCartPayShipSum = mView.findViewById(R.id.tv_foodcartpayshipsum);
        tvFoodCartPayFoodSum = mView.findViewById(R.id.tv_foodcartpayfoodsum);
        tvFoodCartPaySum = mView.findViewById(R.id.tv_foodcartpaysum);
        tvFoodCartPaySumPrice = mView.findViewById(R.id.tv_foodcartpaysumprice);
        tvFoodCartPayBuy = mView.findViewById(R.id.tv_foodcartpaybuy);
        cartViewModel = new CartViewModel();
        billFoodViewModel = new BillFoodViewModel();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        listCart = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String json = bundle.getString("listCartPay");

            Gson gson = new Gson();
            ArrayList<Cart> objectList = gson.fromJson(json, new TypeToken<ArrayList<Cart>>() {}.getType());
            Log.d("listCartPays", String.valueOf(objectList.size()));

            // Tiếp tục xử lý danh sách đối tượng nhận được
            listCart = objectList;
        }
        Log.d("listCartPay", String.valueOf(listCart.size()) + listCart.get(0).getId());
        foodCartPayAdapter = new FoodCartPayAdapter(listCart);
        recyclerView.setAdapter(foodCartPayAdapter);
        int sum = sum();
        tvPhuongThucThanhToan.setText("Tiền mặt");
        tvFoodCartPayShipSum.setText(String.valueOf(18000 * listCart.size()) +"đ");

        tvFoodCartPayFoodSum.setText(String.valueOf(sum) + "đ");
        tvFoodCartPaySum.setText(String.valueOf(sum + 18000*listCart.size()) +"đ");
        tvFoodCartPaySumPrice.setText(String.valueOf(sum + 18000*listCart.size()) +"đ");



    }
    private int sum(){
        int sum =0;
        for(int i=0; i<listCart.size(); i++){
            sum += Integer.parseInt(listCart.get(i).getFood().getPrice()) * listCart.get(i).getAmount() ;
        }
        return sum;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgcardmainpay_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
}
