package com.example.feedi;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class address_info extends AppCompatActivity {
    String req_key;
    double latitude,longitude;
    TextView address_text;
    accepted_requests_donor_info info;
    String address;
    String leftover_request;
    String addressing;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_info);

        address_text=findViewById(R.id.deliver_by_me);

        req_key=getIntent().getStringExtra("req_key");
        leftover_request=getIntent().getStringExtra("leftover_request");

        if(leftover_request!=null)
        {
            if (leftover_request.equals("yes"))
            {
                setting();
            }
        }
        else
        {
            setAddress();
        }






    }

    private void setting() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Requests_On_Leftovers").child(req_key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    addressing=snapshot.child("address").getValue(String.class);
                    address_text.setText(addressing);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(address_info.this, "error"+error, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setAddress()
    {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Needy Requests").child(req_key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String lat=snapshot.child("latitude").getValue(String.class);
                    String longi=snapshot.child("longitude").getValue(String.class);


                    latitude=Double.parseDouble(lat);
                    longitude=Double.parseDouble(longi);

                    Geocoder geocoder = new Geocoder(address_info.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latitude,longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address = addresses.get(0).getAddressLine(0);
                    address_text.setText(address);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(address_info.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void okay(View view)
    {
        String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("Accepted requests").push();
        String key=ref.getKey();
        info=new accepted_requests_donor_info(req_key,"Accepted",address,key);
        ref.setValue(info);
        finish();
    }
}
