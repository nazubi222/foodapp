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

public class LoginActivity extends AppCompatActivity {
    private TextView tvRegester;
    private EditText edtEmail, edtPassword;
    private Button btLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvRegester = findViewById(R.id.textviewRegester);
        edtEmail = findViewById(R.id.editTextUsername);
        edtPassword = findViewById(R.id.editTextPassword);
        btLogin = findViewById(R.id.buttonLogin);
        progressDialog = new ProgressDialog(LoginActivity.this);

        tvRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegesterActivity.class);
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }


    private void loginUserFirebase(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });

    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        boolean check = checkValues(email, password);
        if(!check){
            return;
        }
        loginUserFirebase(email, password);
    }

    private boolean checkValues(String email, String password) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Nhập sai định dạng!");
            return false;
        }
        if(password.length() < 6){
            edtPassword.setError("Mật khẩu ngắn nhất là 6!");
            return false;
        }
        return true;
    }
}