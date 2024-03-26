// Cart.java
package com.example.loginsqllite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Cart extends AppCompatActivity {

    ListView productListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        String username = getIntent().getStringExtra("username");

        productListView = (ListView) findViewById(R.id.cartRecyclerView);
        productListView.setAdapter((ListAdapter) new CartAdapter(Cart.this, username));
    }
}
