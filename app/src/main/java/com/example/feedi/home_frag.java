package com.example.feedi;

import android.Manifest;

import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class home_frag extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button enable;

    private FusedLocationProviderClient fusedLocationProviderClient;
    Double lat;
    Double longi;
    private static final int PERMISSION_CODE = 1000;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Context) getActivity());


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.maps_fragment, container, false);




        //initializing
        enable = v.findViewById(R.id.btnenable);


        //button Click Listener
        enable.setOnClickListener(view ->

        {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location != null) {

                                try {
                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    longi = addresses.get(0).getLongitude();
                                    lat = addresses.get(0).getLatitude();

                                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                                    mapFragment.getMapAsync(home_frag.this);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });

                }
                else
                {
                    String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
                    requestPermissions(permission, PERMISSION_CODE);
                }


            }

        });




        return v;


    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng pak = new LatLng(lat, longi);
        mMap.addMarker(new MarkerOptions().position(pak).title("My Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(pak));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pak, 5));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pak));


        Intent intent = new Intent(getActivity(), needy_request_list.class);
        Bundle b=new Bundle();
        b.putDouble("longitude",longi);
        b.putDouble("latitude",lat);


        intent.putExtras(b);
        startActivity(intent);
        Toast.makeText(getActivity(), "Showing needy request", Toast.LENGTH_SHORT).show();

    }


}


