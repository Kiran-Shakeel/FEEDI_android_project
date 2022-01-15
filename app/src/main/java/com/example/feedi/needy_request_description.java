package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;

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

public class needy_request_description extends AppCompatActivity {


    ImageView imageView;
    TextView first, city_, last, status_, instruction, members, address_info;
    String req_key, userUid, update;
    Button review;
    String set_member, set_ins,reviewed,donor_key;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, request_reference;
    Button profile;
    String view_profile;


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
        review.setVisibility(View.INVISIBLE);
        reviewed=getIntent().getStringExtra("reviewed");
        profile=findViewById(R.id.view_profile);
        //intents
        req_key = getIntent().getStringExtra("req_key");
        update = getIntent().getStringExtra("update");
        view_profile=getIntent().getStringExtra("view_profile");


        if(view_profile!=null)
        {
            if (view_profile.equals("no"))
            {
                profile.setVisibility(View.INVISIBLE);
            }
        }
        else {
            profile.setVisibility(View.VISIBLE);
        }

        if (update != null) {
            if (update.equals("no")) {
                imageView.setVisibility(View.INVISIBLE);
                review.setVisibility(View.VISIBLE);
            }


        }
        else if(reviewed!=null)
        {
            if (reviewed.equals("yes"))
            {
                review.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }
        }

        setrequest();

    }

    private void setLeftoverRequest() {
    }

    private void setrequest() {
        //Firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users").child(userUid).child("profile");
        request_reference = firebaseDatabase.getReference("Needy Requests").child(req_key);


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
                    donor_key=snapshot.child("donor_key").getValue(String.class);



                    instruction.setText(set_ins);
                    members.setText(set_member);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(needy_request_description.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void edit(View view) {

        Intent intent = new Intent(needy_request_description.this, update_request.class);
        intent.putExtra("req_key", req_key);
        startActivity(intent);
    }

    public void del(View view) {

        request_reference.removeValue();

        Toast.makeText(needy_request_description.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void review(View view) {
        Intent intent = new Intent(needy_request_description.this, adding_review.class);
        if (req_key != null ) {


            intent.putExtra("req_key", req_key);
            intent.putExtra("needy_key", donor_key);
            intent.putExtra("from_needy","yes");
            startActivity(intent);
            finish();
        }

    }

    public void view_profile(View view)
    {
        Intent intent=new Intent(needy_request_description.this,user_public_profile.class);
        intent.putExtra("user_key",donor_key);
        startActivity(intent);
    }


}
