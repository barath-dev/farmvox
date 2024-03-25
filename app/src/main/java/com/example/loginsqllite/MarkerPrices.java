package com.example.loginsqllite;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MarkerPrices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_marker_prices);
        Spinner spinner = findViewById(R.id.marketItemsSpinner);
        EditText minPrice = findViewById(R.id.minPrice);
        EditText maxPrice = findViewById(R.id.maxPrice);
        Button submit = findViewById(R.id.updateButton);

        DBHelper dbHelper = new DBHelper(this);
        List<String> crops = dbHelper.getProducts();

        if (crops.isEmpty()) {
            crops.add("No crops available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                crops
        );


        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String crop = spinner.getSelectedItem().toString();
                        String min = minPrice.getText().toString();
                        String max = maxPrice.getText().toString();
                        int res = dbHelper.updatePrices(crop, min, max);

                        if (res == 1) {
                            minPrice.setText("");
                            maxPrice.setText("");
                        }else{
                            Toast.makeText(MarkerPrices.this, "Failed to update prices", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(MarkerPrices.this, "Prices updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MarkerPrices.this, "Please select a product", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }
}