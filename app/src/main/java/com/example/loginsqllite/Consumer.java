package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Consumer extends AppCompatActivity {

    private EditText searchBar;
    private static final int REQUEST_CART = 1;

    ListView productListView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        String username = getIntent().getStringExtra("username");

        // Set up RecyclerView
         productListView =(ListView) findViewById(R.id.productListView);
         productListView.setAdapter(new ConsumerAdapter(Consumer.this, username));



        // Set up search bar
        searchBar = findViewById(R.id.searchBar);

        // Set up search button click listener
        ImageButton searchButton = findViewById(R.id.searchButton);
        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consumer.this, Cart.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, REQUEST_CART);
            }
        });
    }
}
