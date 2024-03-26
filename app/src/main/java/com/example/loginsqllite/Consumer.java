package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Consumer extends AppCompatActivity {

    private static final int REQUEST_CART = 1;

    ListView productListView;

    boolean isFarmer = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        String username = getIntent().getStringExtra("username");
        Button checkOrders = findViewById(R.id.checkOrdersButton);

        isFarmer = new DBHelper(this).isFarmer(username);

        // Set up RecyclerView
         productListView =(ListView) findViewById(R.id.productListView);
         productListView.setAdapter(new ConsumerAdapter(Consumer.this, username, isFarmer, null));

         Button map = findViewById(R.id.mapButton);
            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Consumer.this, MapsActivity.class);
                    startActivity(intent);
                }
            });


        EditText searchBar = findViewById(R.id.searchBar);


        ImageButton searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchBar.getText().toString();
                productListView.setAdapter(new ConsumerAdapter(Consumer.this, username, isFarmer, search));
            }
        });
        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consumer.this, Cart.class);
                intent.putExtra("username", username);
                startActivityForResult(intent, REQUEST_CART);
            }
        });

        checkOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Consumer.this, Order.class);
                intent.putExtra("username", username);
                intent.putExtra("status", "ordered");
                intent.putExtra("isConsumer", "true");
                startActivity(intent);
            }
        });
    }
}
