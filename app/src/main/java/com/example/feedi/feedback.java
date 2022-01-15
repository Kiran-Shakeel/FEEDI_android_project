package com.example.feedi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class feedback extends AppCompatActivity {
    EditText feedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        feedback = findViewById(R.id.feedback);


    }

    public void submit(View view) {
        if (TextUtils.isEmpty(feedback.getText().toString())) {
            Toast.makeText(this, "Feedback can not be empty", Toast.LENGTH_SHORT).show();
        } else {

            String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Feedbacks").child(userUid);
            Map<String,Object>hashmap=new HashMap<>();
            hashmap.put("feedback",feedback.getText().toString());
            hashmap.put("id",userUid);
            reference.updateChildren(hashmap);
        }
    }
}
