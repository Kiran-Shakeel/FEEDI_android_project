package com.example.feedi;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import java.util.ArrayList;

public class leftover_list_adapter extends ArrayAdapter<leftover_info> {
    Context mcontext;
    int mresourse;
    public leftover_list_adapter(@NonNull Context context,int resource,ArrayList<leftover_info>list) {
        super(context,resource,list);
        mcontext=context;
        mresourse=resource;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       // View view= LayoutInflater.from(context).inflate(R.layout.leftover_list_row,parent,false);

        String delivery=getItem(position).getDeliver_option();
        String status=getItem(position).getStatus();
        String expire=getItem(position).getExpirey_date();

        leftover_info left=new leftover_info(expire,delivery,status);
        LayoutInflater layoutInflater=LayoutInflater.from(mcontext);
        convertView=layoutInflater.inflate(mresourse,parent,false);


        TextView get_delivery=convertView.findViewById(R.id.deliver);
        TextView get_status=convertView.findViewById(R.id.status);
        TextView get_expirey=convertView.findViewById(R.id.expire);
        TextView leftover=convertView.findViewById(R.id.left_over);


        get_delivery.setText(delivery);

        if(status.equals("Done"))
        {
            get_status.setTextColor(Color.parseColor("#00FF00"));
        }
        get_status.setText(status);
        get_expirey.setText(expire);

        leftover.setText("Leftover");
          return convertView;

    }
}
