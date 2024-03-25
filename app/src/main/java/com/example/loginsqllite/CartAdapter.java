package com.example.loginsqllite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class CartAdapter extends BaseAdapter {

    Cursor cartItems;

    private static LayoutInflater inflater = null;
    private final Context context;

    String username;

    String crop_name, crop_quantity, crop_price, crop_unit, farmer_name;

    private CartAdapterListener cartAdapterListener;

    public CartAdapter(Context context, String username) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.username = username;
        DBHelper db = new DBHelper(context);
        cartItems = db.getCart(username);
        if (cartItems.getCount() == 0) {
            Toast.makeText(context, "No items in cart", Toast.LENGTH_SHORT).show();
        }else if(cartItems==null){
            Toast.makeText(context, "cursor is null", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(context, username, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return cartItems.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"Range", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.my_crop, null);
        cartItems.moveToPosition(position);
        TextView cropName = (TextView) vi.findViewById(R.id.cartProductName);
        TextView cropQuantity = (TextView) vi.findViewById(R.id.cartProductQuantity);
        TextView cropPrice = (TextView) vi.findViewById(R.id.cartProductPrice);
        TextView farmerName = (TextView) vi.findViewById(R.id.cartFarmerName);
        Button deleteButton = (Button) vi.findViewById(R.id.removeButton);
        Button buyNow = (Button) vi.findViewById(R.id.buyNowButton);
        crop_name = cartItems.getString(cartItems.getColumnIndex("cropName"));
        crop_quantity = cartItems.getString(cartItems.getColumnIndex("quantity"));
        crop_price = cartItems.getString(cartItems.getColumnIndex("price"));
        crop_unit = cartItems.getString(cartItems.getColumnIndex("unit"));
        cropName.setText(crop_name);
        farmer_name = cartItems.getString(cartItems.getColumnIndex("fName"));
        farmerName.setText(String.format("Farmer Name: %s", farmer_name));
        cropQuantity.setText(String.format("Available Product Quantity: %s %s", crop_quantity, crop_unit));
        cropPrice.setText(String.format("Product Price:₹ %s per %s", crop_price, crop_unit));
        deleteButton.setText("Remove from Cart");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(context);
                db.deleteCartItem(username, crop_name, crop_quantity, crop_price);
                Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show();
                deleteButton.setText("Removed from Cart");
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DBHelper db = new DBHelper(context);
                    LatLng user_loc = db.getUserLocation(username);
                    LatLng farmer_loc = db.getUserLocation(farmer_name);
                    double latitude = farmer_loc.latitude;
                    double longitude = farmer_loc.longitude;
                    db.clearTEM();
                    boolean res =  db.createCheckOut(username,user_loc.latitude,user_loc.longitude,latitude,longitude,Double.parseDouble(crop_price), farmer_name, Integer.parseInt(crop_quantity),crop_name,crop_unit);
                    if(res){
                        Intent intent = new Intent(context,CheckOut.class);
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
