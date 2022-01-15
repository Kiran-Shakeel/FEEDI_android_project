package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class admin_home extends AppCompatActivity {

    DatabaseReference reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

    }

    public void addSubadmin(View view)
    {
        Intent intent=new Intent(admin_home.this,admin_create_profile.class);
        intent.putExtra("profile_by","Admin");
        intent.putExtra("status","sub-admin");
        startActivity(intent);
    }

    public void all_profiles(View view)
    {

    }

    public void feedbacks(View view)
    {
Intent intent=new Intent(admin_home.this,feedback_list.class);
startActivity(intent);
    }

    public void Statistics(View view)
    {

    }
    public void sign_out(View view)
    {
        FirebaseAuth.getInstance().signOut();
        finish();

    }

}
