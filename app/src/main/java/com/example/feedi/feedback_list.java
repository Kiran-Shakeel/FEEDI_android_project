package com.example.feedi;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class feedback_list extends AppCompatActivity {

    ListView listView;

    feedback_model info;
    ArrayList<feedback_model> list;
    feedbacck_adapter adapter;
    TextView label;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donor_leftover_list);

        listView = findViewById(R.id.leftover_list);
        list = new ArrayList<>();

        label=findViewById(R.id.label1);
        label.setText("Feedbacks");

        setList();
    }


    private void setList() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Feedbacks");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snap : snapshot.getChildren()) {
                        info = snap.getValue(feedback_model.class);
                        if (info != null) {
                            list.add(0, info);
                        }
                        else
                        {
                            Toast.makeText(feedback_list.this, "No feedback found", Toast.LENGTH_SHORT).show();
                        }


                    }

                    adapter = new feedbacck_adapter(feedback_list.this, R.layout.review_row, list);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(feedback_list.this, "No Data found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(feedback_list.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });

    }

}
