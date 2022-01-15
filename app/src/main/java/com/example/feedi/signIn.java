package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signIn extends AppCompatActivity {
    EditText number;
    TextView country_code, invalid;

    String phone;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);


        //initailization
        number = findViewById(R.id.phone);
        country_code = findViewById(R.id.country_code);
        invalid = findViewById(R.id.invalid);
        invalid.setVisibility(View.INVISIBLE);


    }

    public void home(View view) {
        String number_enter = number.getText().toString();
        String country = country_code.getText().toString();
        phone = country + number_enter;

        if (isValid(number_enter) || kashmircheck(number_enter)) {

            invalid.setVisibility(View.INVISIBLE);
            intent = new Intent(signIn.this,otp_verification.class);
            intent.putExtra("mobile",phone);
            number.setText("");
            startActivity(intent);


        } else {
            invalid.setVisibility(View.VISIBLE);
        }


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
