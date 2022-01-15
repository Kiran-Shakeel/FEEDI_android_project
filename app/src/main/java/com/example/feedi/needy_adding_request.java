package com.example.feedi;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class needy_adding_request extends AppCompatActivity {
    TextView first, city_, last, status_,set_loc;
            EditText instruction, members;

    private static final int PERMISSION_CODE = 1000;
    //firebase
    double longi;
    double latitude;
    String req_status;
    String userUid,donor_key;
    DatabaseReference rootRef;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needy_adding_request);




        first = findViewById(R.id.first_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.post_city);
        instruction = findViewById(R.id.post_req_info);
        members = findViewById(R.id.post_food_req_num);
        status_ = findViewById(R.id.post_status);
        set_loc=findViewById(R.id.address);

        //firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(needy_adding_request.this);

        setcontent();
        setLocation();

    }
    private void setLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location != null) {

                        try {
                            Geocoder geocoder = new Geocoder(needy_adding_request.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            longi = addresses.get(0).getLongitude();
                            latitude = addresses.get(0).getLatitude();
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                                String city = addresses.get(0).getLocality();
//                                String state = addresses.get(0).getAdminArea();
//                                String country = addresses.get(0).getCountryName();
//                                String postalCode = addresses.get(0).getPostalCode();
//                                String knownName = addresses.get(0).getFeatureName();
//                                StringBuilder stringBuilder=new StringBuilder("");
//                                for(int i=0;i<complete_address.getMaxAddressLineIndex();i++)
//                                {
//                                    stringBuilder.append(complete_address.getAddressLine(i)).append("\n");
//
//                                }
                            //address=stringBuilder.toString();
                            set_loc.setText(address);





                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                });

            }
            else
            {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permission, PERMISSION_CODE);
            }


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // pick_img_from_gallery();
             setLocation();
            } else {
                Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show();

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void setcontent() {


        //set name and city

        DatabaseReference uidRef = rootRef.child("Users").child(userUid).child("profile");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(needy_adding_request.this, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void post(View view) {

        String ins = instruction.getText().toString();
        String member = members.getText().toString();

        req_status = "new";


        if (TextUtils.isEmpty(ins) || TextUtils.isEmpty(member)) {
            Toast.makeText(needy_adding_request.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        }


        else {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dbReference = firebaseDatabase.getReference("/Needy Requests");
            DatabaseReference req = dbReference.push();
            String req_key = req.getKey();
            donor_key="";

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
            request_info obj = new request_info(req_key, userUid, ins, req_status, member, String.valueOf(longi), String.valueOf(latitude),donor_key);
            req.setValue(obj);
                Toast.makeText(needy_adding_request.this, "Request added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(needy_adding_request.this, needy_home.class);
                startActivity(intent);}
            else
            {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permission, PERMISSION_CODE);
            }



        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setLocation();
        setcontent();
    }
}
