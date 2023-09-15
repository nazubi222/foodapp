package com.example.foodorderapp.repository;

import android.util.Log;
import android.widget.Toast;

import com.example.foodorderapp.model.BillFood;
import com.example.foodorderapp.model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Date;

public class FirebaseBillFoodRepo implements BillFoodRepo {
    private static final String CUSTOMER_COLLECTION = "customers";
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();
    private String uid = currentUser.getUid();
    @Override
    public void addBillFoodFireStore(BillFood billFood) {
        DocumentReference cardRef = database.collection(CUSTOMER_COLLECTION).document(uid);
        Task<DocumentReference> colectionBill = cardRef.collection("billfoods").add(billFood)
                .addOnSuccessListener((OnSuccessListener<DocumentReference>) documentReference -> {

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirebaseUserRepository", "Error adding user", e);
                    }
                });
    }

    @Override
    public void addBillFoodRealtime(BillFood billFood) {
        CollectionReference billres = database.collection("billfoods");
        billres.add(billFood).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentReference> task) {

            }
        });

    }

    @Override
    public void updateBillFood(String status, String idCart) {
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("cart.id", idCart);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            if(documentSnapshot.exists()) {
                                DocumentReference documentRef = documentSnapshot.getReference();
                                documentRef.update("status", status)
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
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi tìm kiếm
                    }
                });
    }

    @Override
    public void updateBillFoodShipper(Customer customer, String idCart) {
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("cart.id", idCart);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            if(documentSnapshot.exists()) {
                                DocumentReference documentRef = documentSnapshot.getReference();
                                documentRef.update("shipper.customer", customer)
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
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi tìm kiếm
                    }
                });
    }

    @Override
    public void updateDateBillFood(String field, String idCart) {
        Date date = new Date();
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("cart.id", idCart);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            if(documentSnapshot.exists()) {
                                DocumentReference documentRef = documentSnapshot.getReference();
                                documentRef.update(field, date)
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
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi tìm kiếm
                    }
                });

    }

    @Override
    public void updateAmountFood(int amount, String idFood) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("foods").child(idFood).child("amount").setValue(amount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
    }

    @Override
    public void updateAvaluateFood(float avaluatefood, String idFood) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("foods").child(idFood).child("evaluate").setValue(avaluatefood)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
    }

    @Override
    public void updateAvaluated(String idCart, float evaluateCus) {
        CollectionReference collectionRef = database.collection("billfoods");
        Query query = collectionRef.whereEqualTo("cart.id", idCart);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            if(documentSnapshot.exists()) {
                                DocumentReference documentRef = documentSnapshot.getReference();
                                    documentRef.update("evaluated", true)
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
                                documentRef.update("cart.food.evaluate", evaluateCus)
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
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý lỗi tìm kiếm
                    }
                });
    }
}
