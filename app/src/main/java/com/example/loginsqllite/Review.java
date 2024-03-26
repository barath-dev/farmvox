package com.example.loginsqllite;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);


        Spinner ratingSpinner = findViewById(R.id.ratingSpinner);
        Spinner deliveryRating = findViewById(R.id.deliveryRatingSpinner);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button submit = findViewById(R.id.reviewSubmitButton);



        List<Integer> spinnerArray = new ArrayList<>();
            spinnerArray.add(1);
            spinnerArray.add(2);
            spinnerArray.add(3);
            spinnerArray.add(4);
            spinnerArray.add(5);


        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerArray
        );

        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(
                this,
                android.R.layout.simple_spinner_item,
                spinnerArray
        );

        ratingSpinner.setAdapter(adapter);
        deliveryRating.setAdapter(adapter1);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(Review.this);
                String username = getIntent().getStringExtra("farmer");
                String product = getIntent().getStringExtra("product");
                String rating = ratingSpinner.getSelectedItem().toString();
/*
                String deliveryRate = deliveryRating.getSelectedItem().toString();
*/

                int ratingCount = dbHelper.getRatingCount(product,username);


                dbHelper.addRating(product, Integer.parseInt(rating),ratingCount+1,username);
            }
        });

    }
}