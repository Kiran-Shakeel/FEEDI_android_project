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

import java.util.List;

public class chat_list_adapter extends ArrayAdapter<all_profiles> {
    Context mcontext;
    int mresourse;

    public chat_list_adapter(@NonNull Context context, int resource, @NonNull List<all_profiles> list) {
        super(context, resource, list);
        mcontext = context;
        mresourse = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String user_key = getItem(position).getId();
        all_profiles info = new all_profiles();
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresourse, parent, false);


        ImageView imageView = convertView.findViewById(R.id.image);
        TextView get_name = convertView.findViewById(R.id.first_name);
        TextView get_last = convertView.findViewById(R.id.last);
        TextView get_ins = convertView.findViewById(R.id.info);
        TextView get_status = convertView.findViewById(R.id.status);

        get_ins.setVisibility(View.INVISIBLE);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user_key).child("profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String first = snapshot.child("first_name").getValue(String.class);
                    String last = snapshot.child("last_name").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String link = snapshot.child("profile_pic").getValue(String.class);

                    get_name.setText(first);
                    get_last.setText(last);
                    get_status.setText(status);
                    Picasso.get().load(link).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        return convertView;

    }
}


