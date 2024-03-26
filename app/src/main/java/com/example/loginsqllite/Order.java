package com.example.loginsqllite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Order extends AppCompatActivity {

    ListView productListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        DBHelper db = new DBHelper(this);
        String username = getIntent().getStringExtra("username");
        String status = getIntent().getStringExtra("status");

        Cursor cursor = db.getOrders(username, status, true);

        productListView =(ListView) findViewById(R.id.orders);
        productListView.setAdapter(new OrderAdapter(Order.this,cursor));

    }
}