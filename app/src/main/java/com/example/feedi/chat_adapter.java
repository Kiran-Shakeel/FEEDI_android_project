package com.example.feedi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class chat_adapter extends RecyclerView.Adapter<chat_adapter.ViewHolder> {

    public static final int Message_Type_Left=0;
    public static final int Message_Type_Right=1;
    private Context mContext;
    List<chat_info>chat;
    String imageUri;
    String userUid;

    public chat_adapter(Context mContext, List<chat_info> chat, String imageUri) {
        this.mContext = mContext;
        this.chat = chat;
        this.imageUri = imageUri;
    }


    @NonNull
    @NotNull
    @Override
    public chat_adapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType==Message_Type_Right)
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new chat_adapter.ViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new chat_adapter.ViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull chat_adapter.ViewHolder holder, int position) {

        chat_info mchat=chat.get(position);
        holder.show_msg.setText(mchat.getMessage());
        if(imageUri.equals("default"))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);

        }
        else
        {
            Picasso.get().load(imageUri).into(holder.profile_image);
        }


    }


    @Override
    public int getItemCount() {
        return chat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_msg;
        public ImageView profile_image;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);


            show_msg=itemView.findViewById(R.id.show_msg);
            profile_image=itemView.findViewById(R.id.profile);

        }

    }

    @Override
    public int getItemViewType(int position) {
        userUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(chat.get(position).getSender().equals(userUid))
        {
            return Message_Type_Right;
        }
        else
        {
            return Message_Type_Left;
        }





    }
}
