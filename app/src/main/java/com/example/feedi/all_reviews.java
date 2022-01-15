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
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class all_reviews extends AppCompatActivity {

    ListView listView;
    TextView avg_rate;
    review_info info;
    ArrayList<review_info> list;
    review_adapter adapter;
    String userUid, req_key;
    int count;
    float val;
    String check,user_key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_reviews);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_sub);
        toolbar.setNavigationIcon(R.drawable.arrow_white);
        setSupportActionBar(toolbar);

        info = new review_info();
        avg_rate = findViewById(R.id.avg_rate);

        listView = findViewById(R.id.review_list);
        list = new ArrayList<>();

        check=getIntent().getStringExtra("check");
        user_key=getIntent().getStringExtra("user_key");

        if (check!=null)
        {
            if (check.equals("yes"))
            {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(user_key).child("added_reviews");
                val = 0;
                count = 0;

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {


                                info = snap.getValue(review_info.class);
                                list.add(0, info);
                                count = count + 1;

                                val += info.getRating();
                            }
                            adapter = new review_adapter(all_reviews.this, R.layout.review_row, list);
                            listView.setAdapter(adapter);
                            String result = Float.toString(val / count);
                            avg_rate.setText(result);

                        }userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(all_reviews.this, "Error" + error, Toast.LENGTH_SHORT).show();
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        review_info info = (review_info) parent.getItemAtPosition(position);
                        String add = info.getAdded_by().toString();
                        Intent intent = new Intent(all_reviews.this, user_public_profile.class);
                        intent.putExtra("user_key", add);
                        startActivity(intent);


                    }
                });
            }
        }
        else
        {
            userUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("added_reviews");
            val = 0;
            count = 0;

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snap : snapshot.getChildren()) {


                            info = snap.getValue(review_info.class);
                            list.add(0, info);
                            count = count + 1;

                            val += info.getRating();
                        }
                        adapter = new review_adapter(all_reviews.this, R.layout.review_row, list);
                        listView.setAdapter(adapter);
                        String result = Float.toString(val / count);
                        avg_rate.setText(result);

                    }userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(all_reviews.this, "Error" + error, Toast.LENGTH_SHORT).show();
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    review_info info = (review_info) parent.getItemAtPosition(position);
                    String add = info.getAdded_by().toString();
                    Intent intent = new Intent(all_reviews.this, user_public_profile.class);
                    intent.putExtra("user_key", add);
                    startActivity(intent);


                }
            });
        }




    }
}
