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



import java.util.ArrayList;

public class request_list_adapter extends ArrayAdapter<request_info> {
    Context mcontext;
    int mresourse;


    public request_list_adapter(@NonNull Context context, int resource, ArrayList<request_info> list) {
        super(context, resource, list);
        mcontext = context;
        mresourse = resource;


    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // View view= LayoutInflater.from(context).inflate(R.layout.leftover_list_row,parent,false);

        String ins = getItem(position).getIns();
        String status = getItem(position).getReq_status();
        String members = getItem(position).getMembers();

        request_info req = new request_info(ins, status, members);
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresourse, parent, false);


        TextView get_ins = convertView.findViewById(R.id.deliver);
        TextView get_status = convertView.findViewById(R.id.status);
        TextView get_member = convertView.findViewById(R.id.expire);
        TextView request = convertView.findViewById(R.id.left_over);


        get_ins.setText(ins);


        if (status.equals("Delivered")) {
            get_status.setTextColor(Color.parseColor("#00FF00"));
        }
        get_status.setText(status);
        get_member.setText(members);

        String i = "Request";
        request.setText(i);
        return convertView;

    }


}
