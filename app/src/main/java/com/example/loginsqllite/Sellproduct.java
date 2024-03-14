package com.example.loginsqllite;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private Spinner productSpinner, unitSpinner;
    private EditText quantityText,priceTextView;
    private Button submitButton;
    private CheckBox agreeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellproduct);

        String username = getIntent().getStringExtra("username");
        productSpinner = findViewById(R.id.product_spinner);
        quantityText = findViewById(R.id.quantity_text);
        submitButton = findViewById(R.id.submit_button);
        priceTextView = findViewById(R.id.pricePerUnit);
        unitSpinner = findViewById(R.id.unit_spinner);
        agreeCheckBox = findViewById(R.id.agree_checkbox);
        DBHelper dbHelper = new DBHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.products_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.unit_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter1);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Toast.makeText(Sellproduct.this, "Selected: " + productSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                priceTextView.setVisibility(View.GONE);
            }
        });

        quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreeCheckBox.isChecked()) {
                    String selectedProduct = productSpinner.getSelectedItem().toString();
                    String selectedUnit = unitSpinner.getSelectedItem().toString();
                    int quantity = Integer.parseInt(quantityText.getText().toString());
                    int price = Integer.parseInt(priceTextView.getText().toString());
                    // Use PriceCalculator to get market price per kg asynchronously

                            // Calculate the price based on market price and quantity
                            /*double price = calculatePrice(marketPricePerKg, quantity);*/

                            Cursor userDetailsCursor = dbHelper.getuserdetails(username);

                            if (userDetailsCursor != null && userDetailsCursor.moveToFirst()) {
                                // Retrieve user details from the Cursor
                                String password = userDetailsCursor.getString(1);
                                String role = userDetailsCursor.getString(2);
                                String latitudeString = userDetailsCursor.getString(3);
                                String longitudeString = userDetailsCursor.getString(4);
                                double latitude = Double.parseDouble(latitudeString);
                                double longitude = Double.parseDouble(longitudeString);

                                // Insert product details into the product database
                                boolean productInserted = dbHelper.insertproductdata(username, latitude, longitude, selectedProduct, selectedUnit,price, quantity);

                                if (productInserted) {
                                    Toast.makeText(Sellproduct.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(Sellproduct.this, "Failed to submit data", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Sellproduct.this, "User details not found", Toast.LENGTH_SHORT).show();
                            }

                            if (userDetailsCursor != null) {
                                userDetailsCursor.close();
                            }
                        }
            }
        });
    }

    private double calculatePrice(double marketPricePerKg, int quantity) {
        double profitPercentage = 5.0;
        return (marketPricePerKg * quantity) + ((marketPricePerKg * quantity) * profitPercentage / 100.0);
    }

    private void calculateAndDisplayPrice() {
        Spinner productSpinner = findViewById(R.id.product_spinner);
        String selectedProduct = productSpinner.getSelectedItem().toString();
        String quantityTextValue = quantityText.getText().toString();
        String priceTextValue = priceTextView.getText().toString();

        int quantity = 0;

        if (quantityTextValue.isEmpty()) {
            Toast.makeText(this,  "Quantity field should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (priceTextValue.isEmpty()) {
            Toast.makeText(this,  "Price field should not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            quantity = Integer.parseInt(quantityText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}