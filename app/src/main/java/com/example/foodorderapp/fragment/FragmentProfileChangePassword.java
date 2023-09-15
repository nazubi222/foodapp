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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.foodorderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentProfileChangePassword extends Fragment {
    private NavController navController;
    private View mView;
    private ImageView imgBack;
    private Button btUpdatePassword;
    private EditText edtPassWord, edtPassWordNew, edtPassWordNewRetype;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_profile_changepassword, container, false);
        initUI();
        btUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickChangrPassword();

            }
        });


        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        view.findViewById(R.id.imgchangepassword_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }

    private void onclickChangrPassword() {
        String password = edtPassWord.getText().toString().trim();
        String passwordNew = edtPassWordNew.getText().toString().trim();
        String passwordNewRetype = edtPassWordNewRetype.getText().toString().trim();
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Boolean check = false;
        Boolean isCheckValues = isCheckValues(passwordNew, passwordNewRetype);

        if(user == null){
            return;
        }

        if(isCheckValues) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(passwordNew)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                        Toast.makeText(getActivity(), "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(getActivity(), "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }

    }

    private boolean isCheckValues(String newPassWord, String newPassWordRetype) {
        if(!newPassWord.equals(newPassWordRetype)) {
            edtPassWordNewRetype.setError("Nhập sai mật khẩu");
            return false;
        }
        return true;

    }



    private void initUI() {
        progressDialog = new ProgressDialog(getActivity());
        imgBack = mView.findViewById(R.id.imgchangepassword_back);
        btUpdatePassword = mView.findViewById(R.id.btupdate_password);
        edtPassWord = mView.findViewById(R.id.edtpassword_reset);
        edtPassWordNew = mView.findViewById(R.id.edtpassword_resetnew);
        edtPassWordNewRetype = mView.findViewById(R.id.edtpassword_resetnew_retype);

    }


}
