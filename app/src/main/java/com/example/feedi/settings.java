package com.example.feedi;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class settings extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        toolbar.setNavigationIcon(R.drawable.arrow_white);
        setSupportActionBar(toolbar);

    }
}
