package com.example.loginsqllite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        Button history = findViewById(R.id.historyOrder);
        Button currentOrder = findViewById(R.id.CurrentOrder);
        history.setOnClickListener(v -> {
            Intent intent = new Intent(Order.this, History.class);
            startActivity(intent);
            
        });
        currentOrder.setOnClickListener(v -> {

        });

    }
}