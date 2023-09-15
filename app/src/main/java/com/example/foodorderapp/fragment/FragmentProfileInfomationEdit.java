package com.example.foodorderapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Customer;
import com.example.foodorderapp.viewmodel.CustomerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentProfileInfomationEdit extends Fragment {
    private ProgressDialog progressDialog;

    private NavController navController;
    private View mView;
    private ImageView imgBack;
    private Button btUpdate;
    private EditText edtProfileInfoName, edtProfileInfoPhone, edtProfileInfoEmail
            ,edtProfileInfoGender, edtProfileInfoDate;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = auth.getCurrentUser();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_profile_infomations_edit, container, false);
        initUI();
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateInfo();

            }
        });


        return mView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
//        onClickBtBack();
        view.findViewById(R.id.imgprofileinfoedit_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void onClickUpdateInfo() {

        if (firebaseUser == null) {
            return;
        }
        String id = firebaseUser.getUid();
        String name = edtProfileInfoName.getText().toString();
        String phone = edtProfileInfoPhone.getText().toString();
        String email = firebaseUser.getEmail();
        String gender = edtProfileInfoGender.getText().toString();
        String date = edtProfileInfoDate.getText().toString();;
        String location = null;

        boolean check = checkValues(name, phone, date, gender);
        if(check) {

            Customer customer = new Customer(id, name, phone, date, email, gender, location);

            CustomerViewModel customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
            customerViewModel.addCustomer(customer);
            Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        }



    }

    private void initUI() {
        progressDialog = new ProgressDialog(getActivity());
        imgBack = mView.findViewById(R.id.imgprofileinfoedit_back);
        edtProfileInfoName = mView.findViewById(R.id.edtprofileinfo_name);
        edtProfileInfoPhone = mView.findViewById(R.id.edtprofileinfo_phone);
        edtProfileInfoDate = mView.findViewById(R.id.edtprofileinfo_date);
        edtProfileInfoGender = mView.findViewById(R.id.edtprofileinfo_gender);
        edtProfileInfoEmail = mView.findViewById(R.id.edtprofileinfo_email);
        btUpdate = mView.findViewById(R.id.btsaveupdate_info);
        edtProfileInfoEmail.setText(firebaseUser.getEmail());
    }
    private boolean checkValues(String name, String phone, String date, String gender){
        if(name.isEmpty()){
            edtProfileInfoName.setError("Nhập thông tin!");
            return false;
        }
        if(phone.isEmpty()){
            edtProfileInfoPhone.setError("Nhập thông tin!");
            return false;
        }
        if(date.isEmpty()){
            edtProfileInfoDate.setError("Nhập thông tin!");
            return false;
        }
        if(gender.isEmpty()){
            edtProfileInfoGender.setError("Nhập thông tin!");
            return false;
        }
        return true;
    }
}
