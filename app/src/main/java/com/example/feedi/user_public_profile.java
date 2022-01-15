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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class user_public_profile extends AppCompatActivity {

    TextView first, last, gender, status, number, profile_by, city, about;
    String first_, last_, gender_, status_, number_, city_, about_, link;
    ImageView profile;
    String user_key;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_public_profile);

        first = findViewById(R.id.name_label);
        last = findViewById(R.id.last_name_label);
        gender = findViewById(R.id.user_gen);
        status = findViewById(R.id.user_status);
        number = findViewById(R.id.number);
        profile_by = findViewById(R.id.profile_status);
        city = findViewById(R.id.user_city);
        about = findViewById(R.id.about_text);
        profile = findViewById(R.id.user_img);

        number.setVisibility(View.INVISIBLE);

        user_key = getIntent().getStringExtra("user_key");

        reference = FirebaseDatabase.getInstance().getReference("Users").child(user_key).child("profile");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    first_ = snapshot.child("first_name").getValue(String.class);
                    last_ = snapshot.child("last_name").getValue(String.class);
                    city_ = snapshot.child("city").getValue(String.class);
                    gender_ = snapshot.child("gender").getValue(String.class);
                    about_ = snapshot.child("about").getValue(String.class);
                    status_ = snapshot.child("status").getValue(String.class);
                    link = snapshot.child("profile_pic").getValue(String.class);


                    first.setText(first_);
                    last.setText(last_);
                    city.setText(city_);
                    gender.setText(gender_);
                    about.setText(about_);
                    status.setText(status_);
                    Picasso.get().load(link).into(profile);
                    profile_by.setText(status_);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(user_public_profile.this, "Error"+ error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reviews(View view)
    {
        Intent intent=new Intent(user_public_profile.this,all_reviews.class);
        intent.putExtra("user_key",user_key);
        intent.putExtra("check","yes");
        startActivity(intent);
    }

    public void chat(View view)
    {
        Intent intent=new Intent(user_public_profile.this,activity_message.class);
        intent.putExtra("user_key",user_key);
        startActivity(intent);
    }

}
