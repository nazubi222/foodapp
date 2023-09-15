package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
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
import com.example.foodorderapp.model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentProfileInfomation extends Fragment {
    private ProgressDialog progressDialog;
    private NavController navController;
    private View mView;
    private ImageView imgBack;
    private Button btUpdateinfo;
    private TextView tvInfoName, tvInfoPhone, tvInfoEmail, tvInfoSex, tvInfoDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mView = inflater.inflate(R.layout.fragment_profile_infomations, container, false);
       initUI();

       showInfoCustomer();

       return mView;
    }

    private void showInfoCustomer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        progressDialog.show();
        DocumentReference docRef = db.collection("customers").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Customer customer = document.toObject(Customer.class);
                        tvInfoName.setText(customer.getName());
                        tvInfoEmail.setText(customer.getEmail());
                        tvInfoPhone.setText(customer.getNumberphone());
                        tvInfoSex.setText(customer.getGender());
                        tvInfoDate.setText(customer.getDate());
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(getActivity(), "Xin vui lòng cập nhật thông tin!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                }
            }
        });
    }

    private void initUI() {
        progressDialog = new ProgressDialog(getActivity());
        imgBack = mView.findViewById(R.id.imgprofileinfo_back);
        btUpdateinfo = mView.findViewById(R.id.btupdate_info);
        tvInfoName = mView.findViewById(R.id.tvprofileinfo_name);
        tvInfoEmail = mView.findViewById(R.id.tvprofileinfo_email);
        tvInfoPhone = mView.findViewById(R.id.tvprofileinfo_phone);
        tvInfoSex = mView.findViewById(R.id.tvprofileinfo_sex);
        tvInfoDate = mView.findViewById(R.id.tvprofileinfo_date);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        onClickBtUpdateInfo();
        view.findViewById(R.id.imgprofileinfo_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void onClickBtUpdateInfo() {
        btUpdateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfileInfomation_to_fragmentProfileInfomationEdit);
            }
        });
    }
}
