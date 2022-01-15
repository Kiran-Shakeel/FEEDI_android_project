package com.example.feedi;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class donor_adding_leftover extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    private static final int LOCATION_PERMISSION_CODE = 2000;
    Spinner delivery;
    Intent intent;
    ImageView img;
    Uri imageuri;

    DatePickerDialog picker;
    TextView first, city_, last, set_loc;
    EditText instruction, expire, members;
    private static final int IMAGE_PICk_CODE = 100;

    Double longi;
    Double latitude;
    String status;
    String userUid;
    DatabaseReference rootRef;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_adding_leftover);

        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        toolbar.setNavigationIcon(R.drawable.arrow_white);
        toolbar.setBackgroundResource(R.color.black);
        setSupportActionBar(toolbar);

        //initialization
        delivery = findViewById(R.id.delivery);
        img = findViewById(R.id.food_img);
        first = findViewById(R.id.person_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.person_city);
        instruction = findViewById(R.id.ins);
        members = findViewById(R.id.available);
        set_loc = findViewById(R.id.address);

        expire = findViewById(R.id.date);


        //firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference("Leftovers");

        expire.setInputType(InputType.TYPE_NULL);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(donor_adding_leftover.this);

        //expirey date
        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();

                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                picker = new DatePickerDialog(donor_adding_leftover.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                expire.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                picker.show();
                picker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });


        //autoset


        setcontent();
        setLocation();


    }

    @Override
    protected void onStart() {
        super.onStart();

        setLocation();
        setcontent();
    }

    private void setLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {

                            try {
                                Geocoder geocoder = new Geocoder(donor_adding_leftover.this, Locale.getDefault());
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

                    }
                });

            } else {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permission, LOCATION_PERMISSION_CODE);
            }


        }

    }


    private void setcontent() {
        String[] options = {"Delivery Option", "pick by needy", "deliver by me"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        delivery.setAdapter(adapter);

        //set name and city

        DatabaseReference uidRef = rootRef.child("Users").child(userUid).child("profile");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String first_name = snapshot.child("first_name").getValue(String.class);
                String last_name = snapshot.child("last_name").getValue(String.class);
                String city = snapshot.child("city").getValue(String.class);


                first.setText(first_name);
                last.setText(last_name);
                city_.setText(city);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(donor_adding_leftover.this, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void home(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        String ins = instruction.getText().toString();
        String exp = expire.getText().toString();
        String member = members.getText().toString();
        String deliver_option = delivery.getSelectedItem().toString();
        status = "new";


        if (TextUtils.isEmpty(ins) || TextUtils.isEmpty(exp) || TextUtils.isEmpty(member) || deliver_option.equals("Delivery Option") || imageuri == null) {
            Toast.makeText(donor_adding_leftover.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
        } else {


            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dbReference = firebaseDatabase.getReference("/Leftovers");
            DatabaseReference left = dbReference.push();
            String leftover_key = left.getKey();
            //       Log.i("uid", leftover_key);

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                progressDialog.show();

                final StorageReference uploader = storageReference.child(leftover_key + "." + fileext(imageuri));
                uploader.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                //firebase

                                leftover_info obj = new leftover_info(leftover_key, userUid, uri.toString(), ins, exp, deliver_option, status, member, String.valueOf(longi), String.valueOf(latitude));
                                left.setValue(obj);
                                Toast.makeText(donor_adding_leftover.this, "leftover added successfully", Toast.LENGTH_SHORT).show();

                                intent = new Intent(donor_adding_leftover.this, donor_home.class);
                                startActivity(intent);


                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("uploaded: " + (int) percent + "%");


                    }
                });


            } else {
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permission, LOCATION_PERMISSION_CODE);
            }

        }

    }

    public void add_image(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission not granted
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);

            } else {
                //permission granted
                //pick_img_from_gallery();
                CropImage.activity().setAspectRatio(2, 1).start(donor_adding_leftover.this);
            }
        } else {
            // pick_img_from_gallery();
            CropImage.activity().setAspectRatio(2, 1).start(donor_adding_leftover.this);
        }

    }

    private void pick_img_from_gallery() {

        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICk_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // pick_img_from_gallery();
                CropImage.activity().setAspectRatio(2, 1).start(donor_adding_leftover.this);
            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // pick_img_from_gallery();
                setLocation();
            } else {
                Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show();

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();
            Picasso.get().load(imageuri).into(img);


        }


    }

    private String fileext(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

