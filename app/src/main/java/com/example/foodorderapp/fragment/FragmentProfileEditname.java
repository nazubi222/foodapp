package com.example.foodorderapp.fragment;




import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class FragmentProfileEditname extends Fragment {
    private NavController navController;
    private View mView;
    private ImageView imgProfile;
    private EditText edtName;
    private Button btUpdate;
    private Uri mUri;

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_editname, container, false);
        initUI();
        initListener();
        setUserInfomation();
        initUpdateListener();


        return mView;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

    }

    private void initUpdateListener() {
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    return;
                }

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(edtName.getText().toString())
                        .setPhotoUri(mUri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });
                navController.navigateUp();
                mView.invalidate();
            }
        });

    }

    private void initListener() {
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                galleryActivityResultLauncher.launch(intent);
            }
        });
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        mUri = data.getData();
                        Glide.with(mView).load(mUri).into(imgProfile);
                    }
                    else {
                        //cancelled
                        Toast.makeText(getActivity(), "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void setUserInfomation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        edtName.setText(user.getDisplayName());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.imgprofile).into(imgProfile);

    }

    private void initUI() {
        imgProfile = mView.findViewById(R.id.imginsertprofile);
        edtName = mView.findViewById(R.id.edtname);
        btUpdate = mView.findViewById(R.id.btupdate_profile);
    }
}
