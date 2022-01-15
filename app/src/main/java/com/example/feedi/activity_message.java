package com.example.feedi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.feedi.SendNotificationPack.APIService;
import com.example.feedi.SendNotificationPack.Client;
import com.example.feedi.SendNotificationPack.Data;
import com.example.feedi.SendNotificationPack.MyResponse;
import com.example.feedi.SendNotificationPack.NotificationSender;
import com.example.feedi.SendNotificationPack.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import de.hdodenhof.circleimageview.CircleImageView;

public class activity_message extends AppCompatActivity {

    TextView first, last;
    CircleImageView profile;
    String user_key;
    String TAG = "tag", userUid;

    EditText message;
    ImageButton send;

    //adapter
    chat_adapter adapter;
    List<chat_info> chat_list;
    RecyclerView recyclerView;


    private String url = "https://fcm.googleapis.com/";

    APIService apiService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = findViewById(R.id.toolbar_sub);

        setSupportActionBar(toolbar);

        first = findViewById(R.id.first);
        last = findViewById(R.id.last);
        profile = findViewById(R.id.profile);
        user_key = getIntent().getStringExtra("user_key");

        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        send.setOnClickListener(v -> {

            String msg = message.getText().toString();
            if (!msg.equals("")) {
                send_message(userUid, user_key, msg);


                FirebaseDatabase.getInstance().getReference().child("Tokens").child(user_key).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        String usertoken = snapshot.getValue(String.class);
                        sendNotifications(usertoken, "message", "message received");
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                UpdateToken();

            } else {
                Toast.makeText(activity_message.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
            message.setText("");


        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/Users").child(user_key).child("profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                String first_ = snapshot.child("first_name").getValue(String.class);
                String last_ = snapshot.child("last_name").getValue(String.class);
                String link = snapshot.child("profile_pic").getValue(String.class);

                first.setText(first_);
                last.setText(last_);

                Picasso.get().load(link).into(profile);
                read_message(userUid, user_key, link);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    private void send_message(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats").push();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.setValue(hashMap);


    }


    private void read_message(String my_id, String user_key, String imageuri) {
        chat_list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chat_list.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    chat_info info = snap.getValue(chat_info.class);
                    if (info != null) {
                        if (info.getReceiver().equals(user_key) && info.getSender().equals(my_id) ||
                                info.getReceiver().equals(my_id) && info.getSender().equals(user_key)) {
                            chat_list.add(info);
                        }
                    }
                    adapter = new chat_adapter(activity_message.this, chat_list, imageuri);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(activity_message.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private static int getRandomNumber() {
        Date dd = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("mmssSS");
        String s = ft.format(dd);
        return Integer.parseInt(s);
    }
}
