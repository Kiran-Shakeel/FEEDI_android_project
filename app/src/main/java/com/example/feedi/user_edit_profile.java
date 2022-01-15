package com.example.feedi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_edit_profile extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    Intent intent;
    CircleImageView pic;
    private static final int IMAGE_PICk_CODE = 100;
    EditText first, last, address_, about_, email_;
    Uri imageuri;

    String first_name, last_name, email, address, about, link;

    //Database
    String userUid;
    DatabaseReference rootRef;
    DatabaseReference uidRef;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        toolbar.setNavigationIcon(R.drawable.arrow_white);
        setSupportActionBar(toolbar);

        storageReference = FirebaseStorage.getInstance().getReference("Profile pics");
        //initialization
        pic = findViewById(R.id.update_img);
        first = findViewById(R.id.edit_name);
        last = findViewById(R.id.edit_last);
        email_ = findViewById(R.id.edit_email);
        address_ = findViewById(R.id.edit_address);
        about_ = findViewById(R.id.about_description);


        //call to function
        setProfile();

    }

    private void setProfile() {
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        uidRef = rootRef.child("Users").child(userUid).child("profile");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                first_name = snapshot.child("first_name").getValue(String.class);
                last_name = snapshot.child("last_name").getValue(String.class);
                email = snapshot.child("email").getValue(String.class);
                about = snapshot.child("about").getValue(String.class);
                address = snapshot.child("address").getValue(String.class);
                link = snapshot.child("profile_pic").getValue(String.class);


                first.setText(first_name);
                last.setText(last_name);
                email_.setText(email);
                address_.setText(address);
                about_.setText(about);

                Picasso.get().load(link).into(pic);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(user_edit_profile.this, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);


    }


    public void update(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progressing");
        //getting updated values

        first_name = first.getText().toString();
        last_name = last.getText().toString();
        email = email_.getText().toString();
        address = address_.getText().toString();
        about = about_.getText().toString();

        if (TextUtils.isEmpty(first_name) || TextUtils.isEmpty(last_name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(about)) {
            Toast.makeText(this, "All feilds must be filled", Toast.LENGTH_SHORT).show();
        } else {
            if (isValidname(first_name) && isValidname(last_name) && isValidabout(about)) {
                if (isValidEmail(email) || email.equals("")) {
                    Map<String, Object> updatedValues = new HashMap<>();
                    updatedValues.put("/first_name", first_name);
                    updatedValues.put("/last_name", last_name);
                    updatedValues.put("/email", email);
                    updatedValues.put("/address", address);
                    updatedValues.put("/about", about);

                    if (imageuri != null) {

                        progressDialog.show();
                        final StorageReference uploader = storageReference.child(userUid + "." + fileext(imageuri));
                        uploader.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressDialog.dismiss();
                                        uidRef.child("profile_pic").setValue(uri.toString());


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
                    Toast.makeText(this, "Profile Updated successfully", Toast.LENGTH_SHORT).show();
                    uidRef.updateChildren(updatedValues);



                } else {
                    Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill all entries correctly", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void update_img(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission not granted
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);

            } else {
                CropImage.activity().setAspectRatio(1, 1).start(user_edit_profile.this);
                //permission granted
                // pick_img_from_gallery();
            }
        } else {
            CropImage.activity().setAspectRatio(1, 1).start(user_edit_profile.this);
            // pick_img_from_gallery();
        }
    }

    private void pick_img_from_gallery() {
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //  intent.setType("image/*");
        CropImage.activity().setAspectRatio(1, 1).start(user_edit_profile.this);
        startActivityForResult(intent, IMAGE_PICk_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //pick_img_from_gallery();
                CropImage.activity().setAspectRatio(1, 1).start(user_edit_profile.this);
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
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();
            Picasso.get().load(imageuri).into(pic);
        }


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
        Pattern p = Pattern.compile("^[0-9A-Za-z]*");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    //for image extension
    private String fileext(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}
