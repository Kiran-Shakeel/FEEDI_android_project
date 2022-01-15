package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class donor_profile extends AppCompatActivity {

    TextView first, last, cnic_, address_, about_, email_, city_, gender_, phone_, status_;
    CircleImageView pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_profile);

        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        toolbar.setNavigationIcon(R.drawable.arrow_white);
        setSupportActionBar(toolbar);

        first = findViewById(R.id.name_label);
        last=findViewById(R.id.last_name_label);
        cnic_ = findViewById(R.id.cnic_pro);
        address_ = findViewById(R.id.pro_);
        about_ = findViewById(R.id.about_text);
        email_ = findViewById(R.id.mail);
        city_ = findViewById(R.id.city_pro);
        gender_ = findViewById(R.id.gen_pro);
        phone_ = findViewById(R.id.phone_pro);
        status_ = findViewById(R.id.status_label);
        pic = findViewById(R.id.profile);


        setProfile();

    }

    private void setProfile() {
        String phone= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(userUid).child("profile");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String first_name = snapshot.child("first_name").getValue().toString();
                String last_name = snapshot.child("last_name").getValue().toString();
                String cnic = snapshot.child("cnic").getValue().toString();
                String city = snapshot.child("city").getValue().toString();
                String gender = snapshot.child("gender").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String about = snapshot.child("about").getValue().toString();
                String address = snapshot.child("address").getValue().toString();
                String status = snapshot.child("status").getValue().toString();
                String link=snapshot.child("profile_pic").getValue().toString();



                first.setText(first_name);
                last.setText(last_name);
                cnic_.setText(cnic);
                city_.setText(city);
                gender_.setText(gender);
                email_.setText(email);
                address_.setText(address);
                about_.setText(about);
                status_.setText(status);
                phone_.setText(phone);
                Picasso.get().load(link).into(pic);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(donor_profile.this, "Error: "+ error, Toast.LENGTH_SHORT).show();

            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_icon) {
            Intent intent = new Intent(this, user_edit_profile.class);
            startActivity(intent);



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void request(View view) {

        Intent intent=new Intent(donor_profile.this,request_list.class);
        intent.putExtra("received_requests_list","yes");
        startActivity(intent);
    }

    public void chats(View view)
    {
        Intent intent=new Intent(donor_profile.this,chat_list.class);
        startActivity(intent);
    }


    public void accept(View view) {
        Intent intent=new Intent(this,accepted_request_donor_list.class);
        startActivity(intent);
    }

    public void leftovers(View view) {
        Intent intent = new Intent(this, leftover_list.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfile();
    }
}
