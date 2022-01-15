package com.example.feedi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class leftover_description extends AppCompatActivity {

    TextView delivery, member, ins, date, first, last, city_;
    ImageView pic;
    String path;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference, leftover_reference, ref;
    String userUid, leftover_key, view;
    String set_member, set_delivery, set_ins, set_date;

    Button del, done;
    ImageView edit;

    StorageReference storage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leftover_description);

        pic = findViewById(R.id.image1);
        delivery = findViewById(R.id.delivery);
        member = findViewById(R.id.available);
        ins = findViewById(R.id.ins_val);
        date = findViewById(R.id.date);
        first = findViewById(R.id.person_name);
        last = findViewById(R.id.last_name);
        city_ = findViewById(R.id.person_city);

        del = findViewById(R.id.del);
        done = findViewById(R.id.done);
        edit = findViewById(R.id.leftover_edit);

        view = getIntent().getStringExtra("view");

        if (view != null) {
            if (view.equals("yes")) {
                del.setVisibility(View.INVISIBLE);
                done.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);
            }
        }
        leftover_key = getIntent().getStringExtra("key");
        setleftover();


    }

    private void setleftover() {
        //Firebase
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users").child(userUid).child("profile");
        leftover_reference = firebaseDatabase.getReference("Leftovers").child(leftover_key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String first_name = snapshot.child("first_name").getValue(String.class);
                String last_name = snapshot.child("last_name").getValue(String.class);
                String city = snapshot.child("city").getValue(String.class);


                first.setText(first_name);
                last.setText(last_name);
                city_.setText(city);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        leftover_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    set_member = snapshot.child("members").getValue(String.class);
                    set_ins = snapshot.child("ins").getValue(String.class);
                    set_date = snapshot.child("expirey_date").getValue(String.class);
                    set_delivery = snapshot.child("deliver_option").getValue(String.class);
                    path = snapshot.child("image").getValue(String.class);

                    delivery.setText(set_delivery);
                    member.setText(set_member);
                    ins.setText(set_ins);
                    date.setText(set_date);


                    Picasso.get().load(path).into(pic);
                }
                else
                {
                    Toast.makeText(leftover_description.this, "leftover not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(leftover_description.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void done(View view) {

        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("/status", "Done");
        leftover_reference.updateChildren(updatedValues);
        finish();

    }

    public void del(View view) {

        storage = FirebaseStorage.getInstance().getReferenceFromUrl(path);
        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ref = FirebaseDatabase.getInstance().getReference("Leftovers").child(leftover_key);
                ref.removeValue();

                Toast.makeText(leftover_description.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(leftover_description.this, "Error" + e, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void edit(View view) {
        Intent intent = new Intent(leftover_description.this, update_leftover.class);
        intent.putExtra("key", leftover_key);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setleftover();

    }
}
