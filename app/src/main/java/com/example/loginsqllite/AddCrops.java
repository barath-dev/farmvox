package com.example.loginsqllite;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddCrops extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_crops);

        DBHelper dbHelper = new DBHelper(this);
        Button addCrop = (Button) findViewById(R.id.submit_button);

        addCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cropName = (EditText) findViewById(R.id.productName);
                EditText minPrice = (EditText) findViewById(R.id.minimumPrice);
                EditText maxPrice = (EditText) findViewById(R.id.maximumPrice);
                EditText url = (EditText) findViewById(R.id.imageUrl);
                if (cropName.getText().toString().isEmpty() || minPrice.getText().toString().isEmpty() || maxPrice.getText().toString().isEmpty() || url.getText().toString().isEmpty()) {
                    Toast.makeText(AddCrops.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
                int res = dbHelper.addProduct(cropName.getText().toString(), Integer.parseInt(minPrice.getText().toString()), Integer.parseInt(maxPrice.getText().toString()), url.getText().toString());

                if (res>0) {
                    Toast.makeText(AddCrops.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddCrops.this, "Product not added", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }
}