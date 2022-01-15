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

public class needy_leftover_request_description extends AppCompatActivity {

    ImageView imageView;
    TextView first, city_, last, status_, instruction, members, address_info, view_leftover;
    String req_key, userUid, update;
    Button review, del;
    String set_member, set_ins;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, request_reference;

    String needy_key;
    public String donor_key;

    String leftover_key, key_no, reviewed;

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
        imageView = findViewById(R.id.req_edit_icon);
        address_info.setVisibility(View.INVISIBLE);
        review = findViewById(R.id.review);
        view_leftover = findViewById(R.id.view_leftover);

        view_leftover.setVisibility(View.VISIBLE);


        review.setVisibility(View.INVISIBLE);

        reviewed = getIntent().getStringExtra("reviewed");


        //intents
        key_no = getIntent().getStringExtra("key");
        req_key = getIntent().getStringExtra("req_key");
        update = getIntent().getStringExtra("update");

        if (update != null) {
            if (update.equals("no")) {
                Log.i("tag", "enter" + update);
                imageView.setVisibility(View.INVISIBLE);
                review.setVisibility(View.VISIBLE);
            }
        } else if (reviewed != null) {
            if (reviewed.equals("yes")) {
                review.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }
        }


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
                Toast.makeText(needy_leftover_request_description.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });

        request_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    set_member = snapshot.child("members").getValue(String.class);
                    set_ins = snapshot.child("ins").getValue(String.class);
                    leftover_key = snapshot.child("leftover_key").getValue(String.class);

                    needy_key = snapshot.child("user_key").getValue(String.class);
                    donor_key = snapshot.child("donor_key").getValue(String.class);
                    Log.i("donor ker", "meed" + donor_key);

                    instruction.setText(set_ins);
                    members.setText(set_member);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(needy_leftover_request_description.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void edit(View view) {

        Intent intent = new Intent(needy_leftover_request_description.this, update_leftover_request.class);
        intent.putExtra("req_key", req_key);
        startActivity(intent);
    }

    public void del(View view) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("Leftover Requests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        String key = snap.child("key").getValue(String.class);
                        String req = snap.child("req_key").getValue(String.class);
                        assert req != null;
                        if (req.equals(req_key)) {
                            assert key != null;
                            ref.child(key).removeValue();
                            //deleting from donor
                            DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("Users").child(donor_key).child("Accepted requests");

                            ref3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                            String key = snap.child("key").getValue(String.class);
                                            String req = snap.child("req_key").getValue(String.class);
                                            assert req != null;
                                            if (req.equals(req_key)) {
                                                assert key != null;
                                                ref3.child(key).removeValue();

                                                //deleting from donor
                                                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Users").child(donor_key).child("Leftover Requests");
                                                Log.i("TAG", "donor key : " + donor_key);
                                                ref2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                        if (snapshot.exists()) {
                                                            for (DataSnapshot snap : snapshot.getChildren()) {
                                                                String key = snap.child("key").getValue(String.class);
                                                                String req = snap.child("req_key").getValue(String.class);
                                                                assert req != null;
                                                                if (req.equals(req_key)) {
                                                                    assert key != null;
                                                                    ref2.child(key).removeValue();


                                                                }
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                        Toast.makeText(needy_leftover_request_description.this, "Error" + error, Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    Toast.makeText(needy_leftover_request_description.this, "Error" + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(needy_leftover_request_description.this, "Error" + error, Toast.LENGTH_SHORT).show();
            }
        });


        request_reference.removeValue();


        Toast.makeText(needy_leftover_request_description.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void review(View view) {

        Intent intent = new Intent(needy_leftover_request_description.this, adding_review.class);
        if (req_key != null && key_no != null) {

            intent.putExtra("key", key_no);
            intent.putExtra("req_key", req_key);
            intent.putExtra("needy_key", donor_key);
            intent.putExtra("from_needy", "yes");
            startActivity(intent);
            finish();
        }


    }

    public void View_leftover(View view) {
        if (leftover_key != null) {
            Intent intent = new Intent(needy_leftover_request_description.this, leftover_description.class);
            intent.putExtra("key", leftover_key);
            intent.putExtra("view", "yes");
            startActivity(intent);
        }


    }
}
