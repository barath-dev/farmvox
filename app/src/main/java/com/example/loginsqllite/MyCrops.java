package com.example.loginsqllite;

import static com.example.loginsqllite.R.id.myCropsList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MyCrops extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crops);

        String username = getIntent().getStringExtra("username");

        listView = (ListView) findViewById(R.id.myCropsList);
        listView.setAdapter(new MyCropsAdapter(MyCrops.this,username));

    }
}