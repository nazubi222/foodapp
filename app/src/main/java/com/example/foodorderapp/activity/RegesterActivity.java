package com.example.foodorderapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegesterActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button btRegester;
    private TextView tvLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);

        edtEmail = findViewById(R.id.editTextEmailPhone);
        edtPassword = findViewById(R.id.passwordRegister);
        edtConfirmPassword = findViewById(R.id.confirmPasswordRegister);
        btRegester = findViewById(R.id.buttonRegester);
        tvLogin = findViewById(R.id.textviewLogin);
        progressDialog = new ProgressDialog(this);

        btRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();

            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegesterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount(){
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        boolean check = checkValues(email, password, confirmPassword);
        if(!check){
            return;
        }
        createAcountFirebase(email, password);


    }

    private void createAcountFirebase(String email, String password) {
//        changInProgress(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegesterActivity.this, "Đăng ký tài khoản thành công!",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();

                        } else {
                            Toast.makeText(RegesterActivity.this, "Đăng ký tài khoản thất bại!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean checkValues(String email, String password, String confirmPassword) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Nhập sai định dạng!");
            return false;
        }
        if(password.length() < 6){
            edtPassword.setError("Mật khẩu ngắn nhất là 6!");
            return false;
        }
        if(!password.equals(confirmPassword)){
            edtConfirmPassword.setError("Mật khẩu không giống nhau");
            return false;
        }
        return true;
    }
}