package com.example.loginsqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
public class Cover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);
        Button btnSignIn = findViewById(R.id.buttonSignIn);
        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(Cover.this, Login.class);
            startActivity(intent);
        });
        Button btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(Cover.this, MainActivity.class);
            startActivity(intent);
        });
    }
}


