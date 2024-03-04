package com.example.loginsqllite;

import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.loginsqllite.databinding.ActivityMapsBinding;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private double latitude, longitude;

    private String username;

    DBHelper DB = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         username = getIntent().getStringExtra("username");






        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Cursor userDetailsCursor = DB.getuserdetails(username);

        ArrayList<LatLng> userLocations = null;
        if (userDetailsCursor.getCount() == 0) {
            Toast.makeText(MapsActivity.this, "No user details found", Toast.LENGTH_SHORT).show();
        } else {
            int index = 0;
            userLocations = new ArrayList<>();
            while (userDetailsCursor.moveToNext()) {
                LatLng userLocation = new LatLng(Double.parseDouble(userDetailsCursor.getString(3)), Double.parseDouble(userDetailsCursor.getString(4)));
                userLocations.add(userLocation);
                index++;

            }
            Toast.makeText(this, index, Toast.LENGTH_SHORT).show();
            userLocations.add(new LatLng(0, 0));
        }
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (int i = 0; i < userLocations.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(userLocations.get(i)).title("Marker in User " ));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocations.get(i)));
        }
    }
}