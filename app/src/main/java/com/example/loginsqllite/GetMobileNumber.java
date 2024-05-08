package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GetMobileNumber extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_mobile_number);


        EditText mobileNumber = findViewById(R.id.editTextPhone);
        Button submit = findViewById(R.id.submitMobile);
        String username  = getIntent().getStringExtra("username");

        DBHelper dbHelper = new DBHelper(this);

        submit.setOnClickListener(v -> {
            String mobile = mobileNumber.getText().toString();
            if (mobile.isEmpty()) {
                mobileNumber.setError("Mobile number is required");
                mobileNumber.requestFocus();
                return;
            }
            if (mobile.length() != 10) {
                mobileNumber.setError("Mobile number should be 10 digits");
                mobileNumber.requestFocus();
                return;
            }

            dbHelper.setMobile(username, mobile);
            Toast.makeText(GetMobileNumber.this, "Mobile number added successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(GetMobileNumber.this, Farmer.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });


    }
}