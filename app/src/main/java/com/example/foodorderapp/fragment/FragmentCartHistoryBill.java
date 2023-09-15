package com.example.foodorderapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.viewmodel.BillFoodViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentCartHistoryBill extends Fragment {
    private Food food;
    private NavController navController;
    private ImageView imgFood;
    private TextView tvCartHistoryStatusFood, tvCartHistoryStatusDateFood, tvCartHistoryNameShip, tvCartHistoryPhoneShip, tvCartHitoryBillAvaluate
            , tvCartHistoryNameCus , tvCartHistoryPhoneCus , tvCartHistoryLocationCus , tvCartHistoryBillName, tvCartHistoryLocationRes
            , tvCartHistoryBillPrice, tvCartHistoryBillAmount, tvPhuonThucThanhToanHis, tvCartHistoryBillShip, tvCartHistoryBillSum
            , tvCartHistoryBillSumPay, tvCartHistoryDateOrder, tvCartHistoryDateReceiver, tvCartHistoryDateGet
            , tvCartHistoryDateSuccess, tvThoiGianGiaoHangThanhCong;
    private LinearLayout lnCartHistoryShip;
    private RelativeLayout rltReceiver, rltGetBill;
    private View mView;
    private BillFoodViewModel billFoodViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart_history_bill, container, false);
        initUI();
        getData();
        onClickAvaluate();
        return mView;

    }

    private void onClickAvaluate() {
        Bundle bundle = getArguments();
        String json = bundle.getString("billhistory");
        Gson gson = new Gson();
        BillFood billFood = gson.fromJson(json, BillFood.class);
        if(billFood.getStatus().equals("Đơn đã hủy!")){
            tvCartHitoryBillAvaluate.setEnabled(false);
            tvCartHitoryBillAvaluate.setBackgroundColor(Color.parseColor("#6F111111"));
            return;
        }

        if(billFood.getEvaluated() == true){
            tvCartHitoryBillAvaluate.setText("Đã đánh giá");
            tvCartHitoryBillAvaluate.setEnabled(false);
        }else {

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("foods").child(billFood.getCart().getFood().getId().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                    } else {
                        food = task.getResult().getValue(Food.class);
                    }
                }
            });

            tvCartHitoryBillAvaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_cart_history_bill_ratting);
                    Window window = dialog.getWindow();
                    if (window == null) {
                        return;
                    }
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    WindowManager.LayoutParams windowAttribute = window.getAttributes();
                    windowAttribute.gravity = Gravity.CENTER;
                    window.setAttributes(windowAttribute);

                    RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
                    TextView Send = dialog.findViewById(R.id.sendrating);
                    TextView exit = dialog.findViewById(R.id.exitsendrating);



                    exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            float result = ratingBar.getRating();

                            float avaluated = food.getEvaluate();
                            int amountfood = food.getAmount();
                            int amountfoodcus = billFood.getCart().getAmount();
                            float avaluateFood = (float) (((amountfood - amountfoodcus)*avaluated
                                    + amountfoodcus *result) / (amountfood));
                            Log.d("checkvalue", String.valueOf(result) + "/"+ String.valueOf(amountfood)
                                    +"/"+ String.valueOf(amountfoodcus) +"/"+String.valueOf(avaluateFood));
                            billFoodViewModel.updateAvaluateFood(avaluateFood, billFood.getCart().getFood().getId());
                            billFoodViewModel.updateAvaluated(billFood.getCart().getId(), result);
                            tvCartHitoryBillAvaluate.setText("Đã đánh giá");
                            tvCartHitoryBillAvaluate.setEnabled(false);
                            billFood.setEvaluated(true);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        String json = bundle.getString("billhistory");
        Gson gson = new Gson();
        BillFood billFood = gson.fromJson(json, BillFood.class);

        String status = billFood.getStatus();

        tvCartHistoryStatusFood.setText(status);
        tvCartHistoryStatusDateFood.setText(dateFormat(billFood.getCreatedDate()));
        if(status.equals("Giao hàng thành công!")){
            lnCartHistoryShip.setVisibility(View.VISIBLE);
            tvCartHistoryNameShip.setText(billFood.getShipper().getCustomer().getName());
            tvCartHistoryPhoneShip.setText(billFood.getShipper().getCustomer().getNumberphone().toString());
        }
        tvCartHistoryNameCus.setText(billFood.getCustomer().getName());
        tvCartHistoryPhoneCus.setText(billFood.getCustomer().getNumberphone().toString());
        tvCartHistoryLocationCus.setText(billFood.getCustomer().getLocation());
        tvCartHistoryBillName.setText(billFood.getCart().getFood().getName() + "-" + billFood.getCart().getFood().getNameRestaurant());
        tvCartHistoryLocationRes.setText(billFood.getCart().getFood().getLocationRestaurant());
        tvCartHistoryBillPrice.setText(billFood.getCart().getFood().getPrice().toString() + "đ");
        tvCartHistoryBillAmount.setText("x" + String.valueOf(billFood.getCart().getAmount()));
        tvPhuonThucThanhToanHis.setText("Tiền mặt");
        tvCartHistoryBillShip.setText("18000đ");
        int sum = Integer.parseInt(billFood.getCart().getFood().getPrice()) * billFood.getCart().getAmount();
        tvCartHistoryBillSum.setText(String.valueOf(sum));
        tvCartHistoryBillSumPay.setText(String.valueOf(billFood.getTotalMoney()));
        tvCartHistoryDateOrder.setText(dateFormat(billFood.getCreatedDate()));

        Log.d("checkdate", String.valueOf(billFood.getDateReceiverBill()) +"/" +String.valueOf(String.valueOf(billFood.getDateReceiverBill()) == null));

        if(String.valueOf(billFood.getDateReceiverBill()) == "null"){
            rltReceiver.setVisibility(View.GONE);
            tvThoiGianGiaoHangThanhCong.setText("Thời gian hủy đơn");
        }else {
            tvCartHistoryDateReceiver.setText(dateFormat(billFood.getDateReceiverBill()));
        }
        if(String.valueOf(billFood.getDateGetBill()) == "null"){
            rltGetBill.setVisibility(View.GONE);
        }else {
            tvCartHistoryDateGet.setText(dateFormat(billFood.getDateGetBill()));
        }





        tvCartHistoryDateSuccess.setText(dateFormat(billFood.getEndDate()));

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
        tvCartHistoryStatusFood = mView.findViewById(R.id.tv_carthistorystatusfood);
        tvCartHistoryStatusDateFood = mView.findViewById(R.id.tv_carthistorystatusdatefood);
        tvCartHistoryNameShip = mView.findViewById(R.id.tv_carthistorynameship);
        tvCartHistoryPhoneShip = mView.findViewById(R.id.tv_carthistoryphoneship);
        tvCartHistoryNameCus = mView.findViewById(R.id.tv_carthistorynamecus) ;
        tvCartHistoryPhoneCus = mView.findViewById(R.id.tv_carthistoryphonecus) ;
        tvCartHistoryLocationCus = mView.findViewById(R.id.tv_carthistorylocationcus) ;
        tvCartHistoryBillName = mView.findViewById(R.id.tv_carthitorybillname);
        tvCartHistoryLocationRes = mView.findViewById(R.id.tv_carthitorylocationres) ;
        tvCartHistoryBillPrice = mView.findViewById(R.id.tv_carthitorybillprice);
        tvCartHistoryBillAmount = mView.findViewById(R.id.tv_carthitorybillamount);
        tvPhuonThucThanhToanHis = mView.findViewById(R.id.tv_phuongthucthanhtoanhistory);
        tvCartHistoryBillShip = mView.findViewById(R.id.tv_carthitorybillship);
        tvCartHistoryBillSum = mView.findViewById(R.id.tv_carthitorybillsum);
        tvCartHistoryBillSumPay = mView.findViewById(R.id.tv_carthitorybillsumpay);
        tvCartHistoryDateOrder = mView.findViewById(R.id.tv_carthitorydateorder);
        tvCartHistoryDateReceiver = mView.findViewById(R.id.tv_carthitorydatereceiver);
        tvCartHistoryDateGet = mView.findViewById(R.id.tv_carthitorydateget);
        tvCartHistoryDateSuccess = mView.findViewById(R.id.tv_carthitorydatesucces);
        lnCartHistoryShip = mView.findViewById(R.id.ln_carthistoryship);
        imgFood = mView.findViewById(R.id.img_carthitorybill);
        rltReceiver = mView.findViewById(R.id.rlt_receiver);
        rltGetBill = mView.findViewById(R.id.rlt_getbill);
        tvThoiGianGiaoHangThanhCong = mView.findViewById(R.id.thoigiangiaohangthanhcong);
        tvCartHitoryBillAvaluate = mView.findViewById(R.id.tv_carthitorybillavaluate);
        billFoodViewModel = new BillFoodViewModel();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgcarthisbill_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }
    private String dateFormat(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String ngay = dateFormat.format(date);
        String gio = timeFormat.format(date);

        return ngay + " " + gio;
    }
}
