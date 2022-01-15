package com.example.feedi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

public class update_leftover_request extends AppCompatActivity {
    TextView first, city_, last, status_, address, msg;
    EditText instruction, members;


    String userUid;

    String req_key;
    String set_member, set_ins;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, request_reference;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.needy_adding_request);


        first = findViewById(R.id.first_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.post_city);
        instruction = findViewById(R.id.post_req_info);
        members = findViewById(R.id.post_food_req_num);
        status_ = findViewById(R.id.post_status);

        address = findViewById(R.id.address);
        msg = findViewById(R.id.msg);
        address.setVisibility(View.INVISIBLE);
        msg.setVisibility(View.INVISIBLE);


        req_key = getIntent().getStringExtra("req_key");
        setrequest();

    }

    private void setrequest() {
        //Firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users").child(userUid).child("profile");
        request_reference = firebaseDatabase.getReference("Requests_On_Leftovers").child(req_key);


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

            }
        });

        request_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    set_member = snapshot.child("members").getValue(String.class);
                    set_ins = snapshot.child("ins").getValue(String.class);


                    instruction.setText(set_ins);
                    members.setText(set_member);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(update_leftover_request.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void post(View view) {

        String ins = instruction.getText().toString();
        String member = members.getText().toString();


        if (TextUtils.isEmpty(ins) || TextUtils.isEmpty(member)) {
            Toast.makeText(update_leftover_request.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dbReference = firebaseDatabase.getReference("/Requests_On_Leftovers");
            DatabaseReference req = dbReference.push();


            Map<String, Object> updatedValues = new HashMap<>();

            updatedValues.put("/ins", ins);
            updatedValues.put("/members", member);
            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
            request_reference.updateChildren(updatedValues);


        }
    }
}
