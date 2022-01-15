package com.example.feedi;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
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

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class update_leftover extends AppCompatActivity {
    Spinner delivery;
    Intent intent;
    ImageView img;
    Uri imageuri;

    DatePickerDialog picker;
    TextView first, city_, last;
    EditText instruction, expire, members;
    String longi;
    String latitude;

    String path, check, leftover_key;
    String set_member, set_delivery, set_ins, set_date;
    //Firebase
    String userUid;
    DatabaseReference rootRef, leftover_reference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    private static final int IMAGE_PICk_CODE = 100;
    private static final int PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_adding_leftover);

        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        toolbar.setNavigationIcon(R.drawable.arrow_white);
        toolbar.setBackgroundResource(R.color.black);
        setSupportActionBar(toolbar);

        delivery = findViewById(R.id.delivery);
        img = findViewById(R.id.food_img);
        first = findViewById(R.id.person_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.person_city);
        instruction = findViewById(R.id.ins);
        members = findViewById(R.id.available);
        expire = findViewById(R.id.date);

        //firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        leftover_key = getIntent().getStringExtra("key");

        storageReference = firebaseStorage.getReference("Leftovers");

        expire.setInputType(InputType.TYPE_NULL);


        //expirey date
        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();

                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(update_leftover.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                expire.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                picker.show();
                picker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        //autoset


        setcontent();
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

                String first_name = snapshot.child("first_name").getValue().toString();
                String last_name = snapshot.child("last_name").getValue().toString();
                String city = snapshot.child("city").getValue().toString();


                first.setText(first_name);
                last.setText(last_name);
                city_.setText(city);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(update_leftover.this, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);

        //setting rest of data
        leftover_reference = FirebaseDatabase.getInstance().getReference("Leftovers").child(leftover_key);
        leftover_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    set_member = snapshot.child("members").getValue().toString();
                    set_ins = snapshot.child("ins").getValue().toString();
                    set_date = snapshot.child("expirey_date").getValue().toString();
                    set_delivery = snapshot.child("deliver_option").getValue().toString();
                    path = snapshot.child("image").getValue().toString();


                    members.setText(set_member);
                    instruction.setText(set_ins);
                    expire.setText(set_date);

                    Picasso.get().load(path).into(img);


                    if (set_delivery != null) {
                        int spinnerPosition = adapter.getPosition(set_delivery);
                        delivery.setSelection(spinnerPosition);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }


    public void home(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating");

        String ins = instruction.getText().toString();
        String exp = expire.getText().toString();
        String member = members.getText().toString();
        String deliver_option = delivery.getSelectedItem().toString();
        longi = "";
        latitude = "";

        if (TextUtils.isEmpty(ins) || TextUtils.isEmpty(exp) || TextUtils.isEmpty(member) || deliver_option.equals("Delivery Option")) {
            Toast.makeText(update_leftover.this, "All feilds must be filled", Toast.LENGTH_SHORT).show();
        } else {

            Map<String, Object> updatedValues = new HashMap<>();
            updatedValues.put("/ins", ins);
            updatedValues.put("/expirey_date", exp);
            updatedValues.put("/members", member);
            updatedValues.put("/deliver_option", deliver_option);

            if (imageuri != null) {

                progressDialog.show();
                final StorageReference uploader = storageReference.child(leftover_key + "." + fileext(imageuri));
                uploader.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                leftover_reference.child("image").setValue(uri.toString());


                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Setting profile : " + (int) percent + "%");


                    }
                });

            }
            Toast.makeText(this, "leftover Updated successfully", Toast.LENGTH_SHORT).show();
            leftover_reference.updateChildren(updatedValues);



        }
    }

        public void add_image (View view){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission not granted
                    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);

                } else {
                    //permission granted
                    //pick_img_from_gallery();
                    CropImage.activity().setAspectRatio(2, 1).start(update_leftover.this);
                }
            } else {
                // pick_img_from_gallery();
                CropImage.activity().setAspectRatio(2, 1).start(update_leftover.this);
            }

        }

        private void pick_img_from_gallery () {

            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //  intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICk_CODE);
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            if (requestCode == PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick_img_from_gallery();
                    CropImage.activity().setAspectRatio(2, 1).start(update_leftover.this);
                } else {
                    Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
                }
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageuri = result.getUri();
                Picasso.get().load(imageuri).into(img);


            }


        }

        private String fileext (Uri uri){
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));


    }

}