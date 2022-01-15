package com.example.feedi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class accepted_adapter extends ArrayAdapter<accepted_requests_donor_info> {
    Context mcontext;
    int mresourse;


    public accepted_adapter(@NonNull Context context, int resource, ArrayList<accepted_requests_donor_info> list) {
        super(context, resource, list);
        mcontext = context;
        mresourse = resource;


    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String key = getItem(position).getKey();
        accepted_requests_donor_info info = new accepted_requests_donor_info(key);
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresourse, parent, false);


        ImageView imageView = convertView.findViewById(R.id.image);
        TextView get_name = convertView.findViewById(R.id.first_name);
        TextView get_last = convertView.findViewById(R.id.last);
        TextView get_ins = convertView.findViewById(R.id.info);
        TextView get_status = convertView.findViewById(R.id.status);

        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("Accepted requests").child(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String req_key = snapshot.child("req_key").getValue(String.class);
                    String status = snapshot.child("req_status").getValue(String.class);
                    get_status.setText(status);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Needy Requests").child(req_key);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String ins = snapshot.child("ins").getValue(String.class);
                                get_ins.setText(ins);
                                String needy_key = snapshot.child("user_key").getValue(String.class);



                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(needy_key).child("profile");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String first = snapshot.child("first_name").getValue(String.class);
                                            String last = snapshot.child("last_name").getValue(String.class);
                                            String pic = snapshot.child("profile_pic").getValue(String.class);


                                            get_name.setText(first);
                                            get_last.setText(last);

                                            Picasso.get().load(pic).into(imageView);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        Toast.makeText(mcontext, "Error" + error, Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } else {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Requests_On_Leftovers").child(req_key);
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String ins = snapshot.child("ins").getValue(String.class);
                                            get_ins.setText(ins);
                                            String needy_key = snapshot.child("user_key").getValue(String.class);



                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(needy_key).child("profile");
                                            reference.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        String first = snapshot.child("first_name").getValue(String.class);
                                                        String last = snapshot.child("last_name").getValue(String.class);
                                                        String pic = snapshot.child("profile_pic").getValue(String.class);


                                                        get_name.setText(first);
                                                        get_last.setText(last);

                                                        Picasso.get().load(pic).into(imageView);
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                    Toast.makeText(mcontext, "Error" + error, Toast.LENGTH_SHORT).show();

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        Toast.makeText(mcontext, "Error" + error, Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                            Toast.makeText(mcontext, "Error" + error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(mcontext, "Error" + error, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;

    }
}
