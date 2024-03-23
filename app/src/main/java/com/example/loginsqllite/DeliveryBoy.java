package com.example.loginsqllite;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeliveryBoy extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy);
        String username = getIntent().getStringExtra("username");

        Button presentOrder = (Button) findViewById(R.id.button);
        Button pastOrders = (Button) findViewById(R.id.button2);
        Button logout = (Button) findViewById(R.id.logoutButton);

        pastOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryBoy.this, Order.class);
                intent.putExtra("status", "delivered");
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        presentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryBoy.this, Order.class);
                intent.putExtra("status", "pending");
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}