package com.example.loginsqllite;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
public class MainActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup, signin;
    DBHelper DB;
    Spinner roleSpinner;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        DB = new DBHelper(this);
        roleSpinner = findViewById(R.id.role_spinner);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        roleSpinner.setAdapter(adapter);

        // Set a listener to handle the selected role
        roleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedRole = roleSpinner.getSelectedItem().toString();
                // You can use 'selectedRole' in your registration logic
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where nothing is selected
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String selectedRole = roleSpinner.getSelectedItem().toString();

                if (user.isEmpty() || pass.isEmpty() || repass.isEmpty())
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(repass)) {
                        boolean checkuser = DB.checkusername(user);
                        if (!checkuser) {
                            retrieveLocation(new LocationCallback() {
                                @Override
                                public void onLocationReceived(double latitude, double longitude) {
                                    boolean insert = DB.insertuserdata(user, pass, selectedRole, latitude, longitude-2);
                                    if (insert) {
                                        Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        String userRole=DB.getUserRole(user);
                                        if("Farmer".equals(userRole)){
                                            Intent farmerIntent=new Intent(getApplicationContext(),Farmer.class);
                                            farmerIntent.putExtra("username", user);
                                            startActivity(farmerIntent);
                                        } else if ("Consumer".equals(userRole)) {
                                            Intent consumerIntent = new Intent(getApplicationContext(), Consumer.class);
                                            consumerIntent.putExtra("username", user);
                                            startActivity(consumerIntent);
                                        } else if ("Admin".equals(userRole)) {
                                            Intent admin = new Intent(getApplicationContext(), MapsActivity.class);
                                           admin.putExtra("username", user);
                                            startActivity(admin);
                                        }
                                        else if ("DeliveryBoy".equals(userRole)) {
                                            DBHelper db = new DBHelper(getApplicationContext());
                                            db.createDispatcher(user,latitude,longitude);
                                            Intent consumerIntent = new Intent(getApplicationContext(), DeliveryBoy.class);
                                            startActivity(consumerIntent);
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        }
        private boolean checkLocationPermission() {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        private void requestLocationPermission() {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        private void retrieveLocation(LocationCallback callback) {
            // Check for location permission explicit,lkly before retrieving location
            if (checkLocationPermission()) {
                // Use fusedLocationClient to get the location
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        // Call the callback with the obtained latitude and longitude
                        callback.onLocationReceived(latitude, longitude);
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                        double latitude =  11.0908;
                        double longitude = 77.2365+2;
                        // Call the callback with the obtained latitude and longitude
                        callback.onLocationReceived(latitude, longitude);
                    }
                });
            } else {
                // Handle the case where permission is not granted
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
                requestLocationPermission(); // Request permission
            }
        }
        interface LocationCallback {
            void onLocationReceived(double latitude, double longitude);
        }

    }
