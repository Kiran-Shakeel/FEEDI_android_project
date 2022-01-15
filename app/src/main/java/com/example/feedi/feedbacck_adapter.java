package com.example.feedi;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class feedbacck_adapter extends ArrayAdapter<feedback_model> {
    Context mcontext;
    int mresourse;

    public feedbacck_adapter(@NonNull Context context, int resource, ArrayList<feedback_model> list) {
        super(context, resource, list);
        mcontext = context;
        mresourse = resource;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        String comment = getItem(position).getFeedback();
        String key=getItem(position).getId();
        Log.i("TAG", "key"+key);

       feedback_model model=new feedback_model(comment,key);
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresourse, parent, false);


        ImageView imageView = convertView.findViewById(R.id.profile);
        ImageView star = convertView.findViewById(R.id.star);
        TextView get_name = convertView.findViewById(R.id.first);
        TextView get_last = convertView.findViewById(R.id.last);
        TextView commenting = convertView.findViewById(R.id.comment);
        TextView get_status = convertView.findViewById(R.id.status);
        TextView rate=convertView.findViewById(R.id.rate);
        rate.setVisibility(View.INVISIBLE);
        star.setVisibility(View.INVISIBLE);

        commenting.setText(comment);



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(key).child("profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String first = snapshot.child("first_name").getValue(String.class);
                    String last = snapshot.child("last_name").getValue(String.class);
                    String pic = snapshot.child("profile_pic").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);

                    get_name.setText(first);
                    get_last.setText(last);
                    get_status.setText(status);
                    Picasso.get().load(pic).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        return convertView;


    }



}
