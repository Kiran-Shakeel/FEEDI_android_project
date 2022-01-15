package com.example.feedi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

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

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class admin_create_profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner gender, city;
    Button done;
    CircleImageView imageView;


    EditText first, last, email_, address_, about_, cnic_,phone;

    private static final int PERMISSION_CODE = 1001;
    private static final int IMAGE_PICk_CODE = 1000;
    String spinner_city, spinner_gender;
    Intent intent;
    Uri imageuri;
    ProgressBar progressBar;


    //for image
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    //For realtime database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;



    String user_status,profile_by,phone_no;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_create_profile);

        //initialization
        gender = findViewById(R.id.gender_spinner);
        city = findViewById(R.id.city_spinner);
        imageView = findViewById(R.id.about_img33);
        done = findViewById(R.id.done);

        first = findViewById(R.id.Name);
        last = findViewById(R.id.last_name);
        cnic_ = findViewById(R.id.cnic);
        email_ = findViewById(R.id.email);
        about_ = findViewById(R.id.about_description);
        address_ = findViewById(R.id.address);
        progressBar = findViewById(R.id.progress1);

        phone=findViewById(R.id.phone);




        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference("SubAdmin Pics");

        //function call
        spinner_values();



        //Spinner
     city.setOnItemSelectedListener(this);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_gender = gender.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }



    private void spinner_values() {
        String[] city_names = new String[]{"Select City", "Gujranwala", "Lahore", "Multan"};
        String[] gender_list = new String[]{"Select Gender", "Male", "Female", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, city_names);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gender_list);

        city.setAdapter(adapter);
        gender.setAdapter(adapter2);


    }


    //when button done clicked
    public void done(View view) {
        uploadData();

    }

    private void uploadData() {
        //Progress dialogue
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progressing");


        String first_name = first.getText().toString();
        String last_name = last.getText().toString();
        String cnic = cnic_.getText().toString();
        String email = email_.getText().toString();
        String address = address_.getText().toString();
        String about = about_.getText().toString();
        phone_no=phone.getText().toString();

        if (TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name) || TextUtils.isEmpty(cnic) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(about) ||
                spinner_city.equals("Select City") || spinner_gender.equals("Select Gender") ||
                 imageuri == null || TextUtils.isEmpty(phone_no)) {

            Toast.makeText(admin_create_profile.this, "All feilds must be filled", Toast.LENGTH_SHORT).show();
        } else {

            if (isValidname(first_name)) {
                first.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
                first.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                if (isValidname(last_name)) {
                    last.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
                    last.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    if (isValidcnic(cnic)) {

                        cnic_.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);

                        if (isValid(phone_no) || kashmircheck(phone_no)) {
                            phone_no="+92"+phone_no;
                          // phone.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
                            if (isValidEmail(email) || email.equals(null)) {

                                email_.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);

                                progressDialog.show();
                                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final StorageReference uploader = storageReference.child(phone_no + "." + fileext(imageuri));

                                uploader.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                progressDialog.dismiss();
                                                String id=userUid;
                                                user_status="Sub-Admin";
                                                profile_by="Admin";

                                                //firebase

                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                dbReference = firebaseDatabase.getReference("Sub-Admins");
                                                all_profiles obj = new all_profiles(uri.toString(), user_status, first_name, last_name, spinner_gender, spinner_city, cnic, email, address, about,id,profile_by,phone_no);
                                                dbReference.child(phone_no).child("profile").setValue(obj);

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admin").child("Added Sub-Admins");
                                                HashMap<String, Object> hashMap = new HashMap<>();
                                                hashMap.put("id", phone_no);
                                                reference.setValue(hashMap);

                                                Toast.makeText(admin_create_profile.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.VISIBLE);
                                                finish();


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
                                email_.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
                            }
                        } else {
                            phone.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
                        }
                    } else {
                        cnic_.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
                    }

                } else {
                    last.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);
                }

            } else {
                first.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.red), PorterDuff.Mode.SRC_ATOP);

            }
        }


    }




    //Image pick
    public void select_img(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission not granted
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);

            } else {
                //permission granted
                // pick_img_from_gallery();
                CropImage.activity().setAspectRatio(1, 1).start(admin_create_profile.this);
            }
        } else {
            CropImage.activity().setAspectRatio(1, 1).start(admin_create_profile.this);
            // pick_img_from_gallery();
        }

    }
    //city spinner item selection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (city.getSelectedItem() != null) {
            spinner_city = city.getSelectedItem().toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // pick_img_from_gallery();
                CropImage.activity().setAspectRatio(1, 1).start(admin_create_profile.this);
            } else {
                Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageuri = result.getUri();
                Picasso.get().load(imageuri).into(imageView);
                //imageView.setImageURI(uri);
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();

            }

        }


    }

    //for image extension
    private String fileext(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    private boolean isValidEmail(String s) {
        Pattern p = Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private boolean isValidname(String s) {
        Pattern p = Pattern.compile("[a-zA-Z]*");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }


    private boolean isValidcnic(String s) {
        Pattern p = Pattern.compile("[0-9]{13}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private boolean kashmircheck(String s) {
        Pattern p = Pattern.compile("(3)?[5][5][0-9]{7}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private boolean isValid(String s) {
        Pattern p = Pattern.compile("(3)?[0-4][0-9]{8}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));

    }




}
