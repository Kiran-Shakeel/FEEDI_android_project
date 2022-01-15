package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class needy_request_list extends AppCompatActivity {
    ListView listView;
    TextView setlabel;
    request_info requestInfo;
    ArrayList<request_info> list;


    //Firebase

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    home_adapter adapter;
    String userUid;

    double Latitude;
    double Longitude;
    LatLng loc, loc_2;
    Double lat, longi;
    Double distance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_leftover_list);
        requestInfo = new request_info();

        setlabel = findViewById(R.id.label1);
        String i = "Requests";
        setlabel.setText(i);


        listView = findViewById(R.id.leftover_list);
        list = new ArrayList<>();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Needy Requests");


        Bundle b = getIntent().getExtras();
        Longitude = b.getDouble("longitude");
        Latitude = b.getDouble("latitude");

        loc = new LatLng(Latitude, Longitude);
        //setList();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap : snapshot.getChildren()) {


                        requestInfo = snap.getValue(request_info.class);


                        if (requestInfo != null) {

                            longi = Double.parseDouble(requestInfo.getLongitude());
                            lat = Double.parseDouble(requestInfo.getLatitude());
                            loc_2 = new LatLng(lat, longi);

                            distance = SphericalUtil.computeDistanceBetween(loc, loc_2);
                            distance = distance / 1000;

                            // String dis=String.format("%.2f", distance) + "km";

                            if (requestInfo.getReq_status().equals("new") && distance < 70) {
                                list.add(0, requestInfo);
                            }
                        }

                    }

                    adapter = new home_adapter(needy_request_list.this, R.layout.home_adapter, list);
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {


            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                request_info requestInfo = (request_info) parent.getItemAtPosition(position);


                Intent intent = new Intent(needy_request_list.this, donor_checking_request.class);
                intent.putExtra("req_key", requestInfo.getReq_key());
                intent.putExtra("user_key", requestInfo.getUser_key());
                startActivity(intent);
                finish();


            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();


    }
}
