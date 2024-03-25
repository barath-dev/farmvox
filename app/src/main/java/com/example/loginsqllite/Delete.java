package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginsqllite.ui.RemoveAccountsAdapter;


public class Delete extends AppCompatActivity {

    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_crops);

        if (getIntent().getStringExtra("accounts") != null) {
            listView = (ListView) findViewById(R.id.myCropsList);
            listView.setAdapter(new RemoveAccountsAdapter(Delete.this));
        }else{
            listView = (ListView) findViewById(R.id.myCropsList);
            listView.setAdapter(new MyCropsAdapter(Delete.this,"admin"));
        }
    }
}