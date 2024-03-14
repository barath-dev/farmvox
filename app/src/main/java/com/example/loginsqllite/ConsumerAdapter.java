package com.example.loginsqllite;



import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ConsumerAdapter extends BaseAdapter {

    Context context;

    Double latitude, longitude;

    DBHelper db;

    String username;
    private static LayoutInflater inflater = null;

    String crop_name, crop_quantity, crop_price, crop_unit, farmer_name;

    Cursor cursor, user_loc;

    public ConsumerAdapter(Context context, String username) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
         db = new DBHelper(context);
        this.username = username;
        cursor =  db.getAllProducts();
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

    @SuppressLint({"Range", "InflateParams"})
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
        cropPrice.setText(String.format("₹Product Price: %s per %s", crop_price, crop_unit));
        deleteButton.setText("Add to Cart");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   String res =  db.createCart(username, crop_name, crop_quantity, crop_price, crop_unit, farmer_name);
                    Toast.makeText(context, latitude.toString(), Toast.LENGTH_SHORT).show();

                deleteButton.setText("Added to Cart");
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    boolean res =  db.createOrder(username, Double.parseDouble(user_loc.getString(user_loc.getColumnIndex("latitude"))), Double.parseDouble(user_loc.getString(user_loc.getColumnIndex("longitude"))), latitude,longitude,Double.parseDouble(crop_price), farmer_name, Integer.parseInt(crop_quantity),crop_name,crop_unit);
                    if(res){
                        Toast.makeText(context, "Order Placed", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Order Failed", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Log.i("Error", e.getMessage());
                }
            }
        });
        return vi;
    }
}
