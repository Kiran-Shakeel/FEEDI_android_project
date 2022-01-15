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

public class leftover_list_needy_home extends AppCompatActivity {
    ListView listView;
    TextView setlabel;
    leftover_info info;
    ArrayList<leftover_info> list;


    //Firebase

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    leftover_home_adapter adapter;
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


        info = new leftover_info();

        setlabel = findViewById(R.id.label1);
        String i = "Leftovers";
        setlabel.setText(i);


        listView = findViewById(R.id.leftover_list);
        list = new ArrayList<>();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Leftovers");


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


                        info = snap.getValue(leftover_info.class);

                        if (info != null) {

                            longi = Double.parseDouble(info.getLongitude());
                            lat = Double.parseDouble(info.getLatitude());
                            loc_2 = new LatLng(lat, longi);

                            distance = SphericalUtil.computeDistanceBetween(loc, loc_2);
                            distance = distance / 1000;

                            // String dis=String.format("%.2f", distance) + "km";

                            if (info.getStatus().equals("new") && distance < 70) {
                                list.add(0, info);
                            }
                        }


                    }

                    adapter = new leftover_home_adapter(leftover_list_needy_home.this, R.layout.home_adapter, list);
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

              leftover_info info = (leftover_info) parent.getItemAtPosition(position);


                Intent intent = new Intent(leftover_list_needy_home.this, needy_checking_leftover.class);
                intent.putExtra("leftover_key", info.getLeftover_key());
                intent.putExtra("user_key", info.getUser_key());
                startActivity(intent);
                finish();


            }
        });
    }


    }

