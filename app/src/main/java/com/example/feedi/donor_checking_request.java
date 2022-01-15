package com.example.feedi;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class donor_checking_request extends AppCompatActivity {
    TextView first, city_, last, status_, instruction, members, view_leftover;
    String req_key, userUid, user_key;
Button check;
    String set_member, set_ins;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, request_reference;
    String req_status, leftover_request, leftover_key;
    String donor_key;
    String accept;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_checking_request);

        first = findViewById(R.id.first_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.person_city);
        instruction = findViewById(R.id.ins);
        members = findViewById(R.id.food_req);
        status_ = findViewById(R.id.req_status);
        view_leftover = findViewById(R.id.view_leftover);
        check=findViewById(R.id.accept);


        req_key = getIntent().getStringExtra("req_key");
        user_key = getIntent().getStringExtra("user_key");
        leftover_request = getIntent().getStringExtra("leftover_request");
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        accept=getIntent().getStringExtra("accept");
        setrequest();
        if (leftover_request != null) {
            if (leftover_request.equals("yes")) {
                view_leftover.setVisibility(View.VISIBLE);
                left_request();
            }
        } else {
            needy_request();
        }

        if(accept!=null)
        {
            if (accept.equals("yes"))
            {
                check.setVisibility(View.INVISIBLE);
            }
        }


    }

    private void left_request() {
        request_reference = firebaseDatabase.getReference("Requests_On_Leftovers").child(req_key);
        request_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    set_member = snapshot.child("members").getValue(String.class);
                    set_ins = snapshot.child("ins").getValue(String.class);
                    leftover_key = snapshot.child("leftover_key").getValue(String.class);
                    String key=snapshot.child("req_status").getValue(String.class);
                    Log.i("Tag", "beforeDataChange: "+key);
                    if(key!=null)
                    {
                        if (!key.equals("new"))
                        {
                            Log.i("Tag", "OnDataChange: "+key);
                            check.setVisibility(View.INVISIBLE);
                        }
                    }


                    instruction.setText(set_ins);
                    members.setText(set_member);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(donor_checking_request.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setrequest() {
        //Firebase

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users").child(user_key).child("profile");


        reference.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(donor_checking_request.this, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void needy_request() {
        request_reference = firebaseDatabase.getReference("Needy Requests").child(req_key);
        request_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    set_member = snapshot.child("members").getValue(String.class);
                    set_ins = snapshot.child("ins").getValue(String.class);
                    donor_key=snapshot.child("donor_key").getValue(String.class);

                    instruction.setText(set_ins);
                    members.setText(set_member);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(donor_checking_request.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void view_profile(View view) {
        Intent intent = new Intent(donor_checking_request.this, user_public_profile.class);
        intent.putExtra("user_key", user_key);
        startActivity(intent);

    }

    public void accept(View view) {
        Map<String, Object> updatedValues = new HashMap<>();
        req_status = "Accepted";
        updatedValues.put("/req_status", req_status);
        if(donor_key==null)
        {
            updatedValues.put("/donor_key",userUid);
        }
        request_reference.updateChildren(updatedValues);

        Intent intent = new Intent(donor_checking_request.this, delivery_option.class);
        intent.putExtra("req_key", req_key);
        startActivity(intent);
    }


    public void View_leftover(View view) {
        Intent intent = new Intent(donor_checking_request.this, leftover_description.class);
        intent.putExtra("key", leftover_key);
        intent.putExtra("leftover_request", "yes");
        startActivity(intent);

    }


//    public void chat(View view)
//    {
//        Intent intent=new Intent(donor_checking_request.this,activity_message.class);
//        intent.putExtra("user_key",user_key);
//        startActivity(intent);
//    }
}
