package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class CheckOut extends AppCompatActivity {

   ListView checkOutListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);

        Button checkOutButton = findViewById(R.id.confirmCheckout);

        String username = getIntent().getStringExtra("username");

        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(CheckOut.this);
                Cursor cursor = db.getTEM(username);
                if(cursor.getCount() == 0){
                    return;
                }
                String oid = UUID.randomUUID().toString();
                while(cursor.moveToNext()){
                    double lat_src = cursor.getDouble(cursor.getColumnIndex("latitude_pickup"));
                     double long_src = cursor.getDouble(cursor.getColumnIndex("longitude_pickup"));
                     double lat_dest = cursor.getDouble(cursor.getColumnIndex("latitude_dest"));
                     double long_dest = cursor.getDouble(cursor.getColumnIndex("longitude_dest"));
                     String price = cursor.getString(cursor.getColumnIndex("product_price"));
                     String quantity = cursor.getString(cursor.getColumnIndex("product_quantity"));
                     String product = cursor.getString(cursor.getColumnIndex("product_name"));
                     String farmerName = cursor.getString(cursor.getColumnIndex("farmer_name"));
                     String unit = cursor.getString(cursor.getColumnIndex("unit"));

                     String Dboy = db.getFreeDeliveryBoy();


                    boolean res =  db.createOrder(username, lat_dest, long_dest, lat_src, long_src, Double.parseDouble(price), farmerName,Integer.parseInt(quantity),product,unit, oid,Dboy);

                    if (res && !Dboy.equals("null")){
                        boolean res1 = db.assignOrder(Dboy,oid);
                        Toast.makeText(CheckOut.this, "Order placed", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CheckOut.this, "No Delivery boy", Toast.LENGTH_SHORT).show();
                    }
                }
                db.clearTEM();
                finish();
            }
        });



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