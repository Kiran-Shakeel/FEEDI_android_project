package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class delivery_option extends AppCompatActivity {
    RadioGroup deliver;
    String selection;
    Intent intent;
    String leftover_request;
String req_key;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_delivery_options);

        deliver=findViewById(R.id.deliver_option);
        req_key=getIntent().getStringExtra("req_key");

        leftover_request=getIntent().getStringExtra("leftover_request");

        deliver.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.deliver_by_me)
                {
                    if(leftover_request!=null)
                    {
                        if(leftover_request.equals("yes"))
                        {
                            intent.putExtra("leftover_request","yes");
                        }
                    }

                    intent=new Intent(delivery_option.this,address_info.class);
                    intent.putExtra("req_key",req_key);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    selection="pick up by needy";
                }
            }
        });
    }
}
