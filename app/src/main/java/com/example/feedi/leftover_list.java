package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
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


public class leftover_list extends AppCompatActivity {

    ListView listView;

    leftover_info leftoverInfo;
    ArrayList<leftover_info> list;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    leftover_list_adapter adapter;
    String userUid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_leftover_list);
        leftoverInfo = new leftover_info();

        listView = findViewById(R.id.leftover_list);
        list = new ArrayList<>();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Leftovers");
        setList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                leftover_info leftoverInfo = (leftover_info) parent.getItemAtPosition(position);

                Intent intent;

                if (leftoverInfo.getStatus().equals("Done")) {
                    intent = new Intent(leftover_list.this, leftover_done.class);
                } else {
                    intent = new Intent(leftover_list.this, leftover_description.class);


                }
                intent.putExtra("key", leftoverInfo.getLeftover_key());
                startActivity(intent);
                finish();


            }
        });


    }

    private void setList() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        leftoverInfo = snap.getValue(leftover_info.class);
                        if (leftoverInfo != null) {
                            if (leftoverInfo.getUser_key().equals(userUid)) {
                                list.add(0, leftoverInfo);
                            }

                        }
                        else
                        {
                            Toast.makeText(leftover_list.this, "No leftover found", Toast.LENGTH_SHORT).show();
                        }


                    }

                    adapter = new leftover_list_adapter(leftover_list.this, R.layout.leftover_list_row, list);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(leftover_list.this, "No Data found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(leftover_list.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();


    }
}
