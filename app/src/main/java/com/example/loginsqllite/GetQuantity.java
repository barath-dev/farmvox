package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class GetQuantity extends AppCompatActivity {

    int quantityIn;

    @Override
    @SuppressLint("Range")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_quantity);

        String username = getIntent().getStringExtra("username");

        EditText quantityText = findViewById(R.id.getQuantity);
        TextView maxQuantity = findViewById(R.id.maxQuantity);
        TextView  pricePerKg = findViewById(R.id.pricePerKg);
        TextView totalPrice = findViewById(R.id.totalPrice);

        Button checkOutButton = findViewById(R.id.orderButton);

        DBHelper db = new DBHelper(this);
        Cursor cursor = db.getTEM(username);
        if(cursor.getCount() == 0){
            return;
        }
            cursor.moveToFirst();
            double lat_src = cursor.getDouble(cursor.getColumnIndex("latitude_pickup"));
            double long_src = cursor.getDouble(cursor.getColumnIndex("longitude_pickup"));
            double lat_dest = cursor.getDouble(cursor.getColumnIndex("latitude_dest"));
            double long_dest = cursor.getDouble(cursor.getColumnIndex("longitude_dest"));
            String price = cursor.getString(cursor.getColumnIndex("product_price"));
            String quantity = cursor.getString(cursor.getColumnIndex("product_quantity"));
            String product = cursor.getString(cursor.getColumnIndex("product_name"));
            String farmerName = cursor.getString(cursor.getColumnIndex("farmer_name"));
            String unit = cursor.getString(cursor.getColumnIndex("unit"));


        quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void afterTextChanged(Editable s) {
                DBHelper db = new DBHelper(GetQuantity.this);
                Cursor cursor = db.getTEM(username);
                if (cursor.getCount() == 0) {
                    return;
                }
                quantityIn = Integer.parseInt(String.valueOf(quantityText.getText()));
                if (quantityIn > Integer.parseInt(quantity)) {
                    quantityText.setText(quantity);
                    Toast.makeText(GetQuantity.this, "Quantity Exceeded", Toast.LENGTH_SHORT).show();
                }else if(quantityIn <= 0){
                    quantityText.setText("0");
                    Toast.makeText(GetQuantity.this, "Quantity cannot be negative or zero", Toast.LENGTH_SHORT).show();
                }

                totalPrice.setText(String.format("Total Price: %d", quantityIn * Integer.parseInt(price)));
            }
        });


        checkOutButton.setOnClickListener(v -> {
            DBHelper db1 = new DBHelper(GetQuantity.this);
            Cursor cursor1 = db1.getTEM(username);
            if (cursor1.getCount() == 0) {
                return;
            }
            String oid = UUID.randomUUID().toString();
            String Dboy = db1.getFreeDeliveryBoy();
            if (Dboy.equals("null")) {
                Toast.makeText(GetQuantity.this, "No Delivery Available", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean res = db1.createOrder(username, lat_dest, long_dest, lat_src, long_src, Double.parseDouble(String.valueOf(quantityIn * Integer.parseInt(price))), farmerName, quantityIn, product, unit, oid, Dboy);
            if (res) {
                boolean res1 = db1.assignOrder(Dboy, oid);
                if (res1) {
                    Toast.makeText(GetQuantity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                    db.updateQuantity(product, String.valueOf(Integer.parseInt(quantity) - quantityIn),farmerName);
                } else {
                    Toast.makeText(GetQuantity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(GetQuantity.this, "No Delivery Boy", Toast.LENGTH_SHORT).show();
            }
            db1.clearTEM();
            finish();
        });

        maxQuantity.setText(String.format("Max Quantity: %s", quantity));
        pricePerKg.setText(String.format("Price per Kg: %s", price));
    }
}