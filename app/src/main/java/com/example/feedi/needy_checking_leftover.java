package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class needy_checking_leftover extends AppCompatActivity {

    TextView first, last, city_, instruction, date, members;
    ImageView pic;
    String leftover_key, user_key, userUid;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, leftover_reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needy_checking_leftover);

        first = findViewById(R.id.person_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.check_person_city);
        instruction = findViewById(R.id.chk_ins);
        members = findViewById(R.id.chk_member);
        date = findViewById(R.id.chk_date);
        pic = findViewById(R.id.checking_image);


        leftover_key = getIntent().getStringExtra("leftover_key");
        user_key = getIntent().getStringExtra("user_key");

        setleftover();


    }

    private void setleftover() {
        //Firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users").child(user_key).child("profile");
        leftover_reference = firebaseDatabase.getReference("Leftovers").child(leftover_key);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String first_name = snapshot.child("first_name").getValue(String.class);
                String last_name = snapshot.child("last_name").getValue(String.class);
                String city = snapshot.child("city").getValue(String.class);


                first.setText(first_name);
                last.setText(last_name);
                city_.setText(city);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        leftover_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String set_member = snapshot.child("members").getValue(String.class);
                    String set_ins = snapshot.child("ins").getValue(String.class);
                    String expiry = snapshot.child("expirey_date").getValue(String.class);
                    String link = snapshot.child("image").getValue(String.class);


                    instruction.setText(set_ins);
                    members.setText(set_member);
                    date.setText(expiry);
                    Picasso.get().load(link).into(pic);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(needy_checking_leftover.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void public_profile(View view) {
        Intent intent = new Intent(needy_checking_leftover.this, user_public_profile.class);
        intent.putExtra("user_key", user_key);
        startActivity(intent);
    }

    public void request(View view) {
        Intent intent = new Intent(needy_checking_leftover.this, needy_adding_request_leftover.class);
        intent.putExtra("user_key", user_key);
        intent.putExtra("leftover_key", leftover_key);
        startActivity(intent);
    }
}
