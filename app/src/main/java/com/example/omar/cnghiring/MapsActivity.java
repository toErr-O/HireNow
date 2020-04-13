package com.example.omar.cnghiring;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;

public class MapsActivity extends AppCompatActivity  {


    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, map_frag.newInstance());
        fragmentTransaction.commit();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }
}
