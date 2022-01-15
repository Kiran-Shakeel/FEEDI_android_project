package com.example.feedi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class adding_review extends AppCompatActivity {

    TextView first_, last_, city_;
    String first, last, city;
    EditText comment;
    Button submit;
    RatingBar rating_bar;

    float rate_value;
    String temp, needy_key;

    String added_review_key, my_review_key, req_key, key, userUid, from_needy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_review);


        comment = findViewById(R.id.commenting);
        rating_bar = findViewById(R.id.rating);
        submit = findViewById(R.id.submit);
        first_ = findViewById(R.id.first_name);
        last_ = findViewById(R.id.last_name);
        city_ = findViewById(R.id.review_person_city);
        needy_key = getIntent().getStringExtra("needy_key");
        req_key = getIntent().getStringExtra("req_key");
        key = getIntent().getStringExtra("key");
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        from_needy = getIntent().getStringExtra("from_needy");

        setProfile();


        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate_value = rating_bar.getRating();

                if (rate_value > 0 && rate_value <= 1) {
                    Toast.makeText(adding_review.this, "Bad" + rate_value, Toast.LENGTH_SHORT).show();

                } else if (rate_value > 1 && rate_value <= 2) {

                    Toast.makeText(adding_review.this, "Ok" + rate_value, Toast.LENGTH_SHORT).show();
                } else if (rate_value > 2 && rate_value <= 3) {

                    Toast.makeText(adding_review.this, "Good" + rate_value, Toast.LENGTH_SHORT).show();
                } else if (rate_value > 3 && rate_value <= 4) {

                    Toast.makeText(adding_review.this, "Better" + rate_value, Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(adding_review.this, "Best" + rate_value, Toast.LENGTH_SHORT).show();
                }


            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getText().toString().equals(null)) {
                    Toast.makeText(adding_review.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else {

                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if(from_needy!=null)
                    {
                        if(from_needy.equals("yes"))
                        {
                            //adding request to needy profile
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(needy_key).child("added_reviews").push();
                            added_review_key = ref.getKey();
                            review_info info = new review_info(rate_value, comment.getText().toString(), userUid, added_review_key, req_key);
                            ref.setValue(info);


                            //adding request to donor_profile

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("my reviews").child(added_review_key);
                            //my_review_key = databaseReference.getKey();
                            review_info info1 = new review_info(added_review_key, needy_key, req_key);
                            databaseReference.setValue(info1);

                            Toast.makeText(adding_review.this, "Review added Successfully", Toast.LENGTH_SHORT).show();
                            Toast.makeText(adding_review.this, "You can check them in my reviews", Toast.LENGTH_SHORT).show();

                            //updating status of requests
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests_On_Leftovers").child(req_key);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Map<String, Object> updatedValues = new HashMap<>();
                                        updatedValues.put("/req_status", "Reviewed");
                                        reference.updateChildren(updatedValues);
                                    } else {
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Needy Requests").child(req_key);
                                        reference1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                                Map<String, Object> updatedValues = new HashMap<>();
                                                updatedValues.put("/req_status", "Reviewed");
                                                reference1.updateChildren(updatedValues);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                Toast.makeText(adding_review.this, "Error" + error, Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    Toast.makeText(adding_review.this, "Error" + error, Toast.LENGTH_SHORT).show();
                                }

                            });

                            finish();

                        }
                    }

                    else
                    {
                        //adding request to needy profile
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(needy_key).child("added_reviews").push();
                        added_review_key = ref.getKey();
                        review_info info = new review_info(rate_value, comment.getText().toString(), userUid, added_review_key, req_key);
                        ref.setValue(info);


                        //adding request to donor_profile

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("my reviews").child(added_review_key);
                        //my_review_key = databaseReference.getKey();
                        review_info info1 = new review_info(added_review_key, needy_key, req_key);
                        databaseReference.setValue(info1);

                        Toast.makeText(adding_review.this, "Review added Successfully", Toast.LENGTH_SHORT).show();
                        Toast.makeText(adding_review.this, "You can check them in my reviews", Toast.LENGTH_SHORT).show();

                        DatabaseReference accept = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("Accepted requests").child(key);
                        Map<String, Object> update = new HashMap<>();
                        update.put("/req_status", "Reviewed");
                        accept.updateChildren(update);
                        finish();

                    }



                }


            }

        });


    }

    private void setProfile() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(needy_key).child("profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    first = snapshot.child("first_name").getValue(String.class);
                    last = snapshot.child("last_name").getValue(String.class);
                    city = snapshot.child("city").getValue(String.class);
                    first_.setText(first);
                    last_.setText(last);
                    city_.setText(city);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(adding_review.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });

    }
}
