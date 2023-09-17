package com.example.foodorderapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentProfileRestaurantsAddFoodEXXXX extends Fragment{
    private View mView;
    private ImageView imageView;
    private EditText edtFoodName, edtFoodPrice, edtFoodType, edtFoodInfo;
    private String[] foodtype;
    private ArrayAdapter<String> arrayAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private Button btAddfood;
    private NavController navController;
    private ProgressDialog progressDialog;
    private String selectedOption = "Cơm";
    private Uri uri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_addfood, container, false);
        initUI();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = (String) parent.getItemAtPosition(position);
            }
        });
        btAddfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onClickBtAddFood();
                    navController.navigateUp();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return mView;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);

    }



    private void onClickBtAddFood() throws FileNotFoundException {
        String foodType = selectedOption;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = database.getReference("foods");


        String name = edtFoodName.getText().toString();
        String price = (edtFoodPrice.getText().toString());
        String foodInfo = edtFoodInfo.getText().toString();

        Bundle bundle = getArguments();

        if (bundle != null) {
            String location = bundle.getString("locationRestaurant", null);
            String nameRestaurant = bundle.getString("nameRestaurant", null);
            String idRestaurant = bundle.getString("idRestaurant", null);
            String fileName = name + idRestaurant + ".jpg";
            String idPhoto = "gs://foodorderdb-16679.appspot.com/images/"+fileName;
            String idfood = "";
            int amount = 0;
            Float evaluate = Float.valueOf(5);
            Log.d("restaurants", nameRestaurant + location);
            Food food = new Food(idfood, idPhoto, name, foodType, foodInfo, price, idRestaurant, location, nameRestaurant, amount, evaluate);
            boolean check = checkValues(name, price);

            if(check){
                DatabaseReference newFood = foodRef.push();
                newFood.setValue(food)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }else {
            }
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + fileName);
            InputStream stream = getActivity().getContentResolver().openInputStream(uri);
            UploadTask uploadTask = imageRef.putStream(stream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Xử lý khi tải lên thành công
                    // taskSnapshot.getMetadata() chứa các thông tin metadata của tệp tin đã tải lên
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Xử lý khi tải lên thất bại
                }
            });

        }else {
            Toast.makeText(getActivity(), "bundle không có dữu liệu ", Toast.LENGTH_SHORT).show();
        }


    }

    private void initUI() {
        edtFoodName = mView.findViewById(R.id.tvprofile_foodname);
        edtFoodPrice = mView.findViewById(R.id.tvprofile_foodprice);
        edtFoodInfo = mView.findViewById(R.id.tvprofile_foodinfo);
        btAddfood = mView.findViewById(R.id.button_addfood);
        imageView = mView.findViewById(R.id.imgaddfood_image);
        progressDialog = new ProgressDialog(getActivity());


        foodtype = getResources().getStringArray(R.array.foodtype);
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_foodtype, foodtype);
        autoCompleteTextView = mView.findViewById(R.id.tvprofile_foodtypeex);
        autoCompleteTextView.setAdapter(arrayAdapter);





    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        view.findViewById(R.id.imgprofileaddfood_back).setOnClickListener(v -> {
            navController.navigateUp();
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100  && resultCode == Activity.RESULT_OK && data != null){
            uri = data.getData();
            Log.d("abcdef", String.valueOf(uri));
            Glide.with(mView).load(uri).into(imageView);

        }
    }

    private boolean checkValues(String name, String price) {
        if(name.isEmpty()){
            edtFoodName.setError("Nhập thông tin");
            return false;
        }
        if(price.isEmpty()){
            edtFoodPrice.setError("Nhập thông tin");
            return false;
        }
        return true;
    }
}
