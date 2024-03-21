package com.example.loginsqllite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FarmerProfile extends AppCompatActivity {

    private EditText searchBar;
    private static final int REQUEST_CART = 1;

    ListView productListView;

    boolean isFarmer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consumer);

        String username = getIntent().getStringExtra("username");

        isFarmer = new DBHelper(this).isFarmer(username);

        productListView =(ListView) findViewById(R.id.productListView);
        productListView.setAdapter(new ConsumerAdapter(FarmerProfile.this, username, isFarmer));

        TextView textView = findViewById(R.id.farmersProfile);
        textView.setVisibility(View.VISIBLE);



        // Set up search bar
        searchBar = findViewById(R.id.searchBar);

        // Set up search button click listener
        ImageButton searchButton = findViewById(R.id.searchButton);
        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmerProfile.this, Cart.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, REQUEST_CART);
            }
        });
    }
}