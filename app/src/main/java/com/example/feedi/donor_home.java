package com.example.feedi;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class donor_home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    Intent intent;
    CircleImageView imageView;
    TextView first, last;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_content);


        //setting map fragment
        home_frag frag = new home_frag();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainlayout, frag).commit();


        //Navigation Drawer
        drawerLayout = findViewById(R.id.draw_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //navigation header
        View nav_view=navigationView.getHeaderView(0);
        imageView = nav_view.findViewById(R.id.profileimageView);
        first = nav_view.findViewById(R.id.name_pers);
        last = nav_view.findViewById(R.id.name_last);

        setProfile();
        //Navigation

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            if (item.getItemId() == R.id.nav_profile) {
                intent = new Intent(donor_home.this, donor_profile.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.nav_settings) {
                intent = new Intent(donor_home.this, settings.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.nav_review) {
                intent = new Intent(donor_home.this, all_reviews.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.signout) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
            return true;
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addition, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(donor_home.this, donor_adding_leftover.class);
            startActivity(intent);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setProfile() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference uidRef = rootRef.child("Users").child(userUid).child("profile");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String first_name = snapshot.child("first_name").getValue().toString();
                String last_name = snapshot.child("last_name").getValue().toString();

                String link = snapshot.child("profile_pic").getValue().toString();


                first.setText(first_name);
                last.setText(last_name);


                Picasso.get().load(link).into(imageView);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(donor_home.this, "Error: " + error, Toast.LENGTH_SHORT).show();

            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setProfile();
    }
}
