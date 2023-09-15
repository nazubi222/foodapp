package com.example.foodorderapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class FragmentProfile extends Fragment {
    private NavController navController;
    private View mView;
    private TextView tvProfileName,tvprofileProfileInfo,
             tvProfileChangePassword, tvProfileRegulaytoryPolicy, tvProfileLocation, tvRestaurant;
    private LinearLayout tvProfileShipper;
    private ImageView imgProfile, imgEditname;
    private Button btLogout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();
        showUserProfile();
        onClickTvprofileProfileInfo();
        onClickTvProfileLocation();
        onclickTvProfileChangePassword();
        onClickTvProfileRegulaytoryPolicy();
        onClickBtLogout();
        onClickImgEditName();
        eventOnClickOther();



        return mView;
    }

    private void eventOnClickOther() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String email = user.getEmail();


        tvRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.equals("lehuy@gmail.com") || email.equals("tunganh@gmail.com")){
                    navController.navigate(R.id.action_fragmentProfile_to_fragmentProfileRestaurantsEXXXX);
                }else{
                    Toast.makeText(getActivity(), "Bạn chưa được cấp quyền!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvProfileShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.equals("lehuy@gmail.com") || email.equals("tunganh@gmail.com")) {
                    navController.navigate(R.id.action_fragmentProfile_to_fragmentProfileShipper);
                }else {
                    Toast.makeText(getActivity(), "Bạn chưa được cấp quyền!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void onClickTvProfileLocation() {
        tvProfileLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentProfileLocation);
            }
        });
    }

    private void onClickBtLogout() {
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void onClickTvProfileRegulaytoryPolicy() {
        tvProfileRegulaytoryPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentProfileRegulatoryPolicy);
            }
        });
    }

    private void onclickTvProfileChangePassword() {
        tvProfileChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentProfileChangePassword);
            }
        });

    }



    private void onClickTvprofileProfileInfo() {
        tvprofileProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentProfileInfomation);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void showUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String name = user.getDisplayName();

        if(name == null ){
            return;
        }

        tvProfileName.setText(name);
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.imgprofile).into(imgProfile);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("customers").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String locations = String.valueOf(document.get("location"));
                        if(locations != "null" || locations != ""){
                            tvProfileLocation.setText(locations);
                            Log.d("locations", locations);
                        }

                    } else {
                        Toast.makeText(getActivity(), "Xin vui lòng cập nhật địa ch!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void onClickImgEditName(){
        imgEditname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_fragmentProfile_to_fragmentProfileEditname);
            }
        });
    }






    private void initUI() {
        tvProfileName = mView.findViewById(R.id.tvprofile_name);
        imgProfile = mView.findViewById(R.id.imgprofile);
        imgEditname = mView.findViewById(R.id.imgv_editname);
        tvprofileProfileInfo = mView.findViewById(R.id.tvprofile_profileinfo);
        tvProfileRegulaytoryPolicy = mView.findViewById(R.id.tvprofile_policy);
        tvProfileChangePassword = mView.findViewById(R.id.tvprofile_password);
        btLogout = mView.findViewById(R.id.btprofile_logout);
        tvProfileLocation = mView.findViewById(R.id.tvprofile_location);
        tvRestaurant = mView.findViewById(R.id.tvprofile_addfood);
        tvProfileShipper = mView.findViewById(R.id.tvprofile_shipper);
    }



}
