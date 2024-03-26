package com.example.loginsqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button removeProducts = (Button) findViewById(R.id.removeAccounts);
        Button viewAccounts = (Button) findViewById(R.id.removeProducts);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button marketPrices = (Button) findViewById(R.id.marketPrices);
        Button addCrops = (Button) findViewById(R.id.addProducts);
        Button logout = (Button) findViewById(R.id.logoutButton);

        viewAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        removeProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, Delete.class);
                intent.putExtra("accounts", "true");
                startActivity(intent);
            }
        });

        marketPrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, MarkerPrices.class);
                startActivity(intent);
            }
        });

        addCrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, AddCrops.class);
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