package com.example.loginsqllite;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheckOut extends AppCompatActivity {

   ListView checkOutListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);

        String username = getIntent().getStringExtra("username");



        checkOutListView = (ListView) findViewById(R.id.checkOutList);
        checkOutListView.setAdapter(new CheckOutAdapter(CheckOut.this,username));
    }

    @Override
    protected void onPause() {
        super.onPause();
        DBHelper db = new DBHelper(this);
        db.clearTEM();
    }
}