package com.example.loginsqllite;
import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;

import java.text.DecimalFormat;

public class Sellproduct extends AppCompatActivity {
    private Spinner productSpinner;
    private EditText quantityText,priceText;
    private TextView priceTextView;
    private CheckBox agreeCheckBox;

    private int price = 0,minPrice = 0,maxPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellproduct);

        String username = getIntent().getStringExtra("username");
        productSpinner = findViewById(R.id.product_spinner);
        quantityText = findViewById(R.id.quantity_text);
        Button submitButton = findViewById(R.id.submit_button);
        priceTextView = findViewById(R.id.priceRange);
        agreeCheckBox = findViewById(R.id.agree_checkbox);
        priceText = findViewById(R.id.pricePerUnit);
        DBHelper dbHelper = new DBHelper(this);

        ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                dbHelper.getProducts()
        );

        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(productAdapter);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int[] prices = dbHelper.getPrices(productSpinner.getSelectedItem().toString());
                minPrice = prices[0];
                maxPrice = prices[1];

                priceTextView.setText("Price: ₹" + minPrice + ".00" + " - ₹" + maxPrice + ".00");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                priceTextView.setText("Price: ₹0.00");
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreeCheckBox.isChecked()) {
                    String selectedProduct = productSpinner.getSelectedItem().toString();
                    int quantity = Integer.parseInt(quantityText.getText().toString());

                    Cursor userDetailsCursor = dbHelper.getuserdetails(username);

                    if (userDetailsCursor != null && userDetailsCursor.moveToFirst()) {
                        // Retrieve user details from the Cursor
                        String latitudeString = userDetailsCursor.getString(3);
                        String longitudeString = userDetailsCursor.getString(4);
                        double latitude = Double.parseDouble(latitudeString);
                        double longitude = Double.parseDouble(longitudeString);

                        if (minPrice<= Integer.parseInt(priceText.getText().toString()) && Integer.parseInt(priceText.getText().toString()) <= maxPrice){
                            price = Integer.parseInt(priceText.getText().toString());

                        // Insert product details into the product database
                        boolean productInserted = dbHelper.insertproductdata(username, latitude, longitude, selectedProduct, "Kg", price, quantity);

                        if (productInserted) {
                            Toast.makeText(Sellproduct.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Sellproduct.this, "Failed to submit data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Sellproduct.this, "Price not in range", Toast.LENGTH_SHORT).show();
                    }
                        userDetailsCursor.close();
                    } else {
                    Toast.makeText(Sellproduct.this, "User details not found", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(Sellproduct.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }
}