package com.example.foodorderapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Customer;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.viewmodel.BillFoodViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class FragmentProfileShipperBillFood extends Fragment {
    private NavController navController;
    private TextView tvFoodShipperNameCus, tvFoodShipperLocationCus, tvFoodShipperBillName, tvFoodShipperBillPrice
            , tvFoodShipperBillAmount, tvPhuongThucThanhToanShipper, tvFoodShipperBillShip, tvFoodShipperBillSum
            , tvFoodShipperBillSumPay, tvFoodShipperLocationRes;
    private ImageView imgBillFood;
    private Button btReceiveBill, btGetBill, btDeliverySuccess;
    private View mView;
    private BillFoodViewModel billFoodViewModel;
    private Customer customer;
    private Food food;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_shipper_bill, container, false);

        initUI();

        getDataToShipper();
        setEventOnClick();

        return mView;
    }

    private void    setEventOnClick() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        String uid = currentUser.getUid();
        db.collection("customers").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        customer = document.toObject(Customer.class);
                    }
                }
            }
        });



        Bundle bundle = getArguments();
        String json = bundle.getString("billshipper");
        Gson gson = new Gson();
        BillFood billFood = gson.fromJson(json, BillFood.class);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("foods").child(billFood.getCart().getFood().getId().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    food = task.getResult().getValue(Food.class);
                }
            }
        });

        if(billFood.getStatus().equals("Shipper đã nhận hàng!")){
            btReceiveBill.setEnabled(false);
        }else{
            btReceiveBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btReceiveBill.setEnabled(false);
                    billFoodViewModel.updateBillFood("Shipper đã nhận hàng!", billFood.getCart().getId().toString());
                    billFoodViewModel.updateBillFoodShipper(customer, billFood.getCart().getId().toString());
                    billFoodViewModel.updateDateBillFood("dateReceiverBill", billFood.getCart().getId());
                }
            });
        }

        if(billFood.getStatus().equals("Shipper đã lấy hàng thành công!")){
            btGetBill.setEnabled(false);
            btReceiveBill.setEnabled(false);
        }else{
            btGetBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(btReceiveBill.isEnabled() == true){
                        Toast.makeText(getActivity(), "Đơn hàng chưa nhận!", Toast.LENGTH_SHORT).show();
                    }else {
                        btGetBill.setEnabled(false);
                        billFoodViewModel.updateBillFood("Shipper đã lấy hàng thành công!", billFood.getCart().getId().toString());
                        billFoodViewModel.updateDateBillFood("dateGetBill", billFood.getCart().getId());

                    }

                }
            });
        }



        btDeliverySuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btReceiveBill.isEnabled() == true || btGetBill.isEnabled() == true){
                    Toast.makeText(getActivity(), "Đơn hàng chưa được lấy!", Toast.LENGTH_SHORT).show();
                }else {
                    btDeliverySuccess.setEnabled(false);
                    billFoodViewModel.updateBillFood("Giao hàng thành công!", billFood.getCart().getId().toString());
                    billFoodViewModel.updateDateBillFood("endDate", billFood.getCart().getId());
                    int amountFood = food.getAmount() + billFood.getCart().getAmount();
                    billFoodViewModel.updateAmountFood(amountFood, billFood.getCart().getFood().getId());
                }

            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgshipperbill_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void getDataToShipper() {
        Bundle bundle = getArguments();
        String json = bundle.getString("billshipper");
        Gson gson = new Gson();
        BillFood billFood = gson.fromJson(json, BillFood.class);

        tvFoodShipperNameCus.setText(billFood.getCustomer().getName().toString() + "|" + billFood.getCustomer().getNumberphone().toString());
        tvFoodShipperLocationCus.setText(billFood.getCustomer().getLocation().toString());
        tvFoodShipperBillName.setText(billFood.getCart().getFood().getName() + "-" + billFood.getCart().getFood().getNameRestaurant());
        tvFoodShipperLocationRes.setText(billFood.getCart().getFood().getLocationRestaurant());
        tvFoodShipperBillPrice.setText(String.valueOf(billFood.getCart().getFood().getPrice())+ "đ");
        tvFoodShipperBillAmount.setText(String.valueOf(billFood.getCart().getAmount()));
        tvPhuongThucThanhToanShipper.setText("Tiền mặt");
        tvFoodShipperBillShip.setText("18000đ");
        int sum = Integer.valueOf(billFood.getCart().getFood().getPrice()) * Integer.valueOf(billFood.getCart().getAmount());
        tvFoodShipperBillSum.setText(String.valueOf(sum));
        tvFoodShipperBillSumPay.setText(String.valueOf(sum + 18000) + "đ");

        String idPhoto = billFood.getCart().getFood().getIdPhoto().toString();
        if(idPhoto == null || idPhoto == "") {
            return;
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(idPhoto);
        gsReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        Picasso.get().load(downloadUrl).error(R.drawable.baseline_image_24).into(imgBillFood);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }


    private void initUI() {
        tvFoodShipperNameCus = mView.findViewById(R.id.tv_foodshippernamecus);
        tvFoodShipperLocationCus = mView.findViewById(R.id.tv_foodshipperlocationcus);
        tvFoodShipperBillName = mView.findViewById(R.id.tv_foodshipperbillname);
        tvFoodShipperBillPrice = mView.findViewById(R.id.tv_foodshipperbillprice);
        tvFoodShipperBillAmount = mView.findViewById(R.id.tv_foodshipperbillamount);
        tvPhuongThucThanhToanShipper = mView.findViewById(R.id.tv_phuongthucthanhtoanshipper);
        tvFoodShipperBillShip = mView.findViewById(R.id.tv_foodshipperbillship);
        tvFoodShipperBillSum = mView.findViewById(R.id.tv_foodshipperbillsum);
        tvFoodShipperBillSumPay = mView.findViewById(R.id.tv_foodshipperbillsumpay);
        tvFoodShipperLocationRes = mView.findViewById(R.id.tv_foodshipperlocationres);
        imgBillFood = mView.findViewById(R.id.img_foodshipperbill);
        btReceiveBill = mView.findViewById(R.id.bt_receivebill);
        btGetBill = mView.findViewById(R.id.bt_getbill);
        btDeliverySuccess = mView.findViewById(R.id.tv_deliverysuccess);

        billFoodViewModel = new BillFoodViewModel();


    }
}
