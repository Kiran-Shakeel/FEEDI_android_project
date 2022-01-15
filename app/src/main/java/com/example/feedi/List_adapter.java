package com.example.feedi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class List_adapter extends ArrayAdapter {
    //for admin
    List<String>names;
    List<Integer>images;
    List<String> status;
    Context context;


    public List_adapter(@NonNull Context context,List<String>names,List<Integer>images,List<String>status) {
        super(context, R.layout.list_of_items,names);
        this.names=names;
        this.images=images;
        this.status=status;
        this.context=context;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View view= LayoutInflater.from(context).inflate(R.layout.list_of_items,parent,false);
        ImageView imageView= view.findViewById(R.id.img66);
        TextView all_names=view.findViewById(R.id.name66);
        TextView statuses=view.findViewById(R.id.status66);

        all_names.setText(names.get(position));
        imageView.setImageResource(images.get(position));
        statuses.setText(status.get(position));
        return view;

    }
}
