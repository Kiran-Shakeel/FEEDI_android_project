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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class user_create_profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner gender, city;
    Button done;
    CircleImageView imageView;
    TextView num;
    RadioGroup status;
    EditText first, last, email_, address_, about_, cnic_;
    RadioButton donor, needy;
    private static final int PERMISSION_CODE = 1001;
    private static final int IMAGE_PICk_CODE = 1000;
    String user_status, spinner_city, spinner_gender;
    Intent intent;
    Uri imageuri;
    ProgressBar progressBar;


    //for image
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    //For realtime database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;

    //cardView
    CardView cardView;

    String profile_by;
    String sub_status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_create_profile);

        //initialization
        gender = findViewById(R.id.gender_spinner);
        city = findViewById(R.id.city_spinner);
        imageView = findViewById(R.id.about_img33);
        done = findViewById(R.id.done);
        num = findViewById(R.id.number);
        //  phone = getIntent().getExtras().getString("mobile");
        status = findViewById(R.id.status_id);
        donor = findViewById(R.id.donor);
        needy = findViewById(R.id.needy);
        first = findViewById(R.id.Name);
        last = findViewById(R.id.last_name);
        cnic_ = findViewById(R.id.cnic);
        email_ = findViewById(R.id.email);
        about_ = findViewById(R.id.about_description);
        address_ = findViewById(R.id.address);
        progressBar = findViewById(R.id.progress1);
        cardView = findViewById(R.id.card_);


        sub_status=getIntent().getStringExtra("status");

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference("Profile pics");
        String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        num.setText(phone);
        //function call
        spinner_values();

        //for checking which radio button is slected
        status.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.donor) {
                user_status = "Donor";
            } else {
                user_status = "Needy";
            }
        });

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

        if (TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name) || TextUtils.isEmpty(cnic) ||
                TextUtils.isEmpty(address) || TextUtils.isEmpty(about) ||
                spinner_city.equals("Select City") || spinner_gender.equals("Select Gender") ||
                TextUtils.isEmpty(user_status) || imageuri == null) {
            cardView.setBackground(ContextCompat.getDrawable(this, R.drawable.border_red));
            Toast.makeText(user_create_profile.this, "All feilds must be filled", Toast.LENGTH_SHORT).show();
        } else {

            if (isValidname(first_name)) {
                first.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
                first.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                if (isValidname(last_name)) {
                    last.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);
                    last.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    if (isValidcnic(cnic)) {

                        cnic_.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);



                            if (isValidEmail(email) || email.equals(null)) {

                                email_.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.green), PorterDuff.Mode.SRC_ATOP);

                                progressDialog.show();
                                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final StorageReference uploader = storageReference.child(userUid + "." + fileext(imageuri));

                                uploader.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                progressDialog.dismiss();
                                                String id=userUid;
                                                //check();
                                                profile_by=user_status;




                                                //firebase
                                                String phone_no=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                dbReference = firebaseDatabase.getReference("Users");
                                                all_profiles obj = new all_profiles(uri.toString(), user_status, first_name, last_name, spinner_gender, spinner_city, cnic, email, address, about,id,profile_by,phone_no);
                                                dbReference.child(userUid).child("profile").setValue(obj);
                                                Toast.makeText(user_create_profile.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.VISIBLE);

                                      checkStatus();



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

    private void checkStatus() {
        String userUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference checkReference = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("profile").child("status");

        checkReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String status=snapshot.getValue(String.class);
                    if(status.equals("Donor"))
                    {
                        intent = new Intent(user_create_profile.this, donor_home.class);
                    }
                    else
                    {
                        intent = new Intent(user_create_profile.this, needy_home.class);
                    }
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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
                CropImage.activity().setAspectRatio(1, 1).start(user_create_profile.this);
            }
        } else {
            CropImage.activity().setAspectRatio(1, 1).start(user_create_profile.this);
            // pick_img_from_gallery();
        }

    }

    private void pick_img_from_gallery() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//         intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICk_CODE);

//        intent=new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // pick_img_from_gallery();
                CropImage.activity().setAspectRatio(1, 1).start(user_create_profile.this);
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

    private boolean isValidabout(String s) {
        Pattern p = Pattern.compile("[0-9A-Za-z]*");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private boolean isValidcnic(String s) {
        Pattern p = Pattern.compile("[0-9]{13}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    private void check()
    {
        if(profile_by!=null)
        {
            if(profile_by.equals("user"))
            {
                profile_by=user_status;
            }
            else if(profile_by.equals("subadmin"))
            {
                profile_by="Sub-Admin";
            }
            else if(profile_by.equals("Admin"))
            {
                profile_by="Admin";
                if(sub_status!=null)
                {
                    user_status=sub_status;
                }

            }
        }

    }


}
