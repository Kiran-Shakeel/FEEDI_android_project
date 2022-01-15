package com.example.feedi;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    Intent intent;
    String userUid;
    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        int SPLASH_DISPLAY_LENGTH = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser!=null)
                {
                    String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admin").child("Added Sub-Admins");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sub-Admins").child(phone);
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            check=snapshot.child("status").getValue(String.class);
                                            intent = new Intent(MainActivity.this, sub_admin_home.class);
                                            startActivity(intent);
                                            finish();
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

                    if (phone.equals("+923201234567")) {
                        intent = new Intent(MainActivity.this, admin_home.class);
                        startActivity(intent);
                        finish();
                    }
                    userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("profile");
                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            String status=snapshot.child("status").getValue(String.class);
                            if(status!=null) {

                                if (status.equals("Donor") || status.equals("Needy")) {
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference uidRef = rootRef.child("Users").child(userUid);
                                    //check in all profiles
                                    ValueEventListener valueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!snapshot.exists()) {
                                                //create new user
                                                Toast.makeText(MainActivity.this, "Let's create a profile", Toast.LENGTH_SHORT).show();
                                                //intent.putExtra("profile_by", "user");
                                                intent = new Intent(MainActivity.this, user_create_profile.class);
                                            } else {
                                                String statusKey = snapshot.child("profile").child("status").getValue(String.class);
                                                if (statusKey.equals("Donor")) {

                                                    intent = new Intent(MainActivity.this, donor_home.class);

                                                } else {
                                                    intent = new Intent(MainActivity.this, needy_home.class);
                                                }

                                            }
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };
                                    uidRef.addListenerForSingleValueEvent(valueEventListener);
                                }
                            }
                            else {
                                if(check.equals(""))
                                {
                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference uidRef = rootRef.child("Users").child(userUid);
                                    //check in all profiles
                                    ValueEventListener valueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (!snapshot.exists()) {
                                                //create new user
                                                Toast.makeText(MainActivity.this, "Let's create a profile", Toast.LENGTH_SHORT).show();
                                                //intent.putExtra("profile_by", "user");
                                                intent = new Intent(MainActivity.this, user_create_profile.class);
                                            } else {
                                                String statusKey = snapshot.child("profile").child("status").getValue(String.class);
                                                if (statusKey.equals("Donor")) {

                                                    intent = new Intent(MainActivity.this, donor_home.class);

                                                } else {
                                                    intent = new Intent(MainActivity.this, needy_home.class);
                                                }

                                            }
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    };
                                    uidRef.addListenerForSingleValueEvent(valueEventListener);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });



                }
                else
                {
                    Intent mainIntent = new Intent(MainActivity.this, signIn.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}