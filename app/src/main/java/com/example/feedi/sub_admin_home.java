package com.example.feedi;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class sub_admin_home extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_admin_home);
    }

    public void sign_out1(View view)
    {
        FirebaseAuth.getInstance().signOut();
        finish();

    }
}
