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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class request_list extends AppCompatActivity {

    ListView listView;
    TextView setlabel;

    request_info requestInfo;
    leftover_request_info info;
    //Firebase

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref,check;

    //adapters
    request_list_adapter adapter;
    leftover_request_adapter left_adapter;
    received_request_adapter received;
    String user_key;

    String userUid, requestLeftover, received_requests,new_key;
    String status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_leftover_list);


        setlabel = findViewById(R.id.label1);
        String i = "Requests";
        setlabel.setText(i);

        listView = findViewById(R.id.leftover_list);

        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        received_requests = getIntent().getStringExtra("received_requests_list");

        requestLeftover = getIntent().getStringExtra("request_on_leftover_list");

        if (requestLeftover != null) {
            if (requestLeftover.equals("yes")) {
                setRequestOnLeftoverList();

            }
        } else if (received_requests != null) {
            if (received_requests.equals("yes")) {
                setReceivedRequests();
            }
        } else {
            setList();
        }


    }

    private void setReceivedRequests() {

        ArrayList<leftover_request_info> set_list;

        info = new leftover_request_info();
        set_list = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users").child(userUid).child("Leftover Requests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user_key = snapshot.child("needy_key").getValue(String.class);
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        info = snap.getValue(leftover_request_info.class);
                        if (info != null) {
                            set_list.add(0,info);
                        } else {
                            Toast.makeText(request_list.this, "No request found", Toast.LENGTH_SHORT).show();
                        }


                    }
                    received = new received_request_adapter(request_list.this, R.layout.leftover_list_row, set_list);
                    listView.setAdapter(received);


                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(request_list.this, "Error" + error, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                leftover_request_info Info = (leftover_request_info) parent.getItemAtPosition(position);
                Intent intent = new Intent(request_list.this, donor_checking_request.class);


                intent.putExtra("req_key", Info.getReq_key());
                intent.putExtra("user_key", Info.getNeedy_key());

                intent.putExtra("leftover_request", "yes");

//                    if(!Info.getReq_status().equals("new"))
//                    {
//                        intent.putExtra("accept","yes");
//                        Log.i("TAG", "onItemClick: "+"not null");
//                    }



                startActivity(intent);
                finish();


            }
        });
    }

    private void setRequestOnLeftoverList() {

        ArrayList<leftover_request_info> Mylist;

        info = new leftover_request_info();
        Mylist = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();


        ref = firebaseDatabase.getReference("Users").child(userUid).child("Leftover Requests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        info = snap.getValue(leftover_request_info.class);
                        if (info != null) {
                            Mylist.add(0,info);

                        } else {
                            Toast.makeText(request_list.this, "NO request found", Toast.LENGTH_SHORT).show();
                        }


                    }

                    left_adapter = new leftover_request_adapter(request_list.this, R.layout.leftover_list_row, Mylist);
                    listView.setAdapter(left_adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(request_list.this, "Error" + error, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                leftover_request_info Info = (leftover_request_info) parent.getItemAtPosition(position);


                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Requests_On_Leftovers").child(Info.getReq_key());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            Intent intent = new Intent(request_list.this, needy_leftover_request_description.class);
                            status=snapshot.child("req_status").getValue(String.class);

                            if (status!=null)
                            {
                                if (status.equals("Delivered")) {

                                    intent.putExtra("update", "no");
                                    intent.putExtra("req_key", Info.getReq_key());
                                    intent.putExtra("leftover_request", "yes");
                                    intent.putExtra("key",Info.getKey());
                                }
                                else if (status.equals("Reviewed"))
                                {
                                    intent.putExtra("reviewed","yes");
                                    intent.putExtra("req_key", Info.getReq_key());
                                }

                                else
                                {
                                    intent.putExtra("req_key", Info.getReq_key());
                                    intent.putExtra("leftover_request", "yes");

                                }
                                startActivity(intent);
                                finish();
                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        Toast.makeText(request_list.this, "Error "+ error, Toast.LENGTH_SHORT).show();
                    }
                });

                Log.i("tag", "status "+status);






            }
        });
    }

    public void setList() {

        ArrayList<request_info> list;
        requestInfo = new request_info();
        list = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Needy Requests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        requestInfo = snap.getValue(request_info.class);
                        if (requestInfo != null) {
                            if (requestInfo.getUser_key().equals(userUid)) {
                                list.add(0, requestInfo);
                            }

                        } else {
                            Toast.makeText(request_list.this, "No request found", Toast.LENGTH_SHORT).show();
                        }


                    }

                    adapter = new request_list_adapter(request_list.this, R.layout.leftover_list_row, list);
                    listView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(request_list.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {

            request_info requestInfo = (request_info) parent.getItemAtPosition(position);
            Intent intent = new Intent(request_list.this, needy_request_description.class);
            if(requestInfo.getReq_status()!=null)
            {
                if (requestInfo.getReq_status().equals("Delivered")) {

                    intent.putExtra("update", "no");

                }
                else if (requestInfo.getReq_status().equals("Reviewed"))
                {
                    intent.putExtra("reviewed","yes");



                }
                else if (requestInfo.getReq_status().equals("new"))
                {
                    intent.putExtra("view_profile","no");
                }

            }

            intent.putExtra("req_key", requestInfo.getReq_key());
            startActivity(intent);
            finish();


        });

    }


    @Override
    protected void onResume() {
        super.onResume();


    }
}
