package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

public class sign_up extends AppCompatActivity {

    public sign_up()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
//        ActionBar actionBar = getSupportActionBar();
//
//        actionBar.hide();

    }


    public void sign_up_func(View view) {
        Intent intent = new Intent(sign_up.this, user_create_profile.class);
        startActivity(intent);

    }
    public void sign_in(View view) {
        Intent intent = new Intent(sign_up.this,signIn.class);
        startActivity(intent);

    }

}
