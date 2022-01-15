package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class accepted_request_donor_description extends AppCompatActivity {
    String req_key, needy_user_key, key;
    TextView first, city_, last, status_, instruction, members, address_info;
    Button delivered, review;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, request_reference;
    String set_member, set_ins, address, req_key_for_review;
    String userUid;
    ImageView imageView;
    String reviewed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needy_req_description);

        first = findViewById(R.id.first_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.needy_city);
        instruction = findViewById(R.id.needy_req_info);
        members = findViewById(R.id.need_food_req_num);
        status_ = findViewById(R.id.needy_status);
        address_info = findViewById(R.id.address_info);
        delivered = findViewById(R.id.req_del_bttn);
        imageView = findViewById(R.id.req_edit_icon);
        imageView.setVisibility(View.INVISIBLE);
        review = findViewById(R.id.review);


        reviewed = getIntent().getStringExtra("reviewed");
        if (reviewed == null) {

            review.setVisibility(View.VISIBLE);
        }


//        delivered.setBackgroundResource(R.drawable.button_shape);
//        delivered.setText("Add Review");

        key = getIntent().getStringExtra("key");
        req_key_for_review = getIntent().getStringExtra("req_key_for_review");

        setrequest();

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(accepted_request_donor_description.this, adding_review.class);
                if (req_key_for_review != null) {

                    intent.putExtra("key", key);
                    intent.putExtra("req_key", req_key_for_review);
                    intent.putExtra("needy_key", needy_user_key);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }

    private void setrequest() {
        //Firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users").child(userUid).child("Accepted requests").child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                req_key = snapshot.child("req_key").getValue(String.class);
                address = snapshot.child("address").getValue(String.class);

                request_reference = firebaseDatabase.getReference("Needy Requests").child(req_key);
                request_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            set_ins = snapshot.child("ins").getValue(String.class);
                            needy_user_key = snapshot.child("user_key").getValue(String.class);
                            set_member = snapshot.child("members").getValue(String.class);
                            address = snapshot.child("address").getValue(String.class);


                            instruction.setText(set_ins);
                            members.setText(set_member);
                            address_info.setText(address);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(needy_user_key).child("profile");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    String first_name = snapshot.child("first_name").getValue(String.class);
                                    String last_name = snapshot.child("last_name").getValue(String.class);
                                    String city = snapshot.child("city").getValue(String.class);
                                    String status = snapshot.child("status").getValue(String.class);


                                    first.setText(first_name);
                                    last.setText(last_name);
                                    city_.setText(city);
                                    status_.setText(status);

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    Toast.makeText(accepted_request_donor_description.this, "Error" + error, Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            request_reference = firebaseDatabase.getReference("Requests_On_Leftovers").child(req_key);
                            request_reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        set_ins = snapshot.child("ins").getValue(String.class);
                                        needy_user_key = snapshot.child("user_key").getValue(String.class);
                                        set_member = snapshot.child("members").getValue(String.class);
                                        address = snapshot.child("address").getValue(String.class);


                                        instruction.setText(set_ins);
                                        members.setText(set_member);
                                        address_info.setText(address);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(needy_user_key).child("profile");
                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                String first_name = snapshot.child("first_name").getValue(String.class);
                                                String last_name = snapshot.child("last_name").getValue(String.class);
                                                String city = snapshot.child("city").getValue(String.class);
                                                String status = snapshot.child("status").getValue(String.class);


                                                first.setText(first_name);
                                                last.setText(last_name);
                                                city_.setText(city);
                                                status_.setText(status);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                Toast.makeText(accepted_request_donor_description.this, "Error" + error, Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(accepted_request_donor_description.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void del(View view) {
        reference.removeValue();

        Toast.makeText(this, "Delete Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


}
