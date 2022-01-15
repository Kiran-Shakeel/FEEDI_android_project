package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class chat_list extends AppCompatActivity {
    ListView listView;

    chat_info info;
    all_profiles user;
    ArrayList<String> list;

    ArrayList<String> us_list;

    ArrayList<all_profiles> user_list;

    //Firebase
    String key;

    chat_list_adapter adapter;
    String userUid;
    DatabaseReference ref, reference, refe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);
        listView = findViewById(R.id.chat_list);
        user = new all_profiles();
        info = new chat_info();

        us_list = new ArrayList<>();

        list = new ArrayList<>();
        user_list = new ArrayList<>();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        setList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                all_profiles info = (all_profiles) parent.getItemAtPosition(position);
                String user_key=info.getId();
                Log.i("TAG", "user_key"+user_key);
                Intent intent=new Intent(chat_list.this,activity_message.class);
                intent.putExtra("user_key",user_key);
                startActivity(intent);
            }
        });


    }

    private void setList() {

        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        info = snap.getValue(chat_info.class);
                        if (info != null) {

                            if (info.getSender().equals(userUid)) {
                                list.add(0, info.getReceiver());

                            } else if (info.getReceiver().equals(userUid)) {
                                list.add(0, info.getSender());
                            }


                        } else {
                            Toast.makeText(chat_list.this, "no chat found", Toast.LENGTH_SHORT).show();
                        }


                    }

                    readmessage();

                } else {
                    Toast.makeText(chat_list.this, "No Data found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(chat_list.this, "Nothing found", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void readmessage() {

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        key = snap.getKey();
                        for (String id : list) {
                            if (key.equals(id)) {
                                refe = FirebaseDatabase.getInstance().getReference("Users").child(key).child("profile");
                                refe.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        if (snapshot.exists()) {

                                            user = snapshot.getValue(all_profiles.class);

                                            if (user_list.size() != 0) {
                                                for (all_profiles user1 : user_list) {
                                                    if (!user.getId().equals(user1.getId())) {

                                                        user_list.add(user);
                                                    }
                                                }
                                            } else {
                                                user_list.add(user);
                                            }


                                        }
                                        adapter = new chat_list_adapter(chat_list.this, R.layout.home_adapter, user_list);
                                        listView.setAdapter(adapter);

                                    }


                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });


                            }


                        }
                    }


                } else {
                    Toast.makeText(chat_list.this, "snapshot not exist", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}
