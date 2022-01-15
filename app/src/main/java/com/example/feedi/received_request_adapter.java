package com.example.feedi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class received_request_adapter extends ArrayAdapter<leftover_request_info> {

    Context mcontext;
    int mresourse;


    public received_request_adapter(@NonNull Context context, int resource, ArrayList<leftover_request_info> set_list) {
        super(context, resource, set_list);
        mcontext = context;
        mresourse = resource;


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        String req_key = getItem(position).getReq_key();
        leftover_request_info info = new leftover_request_info(req_key);

        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresourse, parent, false);


        TextView get_ins = convertView.findViewById(R.id.deliver);
        TextView get_status = convertView.findViewById(R.id.status);
        TextView get_member = convertView.findViewById(R.id.expire);
        TextView request = convertView.findViewById(R.id.left_over);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Requests_On_Leftovers").child(req_key);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String ins = snapshot.child("ins").getValue(String.class);
                    String status = snapshot.child("req_status").getValue(String.class);
                    String members = snapshot.child("members").getValue(String.class);
                    get_ins.setText(ins);


                    if (status.equals("Delivered")) {
                        get_status.setTextColor(Color.parseColor("#00FF00"));
                    }
                    get_status.setText(status);
                    get_member.setText(members);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        String i = "Request On Leftovers";
        request.setText(i);
        return convertView;

    }
}
