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
    private Spinner productSpinner;
    private EditText quantityText;
    private Button submitButton;
    private TextView priceTextView;
    private CheckBox agreeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellproduct);

        String username = getIntent().getStringExtra("username");
        productSpinner = findViewById(R.id.product_spinner);
        quantityText = findViewById(R.id.quantity_text);
        submitButton = findViewById(R.id.submit_button);
        priceTextView = findViewById(R.id.price_text);
        agreeCheckBox = findViewById(R.id.agree_checkbox);
        DBHelper dbHelper = new DBHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.products_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(adapter);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                calculateAndDisplayPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                priceTextView.setText("Price: ₹0.00");
            }
        });

        quantityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAndDisplayPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreeCheckBox.isChecked()) {
                    String selectedProduct = productSpinner.getSelectedItem().toString();
                    int quantity = Integer.parseInt(quantityText.getText().toString());
                    // Use PriceCalculator to get market price per kg asynchronously
                    PriceCalculator.getMarketPricePerKg(selectedProduct, new PriceCalculator.PriceCallback() {
                        @Override
                        public void onPriceReceived(double marketPricePerKg) {
                            // Calculate the price based on market price and quantity
                            double price = calculatePrice(marketPricePerKg, quantity);

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
                                boolean productInserted = dbHelper.insertproductdata(username, password, role, latitude, longitude, selectedProduct, price, quantity);

                                if (productInserted) {
                                    Toast.makeText(Sellproduct.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
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

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(Sellproduct.this, "Error fetching market price: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Sellproduct.this, "Please agree with the price", Toast.LENGTH_SHORT).show();
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
        int quantity = 0;
        TextView priceTextView = findViewById(R.id.price_text);

        if (quantityTextValue.isEmpty()) {
            priceTextView.setText("Please enter a quantity.");
            return;
        }

        try {
            quantity = Integer.parseInt(quantityText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Use PriceCalculator to get market price per kg asynchronously
        int finalQuantity = quantity;
        PriceCalculator.getMarketPricePerKg(selectedProduct, new PriceCalculator.PriceCallback() {
            @Override
            public void onPriceReceived(double marketPricePerKg) {
                // Calculate the price based on market price and quantity
                double price = calculatePrice(marketPricePerKg, finalQuantity);

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String priceText = "Price: ₹" + decimalFormat.format(price);
                priceTextView.setText(priceText);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(Sellproduct.this, "Error fetching market price: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}