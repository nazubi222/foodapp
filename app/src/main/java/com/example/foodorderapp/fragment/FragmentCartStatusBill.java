package com.example.foodorderapp.fragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.viewmodel.BillFoodViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentCartStatusBill extends Fragment {
    private NavController navController;
    private ImageView imgFood, imgCartStatusReceiver, imgCartStatusGet, imgCartStatusSuccess;
    private TextView tvCartStatusNameShip, tvCartStatusPhoneShip
            , tvCartStatusNameCus , tvCartStatusPhoneCus , tvCartStatusLocationCus , tvCartStatusBillName, tvCartStatusLocationRes
            , tvCartStatusBillPrice, tvCartStatusBillAmount, tvPhuongThucThanhToanStatus, tvCartStatusBillSumPay, tvDestroyBill;
    private LinearLayout lnCartStatusShip;
    private View mView;
    private BillFoodViewModel billFoodViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart_status_bill, container, false);
        initUI();
        getData();
        onCLickDestroyBill();
        return mView;

    }

    private void onCLickDestroyBill() {
        Bundle bundle = getArguments();
        String json = bundle.getString("billstatus");
        Gson gson = new Gson();
        BillFood billFood = gson.fromJson(json, BillFood.class);
        tvDestroyBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (billFood.getStatus().equals("Chờ xác nhận")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Bạn có chắc chắn muốn hủy đơn này không ?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    billFoodViewModel.updateDateBillFood("endDate", billFood.getCart().getId());
                                    billFoodViewModel.updateBillFood("Đơn đã hủy!", billFood.getCart().getId());
                                    Toast.makeText(getActivity(), "Hủy đơn thành công!", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }else{
                    Toast.makeText(getActivity(), "Shipper đã nhận hàng đơn hàng không thể hủy!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgcartstatusbill_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }


    private void getData() {
        Bundle bundle = getArguments();
        String json = bundle.getString("billstatus");
        Gson gson = new Gson();
        BillFood billFood = gson.fromJson(json, BillFood.class);


        tvCartStatusNameCus.setText(billFood.getCustomer().getName());
        tvCartStatusPhoneCus.setText(billFood.getCustomer().getNumberphone().toString());
        tvCartStatusLocationCus.setText(billFood.getCustomer().getLocation());
        tvCartStatusBillName.setText(billFood.getCart().getFood().getName() + "-" + billFood.getCart().getFood().getNameRestaurant());
        tvCartStatusLocationRes.setText(billFood.getCart().getFood().getLocationRestaurant());
        tvCartStatusBillPrice.setText(billFood.getCart().getFood().getPrice().toString() + "đ");
        tvCartStatusBillAmount.setText("x" + String.valueOf(billFood.getCart().getAmount()));
        tvPhuongThucThanhToanStatus.setText("Tiền mặt");
        tvCartStatusBillSumPay.setText(String.valueOf(billFood.getTotalMoney()));

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference bill = database.collection("billfoods");
        bill.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Xử lý lỗi nếu có
                    return;
                }
                if (value != null ) {
                    for (DocumentSnapshot document : value.getDocuments()) {

                        BillFood billFoods = document.toObject(BillFood.class);
                        if(billFoods.getCart().getId().equals(billFood.getCart().getId())){
                            if(billFoods.getStatus().trim().equals("Shipper đã nhận hàng!".trim())){
                                imgCartStatusReceiver.setImageResource(R.drawable.yes);
                                lnCartStatusShip.setVisibility(View.VISIBLE);
                                tvCartStatusPhoneShip.setText(billFoods.getShipper().getCustomer().getNumberphone().toString());
                                tvCartStatusNameShip.setText(billFoods.getShipper().getCustomer().getName());
                            }else{
                                imgCartStatusReceiver.setImageResource(R.drawable.dry_clean);
                            }

                            if(billFoods.getStatus().trim().equals("Shipper đã lấy hàng thành công!".trim())){
                                imgCartStatusGet.setImageResource(R.drawable.yes);
                                imgCartStatusReceiver.setImageResource(R.drawable.yes);
                                lnCartStatusShip.setVisibility(View.VISIBLE);
                                tvCartStatusPhoneShip.setText(billFoods.getShipper().getCustomer().getNumberphone().toString());
                                tvCartStatusNameShip.setText(billFoods.getShipper().getCustomer().getName());
                            }else{
                                imgCartStatusGet.setImageResource(R.drawable.dry_clean);
                            }
                            if(billFoods.getStatus().trim().equals("Giao hàng thành công!".trim())){
                                imgCartStatusGet.setImageResource(R.drawable.yes);
                                imgCartStatusReceiver.setImageResource(R.drawable.yes);
                                imgCartStatusSuccess.setImageResource(R.drawable.yes);
                            }else{
                                imgCartStatusSuccess.setImageResource(R.drawable.dry_clean);
                            }


                        }


                    }
                    mView.invalidate();
                }
            }
        });


        String idPhoto = billFood.getCart().getFood().getIdPhoto().toString().trim();
        Log.d("testuri", idPhoto);
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
                        Picasso.get().load(downloadUrl).error(R.drawable.baseline_image_24).into(imgFood);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void initUI() {
        tvCartStatusNameShip = mView.findViewById(R.id.tv_cartstatusnameship);
        tvCartStatusPhoneShip = mView.findViewById(R.id.tv_cartstatusphoneship);
        tvCartStatusNameCus = mView.findViewById(R.id.tv_cartstatusnamecus);
        tvCartStatusPhoneCus = mView.findViewById(R.id.tv_cartstatusphonecus);
        tvCartStatusLocationCus = mView.findViewById(R.id.tv_cartstatuslocationcus);
        tvCartStatusBillName = mView.findViewById(R.id.tv_cartstatusbillname);
        tvCartStatusLocationRes = mView.findViewById(R.id.tv_cartstatuslocationres);
        tvCartStatusBillPrice = mView.findViewById(R.id.tv_cartstatusbillprice);
        tvCartStatusBillAmount = mView.findViewById(R.id.tv_cartstatusbillamount);
        tvPhuongThucThanhToanStatus = mView.findViewById(R.id.tv_phuongthucthanhtoanstatus);
        tvCartStatusBillSumPay = mView.findViewById(R.id.tv_cartstatusbillsumpay);
        imgFood = mView.findViewById(R.id.img_cartstatusbill);
        imgCartStatusReceiver = mView.findViewById(R.id.img_cartstatusreceiver);
        imgCartStatusGet = mView.findViewById(R.id.img_cartstatusget);
        imgCartStatusSuccess = mView.findViewById(R.id.img_cartstatussuccess);
        lnCartStatusShip = mView.findViewById(R.id.ln_cartstatusship);
        tvDestroyBill = mView.findViewById(R.id.destroybill);
        billFoodViewModel = new BillFoodViewModel();


    }
}
