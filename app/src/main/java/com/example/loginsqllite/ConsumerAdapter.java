package com.example.loginsqllite;






import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;


import java.util.Objects;

public class ConsumerAdapter extends BaseAdapter {

    Context context;

    Double latitude, longitude;

    DBHelper db;

    String username;
    private static LayoutInflater inflater = null;

    String crop_name, crop_quantity, crop_price, crop_unit, farmer_name;

    Cursor cursor;

    LatLng user_loc;

    public ConsumerAdapter(Context context, String username, boolean isFarmer) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
         db = new DBHelper(context);
        this.username = username;
        cursor =  !isFarmer?db.getAllProducts():db.getAllProductsForFarmer(username);
        user_loc = db.getUserLocation(username);
    }


    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"Range", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.my_crop, null);
        cursor.moveToPosition(position);
        TextView cropName = (TextView) vi.findViewById(R.id.cartProductName);
        TextView cropQuantity = (TextView) vi.findViewById(R.id.cartProductQuantity);
        TextView cropPrice = (TextView) vi.findViewById(R.id.cartProductPrice);
        TextView farmerName = (TextView) vi.findViewById(R.id.cartFarmerName);
        Button deleteButton = (Button) vi.findViewById(R.id.removeButton);
        Button buyNow = (Button) vi.findViewById(R.id.buyNowButton);
         crop_name = cursor.getString(cursor.getColumnIndex("product_name"));
         crop_quantity = cursor.getString(cursor.getColumnIndex("product_quantity"));
         crop_price = cursor.getString(cursor.getColumnIndex("product_price"));
         crop_unit = cursor.getString(cursor.getColumnIndex("product_unit"));
         latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));

         longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));

        cropName.setText(String.format("Product Name: %s", crop_name));
        farmer_name = cursor.getString(cursor.getColumnIndex("username"));
        farmerName.setText(String.format("Farmer Name: %s", farmer_name));
        cropQuantity.setText(String.format("Product Quantity: %s %s", crop_quantity, crop_unit));
        cropPrice.setText(String.format("Product Price:₹ %s per %s", crop_price, crop_unit));
        deleteButton.setText("Add to Cart");

        ImageView cropImage = (ImageView) vi.findViewById(R.id.imageView);
        DBHelper db = new DBHelper(context);

        String url =  db.getUrl(crop_name);

        ImageLoaderTask imageLoaderTask = new ImageLoaderTask(cropImage);
        imageLoaderTask.execute(url);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                   db.createCart(username, crop_name, crop_quantity, crop_price, crop_unit, farmer_name);

                deleteButton.setText("Added to Cart");
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    db.clearTEM();
                    boolean res =  db.createCheckOut(username,user_loc.latitude,user_loc.longitude,latitude,longitude,Double.parseDouble(crop_price), farmer_name, Integer.parseInt(crop_quantity),crop_name,crop_unit);
                    Toast.makeText(context, "Check out created", Toast.LENGTH_SHORT).show();
                    if(res){
                        Intent intent = new Intent(context,GetQuantity.class);
                        intent.putExtra("username",username);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(context, "Order Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Log.i("Error", Objects.requireNonNull(e.getMessage()));
                }
            }
        });
        return vi;
    }
}
