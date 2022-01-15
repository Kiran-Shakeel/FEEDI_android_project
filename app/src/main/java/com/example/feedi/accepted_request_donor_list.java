package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class accepted_request_donor_list extends AppCompatActivity {

    ListView listView;
    TextView setlabel;
    accepted_requests_donor_info info;
    ArrayList <accepted_requests_donor_info> list;
   accepted_adapter adapter;

   String userUid,req_key;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_leftover_list);

        info = new accepted_requests_donor_info();

        setlabel = findViewById(R.id.label1);
        String i = "Accepted Requests";
        setlabel.setText(i);
        listView = findViewById(R.id.leftover_list);
        list = new ArrayList<>();

        userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("Accepted requests");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for (DataSnapshot snap:snapshot.getChildren())
                    {
                        info=snap.getValue(accepted_requests_donor_info.class);
                        list.add(0,info);
                    }
                    adapter = new accepted_adapter(accepted_request_donor_list.this, R.layout.home_adapter, list);
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(accepted_request_donor_list.this, "Error" + error, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                accepted_requests_donor_info info = (accepted_requests_donor_info) parent.getItemAtPosition(position);

                Intent intent;
                if (info.getReq_status().equals("Delivered"))
                {
                    intent = new Intent(accepted_request_donor_list.this, accepted_request_donor_description.class);
                    intent.putExtra("req_key_for_review",info.getReq_key());

                }
                else if (info.getReq_status().equals("Reviewed"))
                {
                    intent = new Intent(accepted_request_donor_list.this, accepted_request_donor_description.class);
                    intent.putExtra("reviewed","yes");
                }

                else {
                    intent = new Intent(accepted_request_donor_list.this, accepted_requests_donor.class);
                }
                intent.putExtra("key", info.getKey());


                startActivity(intent);
                finish();


            }
        });



    }
}
