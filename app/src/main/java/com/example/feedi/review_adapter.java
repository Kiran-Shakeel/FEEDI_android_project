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

public class review_adapter extends ArrayAdapter<review_info> {

    Context mcontext;
    int mresourse;


    public review_adapter(@NonNull Context context, int resource, ArrayList<review_info> list) {
        super(context, resource, list);
        mcontext = context;
        mresourse = resource;


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        float rating = getItem(position).getRating();
        String comment = getItem(position).getComment();
        String added_by=getItem(position).getAdded_by();


        review_info info = new review_info(rating,comment);
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresourse, parent, false);


        ImageView imageView = convertView.findViewById(R.id.profile);
        TextView get_name = convertView.findViewById(R.id.first);
        TextView get_last = convertView.findViewById(R.id.last);
        TextView commenting = convertView.findViewById(R.id.comment);
        TextView get_status = convertView.findViewById(R.id.status);
        TextView rate=convertView.findViewById(R.id.rate);

        commenting.setText(comment);
        String rati=Float.toString(rating);
        rate.setText(rati);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(added_by).child("profile");
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
