package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class otp_verification extends AppCompatActivity {

    private static final String TAG = "TAG";
    EditText getcode;
    TextView invalid, pre;
    Button verify;

    FirebaseAuth mAuth;
    String code, otpid, phone_number;
    Intent intent;
    String userUid;

    public otp_verification() {
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        toolbar.setNavigationIcon(R.drawable.arrow_white);
        setSupportActionBar(toolbar);

        //initialization

        invalid = findViewById(R.id.invalid);
        verify = findViewById(R.id.verify);
        phone_number = getIntent().getExtras().getString("mobile");
        mAuth = FirebaseAuth.getInstance();
        getcode = findViewById(R.id.code);
        invalid.setVisibility(View.INVISIBLE);
        pre = findViewById(R.id.pre_num);
        pre.setText(phone_number);
//refrenece


        //call to function
        initiateOtp();
    }

    private void initiateOtp() {


        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone_number)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        super.onCodeSent(s, forceResendingToken);
                        otpid = s;
                        Log.d(TAG, "onCodeSent:" + s);


                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                })          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        invalid.setVisibility(View.INVISIBLE);
                        Toast.makeText(this, "Signed in Successfully", Toast.LENGTH_SHORT).show();

                        String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                        Log.i(TAG, "phone" + phone);

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
                                                intent = new Intent(otp_verification.this, sub_admin_home.class);
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
                            intent = new Intent(otp_verification.this, admin_home.class);
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
                                        user();

                                    }
                                }
                                else
                                {
                                    user();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });












                    } else {
                        Toast.makeText(getApplicationContext(), "Verification code error", Toast.LENGTH_LONG).show();
                        invalid.setVisibility(View.VISIBLE);
                    }
                });
    }


    public void Verify(View view) {
        code = getcode.getText().toString();
        if (code.length() != 6) {
            invalid.setVisibility(View.VISIBLE);
        } else {
            invalid.setVisibility(View.INVISIBLE);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, code);
            signInWithPhoneAuthCredential(credential);
        }


    }

    public void user()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(userUid);
        //check in all profiles
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    //create new user
                    Toast.makeText(otp_verification.this, "Let's create a profile", Toast.LENGTH_SHORT).show();
                    //intent.putExtra("profile_by", "user");
                    intent = new Intent(otp_verification.this, user_create_profile.class);
                } else {
                    String statusKey = snapshot.child("profile").child("status").getValue(String.class);
                    if (statusKey.equals("Donor")) {

                        intent = new Intent(otp_verification.this, donor_home.class);

                    } else {
                        intent = new Intent(otp_verification.this, needy_home.class);
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
