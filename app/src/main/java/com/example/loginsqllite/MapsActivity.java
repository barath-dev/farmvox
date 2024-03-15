package com.example.loginsqllite;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.loginsqllite.databinding.ActivityMapsBinding;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{


    DBHelper DB = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.loginsqllite.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String username = getIntent().getStringExtra("username");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


         ArrayList<LatLng> userLocations = DB.getAllFarmerLocations();
         ArrayList<String> usernames = DB.getAllUsernames();

        Toast.makeText(this, String.valueOf(userLocations.size()), Toast.LENGTH_SHORT).show();


        googleMap.setOnMarkerClickListener(this);

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < userLocations.size(); i++) {
            googleMap.addMarker(new MarkerOptions().position(userLocations.get(i)).title(usernames.get(i)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocations.get(i)));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "Opening "+ marker.getTitle() + " profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MapsActivity.this, FarmerProfile.class);
        intent.putExtra("username", marker.getTitle());
        startActivity(intent);
        return false;
    }
}